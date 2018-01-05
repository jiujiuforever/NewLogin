package com.lesso.log;

class DiskLogAdapter implements LogAdapter {

    private String fileName;
    private long fileSize;

    private final DiskLogStrategy mDiskLogStrategy;

    public DiskLogAdapter(String fileName,long fileSize, int limitDay) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        mDiskLogStrategy = DiskLogStrategy.newBuilder()
                .fileName(fileName)
                .fileSize(fileSize)
                .limitDay(limitDay)
                .build();
    }

    @Override
    public boolean isLoggable(int priority, String tag) {
        return true;
    }

    @Override
    public void log(int priority, String tag, String message) {
        mDiskLogStrategy.log(priority, tag, message);
    }

    public String getLogFilePath() {
        return mDiskLogStrategy.getLogFilePath();
    }

    public String getFilePath() {
        return mDiskLogStrategy.getFilePath();
    }
}