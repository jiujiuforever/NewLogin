package com.lesso.io;

/**
 * Created by zhengxiaoming on 2017/7/17.
 */
public interface IMSocketListener {
    /**
     * 链接建立成功
     * @param handle
     */
    void onConnect(int handle);

    /**
     * 链接数据读取
     * @param handle
     * @param message
     * @param len
     */
    void onRead(int handle, byte[] message, int len);

    /**
     * 关闭链接
     * @param handle
     * @param reason
     */
    void onClose(int handle, int reason);
}
