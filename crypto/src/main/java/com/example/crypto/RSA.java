package com.example.crypto;

import com.example.crypto.constant.Constants;
import com.example.crypto.utils.DataUtils;

import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSA {
    /**
     * 加密方法，传入明文，公钥，返回密文的base64
     *
     * @param data      明文
     * @param publicKey 公钥
     * @return 密文的base64
     */
    public static String encrypt(int data, String publicKey) {
        String message = String.valueOf(data);

        byte[] decode = DataUtils.base64Decode(publicKey);
        byte[] result = new byte[]{0};

        try {
            RSAPublicKey rsaPublicKey = (RSAPublicKey) KeyFactory.getInstance(Constants._RSA)
                    .generatePublic(new X509EncodedKeySpec(decode));

            Cipher cipher = Cipher.getInstance(Constants.RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);

            result = cipher.doFinal(message.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DataUtils.base64Encode(result);
    }

    /**
     * 传入密文及私钥，返回明文
     *
     * @param encrypted  密文
     * @param privateKey 私钥
     * @return 明文
     */
    public static String decrypt(String encrypted, String privateKey) {
        byte[] decoded = DataUtils.base64Decode(privateKey);
        byte[] content = DataUtils.base64Decode(encrypted);

        byte[] result = new byte[]{0};

        try {
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) KeyFactory.getInstance(Constants._RSA)
                    .generatePrivate(new PKCS8EncodedKeySpec(decoded));

            Cipher cipher = Cipher.getInstance(Constants.RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);

            result = cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(result);
    }
}