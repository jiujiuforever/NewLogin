package com.lesso.io;

import android.util.Log;

final class Network {

    private static IMSocketListener mSocketListener;

    private static Network mInstance;

    // 单例模式
    static Network getInstance(){
        if(mInstance == null){
            synchronized (Network.class){
                if(mInstance == null){
                    mInstance = new Network();
                }
            }
        }
        return mInstance;
    }

    /**
     * 设置链路监听器
     * @param socketListener
     */
    public void setSocketListener(IMSocketListener socketListener) {
        this.mSocketListener = socketListener;
    }

    /*****************************************************/
    /*****************************************************/
    /*****************************************************/

    public native void initEnv();

    public native void runEvent();

    public native int connect(String ip, int port);

    public native int getStatus(int handle);

    public native int write(int handle, byte[] message, int len);

    public native void close(int handle);

    public native byte[] netCheck(String host, int port, byte[]message, int len);


    /*****************************************************/
    /*****************************************************/
    /*****************************************************/

    public static void onRead(int handle, byte[] message, int len) {
        Log.i("Network", "onRead handler:" + handle);
        if(mSocketListener != null){
            mSocketListener.onRead(handle,message,len);
        }
    }

    /*
    enum
    {
        SOCKET_ERROR_TIMEOUT = 1,超时
        SOCKET_ERROR_READ = 2,读错误
        SOCKET_ERROR_WRITE = 3,写错误
        SOCKET_ERROR_EXCEPTION = 4,异常
        SOCKET_ERROR_CLOSE = 5,主动关闭
        SOCKET_ERROR_SYS = 6,系统错误
    };
    */
    public static void onClose(int handle, int reason) {
        Log.i("Network", "onClose:" + handle + ";reason:" + reason);
        if(mSocketListener != null){
            mSocketListener.onClose(handle,reason);
        }
    }


    /*
     enum
     {
     SOCKET_STATE_IDLE = 1,
     SOCKET_STATE_CONNECTING = 2,
     SOCKET_STATE_CONNECTED = 3,
     SOCKET_STATE_CLOSING = 4,
     };
     */
    public static void onConnect(int handle) {
        Log.i("Network", "onConnect handle:" + handle);
        if(mSocketListener != null){
            mSocketListener.onConnect(handle);
        }
    }
}
