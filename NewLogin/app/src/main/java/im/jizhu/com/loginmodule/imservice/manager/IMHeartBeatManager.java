package im.jizhu.com.loginmodule.imservice.manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

import im.jizhu.com.loginmodule.imservice.callback.Packetlistener;
import im.jizhu.com.loginmodule.protobuf.CCBaseDefine;
import im.jizhu.com.loginmodule.protobuf.IMBaseDefine;
import im.jizhu.com.loginmodule.protobuf.IMOther;
import im.jizhu.com.loginmodule.utils.Logger;

/**
 * @author : yingmu on 15-3-26.
 * @email : yingmu@mogujie.com.
 *
 * 备注: 之前采用netty(3.6.6-fianl)支持通道检测IdleStateHandler,发现有些机型
 * 手机休眠之后IdleStateHandler 定时器HashedWheelTimer可能存在被系统停止关闭的现象
 * 所以采用AlarmManager 进行心跳的检测
 *
 * 登陆之后就开始触发心跳检测 【仅仅是在线，重练就会取消的】
 * 退出reset 会释放alarmManager 资源
 */
public class IMHeartBeatManager  extends  IMManager{
    // 心跳检测4分钟检测一次，并且发送心跳包
    // 服务端自身存在通道检测，5分钟没有数据会主动断开通道

    private static final String TAG = "IMHeartBeatManager";

    private static IMHeartBeatManager inst = new IMHeartBeatManager();
    public static IMHeartBeatManager instance() {
        return inst;
    }

    private final int HEARTBEAT_INTERVAL = 4 * 60 * 1000;
    private final String ACTION_SENDING_HEARTBEAT = "com.lesso.cc.imservice.manager.imheartbeatmanager";
    private PendingIntent pendingIntent;

    @Override
    public void doOnStart() {
    }

    // 登陆成功之后
    public void onloginNetSuccess(){
        Logger.e(TAG,"heartbeat#onLocalNetOk");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_SENDING_HEARTBEAT);
        Logger.d(TAG,"heartbeat#register actions");
        ctx.registerReceiver(imReceiver, intentFilter);
        //获取AlarmManager系统服务
        scheduleHeartbeat(HEARTBEAT_INTERVAL);
    }

    @Override
    public void reset() {
        Logger.d(TAG,"heartbeat#reset begin");
        try {
            ctx.unregisterReceiver(imReceiver);
            cancelHeartbeatTimer();
            Logger.d(TAG,"heartbeat#reset stop");
        }catch (Exception e){
            Logger.e(TAG,"heartbeat#reset error:%s",e.getCause());
        }
    }

    // MsgServerHandler 直接调用
    public void onMsgServerDisconn(){
        Logger.w(TAG,"heartbeat#onChannelDisconn");
        cancelHeartbeatTimer();
    }

    private void cancelHeartbeatTimer() {
        Logger.w(TAG,"heartbeat#cancelHeartbeatTimer");
        if (pendingIntent == null) {
            Logger.w(TAG,"heartbeat#pi is null");
            return;
        }
        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pendingIntent);
    }


    private void scheduleHeartbeat(int seconds){
        Logger.d(TAG,"heartbeat#scheduleHeartbeat every %d seconds", seconds);
        if (pendingIntent == null) {
            Logger.w(TAG,"heartbeat#fill in pendingintent");
            Intent intent = new Intent(ACTION_SENDING_HEARTBEAT);
            pendingIntent = PendingIntent.getBroadcast(ctx, 0, intent, 0);
            if (pendingIntent == null) {
                Logger.w(TAG,"heartbeat#scheduleHeartbeat#pi is null");
                return;
            }
        }

        AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + seconds, seconds, pendingIntent);
    }


    /**--------------------boradcast-广播相关-----------------------------*/
    private BroadcastReceiver imReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Logger.w(TAG,"heartbeat#im#receive action:%s", action);
            if (action.equals(ACTION_SENDING_HEARTBEAT)) {
                sendHeartBeatPacket();
            }
        }
    };

    public void sendHeartBeatPacket(){
        Logger.d(TAG,"heartbeat#reqSendHeartbeat");
        PowerManager pm = (PowerManager) ctx.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "lessoCC_heartBeat_wakelock");
        wl.acquire();
        try {
            final long timeOut = 5*1000;
            IMOther.IMHeartBeat imHeartBeat = IMOther.IMHeartBeat.newBuilder()
                    .build();
            int sid = CCBaseDefine.ServiceID.SID_LOGIN_VALUE;
            int cid = CCBaseDefine.LoginCmdID.LOGIN_HEART_BEAT_ACK_VALUE;
//            int sid = IMBaseDefine.ServiceID.SID_OTHER_VALUE;
//            int cid = IMBaseDefine.OtherCmdID.CID_OTHER_HEARTBEAT_VALUE;
            IMSocketManager.instance().sendRequest(imHeartBeat,sid,cid,new Packetlistener(timeOut) {
                @Override
                public void onSuccess(Object response) {
                    Logger.d(TAG,"heartbeat#心跳成功，链接保活");
                }

                @Override
                public void onFaild() {
                    Logger.w(TAG,"heartbeat#心跳包发送失败");
                    IMSocketManager.instance().onMsgServerDisconn();
                }

                @Override
                public void onTimeout() {
                    Logger.w(TAG,"heartbeat#心跳包发送超时");
                    IMSocketManager.instance().onMsgServerDisconn();
                }
            });
            Logger.d(TAG,"heartbeat#send packet to server");
        } finally {
            wl.release();
        }
    }
}