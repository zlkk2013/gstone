package com.greenstone.lvcaihui;

import android.util.Log;

/**
 * Created by gzc on 14-11-3.
 */
public class User {
    public static String token = null;
    public static int user_id = -1;
    public static int type= -1;
    public static void reset() {
        token = null;
        user_id = -1;
    }
    public static void save(String k, int uid, int t) {
        Log.v("user_id", String.valueOf(uid));
        token = k;
        user_id = uid;
        type = t;
    }
    public static boolean isLogin() {
        return (token != null);
    }
}
