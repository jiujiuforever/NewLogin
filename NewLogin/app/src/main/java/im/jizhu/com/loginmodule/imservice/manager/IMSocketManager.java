package im.jizhu.com.loginmodule.imservice.manager;

import android.text.TextUtils;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.GeneratedMessageLite;
import im.jizhu.com.loginmodule.DB.sp.SystemConfigSp;
import im.jizhu.com.loginmodule.config.SysConstant;
import im.jizhu.com.loginmodule.imservice.callback.ListenerQueue;
import im.jizhu.com.loginmodule.imservice.callback.Packetlistener;
import im.jizhu.com.loginmodule.imservice.event.QueuueHandlerEvent;
import im.jizhu.com.loginmodule.imservice.event.SocketEvent;
import im.jizhu.com.loginmodule.imservice.network.IMSocket;
import im.jizhu.com.loginmodule.protobuf.CCBaseDefine;
import im.jizhu.com.loginmodule.protobuf.IMBaseDefine;
import im.jizhu.com.loginmodule.protobuf.base.DataBuffer;
import im.jizhu.com.loginmodule.protobuf.base.DefaultHeader;
import im.jizhu.com.loginmodule.utils.Logger;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/**
 * @author : yingmu on 14-12-30.
 * @email : yingmu@mogujie.com.
 *
 * 业务层面:
 * 长连接建立成功之后，就要发送登陆信息，否则15s之内就会断开
 * 所以connMsg 与 login是强耦合的关系
 */
public class IMSocketManager extends IMManager {

    private static final String TAG = "SocketNetwork";

    private ListenerQueue listenerQueue = ListenerQueue.instance();

    // 请求消息服务器地址
    private AsyncHttpClient client = new AsyncHttpClient();


    /**
     * 快速重新连接的时候需要
     */
    private MsgServerAddrsEntity currentMsgAddress = null;

    /**
     * 自身状态
     */
    private SocketEvent socketStatus = SocketEvent.NONE;

    private int count;

    private static final IMSocketManager inst = new IMSocketManager();

    private IMSocketManager() {
    }

    public static IMSocketManager instance() {
        return inst;
    }

    /**
     * 获取Msg地址，等待链接
     */
    @Override
    public void doOnStart() {
        socketStatus = SocketEvent.NONE;
        IMSocket.getInstance().init();
    }


    //todo check
    @Override
    public void reset() {
        disconnectMsgServer();
        socketStatus = SocketEvent.NONE;
        currentMsgAddress = null;
    }

    /**
     * 实现自身的事件驱动
     * @param event
     */
    public void triggerEvent(SocketEvent event) {
       setSocketStatus(event);
       EventBus.getDefault().postSticky(event);
    }

    /**-------------------------------功能方法--------------------------------------*/

    public void sendRequest(GeneratedMessageLite requset,int sid,int cid){
        sendRequest(requset, sid, cid, null);
    }


    /**
     * todo check exception
     * */
    public void sendRequest(GeneratedMessageLite requset,int sid,int cid,Packetlistener packetlistener){
        int seqNo = 0;
        try{
            //组装包头 header
            im.jizhu.com.loginmodule.protobuf.base.Header header = new DefaultHeader(sid, cid);
            int bodySize = requset.getSerializedSize();
            header.setLength(SysConstant.PROTOCOL_HEADER_LENGTH + bodySize);
            seqNo = header.getSeqnum();
            listenerQueue.push(seqNo, packetlistener);

            DataBuffer headerBuffer = header.encode();
            DataBuffer bodyBuffer = new DataBuffer(bodySize);
            bodyBuffer.writeBytes(requset.toByteArray());

            DataBuffer buffer = new DataBuffer(SysConstant.PROTOCOL_HEADER_LENGTH + bodySize);
            buffer.writeBytes(headerBuffer.array());
            buffer.writeBytes(bodyBuffer.array());

            byte[] message = buffer.array();
            int len = message.length;

            //IMSocket.getInstance().send(message,len);
            int result = IMSocket.getInstance().send(message,len);
            // Socket发送结果，小于0代表发送失败
            if(result < 0){
                Logger.e(TAG,"#sendRequest#result:"+result);
                if (packetlistener != null) {
                    packetlistener.onFaild();
                }
                listenerQueue.pop(seqNo);
            }

        }catch (Exception e){

            e.printStackTrace();
            Logger.e(TAG,"#sendRequest#exception:"+e.getMessage());
            if (packetlistener != null) {
                packetlistener.onFaild();
            }
            listenerQueue.pop(seqNo);
        }
    }

