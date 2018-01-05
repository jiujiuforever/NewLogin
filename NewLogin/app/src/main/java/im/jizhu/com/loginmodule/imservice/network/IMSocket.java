package im.jizhu.com.loginmodule.imservice.network;

import android.text.TextUtils;

import com.lesso.io.IMNetwork;
import com.lesso.io.IMSocketListener;
import im.jizhu.com.loginmodule.imservice.event.QueuueHandlerEvent;
import im.jizhu.com.loginmodule.imservice.manager.IMHeartBeatManager;
import im.jizhu.com.loginmodule.imservice.manager.IMSocketManager;
import im.jizhu.com.loginmodule.protobuf.base.DataBuffer;
import im.jizhu.com.loginmodule.utils.Logger;

import java.util.concurrent.LinkedBlockingQueue;

import de.greenrobot.event.EventBus;

/**
 * 链路通信模块
 * Created by qzy on 2017/8/12.
 */
public class IMSocket {

    /**
     * 日志
     */
    private static final String TAG = "SocketNetwork";

    /**
     * 当前的SocketHandler
     */
    private int mCurrentSocketHandle = -1;

    private static IMSocket mInstance;

    private LinkedBlockingQueue<DataBuffer> queue = new LinkedBlockingQueue<DataBuffer>();
    private boolean isNullQueue = true;

    // 单例模式
    public static IMSocket getInstance(){
        if(mInstance == null){
            synchronized (IMSocket.class){
                if(mInstance == null){
                    mInstance = new IMSocket();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化
     */
    public void init() {

        Logger.d(TAG,"IMSocket#init");
        IMNetwork.getInstance().initEnv();
        IMNetwork.getInstance().runEvent();
        // 建立Socket链接回调
        IMNetwork.getInstance().setSocketListener(new IMSocketListener() {
            @Override
            public void onConnect(int handle) {
                Logger.d(TAG, "IMSocket#onConnect handle = " + handle);
                IMHeartBeatManager.instance().onloginNetSuccess();
                onConnected(handle);
            }

            @Override
            public void onRead(int handle, byte[] message, int len) {
                Logger.d(TAG, "IMSocket#onRead handle = " + handle);
                onMessageRead(handle,message,len);
            }

            @Override
            public void onClose(int handle, int reason) {
                Logger.e(TAG, "IMSocket#onClose handle = " + handle+",reason = " + reason);
                onClosed(handle,reason);
            }
        });
    }

    /**
     * 建立链接
     * @param host  链接IP
     * @param port  链接端口
     * @return 链接结果，需要通过onConnect回调收到是否链接成功
     */
    public int connect(String host,int port){
        Logger.d(TAG,"IMSocket#connect");
        if(TextUtils.isEmpty(host) || port <= 0){
            Logger.e(TAG,"IMSocket#connect host or port is illegal");
            return -1;
        }
        int result = IMNetwork.getInstance().connect(host,port);
        if(result < 0){
            // 链接异常返回
            IMSocketManager.instance().onConnectMsgServerFail();
        }
        Logger.d(TAG,"IMSocket#connect result = " + result);
        return result;
    }

    /**
     * 发送数据
     * @param message 发送的数据包
     * @param len     发送的数据包长度
     * @return 发送结果 -1代表失败
     */
    public int send(byte[] message, int len){
        if(mCurrentSocketHandle < 0){
            Logger.e(TAG,"IMSocket#send socketHandler is below zero");
            return -1;
        }

        if(message == null || message.length <= 0){
            Logger.e(TAG,"IMSocket#send message is null");
            return -1;
        }

        if(len <= 0){
            Logger.e(TAG,"IMSocket#send len is 0");
            return -1;
        }

        int result = IMNetwork.getInstance().write(mCurrentSocketHandle,message,len);
        Logger.d(TAG,"IMSocket#send result is " + result);
        return result;
    }

    /**
     * 关闭当前Socket链路
     */
    public void close(){
        if(mCurrentSocketHandle < 0){
            Logger.e(TAG,"IMSocket#close socketHandler is below zero");
            return;
        }

        IMNetwork.getInstance().close(mCurrentSocketHandle);
    }

    /**
     * 获取链接状态
     * #define NET_STATE_CONNECTED      (0x00)
     * #define NET_STATE_CLOSED         (0x01)
     * #define NET_STATE_CONNECTING     (0x02)
     * #define NET_STATE_INVALID_HANDLE (0x03)
     * @return 链接状态
     */
    private NetState getStatus(){
        if(mCurrentSocketHandle < 0){
            return NetState.NET_STATE_INVALID_HANDLE;
        }

        int status = IMNetwork.getInstance().getStatus(mCurrentSocketHandle);
        return NetState.valueOf(status);
    }

    /**
     * 是否链接中
     * @return 链接情况 true
     */
    public boolean isConnected(){
        NetState status = getStatus();
        if(status == NetState.NET_STATE_CONNECTED){
            return true;
        }
        return false;
    }

    /**********************************************************************/
    /******************************  网络库底层回调  ************************/
    /**********************************************************************/

    /**
     * 网络链接状况回调
     * @param handle
     */
    private void onConnected(int handle) {
        mCurrentSocketHandle = handle;
        // 注册Eventbus
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        IMSocketManager.instance().onMsgServerConnected();


    }

    /**
     * 收到消息报文回调处理
     * @param handle
     * @param message
     * @param len
     */
    private void onMessageRead(int handle, final byte[] message, final int len) {
        if(mCurrentSocketHandle != handle){
            Logger.e(TAG, "channel#messageReceived error,handle is not equal currentHandler");
            return;
        }

        if(message == null){
            Logger.e(TAG, "channel#messageReceived error,message is null");
            return;
        }
        if(len <= 0){
            Logger.e(TAG, "channel#messageReceived error,message len is zero");
            return;
        }

        Logger.d(TAG, "channel#messageReceived message len : %d",len);

        /** 此处需要另起线程，不能跟网络库onRead同个线程 **/
        /** 若是同一个线程，会出现byte数组被释放的Crash **/
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] copy = new byte[len];
                System.arraycopy(message, 0, copy, 0, len);
                DataBuffer dataBuffer = new DataBuffer(copy,len);

                // IMSocketManager.instance().packetDispatch(channelBuffer);

                if (dataBuffer != null) {
                    queue.offer(dataBuffer);
                    if (isNullQueue) {
                        //空队列，可以直接执行(但为了程序号控制，先进队列，然后直接执行)
                        EventBus.getDefault().post(QueuueHandlerEvent.MESSAGE_START);
                    }
                }
            }
        }).start();
    }

    /**
     * 收到断开报文回调处理
     * @param handle
     * @param reason
     */
    private void onClosed(int handle, int reason) {
        mCurrentSocketHandle = -1;
        IMSocketManager.instance().onMsgServerDisconn();
        IMHeartBeatManager.instance().onMsgServerDisconn();
    }

    /**
     * 监听Event数据处理
     * @param event
     */
    public void onEvent(QueuueHandlerEvent event) {
        switch (event) {
            case MESSAGE_START:
                DataBuffer dataBuffer = queue.poll();
                if (dataBuffer == null) {
                    //队列已经空了
                    isNullQueue = true;
                } else {
                    IMSocketManager.instance().packetDispatch(dataBuffer);
                }
                break;
            case MESSAGE_FINISH:
                DataBuffer dataBuffer1 = queue.poll();
                if (dataBuffer1 == null) {
                    isNullQueue = true;
                } else {
                    IMSocketManager.instance().packetDispatch(dataBuffer1);
                }
                break;
        }
    }

}
