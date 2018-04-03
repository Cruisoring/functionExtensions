package com.easyworks.utility;

import com.easyworks.Functions;
import com.easyworks.Loggable;
import com.easyworks.function.ConsumerThrowable;
import com.easyworks.function.RunnableThrowable;

public class Logger implements Loggable {

    public static Logger Default = new Logger(s -> System.out.println(s));

    public static void L(String message, Object... arguments){
        if(Default != null){
            Default.log(message, arguments);
        }
    }

    private final ConsumerThrowable<String> _logging;

    public Logger(ConsumerThrowable<String> logging){
        this._logging = logging;
    }

    @Override
    public void log(String message, Object... arguments){
        if(_logging != null){
            Functions.Default.run(() -> _logging.accept(String.format(message, arguments)));
        }
    }

}
