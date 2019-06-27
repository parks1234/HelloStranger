package com.hbrc.srcapp.Receiver;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;

import com.hbrc.srcapp.Bean.Cmd_list;
import com.hbrc.srcapp.utils.ChipInfo;
import com.hbrc.srcapp.utils.Gsonuntils;

import java.lang.ref.WeakReference;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

/**
 * Created by  王学波 on 2019/1/22.
 */
public class BackService extends Service {

    /**
     * 心跳频率
     */
    private static final long HEART_BEAT_RATE = 30 * 1000;
    /**
     * 服务器ip地址
     */
    //  public static final String HOST = "192.168.123.27";// "192.168.1.21";//
    public static String HOST = "";

    /**
     * 服务器端口号
     */
    public static int PORT = 8080;
    /**
     * 服务器端口号
     */
    public static String TOKEN = "";
    /**
     * 服务器消息回复广播
     */
    public static final String MESSAGE_ACTION = "message_ACTION";
    /**
     * 服务器心跳回复广播
     */
    public static final String HEART_BEAT_ACTION = "heart_beat_ACTION";


    public static LocalBroadcastManager mLocalBroadcastManager;
    /***/
    private static WeakReference<WebSocket> mSocket;

    // For heart Beat
    private static Handler mHandler = new Handler();
    /**
     * 心跳任务，不断重复调用自己
     */
    private static Runnable heartBeatRunnable = new Runnable() {

        @Override
        public void run() {
            if (System.currentTimeMillis() - sendTime >= HEART_BEAT_RATE) {
                boolean isSuccess = sendMsg("HeartBeat");//就发送一个\r\n过去 如果发送失败，就重新初始化一个socket
                if (!isSuccess) {
                    reStart();
                }
            }
            mHandler.postDelayed(this, HEART_BEAT_RATE);
        }
    };

    public static void reStart() {
        try {
            mHandler.removeCallbacks(heartBeatRunnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        sClient = null;
        sWebSocket = null;
        new InitSocketThread().start();
    }

    private static long sendTime = 0L;
    /**
     * aidl通讯回调
     */
    private IBackService.Stub iBackService = new IBackService.Stub() {

        /**
         * 收到内容发送消息
         *
         * @param message 需要发送到服务器的消息
         * @return
         * @throws RemoteException
         */
        @Override
        public boolean sendMessage(String message) throws RemoteException {
            return sendMsg(message);
        }
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return iBackService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new InitSocketThread().start();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

    }

    public static boolean sendMsg(final String msg) {
        if (null == mSocket || null == mSocket.get()) {
            return false;
        }
        mSocket.get().send(BackService.Getmsgstr(msg));
        sendTime = System.currentTimeMillis();//每次发送成数据，就改一下最后成功发送的时间，节省心跳间隔时间
        return true;
    }

    public static final int NORMAL_CLOSURE_STATUS = 1000;

    private static OkHttpClient sClient;
    private static WebSocket sWebSocket;
    private static EchoWebSocketListener listener;

    public static synchronized void initSocket() {//初始化Socket
        try {
            if (sClient == null) {
                sClient = new OkHttpClient();
            }
            if (null == listener)
                listener = new EchoWebSocketListener();
            if (sWebSocket == null) {
                Request request = new Request.Builder()
                        .url("ws://" + HOST + ":" + PORT)
                        .build();
                sWebSocket = sClient.newWebSocket(request, listener);
            }
            sClient.dispatcher().executorService().shutdown();
            mSocket = new WeakReference<WebSocket>(sWebSocket);
            mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);//初始化成功后，就准备发送心跳包
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static synchronized void destroy() {
        if (sClient != null) {
            sWebSocket.close(NORMAL_CLOSURE_STATUS, Getmsgstr("下线"));
            sClient.dispatcher().executorService().shutdown();
            sClient = null;
        }
    }


    static class InitSocketThread extends Thread {
        @Override
        public void run() {
            super.run();
            initSocket();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        destroy();
        mHandler.removeCallbacks(heartBeatRunnable);
    }

    public static String Getmsgstr(String s) {
        Cmd_list bean = new Cmd_list();
        bean.setCmd("msg");
        bean.setInfo(s);
        bean.setShebei_id(ChipInfo.getChipIDHex());
        return Gsonuntils.GsonString(bean) + "￥";
    }

}
