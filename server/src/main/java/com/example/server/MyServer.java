package com.example.server;

import com.example.crypto.AES;
import com.example.crypto.DH;
import com.example.crypto.RSA;
import com.example.crypto.constant.Constants;
import com.example.crypto.utils.DataUtils;
import com.example.server.http.HttpCallback;
import com.example.server.http.HttpServer;

import java.nio.charset.StandardCharsets;

public class MyServer {
    public static void main(String[] args) {
        // startServer();
        // testRsa();
        // testAes();
        testDh();
    }

    private static void testDh() {
        DH dhC = new DH();
        DH dhS = new DH();

        // 客户端创建公钥
        int publicKeyC = dhC.getPublicKey();
        // 服务端创建公钥
        int publicKeyS = dhS.getPublicKey();

        byte[] secretC = dhC.getSecretKey(publicKeyS);

        byte[] secretS = dhS.getSecretKey(publicKeyC);

        System.out.println("client's secrete is " + new String(secretC, StandardCharsets.UTF_8));
        System.out.println("server's secrete is " + new String(secretS, StandardCharsets.UTF_8));

    }

    private static void testAes() {
        AES aes = new AES();
        String content = "this is aes";
        byte[] encrypt = aes.encrypt(content);
        System.out.println(DataUtils.base64Encode(encrypt));
        byte[] decrypt = aes.decrypt(encrypt);
        System.out.println(new String(decrypt, StandardCharsets.UTF_8));
    }

    private static void testRsa() {
        // 可能会出现 IOException : algid parse error, not a sequence
        // 说明对齐方式不对
        // pkcs8 -topk8 -inform PEM -in rsa_private_key.pem -outform pem -nocrypt -out rsa_private_key.pem
        int content = 123456;
        String encrypted = RSA.encrypt(content, Constants.RSA_PUBLIC_KEY);
        System.out.println(encrypted);
        String decrypt = RSA.decrypt(encrypted, Constants.RSA_PRIVATE_KEY);
        System.out.println(decrypt);
    }

    private static void startServer() {
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