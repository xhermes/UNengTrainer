package me.xeno.unengtrainer.application;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.support.v4.content.ContextCompat;

import com.elvishew.xlog.LogConfiguration;
import com.elvishew.xlog.LogLevel;
import com.elvishew.xlog.XLog;
import com.elvishew.xlog.printer.AndroidPrinter;
import com.elvishew.xlog.printer.ConsolePrinter;
import com.elvishew.xlog.printer.Printer;
import com.elvishew.xlog.printer.file.FilePrinter;
import com.elvishew.xlog.printer.file.backup.NeverBackupStrategy;
import com.elvishew.xlog.printer.file.clean.FileLastModifiedCleanStrategy;
import com.elvishew.xlog.printer.file.clean.NeverCleanStrategy;
import com.elvishew.xlog.printer.file.naming.DateFileNameGenerator;

import me.xeno.unengtrainer.util.CrashHandler;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * Created by Administrator on 2017/5/21.
 */

public class UNengApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();



        //do init work
        CrashHandler.getInstance().init(this);
        DataManager.getInstance().init(this);
        LogConfiguration configuration = new LogConfiguration.Builder()
                .logLevel(LogLevel.ALL)
                .tag("xeno")
                .build();

        Printer androidPrinter = new AndroidPrinter();             // 通过 android.util.Log 打印日志的打印器
        Printer consolePrinter = new ConsolePrinter();             // 通过 System.out 打印日志到控制台的打印器
        Printer filePrinter = new FilePrinter                      // 打印日志到文件的打印器
                .Builder(Environment.getExternalStorageDirectory().getPath())                              // 指定保存日志文件的路径
                .fileNameGenerator(new DateFileNameGenerator())        // 指定日志文件名生成器，默认为 ChangelessFileNameGenerator("log")
                .backupStrategy(new NeverBackupStrategy())// 指定日志文件备份策略，默认为 FileSizeBackupStrategy(1024 * 1024)
                        .cleanStrategy(new NeverCleanStrategy())     // 指定日志文件清除策略，默认为 NeverCleanStrategy()
                        .build();
        XLog.init(configuration, androidPrinter, consolePrinter, filePrinter);
    }

}
