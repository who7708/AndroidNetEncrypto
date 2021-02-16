package com.example.server.http;

import com.example.server.constant.Constants;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author Chris
 * @version 1.0.0
 * @date 2/17/21
 */
public class HttpThread implements Runnable {

    /**
     * 一个 socket 就代表一个客户端
     */
    private Socket mSocket;

    /**
     * 回调监听器，由业务方传入，不做任何业务处理
     */
    private HttpCallback mCallback;

    public HttpThread(Socket socket, HttpCallback callback) {
        this.mSocket = socket;
        this.mCallback = callback;
    }

    @Override
    public void run() {
        // 1. 读取客户端请求
        // 2. 根据业务数据采取相应操作
        // 3. 返回数据

        // 1. 提升io效率
        // 2. 便于逐行读入
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            String content;
            StringBuilder request = new StringBuilder();

            // 完成客户端请求内容的逐行读入
            while ((content = reader.readLine()) != null
                    && !content.trim().isEmpty()) {
                request.append(content).append(Constants.LINE);
            }
            System.out.println("客户端请求内容:" + Constants.LINE + request);

            // 返回数据
            byte[] response = new byte[0];
            if (mCallback != null) {
                response = mCallback.onResponse(request.toString());
            }

            // 将业务数据，包装成http格式
            // 1. 拼接请求行
            String responseFirstLine = "HTTP/1.1 200 OK" + Constants.ENTER_LINE;
            // 2. 拼接请求头
            String responseHeader = "Content-Type:text/html" + Constants.ENTER_LINE;
            // 3. 拼接业务数据
            OutputStream outputStream = mSocket.getOutputStream();
            // 发送请求行
            outputStream.write(responseFirstLine.getBytes(StandardCharsets.UTF_8));
            // 发送请求头
            outputStream.write(responseHeader.getBytes(StandardCharsets.UTF_8));
            // 换行
            outputStream.write(Constants.ENTER_LINE.getBytes(StandardCharsets.UTF_8));
            // 消息体
            outputStream.write(response);
            mSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