    public void packetDispatch(DataBuffer buffer){
        Logger.d(TAG, "channel#packetDispatch");
        im.jizhu.com.loginmodule.protobuf.base.Header header = new im.jizhu.com.loginmodule.protobuf.base.Header();
        header.decode(buffer);
        /**buffer 的指针位于body的地方*/
        int commandId = header.getCommandId();
        int serviceId = header.getServiceId();
        int seqNo = header.getSeqnum();
        Logger.d(TAG,"dispatch packet, serviceId:%d, commandId:%d", serviceId,commandId);
        CodedInputStream codedInputStream = CodedInputStream.newInstance(buffer.getOrignalBuffer());

        Packetlistener listener = listenerQueue.pop(seqNo);
        if (listener != null) {
            listener.onSuccess(codedInputStream);
            return;
        }
        // todo eric make it a table
        // 抽象 父类执行
        switch (serviceId) {

            case CCBaseDefine.ServiceID.SID_LOGIN_VALUE:
                IMPacketDispatcher.otherPacketDispatcher(commandId, codedInputStream);
                break;

            case CCBaseDefine.ServiceID.SID_BASEINFO_VALUE:
                IMPacketDispatcher.otherPacketDispatcher(commandId, codedInputStream);
                break;

            case CCBaseDefine.ServiceID.SID_ORG_VALUE:
                IMPacketDispatcher.otherPacketDispatcher(commandId, codedInputStream);
                break;

            case CCBaseDefine.ServiceID.SID_SESSION_VALUE:
                IMPacketDispatcher.otherPacketDispatcher(commandId, codedInputStream);
                break;

            case CCBaseDefine.ServiceID.SID_SESSION_RECENT_VALUE:
                IMPacketDispatcher.otherPacketDispatcher(commandId, codedInputStream);
                break;

            case CCBaseDefine.ServiceID.SID_MESSAGE_VALUE:
                IMPacketDispatcher.otherPacketDispatcher(commandId, codedInputStream);
                break;

            case CCBaseDefine.ServiceID.SID_MESSAGE_HISTORY_VALUE:
                IMPacketDispatcher.otherPacketDispatcher(commandId, codedInputStream);
                break;

            case CCBaseDefine.ServiceID.SID_MESSAGE_PUSH_VALUE:
                IMPacketDispatcher.otherPacketDispatcher(commandId, codedInputStream);
                break;

            case CCBaseDefine.ServiceID.SID_SERVICE_COMMUNICATION_VALUE:
                IMPacketDispatcher.otherPacketDispatcher(commandId, codedInputStream);
                break;

//            case IMBaseDefine.ServiceID.SID_OTHER_VALUE:
//                IMPacketDispatcher.otherPacketDispatcher(commandId, codedInputStream);
//                break;
//
//            case IMBaseDefine.ServiceID.SID_LOGIN_VALUE:
//                IMPacketDispatcher.loginPacketDispatcher(commandId, codedInputStream);
//                break;
//            case IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE:
//                IMPacketDispatcher.buddyPacketDispatcher(commandId, codedInputStream);
//                break;
//            case IMBaseDefine.ServiceID.SID_MSG_VALUE:
//                IMPacketDispatcher.msgPacketDispatcher(commandId, codedInputStream);
//                break;
//            case IMBaseDefine.ServiceID.SID_GROUP_VALUE:
//                IMPacketDispatcher.groupPacketDispatcher(commandId, codedInputStream);
//                break;
//            default:
//                Logger.e(TAG, "packet#unhandled serviceId:" + serviceId + ", commandId:" + commandId);
//                break;
        }
    }

