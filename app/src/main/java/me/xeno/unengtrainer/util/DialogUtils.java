package me.xeno.unengtrainer.util;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import android.support.v7.widget.*;
import android.widget.SeekBar;

import me.xeno.unengtrainer.R;
import me.xeno.unengtrainer.widget.SetSpeedDialogWrapper;

/**
 * Created by xeno on 2017/7/13.
 */

public class DialogUtils {

    public static void logDialog(Context context, String content) {
        new MaterialDialog.Builder(context)
                .title("调试信息")
                .content(content)
                .positiveText("确定")
                .show();

    }

    public static void dialog(Context context, String title, String content, String posText, String negText,
                              MaterialDialog.SingleButtonCallback posCallback, MaterialDialog.SingleButtonCallback negCallback) {
        new MaterialDialog.Builder(context)
                .title(title)
                .content(content)
                .cancelable(false)
                .positiveText(posText)
                .negativeText(negText)
                .onPositive(posCallback)
                .onNegative(negCallback)
                .show();
    }

}
