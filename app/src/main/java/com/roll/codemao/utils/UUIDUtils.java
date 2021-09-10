package com.roll.codemao.utils;

import java.util.UUID;

public class UUIDUtils {
    /**
     * 生成一个32位的不带-的不唯一的uuid
     *
     * @return
     */
    public static String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};


    /**
     * 获取16位的UUID
     *
     * @return
     */
    public static String getShortUuid() {
        int length = 16;
        StringBuffer shortBuffer = new StringBuffer();
        String uuid = UUID.randomUUID().toString().replace("-", "");
//        System.out.println(uuid);
        int index = 32 / length;
        for ( int i = 0; i < length; i++ ) {
            String str = uuid.substring(i * index, i * index + index);
            int x = Integer.parseInt(str, length * 2);
            shortBuffer.append(chars[x % 0x3E]);
        }
        return shortBuffer.toString().toLowerCase();
    }
}