package com.zhujj.vrplayer.http;

import android.os.Handler;
import android.os.Looper;

public class MainThreadExecutor {
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    public static void execute(Runnable runnable) {
        if (null == runnable) {
            return;
        }

        if (Looper.getMainLooper().equals(Looper.myLooper())) {
            // 已经在主线程了
            runnable.run();
        } else {
            // 跳到主线程
            sHandler.post(runnable);
        }
    }

    public static void postDelayed(Runnable runnable, long delayMillis) {
        if (null == runnable) {
            return;
        }
        sHandler.postDelayed(runnable, delayMillis);
    }

    public static void remove(Runnable runnable) {
        if (null == runnable) {
            return;
        }
        sHandler.removeCallbacks(runnable);
    }
}
