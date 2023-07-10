package com.sven.rainbowbeachlib.adblib;


import android.util.Base64;

public class MyAdbBase64 implements AdbBase64 {
    @Override
    public String encodeToString(byte[] data) {
        return Base64.encodeToString(data, Base64.NO_WRAP);
    }
}