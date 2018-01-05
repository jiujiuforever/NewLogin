package com.lesso.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

import static com.lesso.log.LogUtil.ASSERT;
import static com.lesso.log.LogUtil.DEBUG;
import static com.lesso.log.LogUtil.ERROR;
import static com.lesso.log.LogUtil.INFO;
import static com.lesso.log.LogUtil.VERBOSE;
import static com.lesso.log.LogUtil.WARN;

/**
 * Created by it026 on 2017/8/23.
 */
class Utils {

    private Utils() {
        // Hidden constructor.
    }

    /**
     * Returns true if the string is null or 0-length.
     *
     * @param str the string to be examined
     * @return true if str is null or zero length
     */
    static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * Copied from "android.util.Log.getStackTraceString()" in order to avoid usage of Android stack
     * in unit tests.
     *
     * @return Stack trace in form of String
     */
    static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    static String logLevel(int value) {
        switch (value) {
            case VERBOSE:
                return "VERBOSE";
            case DEBUG:
                return "DEBUG";
            case INFO:
                return "INFO";
            case WARN:
                return "WARN";
            case ERROR:
                return "ERROR";
            case ASSERT:
                return "ASSERT";
            default:
                return "UNKNOWN";
        }
    }
}
