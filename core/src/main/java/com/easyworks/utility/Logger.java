package com.easyworks.utility;

import com.easyworks.Functions;
import com.easyworks.Loggable;
import com.easyworks.function.ConsumerThrowable;

public class Logger implements Loggable {

    private final static String textReset = "\033[0m";  // Text Reset
    public static Logger Default = new Logger(s -> System.out.println(s));

    public static void L(String format, Object... arguments){
        if(Default != null){
            String color = "\033[0;34m"; //Blue
            if(StringHelper.containsAnyIgnoreCase(format, "fail", "exception"))
                color = "\033[0;31m";   //Red
            else if(StringHelper.containsAnyIgnoreCase(format,"success", "succeed", "pass"))
                color = "\033[0;32m";   // GREEN

            String reportFormat = format.replaceAll("(%[\\S]+)",
                    String.format("%s$1%s", color, textReset));

            Default.log(reportFormat, arguments);
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
