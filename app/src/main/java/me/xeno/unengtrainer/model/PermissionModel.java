package me.xeno.unengtrainer.model;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * Created by xeno on 2017/5/23.
 * request for permissions
 */

public class PermissionModel {

    private final Context mContext;

    public PermissionModel(Context context) {
        mContext = context.getApplicationContext();
    }

}
