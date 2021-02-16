package com.example.crypto;

import com.example.crypto.utils.DataUtils;

import java.security.MessageDigest;
import java.util.Random;

/**
 * @author Chris
 * @version 1.0.0
 * @date 2/17/21
 */
public class DH {
    private static final int dhP = 23;
    private static final int dhG = 5;

    private int mPrivateKey;

    public DH() {
        Random random = new Random();
        mPrivateKey = random.nextInt(10);
        System.out.println("dh private key is " + mPrivateKey);
    }

    public int getPublicKey() {
        // 使用公钥计算公式，计算出自己的公钥，用以交换
        return (int) (Math.pow(dhG, mPrivateKey) % dhP);
    }

    /**
     * 接收对方的公钥，与自己的私钥通过密钥公式产生密钥
     * 因为需要作为 aes 的密钥，所以需要转换成 byte[]
     *
     * @param publicKey 公钥
     * @return 密钥
     */
    public byte[] getSecretKey(int publicKey) {
        int buf = (int) (Math.pow(publicKey, mPrivateKey) % dhP);
        System.out.println("secrete is " + buf);
        return sha256(buf);
    }

    private byte[] sha256(int data) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(DataUtils.int2Byte(data));
            return messageDigest.digest();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[]{-1};
    }
}
