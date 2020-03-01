package com.pateo.cs.music.util;

public class Dog {

    private static final String TAG = "CSMusicApp";

    private static boolean DEBUG = true;

    public static void i(String className, String content) {
        if (DEBUG) {
            android.util.Log.i(TAG, "[" + className + "]====>>>" + content);
        }
    }

    public static void v(String className, String content) {
        if (DEBUG) {
            android.util.Log.v(TAG, "[" + className + "]====>>>" + content);
        }
    }

    public static void d(String className, String content) {
        if (DEBUG) {
            android.util.Log.i(TAG, "[" + className + "]====>>>" + content);
        }
    }

    public static void e(String className, String content) {
        if (DEBUG) {
            android.util.Log.e(TAG, "[" + className + "]====>>>" + content
                    + "########");
        }
    }
}
