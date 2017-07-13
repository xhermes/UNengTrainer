package me.xeno.unengtrainer.util;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by xeno on 2017/7/13.
 */

public class DialogUtils {

    public static final void logDialog(Context context, String content) {
        new MaterialDialog.Builder(context)
                .title("调试信息")
                .content(content)
                .positiveText("确定")
                .show();

    }
}
