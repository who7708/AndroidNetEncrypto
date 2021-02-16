package com.example.server.http;

/**
 * http 请求回调，将http的请求消息传出，并接收业务数据返回
 *
 * @author Chris
 * @version 1.0.0
 * @date 2/17/21
 */
public interface HttpCallback {
    /**
     * 收到消息的回调通知
     *
     * @param request 客户端请求内容
     * @return 返回客户端的response消息
     */
    byte[] onResponse(String request);
}
