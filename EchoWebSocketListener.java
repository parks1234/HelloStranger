package com.hbrc.srcapp.Receiver;

import android.content.Intent;

import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * Created by  王学波 on 2019/1/22.
 */
public class EchoWebSocketListener extends WebSocketListener {
   

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        webSocket.send(BackService.Getmsgstr("上线"));
        super.onOpen(webSocket, response);
    }

    @Override
    public void onMessage(WebSocket webSocket, String message) {
        if (message.equals("ok")) {//处理心跳回复
            Intent intent = new Intent(BackService.HEART_BEAT_ACTION);
            BackService.mLocalBroadcastManager.sendBroadcast(intent);
        } else {
            //其他消息回复
            Intent intent = new Intent(BackService.MESSAGE_ACTION);
            intent.putExtra("message", message);
            BackService.mLocalBroadcastManager.sendBroadcast(intent);
        }
    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        super.onMessage(webSocket, bytes);
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {

        super.onClosing(webSocket, code, reason);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        super.onFailure(webSocket, t, response);
        BackService.reStart();
    }
}

