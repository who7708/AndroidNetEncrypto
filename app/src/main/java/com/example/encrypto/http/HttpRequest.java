package com.example.encrypto.http;

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

    private Callback mCallback;

    /**
     * 构造 http request 用于发起网络请求
     *
     * @param url 服务地址
     */
    public HttpRequest(String url) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        mCall = client.newCall(request);
    }

    public void request(Callback callback) {
        if (mCall != null) {
            if (mCall.isExecuted()) {
                mCall.clone().enqueue(callback);
            } else {
                mCall.enqueue(callback);
            }
        }
    }
}
