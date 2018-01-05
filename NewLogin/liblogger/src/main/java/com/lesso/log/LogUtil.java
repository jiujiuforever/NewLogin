package com.lesso.log;

import android.content.Context;

/**
 * 对外LogUtil接口
 * Created by it026 on 2017/8/23.
 */
public class LogUtil {

    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int ASSERT = 6;
    public static final int CRASH = 7;

    private Printer printer = new LoggerPrinter();

    private DiskLogAdapter diskLogAdapter;

    private static LogUtil mInstance;

    private LogUtil() {
        // 默认开启Android Log
        printer.addAdapter(new AndroidLogAdapter());
    }

    // 单例模式
    public static LogUtil getInstance() {
        if (mInstance == null) {
            synchronized (LogUtil.class) {
                if (mInstance == null) {
                    mInstance = new LogUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化
     * @param context 上下文对象
     */
    public void init(Context context){
        // 初始化CrashHandler
        CrashHandler.getInstance().init();
    }

    /**
     * 开启本地日志
     * @param fileName 文件名称
     * @param fileSize 文件大小
     * @param limitDay 只允许保留X天日志
     */
    public void diskLog(String fileName,long fileSize, int limitDay) {
        diskLogAdapter = new DiskLogAdapter(fileName,fileSize,limitDay);
        printer.addAdapter(diskLogAdapter);
    }

    /**
     * 关闭本地日志
     */
    public void unDiskLog() {
        if (diskLogAdapter != null) {
            printer.removeAdapter(diskLogAdapter);
        }
    }

    /**
     * 获取文件日志Adapter
     * @return
     */
    protected DiskLogAdapter getDiskLogAdapter(){
        return diskLogAdapter;
    }
    private void addLogAdapter(LogAdapter adapter) {
        printer.addAdapter(adapter);
    }

    public void clearLogAdapters() {
        printer.clearAdapters();
    }

    public void d(String tag, String message, Object... args) {
        printer.d(tag, message, args);
    }

    public void e(String tag, String message, Object... args) {
        printer.e(tag, null, message, args);
    }

    public void e(String tag, Throwable throwable, String message, Object... args) {
        printer.e(tag, throwable, message, args);
    }

    public void i(String tag, String message, Object... args) {
        printer.i(tag, message, args);
    }

    public void v(String tag, String message, Object... args) {
        printer.v(tag, message, args);
    }

    public void w(String tag, String message, Object... args) {
        printer.w(tag, message, args);
    }

    // TODO: 2017/8/20 上传接口未提供
//    /**
//     * 上传保存到本地的日志压缩包
//     */
//    public void uploadLog(){
//        if (diskLogAdapter == null) {
//            return;
//        }
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                String logFilePath = diskLogAdapter.getLogFilePath();
//                File logFile = new File(logFilePath);
//                if(!logFile.exists()){
//                    return;
//                }
//
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.UK);
//                String formatDate = dateFormat.format(new Date());
//                // zip路径：/sdcard/{fileName}/{date.zip}20170805.zip
//                final String destZipFilePath = diskLogAdapter.getFilePath() + File.separator + formatDate + ".zip";
//                // 压缩ZIP
//                ZipUtil.createZipFile(logFilePath, destZipFilePath);
//                // 上传日志
//                // TODO
//                UploadUtil.upLoadFile("", destZipFilePath, new UploadUtil.ReqCallBack() {
//                    @Override
//                    public void onReqSuccess() {
//                        Log.d("UploadLog","Upload log >>>>>> success");
//                        File zipFile = new File(destZipFilePath);
//                        FileUtil.deleteFile(zipFile);
//                    }
//
//                    @Override
//                    public void onReqFailed(String errorMsg) {
//                        Log.d("UploadLog","Upload log >>>>>> fail");
//                    }
//                });
//            }
//        }).start();
//
//    }
}
