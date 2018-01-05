package com.lesso.debug.config;

import android.app.Activity;

/**
 * 应用周期
 */
public interface ApplicationLifeCycleCallbacks {

    /**
     * 切换到前台
     * @param activity
     */
    void onAppBroughtToForeground(Activity activity);

    /**
     * 切换到后台
     * @param activity
     */
    void onAppBroughtToBackground(Activity activity);
}
