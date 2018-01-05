package com.lesso.io;

import android.text.TextUtils;
import android.util.Log;


/**
 * IM网络库-对外接口
 * Created by zhengxiaoming on 2017/7/16.
 */
public class IMNetwork {

    static {
        System.loadLibrary("network");
    }

    private static final String TAG = "IMNetwork";
    private static volatile IMNetwork mInstance;

    // 是否初始化
    private boolean isInit = false;
    // 是否运行
    private boolean isRun = false;

    public static IMNetwork getInstance() {
        if (mInstance == null) {
            synchronized (IMNetwork.class) {
                if (mInstance == null) {
                    mInstance = new IMNetwork();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化环境，与{@link #runEvent()}一起使用
     */
    public void initEnv() {
        if (isInit) {
            Log.e(TAG, "IMNetwork is already init");
            return;
        }
        isInit = true;
        Log.i(TAG, "IMNetWork initEnv");
        Network.getInstance().initEnv();
    }

    /**
     * 运行环境，与{@link #initEnv()}一起使用
     */
    public void runEvent() {
        if (isRun) {
            Log.e(TAG, "IMNetwork is already runEvent");
            return;
        }
        isRun = true;
        Log.i(TAG, "IMNetWork runEvent");
        Network.getInstance().runEvent();
    }

    /**
     * 建立链接
     *
     * @param ip   建立链接的IP
     * @param port 建立链接的端口
     * @return 是否成功 0 代表成功
     */
    public int connect(String ip, int port) {
        Log.i(TAG, "IMNetWork connect");
        if (TextUtils.isEmpty(ip) || port == 0) {
            Log.e(TAG, "Network connect ip or port is null");
            return -1;
        }
        return Network.getInstance().connect(ip, port);
    }

    /**
     * 获取链接状态
     * #define NET_STATE_CONNECTED      (0x00)
     * #define NET_STATE_CLOSED         (0x01)
     * #define NET_STATE_CONNECTING     (0x02)
     * #define NET_STATE_INVALID_HANDLE (0x03)
     *
     * @param handle 链接句柄ID
     * @return 链接状态
     */
    public int getStatus(int handle) {
        Log.i(TAG, "IMNetWork getStatus");
        if (handle < 0) {
            Log.e(TAG, "Network getStatus handle  is below zero");
            return -1;
        }
        return Network.getInstance().getStatus(handle);
    }

    /**
     * 发送数据
     *
     * @param handle  链路句柄ID
     * @param message 发送数据包
     * @param len     发送数据包长度
     * @return 发送结果 <0代表发送失败
     */
    public int write(int handle, byte[] message, int len) {
        Log.i(TAG, "IMNetWork write");
        if (handle < 0) {
            Log.e(TAG, "Network write handle  is below zero");
            return -1;
        }
        return Network.getInstance().write(handle, message, len);
    }

    /**
     * 关闭链路
     *
     * @param handle 链路句柄ID
     */
    public void close(int handle) {
        Log.i(TAG, "IMNetWork close");
        if (handle < 0) {
            Log.e(TAG, "Network close handle is below zero");
            return;
        }
        Network.getInstance().close(handle);
    }

    /**
     * 网络探测
     *
     * @param host    探测IP
     * @param port    探测端口
     * @param message 发送内容
     * @param len     发送内容长度
     */
    public byte[] netCheck(String host, int port, byte[] message, int len) {
        Log.i(TAG, "IMNetWork netCheck");
        if (TextUtils.isEmpty(host) || port <= 0) {
            Log.e(TAG, "Network netCheck ip or port is null");
            return null;
        }
        return Network.getInstance().netCheck(host, port, message, len);
    }

    /**
     * 设置链路链接监听器
     * 只允许存在一个链路监听器，由于JNI层使用的是static方法获取
     */
    public void setSocketListener(IMSocketListener listener) {
        Network.getInstance().setSocketListener(listener);
    }
}
