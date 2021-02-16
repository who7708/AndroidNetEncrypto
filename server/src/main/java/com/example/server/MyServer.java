package com.example.server;

import com.example.server.http.HttpCallback;
import com.example.server.http.HttpServer;

import java.nio.charset.StandardCharsets;

public class MyServer {
    public static void main(String[] args) {
        System.out.println("start http server ...");
        HttpServer server = new HttpServer(new HttpCallback() {
            @Override
            public byte[] onResponse(String request) {
                return "Hello World".getBytes(StandardCharsets.UTF_8);
            }
        });
        server.startHttpServer();
    }
}