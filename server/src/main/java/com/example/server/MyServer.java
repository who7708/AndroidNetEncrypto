package com.example.server;

import com.example.crypto.AES;
import com.example.crypto.Base64;
import com.example.crypto.DH;
import com.example.crypto.RSA;
import com.example.crypto.constant.Constants;
import com.example.crypto.utils.DataUtils;
import com.example.server.http.HttpCallback;
import com.example.server.http.HttpServer;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class MyServer {
    public static void main(String[] args) {
        startServer();
        // testDh();
        // testRsa();
        // testAes();
    }

    private static void startServer() {
        System.out.println("start http server ...");
        HttpServer server = new HttpServer(new HttpCallback() {
            private final DH mDh = new DH();

            private final AES mAes = new AES();

            @Override
            public byte[] onResponse(String request) {
                // 客户端的request，经过相应处理，返回客户端
                // 拿到 request 数据后，首先判断是不是握手请求
                if (isHandshake(request)) {
                    // 握手的相应操作
                    Map<String, String> header = HttpServer.getHeader(request);
                    String handshake = header.get(Constants.HANDSHAKE);
                    System.out.println("客户端请求的 handshake 内容：" + handshake);
                    int dhPublicKey = Integer.parseInt(RSA.decrypt(handshake, Constants.RSA_PRIVATE_KEY));
                    // 拿到客户端的 dh public key, 生成服务端 dh 的 secret
                    // 设置给 aes 作为密钥
                    mAes.setKey(mDh.getSecretKey(dhPublicKey));

                    // 生成服务端的 dh public key 给客户端，并在客户端生成 dh 的 secret
                    int publicKey = mDh.getPublicKey();
                    System.out.println("服务端生成的 DH 公钥为：" + publicKey);
                    return DataUtils.int2Byte(publicKey);
                }
                // 普通请求操作
                return mAes.encrypt("Hello World");
            }
        });
        server.startHttpServer();
    }

    /**
     * 通过 Header 当中的 handshake 字段判断是不为握手请求
     *
     * @param request 客户端请求
     * @return 是不为握手请求
     */
    private static boolean isHandshake(String request) {
        return request != null && request.trim().contains(Constants.HANDSHAKE);
    }

    private static void testDh() {
        DH dhC = new DH();
        DH dhS = new DH();

        // 客户端创建公钥
        int publicKeyC = dhC.getPublicKey();
        System.out.println("客户端创建公钥：" + publicKeyC);
        // 服务端创建公钥
        int publicKeyS = dhS.getPublicKey();
        System.out.println("服务端创建公钥：" + publicKeyS);

        byte[] secretC = dhC.getSecretKey(publicKeyS);

        byte[] secretS = dhS.getSecretKey(publicKeyC);

        System.out.println("client's secret is " + Base64.encodeToString(secretC, Base64.NO_WRAP));
        System.out.println("server's secret is " + Base64.encodeToString(secretS, Base64.NO_WRAP));
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
}