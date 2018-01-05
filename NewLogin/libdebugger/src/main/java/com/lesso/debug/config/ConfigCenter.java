package com.lesso.debug.config;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.Toast;

import com.lesso.debug.ui.DebugActivity;
import com.lesso.debug.utils.FloatUtils;
import com.lesso.debug.utils.SharedPreferenceUtils;
import com.squareup.seismic.ShakeDetector;

import static android.content.Context.SENSOR_SERVICE;
import static com.squareup.seismic.ShakeDetector.SENSITIVITY_LIGHT;

/**
 * 配置初始化
 * 支持摇一摇进入Debug页面
 * 支持前后台切换出现Debug悬浮按钮，点击悬浮按钮跳转到Debug页面
 */
public class ConfigCenter implements ApplicationLifeCycleCallbacks {
    private Application application;
    private ActivityLifecycleCallbacksImpl activityLifecycleCallbacks;
    private ShakeDetector shakeDetector;
    private FloatUtils floatUtils;

    private static class Holder {
        private static ConfigCenter INSTANCE = new ConfigCenter();
    }

    public static ConfigCenter getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void onAppBroughtToForeground(Activity activity) {
        ConfigCenter.getInstance().startShaker();
        if (floatUtils != null) {
            floatUtils.show();
        }
    }

    @Override
    public void onAppBroughtToBackground(Activity activity) {
        ConfigCenter.getInstance().stopShaker();
        if (floatUtils != null) {
            floatUtils.hide();
        }
    }

    public void init(Application application) {
        this.application = application;

        final Context context = application;
        floatUtils = new FloatUtils(context);
        floatUtils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPreferenceUtils.getFloatWindowFlag(context)) {
                    // 点击悬浮框，跳转到Debug页面
                    gotoDebugPage();
                }
            }
        });

        initApplicationLifecycle();
        initShaker();
    }

    /**
     * 初始化生命周期
     */
    private void initApplicationLifecycle() {
        activityLifecycleCallbacks = new ActivityLifecycleCallbacksImpl();
        activityLifecycleCallbacks.setApplicationLifeCycleCallbacks(this);
        getApplication().registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    public Application getApplication() {
        return application;
    }

    /**
     * 支持摇一摇进入Debug
     */
    private void initShaker() {
        shakeDetector = new ShakeDetector(new ShakeDetector.Listener() {
            @Override
            public void hearShake() {
                if (SharedPreferenceUtils.getKeyShakeFlag(getApplication())) {
                    // 进入Debug页面
                    gotoDebugPage();
                }
            }
        });
        shakeDetector.setSensitivity(SENSITIVITY_LIGHT);
    }

    private void stopShaker() {
        if (shakeDetector != null) {
            shakeDetector.stop();
        }
    }

    private void startShaker() {
        if (shakeDetector != null) {
            SensorManager sensorManager = (SensorManager) getApplication().getSystemService(SENSOR_SERVICE);
            shakeDetector.start(sensorManager);
        }
    }

    private Activity getTopActivity() {
        if(activityLifecycleCallbacks == null){
            return null;
        }
        return activityLifecycleCallbacks.getTopActivity();
    }

    /**
     * 跳转到Debug页面
     */
    public void gotoDebugPage(){
        Activity activity = getTopActivity();
        if(activity == null || activity instanceof DebugActivity){
            return;
        }
        Intent intent = new Intent(activity, DebugActivity.class);
        getTopActivity().startActivity(intent);
    }
}
