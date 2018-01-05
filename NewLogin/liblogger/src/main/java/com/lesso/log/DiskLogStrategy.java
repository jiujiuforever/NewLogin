package com.lesso.log;

import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

class DiskLogStrategy implements LogStrategy {

    private static final String TAG = "DiskLogStrategy";

    /**
     * 日志文件夹
     **/
    private static final String LOG_FILE_NAME = "log";

    private String fileName;
    private long fileSize;
    private int limitDay;
    private Handler handler;


    public DiskLogStrategy(Builder builder) {
        this.handler = builder.handler;
        this.fileName = builder.fileName;
        this.fileSize = builder.fileSize;
        this.limitDay = builder.limitDay;

        if (limitDay == 0) {
            // 保存0天则不进行保存本地日志
            return;
        }

        // 日志文件夹目录 /sdcard/{fileName}/log/
        String diskPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String folder = diskPath + File.separator + fileName + File.separator + LOG_FILE_NAME;

        // 删除超过限定日期的文件夹
        delLogFile(folder, limitDay);

        // 本地保存日志
        HandlerThread ht = new HandlerThread("AndroidFileLogger." + folder);
        ht.start();
        handler = new DiskLogStrategy.WriteHandler(ht.getLooper(), folder, fileSize);
    }

    /**
     * 日志文件路径
     * @return
     */
    public String getLogFilePath(){
        String diskPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String folder = diskPath + File.separator + fileName + File.separator + LOG_FILE_NAME;
        return folder;
    }

    /**
     * 日志文件路径
     * @return
     */
    public String getFilePath(){
        String diskPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String folder = diskPath + File.separator + fileName;
        return folder;
    }

    /**
     * 删除日志
     * 异步删除超过期限的日志
     */
    private void delLogFile(String folder, int limitDay) {
        HandlerThread delLogFolderHt = new HandlerThread("AndroidDelLogFolder");
        delLogFolderHt.start();
        Handler delLogFolderHandler = new Handler(delLogFolderHt.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                int limitDay = msg.what;
                String folderPath = (String) msg.obj;
                File folderFile = new File(folderPath);
                if (!folderFile.exists() || !folderFile.isDirectory()) {
                    return;
                }

                // 本地日志文件以20170804的文件夹名称存放
                File[] fileList = folderFile.listFiles();
                if (fileList.length == 0) {
                    return;
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.UK);
                String todayDate = dateFormat.format(new Date());

                for (File file : fileList) {
                    String fileName = file.getName();
                    try {
                        int betweenDay = DateUtil.daysBetween(fileName, todayDate);
                        // 若两者的文件超过限定日期则删除
                        if (betweenDay >= limitDay || betweenDay < 0) {
                            boolean isSuccess = FileUtil.deleteDir(file);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        // 发送数据
        delLogFolderHandler.sendMessage(delLogFolderHandler.obtainMessage(limitDay, folder));
    }

    @Override
    public void log(int level, String tag, String message) {
        // 日期
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", Locale.UK);
        String timeStr = dateFormat.format(new Date());

        // 日志等级
        String levelStr = "【VERBOSE】";
        switch (level){
            case LogUtil.VERBOSE:
                levelStr = "【VERBOSE】";
                break;
            case LogUtil.DEBUG:
                levelStr = "【DEBUG】";
                break;
            case LogUtil.INFO:
                levelStr = "【INFO】";
                break;
            case LogUtil.WARN:
                levelStr = "【WARN】";
                break;
            case LogUtil.ERROR:
                levelStr = "【ERROR】";
                break;
            case LogUtil.ASSERT:
                levelStr = "【ASSERT】";
                break;
            case LogUtil.CRASH:
                levelStr = "【CRASH】";
                break;
        }
        // 线程名称
        String threadName = "【Thread: " + Thread.currentThread().getName() + "】";
        // 日志格式 2017.08.03 10:13:14【Error】 XXXXXX. 【Thread:Main】
        String newMessage = timeStr + levelStr + message + threadName;
        // do nothing on the calling thread, simply pass the tag/msg to the background thread
        handler.sendMessage(handler.obtainMessage(level, newMessage));
    }

    /**
     * 写日志的Handler
     * 日志文件存储文件夹：/sdcard/{fileName}/log/{date}20170805/
     * 日志文件：log_1.log log_2.log...
     */
    private static class WriteHandler extends Handler {

        private final String folder;
        private final long maxFileSize;

        WriteHandler(Looper looper, String folder, long maxFileSize) {
            super(looper);
            this.folder = folder;
            this.maxFileSize = maxFileSize;
        }

        @Override
        public void handleMessage(Message msg) {
            String content = (String) msg.obj;

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.UK);
            String formatDate = dateFormat.format(new Date());
            String logFolder = folder + File.separator + formatDate;

            FileWriter fileWriter = null;
            File logFile = getLogFile(logFolder, "log");

            try {
                fileWriter = new FileWriter(logFile, true);

                writeLog(fileWriter, content);

                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                if (fileWriter != null) {
                    try {
                        fileWriter.flush();
                        fileWriter.close();
                    } catch (IOException e1) { /* fail silently */ }
                }
            }
        }

        /**
         * This is always called on a single background thread.
         * Implementing classes must ONLY write to the fileWriter and nothing more.
         * The abstract class takes care of everything else including close the stream and catching IOException
         *
         * @param fileWriter an instance of FileWriter already initialised to the correct file
         */
        private void writeLog(FileWriter fileWriter, String content) throws IOException {
            fileWriter.append(content + System.getProperty("line.separator"));
        }

        private File getLogFile(String folderName, String fileName) {

            File folder = new File(folderName);
            if (!folder.exists()) {
                //TODO: What if folder is not created, what happens then?
                folder.mkdirs();
            }

            int newFileCount = 0;
            File newFile;
            File existingFile = null;

            newFile = new File(folder, String.format("%s_%s.log", fileName, newFileCount));
            while (newFile.exists()) {
                existingFile = newFile;
                newFileCount++;
                newFile = new File(folder, String.format("%s_%s.log", fileName, newFileCount));
            }

            if (existingFile != null) {
                if (existingFile.length() >= maxFileSize) {
                    return newFile;
                }
                return existingFile;
            }

            return newFile;
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {

        public static final String DEFAULT_FILE_NAME = "NewLogin-IM";

        private static final long MAX_BYTES = 10 * 1024 * 1024; // 10M per file

        private static final int LIMIT_DAY = 5; //默认允许保留5天

        String fileName;
        long fileSize;
        int limitDay = 5; // 允许保留日志天数，默认5天
        Handler handler;

        private Builder() {
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder fileSize(long fileSize) {
            this.fileSize = fileSize;
            return this;
        }

        public Builder limitDay(int limitDay) {
            this.limitDay = limitDay;
            return this;
        }

        public DiskLogStrategy build() {

            if (TextUtils.isEmpty(fileName)) {
                fileName = DEFAULT_FILE_NAME;
            }

            if (fileSize == 0) {
                fileSize = MAX_BYTES;
            }

            return new DiskLogStrategy(this);
        }
    }
}