    /**
     * 新版本流程如下
     1.客户端通过域名获得login_server的地址
     2.客户端通过login_server获得msg_serv的地址
     3.客户端带着用户名密码对msg_serv进行登录
     4.msg_serv转给db_proxy进行认证（do not care on client）
     5.将认证结果返回给客户端
     */
    public void reqMsgServerAddrs() {


        Logger.d(TAG,"socket#reqMsgServerAddrs.");

        client.setUserAgent("Android-TT");
        client.setConnectTimeout(5000);
//        client.get(SystemConfigSp.instance().getStrConfig(SystemConfigSp.SysCfgDimension.LOGINSERVER), new BaseJsonHttpResponseHandler() {
        client.get("http://server1.lesso.com:6120/msg_server", new BaseJsonHttpResponseHandler() {
//        client.get("http://192.168.4.168:8082/msg_server", new BaseJsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Header[] headers, String s, Object o) {
                Logger.d(TAG,"socket#req msgAddress onSuccess, response:%s", s);
                MsgServerAddrsEntity msgServer = (MsgServerAddrsEntity) o;
                if (msgServer == null) {
                    triggerEvent(SocketEvent.REQ_MSG_SERVER_ADDRS_FAILED);
                    return;
                }

                SystemConfigSp.instance().setStrConfig(SystemConfigSp.SysCfgDimension.REQ_SERVER_RESPONSE, s);

                connectMsgServer(msgServer);
                triggerEvent(SocketEvent.REQ_MSG_SERVER_ADDRS_SUCCESS);
            }

            @Override
            public void onFailure(int i, Header[] headers, Throwable throwable, String responseString, Object o) {
                Logger.e(TAG,"socket#req msgAddress Failure, errorResponse:%s", responseString);
                SystemConfigSp.instance().setStrConfig(SystemConfigSp.SysCfgDimension.REQ_SERVER_RESPONSE, "");
                // 这里判断下是否已登录过
                if(!IMLoginManager.instance().isEverLogined()){

                    if (count < 3) {
                        count++;
                        //做个计数来启用备用IP地址
                        triggerEvent(SocketEvent.REQ_MSG_SERVER_ADDRS_RESERVE);
                    } else {
                        count = 0;
                        triggerEvent(SocketEvent.REQ_MSG_SERVER_ADDRS_FAILED);
                    }
                }else{
                    triggerEvent(SocketEvent.REQ_MSG_SERVER_ADDRS_FAILED);
                }

            }

            @Override
            protected Object parseResponse(String s, boolean b) throws Throwable {
                /*子类需要提供实现，将请求结果解析成需要的类型 异常怎么处理*/
                JSONObject jsonObject = new JSONObject(s);
                MsgServerAddrsEntity msgServerAddrsEntity = onRepLoginServerAddrs(jsonObject);
                return msgServerAddrsEntity;
            }
        });
    }

    /**
     * 与登陆login是强耦合的关系
     */
    private void connectMsgServer(MsgServerAddrsEntity currentMsgAddress) {
        triggerEvent(SocketEvent.CONNECTING_MSG_SERVER);
        this.currentMsgAddress = currentMsgAddress;

        String priorIP = currentMsgAddress.priorIP;
        int port = currentMsgAddress.port;
        Logger.i(TAG, "login#connectMsgServer -> (%s:%d)", priorIP, port);

        IMSocket.getInstance().connect(priorIP, port);
    }

    public void reconnectMsg(){
//        synchronized (IMSocketManager.class) {
//            if (currentMsgAddress != null) {
//
//                SystemConfigSp.instance().setIntConfig(SystemConfigSp.SysCfgDimension.LOGIND_TYPE, 2);//重连登录状态
//
//                connectMsgServer(currentMsgAddress);
//            } else {
//                disconnectMsgServer();
//                IMLoginManager.instance().relogin();
//            }
//        }
    }

    /**
     * 断开与msg的链接
     */
    public void disconnectMsgServer() {
        listenerQueue.onDestory();
        Logger.i(TAG, "login#disconnectMsgServer");
        IMSocket.getInstance().close();
    }

    /**判断链接是否处于断开状态*/
    public boolean isSocketConnect(){
        return IMSocket.getInstance().isConnected();
    }

    public void onMsgServerConnected() {
        Logger.i(TAG, "login#onMsgServerConnected");
        listenerQueue.onStart();
        triggerEvent(SocketEvent.CONNECT_MSG_SERVER_SUCCESS);
        IMLoginManager.instance().reqLoginMsgServer();

        // 设置链接的IP为空
        String connectServerStr = "";
        if(currentMsgAddress != null){
            connectServerStr = currentMsgAddress.priorIP + ":" + currentMsgAddress.port;
        }
        SystemConfigSp.instance().setStrConfig(SystemConfigSp.SysCfgDimension.CONNECT_MSG_SERVER, connectServerStr);
    }

    /**
     * 抽离出仅仅不同设备登录通知退出状态
     */
    public void onMsgDeviceLogin() {
        //MSG_SERVER_DEVICE_DISCONNECTED
        //Logger.e("test", "onMsgDeviceLogin");
        disconnectMsgServer();
        triggerEvent(SocketEvent.MSG_SERVER_DEVICE_DISCONNECTED);
        EventBus.getDefault().post(QueuueHandlerEvent.MESSAGE_FINISH);

        // 设置链接的IP为空
        SystemConfigSp.instance().setStrConfig(SystemConfigSp.SysCfgDimension.CONNECT_MSG_SERVER, null);

    }

    /**
     * 1. kickout 被踢出会触发这个状态   -- 不需要重连
     * 2. 心跳包没有收到 会触发这个状态   -- 链接断开，重连
     * 3. 链接主动断开                 -- 重连
     * 之前的长连接状态 connected
     */
    // 先断开链接
    // only 2 threads(ui thread, network thread) would request sending  packet
    // let the ui thread to close the connection
    // so if the ui thread has a sending task, no synchronization issue
    public void onMsgServerDisconn(){
        Logger.w(TAG, "login#onMsgServerDisconn");
        disconnectMsgServer();
        triggerEvent(SocketEvent.MSG_SERVER_DISCONNECTED);
        EventBus.getDefault().post(QueuueHandlerEvent.MESSAGE_FINISH);
    }

    /** 之前没有连接成功*/
    public void onConnectMsgServerFail(){
        triggerEvent(SocketEvent.CONNECT_MSG_SERVER_FAILED);
    }


    /**----------------------------请求Msg server地址--实体信息--------------------------------------*/
    /**请求返回的数据*/
    private class MsgServerAddrsEntity {
        int code;
        String msg;
        String priorIP;
        int port;
        @Override
        public String toString() {
            return "LoginServerAddrsEntity{" +
                    "code=" + code +
                    ", msg='" + msg + '\'' +
                    ", priorIP='" + priorIP + '\'' +
                    ", port=" + port +
                    '}';
        }
    }

    private MsgServerAddrsEntity onRepLoginServerAddrs(JSONObject json)
            throws JSONException {

        Logger.d(TAG,"login#onRepLoginServerAddrs");

        if (json == null) {
            Logger.e(TAG,"login#json is null");
            return null;
        }

        Logger.d(TAG,"login#onRepLoginServerAddrs json:%s", json);

        int code = json.getInt("code");
        if (code != 0) {
            Logger.e(TAG,"login#code is not right:%d, json:%s", code, json);
            return null;
        }

//        //服务器IP,端口
//        String priorIP = json.getString("priorIP");
//        int port = json.getInt("port");

        //服务器IP,端口
        String priorIP = json.getString("msg_server_backup");
        int port = json.getInt("msg_port");


        //文件服务器
        if(json.has("filePrior"))
        {
            String filePrior = json.getString("filePrior");
            String fileBackup = json.getString("fileBackup");
            if(!TextUtils.isEmpty(filePrior))
            {
                SystemConfigSp.instance().setStrConfig(SystemConfigSp.SysCfgDimension.MSFSSERVER,filePrior);
            }
            else
            {
                SystemConfigSp.instance().setStrConfig(SystemConfigSp.SysCfgDimension.MSFSSERVER,fileBackup);
            }
        }

        MsgServerAddrsEntity addrsEntity = new MsgServerAddrsEntity();
        addrsEntity.priorIP = priorIP;
        addrsEntity.port = port;
        Logger.d(TAG,"login#got loginserverAddrsEntity:%s", addrsEntity);
        return addrsEntity;
    }

    /**------------get/set----------------------------*/
    public SocketEvent getSocketStatus() {
        return socketStatus;
    }

    public void setSocketStatus(SocketEvent socketStatus) {
        this.socketStatus = socketStatus;
    }
}
