package im.jizhu.com.loginmodule.app;

import android.app.Application;

import com.lesso.debug.BuildConfig;
import com.lesso.debug.DebuggerLib;

/**
 * Created by it031 on 2017/12/16.
 */

public class IMApplication extends android.app.Application {


    @Override
    public void onCreate() {
        super.onCreate();
        DebuggerLib.init(this);
//        initDebug();
    }
    /**
     * 初始化调试工具
     */
    private void initDebug() {
        if(BuildConfig.DEBUG){
            DebuggerLib.init(this);
        }
    }
}
