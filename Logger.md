# functionExtensions.Logger

The [io.github.curuisoring.logger](https://github.com/Cruisoring/functionExtensions/tree/master/src/main/java/io/github/cruisoring/logger) package, build with FP paradigms, was added into [functionExtensions](https://github.com/Cruisoring/functionExtensions) with quite some unique features to empower software development with JAVA:
 *  Zero-config in most cases.
 *  Self-configurable means of logging to support any outputs and multiple outputs, simply by providing a String consumer.
 *  Error-prove, leveled logging of messages/exceptions with default colourful console outputs.
 *  Concise stack trace screening to show only meaningful call stacks.
 *  Logging messages highlight optional arguments in error/pass/normal states in different color by default.
 *  Logging ON/OFF by setting single static variables without changing source codes, or control minimum level to be logged in either logger or global level. 
 *  Build-in performance measurement support.
 
## Background

When working with a language like JAVA, the window that I watched most could be the the source code editor window, then spend the second to most of time to check the print-outs from the output window. Checking the logs could make my eyes sore quickly when the messages are too long, or containing too much details that shall be discarded at the first place and especially when I have to scrutinize the lengthy mono-coloured text to search some keywords, that shall be the initial key reason for me to develop this logging utility.

Unlike most other loggers, I have used static methods extensively to save time to initialize instances, turning logging on/off or redirecting to different media. When implementing some performance measurement utilities accepting Lambda expressions, I found it could be fitted well and that shall be a cheap means to tune your functions. 
 

## Class Architecture

The classes/interfaces diagram is shown below:

![Class diagram](images/Logger_diagram.png "Logger Classes and Interfaces")

The only Enum type [LogLevel](https://github.com/Cruisoring/functionExtensions/blob/master/src/main/java/io/github/cruisoring/logger/LogLevel.java) defines 6 levels:
 *  ```verbose, debug, info, warning, error``` by referring [Android.log](https://developer.android.com/reference/android/util/Log) to tag the logged messages/exceptions.
 *  ```none``` is used mainly to turn off logging globally.

The [ILogger interface](https://github.com/Cruisoring/functionExtensions/blob/master/src/main/java/io/github/cruisoring/logger/ILogger.java) defines 4 methods to be implemented:
 *  ```void save(String message)```: it is actually the only mandatory method to create a new Logger instance, as a Lambda to specify how to log a message.
 *  ```LogLevel getMinLevel()```: specify the Minimum LogLevel, messages under it would be ignored by next method canLog().
 *  ```boolean canLog(LogLevel level)```: determine if the given *LogLevel* shall be logged or not by check the Logger instance itself and global static settings.
 *  ```String getMessage(LogLevel level, String format, Object... args)```: placeholder to specify how a message shall be created.
 
The ILogger interface has defined multiple default methods of following types:
 *  ```ILogger log(LogLevel level, String format, Object... arguments)```: default method to log messages with optional arguments by: 
    * check and short-cut if logging is OFF or the message with given *LogLevel* shall not be logged by calling ```boolean canLog(LogLevel level)```;
    * compose message by calling the ```String getMessage(LogLevel level, String format, Object... args)``` that can be personised.
    * log the composed message by calling ```void save(String message)```.
 *  ```ILogger log(LogLevel level, Exception ex)```: default method to log exception by:
    * get stack trace of either the captured *Exception ex* itself, or its **cause** when available with assistance of [StackTraceHelper](https://github.com/Cruisoring/functionExtensions/blob/master/src/main/java/io/github/cruisoring/utility/StackTraceHelper.java) that shall effectively reduce hundreds lines of call stacks to dozens highlighting only the source codes of yours.
    * calling ```ILogger log(LogLevel level, String format, Object... arguments)``` to get the exception logged with stack trace highlighted.
 *  ```verbose(Exception), verbose(String, Object...), debug(Exception), ... error(Exception), error(String, Object...)```: just syntactic sugar to feed above 2 log() methods with corresponding **LogLevel**s.
 *  Performance measurement methods to log single execution with explicit/implicit name that would be concluded by names. See later section.
 
The [Logger.java](https://github.com/Cruisoring/functionExtensions/blob/master/src/main/java/io/github/cruisoring/logger/Logger.java)
    

## Extra Action to be invoked by AutoCloseable.close() 

To change the behaviour of AutoCloseable.close(), it is possible to include extra closing action to the constructor:

public Lazy(SupplierThrowable<T> supplier, ConsumerThrowable<T> extraClosingAction){
    checkWithoutNull(supplier);
    this.supplier = supplier;
    this.closing = extraClosingAction == null ? this::reset : () -> this.resetAfterAction(extraClosingAction);
}
When the default reset() could be used in most of cases, if extraClosingAction is used, then the closing field would save a newly created Lambda that would run the extra business logic before the default reset().

private void resetAfterAction(ConsumerThrowable<T> extraAction){
    if(extraAction != null){
        Functions.Default.run(() -> extraAction.accept(value));
    }
    reset();
}
This is actually another example of Lambda with Data, and this time, the data is another Lambda expression, and the newly generated () -> this.resetAfterAction(extraClosingAction) would have the signature of ()->doSomething() expected by the field of final RunnableThrowable closing.

This can be validated with a simple test:
List<String> logs = new ArrayList<>();
Lazy<Integer> integerLazy = new Lazy(() -> Integer.valueOf(33),
        t -> logs.add("You shall see me after the test :)"));
integerLazy.close();
assertEquals("You shall see me after the test :)", logs.get(0));

## Chain Reaction of Evaluation

One Lazy<T> instance could be used to create another Lazy<U> instance, that is, the value of the first Lazy could be used by the second Lazy instance when evaluating its own value.
These dependencies are build up as a side effect of calling create(), the simpler version is as below:
public <U> Lazy<U> create(FunctionThrowable<T, U> function){
    Lazy<U> dependency = new Lazy(() -> function.apply(getValue()));
    if(dependencies == null){
        dependencies = new ArrayList<>();
    }
    dependencies.add(dependency);
    return dependency;
}
Notice the parameter of the create() method is FunctionThrowable<T,U> instead of SupplierThrowable<U> of the supplier instance of the newly created dependency instance. Conversion of the given FunctionThrowable<T,U> to SupplierThrowable<U> would call t.getValue() automatically when u.getValue() is called.

For instance, refBoolean.getValue() would call refInt.getValue() that would also call refString.getValue().

Lazy<String> refString = new Lazy<String>(() -> "abcdefg");
Lazy<Integer> refInt = refString.create(str -> str.length());
Lazy<Boolean> refBoolean = refInt.create(n -> n%2 == 0);

assertEquals(false, refBoolean.getValue());
assertTrue(refString.isValueInitialized());
assertTrue(refInt.isValueInitialized());
Consequently, both refInt and refString would be initialised after refBoolean.getValue().

## Chain Reaction of AutoCloseable.close()

Resource management with JAVA is not as convenient as C# where IDisposable is defined; even after implementing AutoCloseable, not putting the instances with try-with-resources construct or failing to call the close() explicitly may still lead to trigger the intended cleanup logic.

The Lazy<T> class has taken some measures to mitigate this issue by including following default logic to its closeing logic:

Calling close() of the value instance if it is initialised.
If there are any other AutoCloseable instances are registered as its dependencies, their close() methods would be called with reversed order as registered.
As the create() method in #Chain Reaction of Evaluation indicated, the newly created u instance would be added as a dependency of the t instance. Since the default cleanup logic is defined as:

public void reset(){
    if(!isClosed){
        isClosed = true;
        if(dependencies != null && dependencies.size() > 0){
            for (int i = dependencies.size()-1; i >= 0; i--) {
                AutoCloseable child = dependencies.get(i);
                if(child instanceof Lazy && ((Lazy)child).isClosed)
                    continue;

                Functions.Default.run(child::close);
            }
            dependencies.clear();
        }
        if(isInitialized) {
            isInitialized = false;
            if (value != null && value instanceof AutoCloseable) {
                Functions.Default.run(((AutoCloseable) value)::close);
            }
            value = null;
        }
    }
}
Closing of root Lazy<T> would not only release resources of itself, but also all Lazy instances depending on its value directly or indirectly.

A simple example is listed below:

List<String> logs = new ArrayList<>();
Lazy<Boolean> booleanLazy;
try(Lazy<String> stringLazy = new Lazy<String>(() -> "1234567",
        s -> logs.add(String.format("stringLazy is closing")));
    Lazy<Integer> integerLazy = stringLazy.create(str -> 37 + str.length(),
            i -> logs.add(String.format("integerLazy is closing")))) {
    assertEquals(Integer.valueOf(44), integerLazy.getValue());
    assertTrue(stringLazy.isValueInitialized());
    booleanLazy = integerLazy.create(n -> n%2==0,
            b -> logs.add(String.format("booleanLazy is closing")));
    boolean booleanValue = booleanLazy.getValue();
}

assertFalse(booleanLazy.isValueInitialized());
assertTrue(Arrays.deepEquals(new String[]{"integerLazy is closing", "booleanLazy is closing", "stringLazy is closing"}, logs.toArray()));
Notice in above example, the integerLazy.close() is called first, dispite of the logging messages, booleanLazy.reset() would be called first, followed by integerLazy.reset() and finally stringLazy.reset() in debug mode.
Now it is possible to use Lazy<> to manage the expensive resources. Suppose there is a mail application need to allocate various resources for a customer account for Inbox and Outbox operations:

public static List<String> logs = new ArrayList<>();
public class Account implements AutoCloseable {
    private String email;
    public Account(String email){this.email = email; logs.add("Account created: " + email);}
    @Override
    public void close() throws Exception {
        logs.add("Account closed: " + email);
    }
}
public class Inbox implements AutoCloseable {
    public Inbox(Account account){ logs.add("Inbox opened");}
    public void checkMail(){logs.add("Check inbox");}
    @Override
    public void close() throws Exception {
        logs.add("Inbox closed");
    }
}
public class Outbox implements AutoCloseable{
    public Outbox(Account account){ logs.add("Outbox opened");}
    public void sendMail(){logs.add("Send mail");}
    @Override
    public void close() throws Exception {
        logs.add("Outbox closed");
    }
}
Then it is possible to leave the resource management to the chained Lazy<>s:

    @Test
    public void chain_creation_sample(){
        Lazy<String> emailLazy = new Lazy<>(() -> "email@test.com");
        //With try-with-resources pattern:
//        try(Lazy<Account> accountLazy = emailLazy.create(Account::new)){
//            Lazy<Inbox> inboxLazy = accountLazy.create(Inbox::new);
//            Lazy<Outbox> outboxLazy = accountLazy.create(Outbox::new);
//            outboxLazy.getValue().sendMail();
//            inboxLazy.getValue().checkMail();
//        }catch (Exception ex){
//        }

        Lazy<Account> accountLazy = emailLazy.create(Account::new);
        Lazy<Inbox> inboxLazy = accountLazy.create(Inbox::new);
        Lazy<Outbox>  outboxLazy = accountLazy.create(Outbox::new);
        outboxLazy.getValue().sendMail();
        inboxLazy.getValue().checkMail();
        emailLazy.reset();  //Instead of emailLazy.close() to avoid using try-catch

        assertTrue(Arrays.deepEquals(logs.toArray(), new String[]{
                "Account created: email@test.com", "Outbox opened", "Send mail", "Inbox opened", "Check inbox",
                "Outbox closed", "Inbox closed", "Account closed: email@test.com"
        }));
    }