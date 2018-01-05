package im.jizhu.com.loginmodule;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


import com.lesso.debug.DebuggerLib;

import im.jizhu.com.loginmodule.DB.sp.LoginSp;
import im.jizhu.com.loginmodule.DB.sp.SystemConfigSp;
import im.jizhu.com.loginmodule.config.UrlConstant;
import im.jizhu.com.loginmodule.imservice.manager.IMHeartBeatManager;
import im.jizhu.com.loginmodule.imservice.manager.IMSocketManager;
import im.jizhu.com.loginmodule.jni.JNIUtils;

public class LoginActivity extends AppCompatActivity {

    //所有的管理类
    private IMSocketManager socketMgr = IMSocketManager.instance();
    private LoginSp loginSp = LoginSp.instance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mian_login);

        TextView textView = (TextView) findViewById(R.id.text1);
        String a = UrlConstant.accessMsgAddress();
//        String desPwd = new String(Security.getInstance().EncryptPass("123"));
//        textView.setText(new JNIUtils().getString());
        //应用开启初始化 下面这几个怎么释放 todo
        Context ctx = getApplicationContext();
        SystemConfigSp.instance().init(ctx);
        loginSp.init(ctx);
        // 放在这里还有些问题 todo
        IMHeartBeatManager.instance().onStartIMManager(ctx);
        socketMgr.onStartIMManager(ctx);
        socketMgr.reqMsgServerAddrs();
    }

}
