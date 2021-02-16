package com.example.server.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
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

            while (mRunning) {
                // 2. 等待连接
                Socket socket = serverSocket.accept();
                System.out.println("accept");
                ExecutorService threadPool = Executors.newCachedThreadPool();
                threadPool.execute(new HttpThread(socket, mCallback));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前请求的header对象，header为map
     *
     * @param request 请求数据
     * @return header 对象的map形式
     */
    public static Map<String, String> getHeader(String request) {
        Map<String, String> header = new HashMap<>();

        try {
            // 逐行解析 request 内容，读取到 map中
            BufferedReader reader = new BufferedReader(new StringReader(request));
            String line = reader.readLine();
            while (line != null && !line.trim().isEmpty()) {
                int p = line.indexOf(":");
                if (p >= 0) {
                    header.put(line.substring(0, p).trim().toLowerCase(), line.substring(p + 1).trim());
                }
                line = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return header;
    }
}
