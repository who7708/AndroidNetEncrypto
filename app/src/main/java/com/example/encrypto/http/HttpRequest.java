package com.example.encrypto.http;

import com.example.crypto.constant.Constants;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * @author Chris
 * @version 1.0.0
 * @date 2/16/21
 */
public class HttpRequest {
    private Call mCall;

    // private Callback mCallback;

    private Request.Builder mBuilder;

    /**
     * 构造 http request 用于发起网络请求
     *
     * @param url 服务地址
     */
    public HttpRequest(String url) {
        mBuilder = new Request.Builder()
                .get()
                .url(url);
    }

    /**
     * 握手请求，目地是与对方交换公钥
     *
     * @param callback  回调
     * @param publicKey rsa 加密后的dh公钥
     */
    public void handshake(Callback callback, String publicKey) {
        // 通过在header里面添加handshake字段，表示当前是一个握手请求，并且参数就是DH的公钥
        mBuilder.addHeader(Constants.HANDSHAKE, publicKey);
        request(callback);
        // 握手完成后去除 header 字段，避免干扰普通的 requeset 请求
        mBuilder.removeHeader(Constants.HANDSHAKE);
    }

    public void request(Callback callback) {
        OkHttpClient client = new OkHttpClient();
        mCall = client.newCall(mBuilder.build());
        mCall.enqueue(callback);
    }
}
