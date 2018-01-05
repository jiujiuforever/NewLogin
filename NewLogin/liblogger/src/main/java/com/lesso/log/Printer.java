package com.lesso.log;

interface Printer {

    void d(String tag, String message, Object... args);

    void e(String tag, String message, Object... args);

    void e(String tag, Throwable throwable, String message, Object... args);

    void w(String tag, String message, Object... args);

    void i(String tag, String message, Object... args);

    void v(String tag, String message, Object... args);

    void a(String tag, String message, Object... args);

    void addAdapter(LogAdapter adapter);

    void removeAdapter(LogAdapter adapter);

    void clearAdapters();
}
