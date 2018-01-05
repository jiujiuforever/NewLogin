package im.jizhu.com.loginmodule.imservice.callback;

import android.os.Handler;

import im.jizhu.com.loginmodule.utils.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : yingmu on 15-1-7.
 * @email : yingmu@mogujie.com.
 */
public class ListenerQueue {

    private static ListenerQueue listenerQueue = new ListenerQueue();

    private static final String TAG = "ListenerQueue";

    public static ListenerQueue instance(){
        return listenerQueue;
    }

    private volatile  boolean stopFlag = false;
    private volatile  boolean hasTask = false;


    //callback 队列
    private Map<Integer,Packetlistener> callBackQueue = new ConcurrentHashMap<>();
    private Handler timerHandler = new Handler();


    public void onStart(){
        Logger.d(TAG,"ListenerQueue#onStart run");
        stopFlag = false;
        startTimer();
    }
    public void onDestory(){
        Logger.d(TAG,"ListenerQueue#onDestory ");
        callBackQueue.clear();
        stopTimer();
    }

    //以前是TimerTask处理方式
    private void startTimer() {
        if(!stopFlag && hasTask == false) {
            hasTask = true;
            timerHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    timerImpl();
                    hasTask = false;
                    startTimer();
                }
            }, 5 * 1000);
        }
    }

    private void stopTimer(){
        stopFlag = true;
    }

    private void timerImpl() {
        long currentRealtime =   System.currentTimeMillis();//SystemClock.elapsedRealtime();

        for (Map.Entry<Integer, Packetlistener> entry : callBackQueue.entrySet()) {

            Packetlistener packetlistener = entry.getValue();
            Integer seqNo = entry.getKey();
            long timeRange = currentRealtime - packetlistener.getCreateTime();

            try {
                if (timeRange >= packetlistener.getTimeOut()) {
                    Logger.d(TAG,"ListenerQueue#find timeout msg");
                    Packetlistener listener = pop(seqNo);
                    if (listener != null) {
                        listener.onTimeout();
                    }
                }
            } catch (Exception e) {
                Logger.d(TAG,"ListenerQueue#timerImpl onTimeout is Error,exception is %s", e.getCause());
            }
        }
    }

    public void push(int seqNo,Packetlistener packetlistener){

        if(seqNo <= 0){
            Logger.d(TAG, "ListenerQueue#push error,seqNo error is " + seqNo);
            return;
        }
        if(packetlistener == null){
            Logger.d(TAG, "ListenerQueue#push error, packetlistener is null");
            return;
        }
        callBackQueue.put(seqNo,packetlistener);
    }


    public Packetlistener pop(int seqNo){
        synchronized (ListenerQueue.this) {
            if (callBackQueue.containsKey(seqNo)) {
                Packetlistener packetlistener = callBackQueue.remove(seqNo);
                return packetlistener;
            }
            return null;
        }
    }
}
