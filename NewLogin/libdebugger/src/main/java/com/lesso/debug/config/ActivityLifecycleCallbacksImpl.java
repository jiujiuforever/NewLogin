package com.lesso.debug.config;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.lang.ref.WeakReference;

/**
 * 应用周期实现
 */
public class ActivityLifecycleCallbacksImpl implements Application.ActivityLifecycleCallbacks {

    private WeakReference<Activity> topActivityReference;

    private int activityCount = 0;

    private ApplicationLifeCycleCallbacks applicationLifeCycleCallbacks;

    public ActivityLifecycleCallbacksImpl() {
    }

    public Activity getTopActivity() {
        return topActivityReference.get();
    }

    //设置属性
    public void setApplicationLifeCycleCallbacks(ApplicationLifeCycleCallbacks applicationLifeCycleCallbacks) {
        this.applicationLifeCycleCallbacks = applicationLifeCycleCallbacks;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {
        if (activityCount <= 0) {
            if (null != applicationLifeCycleCallbacks) {
                applicationLifeCycleCallbacks.onAppBroughtToForeground(activity);
            }
        }

        activityCount++;
    }

    @Override
    public void onActivityResumed(Activity activity) {
        topActivityReference = new WeakReference<>(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
        activityCount--;
        if (activityCount <= 0) {
            if (null != applicationLifeCycleCallbacks) {
                applicationLifeCycleCallbacks.onAppBroughtToBackground(activity);
            }
        }

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }
}
