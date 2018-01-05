package com.lesso.log;

class AndroidLogAdapter implements LogAdapter {

    private final LogStrategy mLogStrategy;

    public AndroidLogAdapter() {
        mLogStrategy = new AndroidLogStrategy();
    }

    @Override
    public boolean isLoggable(int priority, String tag) {
        return true;
    }

    @Override
    public void log(int priority, String tag, String message) {
        mLogStrategy.log(priority, tag, message);
    }

}