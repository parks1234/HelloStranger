package com.hbrc.srcapp.Receiver;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.hbrc.srcapp.R;
import com.hbrc.srcapp.base.BaseAty1;
import com.hbrc.srcapp.base.Baseview;
import com.hbrc.srcapp.preitem.splash.Splashpre;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WEbActivity1 extends BaseAty1<Baseview, Splashpre> implements Baseview ,View.OnClickListener {

    @BindView(R.id.webview)
    WebView wv;
    @BindView(R.id.activity_help_of_money)
    LinearLayout activityHelpOfMoney;
    @BindView(R.id.base_top_left_btn)
    LinearLayout baseTopLeftBtn;
    @BindView(R.id.base_top_left_btn1)
    LinearLayout baseTopLeftBtn1;

//    private ListView lvMoneyhelp;

    @Override
    public int getLayoutId() {
        return R.layout.activity_webaty1;
    }

    @Override
    public void initView() {
        ButterKnife.bind(this);
 
        findViewById(R.id.base_top_left_btn1).setVisibility(View.GONE);
  
    }

    String shareurl = "";

    @Override
    public void initData() {

//        webSettings.setDefaultFixedFontSize(13);//字体

        //    webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        //    webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        //   webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口 
        // webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        // webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        wv.getSettings().setDatabaseEnabled(true);   //开启 database storage API 功能
        wv.getSettings().setDomStorageEnabled(true);// 开启 DOM storage API 功能
        //  wv.getSettings().setAppCacheMaxSize(1024*1024*8);//最大缓存
        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
        wv.getSettings().setAppCachePath(appCachePath);//cache路径
        wv.getSettings().setAppCacheEnabled(true);//开启 Application Caches 功能
        wv.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);//缓存
        wv.getSettings().setSupportZoom(true);//支持缩放，默认为true。是下面那个的前提。
        wv.getSettings().setSaveFormData(false);
        wv.getSettings().setJavaScriptEnabled(true);//支持Javascript
        wv.getSettings().setPluginState(WebSettings.PluginState.ON);
        wv.getSettings().setBlockNetworkImage(false);
//        wv.getSettings().setPluginsEnabled(true);//可以使用插件
        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv.getSettings().setAllowFileAccess(true);//设置可以访问文件 
        wv.getSettings().setDefaultTextEncodingName("UTF-8");
        wv.getSettings().setLoadWithOverviewMode(true);//将图片调整到适合webview的大小 
        wv.getSettings().setUseWideViewPort(true);//将图片调整到适合webview的大小 
        wv.setVisibility(View.VISIBLE);
        wv.setDownloadListener(new MyWebViewDownLoadListener());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wv.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        wv.getSettings().setUserAgentString(wv.getSettings().getUserAgentString() + "/src/"  + " /(android) /okhttp /retrofit/" + Build.MODEL + "/" + Build.MANUFACTURER + "/" + Build.VERSION.SDK_INT + "/");
//        wv.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
        wv.getSettings().setTextZoom(100);

        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
            
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
             
                if(wv.canGoBack())
                    findViewById(R.id.base_top_left_btn1).setVisibility(View.VISIBLE);
                else
                    findViewById(R.id.base_top_left_btn1).setVisibility(View.GONE);
            }

        };

        // 设置setWebChromeClient对象  
        wv.setWebChromeClient(wvcc);
        // StatService.trackWebView(this, wv,wvcc);
//        String string = getIntent().getExtras().getString("url");
//        if (string != null && !TextUtil.isEmpty(string)) {
//            if (!string.startsWith("http")) {
//                string = "http://" + string;
//            }
//        }
//        shareurl = string;
     //   wv.loadUrl(string);
       wv.loadUrl("file:///android_asset/WebSocketClient.html");
//            tvTitleBlue.setText(wv.getTitle());

        wv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @SuppressLint("NewApi")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {


                try {
                    String url = request.getUrl().toString();
                    if (url == null) {
                        return false;
                    }
                    if (url.startsWith("http:") || url.startsWith("https:")) {
                        view.loadUrl(url);
                        return true;
                    } else if (url.startsWith("tel")) {
//                        Intent intent = new Intent(Intent.ACTION_CALL);
//                        Uri data = Uri.parse(url);
//                        intent.setData(data);
//                        if (ActivityCompat.checkSelfPermission(WEbActivity1.this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
//                            startActivity(intent);
//                            //这个超连接,java已经处理了，webview不要处理
//                            return true;
//                        }else{
//                            //申请权限
//                            ActivityCompat.requestPermissions(WEbActivity1.this, new String[]{Manifest.permission.CALL_PHONE},1);
//                            return true;
//                        }
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                        startActivity(intent);
                        return true;

                    } else {
                        super.shouldOverrideUrlLoading(view, request);
//             
                    }
                } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return false;
                }
                super.shouldOverrideUrlLoading(view, request);
                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) { // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                if (url == null) {
                    return false;
                }

                try {
                    if (url.startsWith("http:") || url.startsWith("https:")) {
                        view.loadUrl(url);
                        return true;
                    } else if (url.startsWith("tel")) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    } else {
                        return super.shouldOverrideUrlLoading(view, url);
//                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                        startActivity(intent);
//                        return true;
                    }
                } catch (Exception e) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return super.shouldOverrideUrlLoading(view, url);
                }

            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
    }

    // 如果希望浏览的网 页回退而不是推出浏览器，需要在当前Activity中处理并消费掉该Back事件。   
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (wv.canGoBack()) {
                wv.goBack();
                return true;
            } else {
                finish();
            }

        }

        return super.onKeyDown(keyCode, event);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override

        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {

//            Log.i("tag", "url="+url);
//            Log.i("tag", "userAgent="+userAgent);
//            Log.i("tag", "contentDisposition="+contentDisposition);
//            Log.i("tag", "mimetype="+mimetype);
//            Log.i("tag", "contentLength="+contentLength);

            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);

        }

    }

    @Override
    protected Splashpre createPresenter() {
        return new Splashpre();
    }

    @Override
    public void onClick(View v) {
       
        switch (v.getId()) {
            case R.id.base_top_left_btn:
                if (wv.canGoBack()) {
                    wv.goBack();
                   
                } else {
                    finish();
                }
                break;
            case R.id.base_top_left_btn1:
                finish();
                break;

        }
    }

 

    @Override
    public void ShowLoding() {

    }

    @Override
    public void CancleLoding() {

    }

    @Override
    public void ONSucced(String msg) {

    }

    @Override
    public void ONErro() {

    }


    @Override
    protected void onPause() {
        super.onPause();
        //暂停WebView在后台的所有活动
        wv.onPause();
//暂停WebView在后台的JS活动
        wv.pauseTimers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        wv.onResume();
        wv.resumeTimers();
    }

    @Override
    protected void onDestroy() {


        wv.destroy();
        super.onDestroy();

    }
}
