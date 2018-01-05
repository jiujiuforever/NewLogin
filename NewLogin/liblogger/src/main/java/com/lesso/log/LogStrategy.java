package com.lesso.log;

/**
 * Created by it026 on 2017/8/23.
 */
interface LogStrategy {

    public void log(int level, String tag, String message);
}
