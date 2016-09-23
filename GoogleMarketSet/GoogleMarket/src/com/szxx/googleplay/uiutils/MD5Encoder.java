package com.szxx.googleplay.uiutils;

import java.security.MessageDigest;

public class MD5Encoder {
    public static String encoder(String string) throws Exception{
        byte[] holder = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        StringBuilder builder = new StringBuilder(holder.length * 2);
        for (byte b : holder) {
            if ((b & 0xFF) < 0x10) {
                builder.append("0");
            }
            builder.append(Integer.toHexString(b & 0xFF));
            
        }
        
        return builder.toString();
        
    }
}  