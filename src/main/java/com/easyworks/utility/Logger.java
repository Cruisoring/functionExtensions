package com.easyworks.utility;

import com.easyworks.Loggable;
import com.easyworks.NoThrows;
import com.easyworks.function.RunnableThrows;

public class Logger implements Loggable {

    public static Logger Default = new Logger(s -> System.out.println(s));

    public static void L(String message){
        if(Default != null){
            Default.log(message);
        }
    }

    private final RunnableThrows.ConsumerThrows<String> _logging;

    public Logger(RunnableThrows.ConsumerThrows<String> logging){
        this._logging = logging;
    }

    @Override
    public void log(String message){
        if(_logging != null){
            NoThrows.run(() -> _logging.accept(message));
        }
    }
}
