package com.lesso.log;

import android.util.Log;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 * <p>
 * 需要在Application中注册，为了要在程序启动器就监控整个程序。
 */
public class CrashHandler implements UncaughtExceptionHandler {

    private static final String TAG = "CrashHandler";

    //系统默认的UncaughtException处理类
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //CrashHandler实例
    private static CrashHandler mInstance;

    /**
     * 保证只有一个CrashHandler实例
     */
    private CrashHandler() {
    }

    // 单例模式
    public static CrashHandler getInstance() {
        if (mInstance == null) {
            synchronized (CrashHandler.class) {
                if (mInstance == null) {
                    mInstance = new CrashHandler();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化
     */
    public void init() {
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
    }


    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }

        Log.e(TAG, "FATAL EXCEPTION : ", ex);

        //保存日志文件
        DiskLogAdapter diskLogAdapter = LogUtil.getInstance().getDiskLogAdapter();
        if (diskLogAdapter != null) {
            String message = Utils.getStackTraceString(ex);
            diskLogAdapter.log(LogUtil.CRASH, "CRASH", message);
        }
        return true;
    }
}