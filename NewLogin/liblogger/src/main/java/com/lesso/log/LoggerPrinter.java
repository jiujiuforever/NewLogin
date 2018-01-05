package com.lesso.log;

import java.util.ArrayList;
import java.util.List;

import static com.lesso.log.LogUtil.ASSERT;
import static com.lesso.log.LogUtil.DEBUG;
import static com.lesso.log.LogUtil.ERROR;
import static com.lesso.log.LogUtil.INFO;
import static com.lesso.log.LogUtil.VERBOSE;
import static com.lesso.log.LogUtil.WARN;

class LoggerPrinter implements Printer {

    private final List<LogAdapter> logAdapters = new ArrayList<>();

    @Override
    public void addAdapter(LogAdapter adapter) {
        if(!logAdapters.contains(adapter)) {
            logAdapters.add(adapter);
        }
    }

    @Override
    public void removeAdapter(LogAdapter adapter) {
        if(logAdapters.contains(adapter)){
            logAdapters.remove(adapter);
        }
    }

    @Override
    public void clearAdapters() {
        logAdapters.clear();
    }

    @Override
    public void d(String tag, String message, Object... args) {
        log(tag, DEBUG, null, message, args);
    }

    @Override
    public void e(String tag, String message, Object... args) {
        log(tag, ERROR, null, message, args);
    }

    @Override
    public void e(String tag, Throwable throwable, String message, Object... args) {
        log(tag, ERROR, throwable, message, args);
    }

    @Override
    public void w(String tag, String message, Object... args) {
        log(tag, WARN, null, message, args);
    }

    @Override
    public void i(String tag, String message, Object... args) {
        log(tag, INFO, null, message, args);
    }

    @Override
    public void v(String tag, String message, Object... args) {
        log(tag, VERBOSE, null, message, args);
    }

    @Override
    public void a(String tag, String message, Object... args) {
        log(tag, ASSERT, null, message, args);
    }

    /**
     * This method is synchronized in order to avoid messy of logs' order.
     */
    private synchronized void log(String tag, int priority, Throwable throwable, String msg, Object... args) {
        String message = createMessage(msg, args);
        log(tag, priority, message, throwable);
    }

    private synchronized void log(String tag,int priority, String message, Throwable throwable) {
        if (throwable != null && message != null) {
            message += " : " + Utils.getStackTraceString(throwable);
        }
        if (throwable != null && message == null) {
            message = Utils.getStackTraceString(throwable);
        }
        if (Utils.isEmpty(message)) {
            message = "Empty/NULL log message";
        }

        for (LogAdapter adapter : logAdapters) {
            if (adapter.isLoggable(priority, tag)) {
                adapter.log(priority, tag, message);
            }
        }
    }

    private String createMessage(String message, Object... args) {
        return args == null || args.length == 0 ? message : String.format(message, args);
    }
}