//package com.hbrc.srcapp.Receiver;
//
//import android.content.BroadcastReceiver;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.ServiceConnection;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.net.wifi.WifiInfo;
//import android.net.wifi.WifiManager;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.os.RemoteException;
//import android.support.v4.content.LocalBroadcastManager;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.hbrc.srcapp.R;
//import com.hbrc.srcapp.utils.TextUtil;
//
//import java.lang.ref.WeakReference;
//import java.net.Inet4Address;
//import java.net.InetAddress;
//import java.net.NetworkInterface;
//import java.net.SocketException;
//import java.util.Enumeration;
//
///**
// * Created by  王学波 on 2019/1/22.
// */
//public class ActivityHeartSocket extends AppCompatActivity {
//
//    private TextView mResultText;
//    private EditText mEditText;
//    private Intent mServiceIntent;
//
//    private IBackService iBackService;
//
//    private ServiceConnection conn = new ServiceConnection() {
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            // 未连接为空  
//            iBackService = null;
//
//        }
//
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            // 已连接  
//            iBackService = IBackService.Stub.asInterface(service);
//        }
//    };
//
//    class MessageBackReciver extends BroadcastReceiver {
//        private WeakReference<TextView> textView;
//
//        public MessageBackReciver(TextView tv) {
//            textView = new WeakReference<TextView>(tv);
//        }
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            TextView tv = textView.get();
//            if (action.equals(BackService.HEART_BEAT_ACTION)) {
//                if (null != tv) {
//                    Log.i("danxx", "Get a heart heat");
//                    tv.setText("Get a heart heat");
//                }
//            } else {
//                Log.i("danxx", "Get a heart heat");
//                String message = intent.getStringExtra("message");
//                tv.setText("服务器消息:"+message);
//            }
//        };
//    }
//    private MessageBackReciver mReciver;
//
//    private IntentFilter mIntentFilter;
//
//    private LocalBroadcastManager mLocalBroadcastManager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_heart_socket);
//
//      
//        mResultText = (TextView) findViewById(R.id.resule_text);
//        mEditText = (EditText) findViewById(R.id.content_edit);
//        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
//
//        mReciver = new MessageBackReciver(mResultText);
//
//        mServiceIntent = new Intent(this, BackService.class);
//
//
//
//    }
//    // 注册广播  
//    private void registerReceiver() {
//        mIntentFilter = new IntentFilter();
//        mIntentFilter.addAction(BackService.HEART_BEAT_ACTION);
//        mIntentFilter.addAction(BackService.MESSAGE_ACTION);
//        mLocalBroadcastManager.registerReceiver(mReciver, mIntentFilter);
//    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//      //  TcpServer.startServer();
//        String s=getIPAddress(this);
//        BackService.HOST="192.168.1.10";
//        BackService.PORT=9000;
//        BackService.TOKEN="a1820a39cd2b4a30a5b86d930ac212a54f6292d21b30428c9ff9e9a323a00c800587c9168e054a7ab0232b55b3ee5f9fc879bd7f6dcb41babd8b480c7e1467b5";
//        registerReceiver();
//        bindService(mServiceIntent, conn, BIND_AUTO_CREATE);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        unbindService(conn);
//        mLocalBroadcastManager.unregisterReceiver(mReciver);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//    
//    }
//
//    public void onClick(View view) {   
//        String content = mEditText.getText().toString();
//        switch (view.getId()) {    
//         
//            case R.id.send:
//      
//                try {
//                    if (TextUtil.isEmpty(content)||null==iBackService)
//                        break;
//                    
//                    boolean isSend = iBackService.sendMessage(content);//Send Content by socket
//                    Toast.makeText(this, isSend ? "success" : "fail",
//                            Toast.LENGTH_SHORT).show();
//                    mEditText.setText("");
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//                break;
//           
//            
//            default:
//              
//                break;
//        }
//    }
//    public static String getIPAddress(Context context) {
//        NetworkInfo info = ((ConnectivityManager) context
//                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
//        if (info != null && info.isConnected()) {
//            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
//                try {
//                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
//                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
//                        NetworkInterface intf = en.nextElement();
//                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
//                            InetAddress inetAddress = enumIpAddr.nextElement();
//                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
//                                return inetAddress.getHostAddress();
//                            }
//                        }
//                    }
//                } catch (SocketException e) {
//                    e.printStackTrace();
//                }
//
//            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
//                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
//                return ipAddress;
//            }
//        } else {
//            //当前无网络连接,请在设置中打开网络
//        }
//        return null;
//    }
//
//    /**
//     * 将得到的int类型的IP转换为String类型
//     *
//     * @param ip
//     * @return
//     */
//    public static String intIP2StringIP(int ip) {
//        return (ip & 0xFF) + "." +
//                ((ip >> 8) & 0xFF) + "." +
//                ((ip >> 16) & 0xFF) + "." +
//                (ip >> 24 & 0xFF);
//    }
//
//}
//
