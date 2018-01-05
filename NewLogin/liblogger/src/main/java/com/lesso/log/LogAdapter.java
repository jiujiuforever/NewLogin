package com.lesso.log;

interface LogAdapter {

    boolean isLoggable(int priority, String tag);

    void log(int priority, String tag, String message);
}