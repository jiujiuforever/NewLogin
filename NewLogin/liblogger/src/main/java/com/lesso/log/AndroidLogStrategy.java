package com.lesso.log;

import android.util.Log;

/**
 * Created by it026 on 2017/8/23.
 */
class AndroidLogStrategy implements LogStrategy{
    @Override
    public void log(int level, String tag, String message) {
        Log.println(level, tag, message);
    }
}
