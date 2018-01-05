package com.lesso.debug;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.facebook.stetho.Stetho;
import com.lesso.debug.config.ConfigCenter;
import com.lesso.debug.ui.DebugActivity;
import com.lesso.debug.utils.SharedPreferenceUtils;

/**
 * 调试工具
 * Created by zhengxiaoming on 2017/8/28.
 */
public final class DebuggerLib {

    /** Debug本地配置 **/
    public static final String SP_DEBUG_CONFIG = "debug_config";
    public static final String SP_KEY_CHANGE_OFFICIAL = "change_official";

    private static Context context;


    /**
     * 初始化
     * @param application
     */
    public static void init(Application application){

        context = application;

        // 初始化Stetho
        // 打开Chrome输入chrome://inspect/#devices,可以查看App的数据库和Sp内容
        Stetho.initializeWithDefaults(application);
        // 初始化配置
        ConfigCenter.getInstance().init(application);
    }

    /**
     * 跳转到Debug页面
     */
    public static void gotoDebugPage(){
        ConfigCenter.getInstance().gotoDebugPage();
    }

    /**
     * 获取Debug的Env环境
     * @return true
     */
    public static boolean isOfficialEnv(){
        if(context == null){
            return false;
        }
        // 默认测试环境
        return SharedPreferenceUtils.getBoolean(context,SP_DEBUG_CONFIG,SP_KEY_CHANGE_OFFICIAL,false);
    }
}
