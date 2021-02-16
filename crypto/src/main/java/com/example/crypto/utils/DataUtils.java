package com.example.crypto.utils;

import java.nio.ByteBuffer;
import java.util.Base64;

/**
 * @author Chris
 * @version 1.0.0
 * @date 2/17/21
 */
public class DataUtils {
    /**
     * 传入base64编码后的字符串，返回解码后的byte[]
     *
     * @param data base64编码
     * @return byte[]
     */
    public static byte[] base64Decode(String data) {
        return Base64.getMimeDecoder().decode(data);
    }

    public static String base64Encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * 将 int 转换为 byte[]，一个 int 对应 4 个 byte
     *
     * @param data int 型数据
     * @return byte[]
     */
    public static byte[] int2Byte(int data) {
        // int 是4个字节
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(data);
        return buffer.array();
    }
}
