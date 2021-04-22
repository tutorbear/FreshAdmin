package com.example.freshadmin.utils;

import android.os.Handler;

import java.io.IOException;

public class InternetCheck {
    public static void checkNet(final InternetCheckCallback inc){
        final Handler x = new Handler();
        new Thread(() -> {
            Runtime runtime = Runtime.getRuntime();
            try {
                Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                int     exitValue = ipProcess.waitFor();
                final boolean b = (exitValue == 0);
                x.post(() -> inc.onFound(b));
            } catch (InterruptedException | IOException e) {
                x.post(() -> inc.onFound(false));
            }
        }).start();
    }
}
