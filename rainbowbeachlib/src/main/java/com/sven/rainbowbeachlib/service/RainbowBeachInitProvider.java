package com.sven.rainbowbeachlib.service;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;

import com.sven.rainbowbeachlib.RainbowBeach;

/**
 * @Author: xwp
 * @CreateDate: 2023/7/17
 * @Version: 1.0
 */
public class RainbowBeachInitProvider extends ContentProvider {


    public RainbowBeachInitProvider() {
    }

    @Override
    public boolean onCreate() {
        RainbowBeach.INSTANCE.start(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public void attachInfo(Context context, ProviderInfo providerInfo) {
        if (providerInfo == null) {
            throw new NullPointerException("RainbowBeachInitProvider ProviderInfo cannot be null.");
        }
        super.attachInfo(context, providerInfo);
    }

}
