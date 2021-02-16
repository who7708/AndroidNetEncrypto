package com.example.server.http;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Chris
 * @version 1.0.0
 * @date 2/17/21
 */
public class HttpServer {

    private boolean mRunning;

    private HttpCallback mCallback;

    public HttpServer(HttpCallback callback) {
        this.mCallback = callback;
    }

    /**
     * 启动http服务
     */
    public void startHttpServer() {
        if (mRunning) {
            // 如果正在运行，直接返回
            return;
        }
        mRunning = true;

        try {
            // 1. 启动 socket
            ServerSocket serverSocket = new ServerSocket(7001);

            // 2. 等待连接
            while (mRunning) {
                //
                Socket socket = serverSocket.accept();
                ExecutorService threadPool = Executors.newCachedThreadPool();
                threadPool.execute(new HttpThread(socket, mCallback));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
