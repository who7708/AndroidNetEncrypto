package com.example.crypto;

import com.example.crypto.constant.Constants;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Chris
 * @version 1.0.0
 * @date 2/17/21
 */
public class AES {
    private SecretKey mKey;

    public AES() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(Constants._AES);

            // 创建随机密码，并设置种子
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.setSeed(System.currentTimeMillis());

            // 初始化密钥对象
            keyGenerator.init(128, secureRandom);

            // 生成 AES 密钥并且保持
            mKey = keyGenerator.generateKey();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AES(byte[] key) {
        mKey = new SecretKeySpec(key, Constants._AES);
    }

    public byte[] getKey() {
        return mKey.getEncoded();
    }

    /**
     * 动态设置 aes key
     *
     * @param key key
     */
    public void setKey(byte[] key) {
        this.mKey = new SecretKeySpec(key, Constants._AES);
    }

    public byte[] encrypt(String content) {
        if (mKey == null) {
            return new byte[]{-1};
        }
        try {
            Cipher cipher = Cipher.getInstance(Constants.AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, mKey);
            return cipher.doFinal(content.getBytes(Constants.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[]{-1};
    }

    public byte[] decrypt(byte[] content) {
        if (mKey == null) {
            return new byte[]{-1};
        }
        try {
            Cipher cipher = Cipher.getInstance(Constants.AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, mKey);
            return cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[]{-1};
    }
}
