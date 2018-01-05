package com.lesso.debug.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lesso.debug.BuildConfig;
import com.lesso.debug.DebuggerLib;
import com.lesso.debug.R;
import com.lesso.debug.utils.DataCleanUtils;
import com.lesso.debug.utils.PackageUtils;
import com.lesso.debug.utils.SharedPreferenceUtils;

/**
 * Created by zhengxiaoming on 2017/8/29.
 */
public class DebugActivity extends AppCompatActivity {

    private TextView imDebugVersion;
    private TextView imDebugAppName;
    private TextView imDebugAppVersion;
    private LinearLayout imDebugSvnCommitLayout;
    private LinearLayout imDebugGitCommitLayout;
    private TextView imDebugSvnCommit;
    private TextView imDebugGitCommit;
    private TextView imDebugUserId;
    private TextView imDebugUserName;
    private TextView imDebugSwitchServer;
    private TextView imDebugCleanData;
    private TextView imDebugRestart;
    private TextView imDebugServerUrl;
    private TextView imDebugCurrentServer;
    private TextView imDebugServerResponse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.im_activity_debug);
        // 初始化控件
        findViews();
        // 初始化数据
        initData();
    }

    /**
     * 初始化控件
     */
    private void findViews() {
        imDebugVersion = (TextView) findViewById(R.id.im_debug_version);
        imDebugAppName = (TextView) findViewById(R.id.im_debug_app_name);
        imDebugAppVersion = (TextView) findViewById(R.id.im_debug_app_version);
        imDebugSvnCommitLayout = (LinearLayout) findViewById(R.id.im_debug_svn_commit_layout);
        imDebugGitCommitLayout = (LinearLayout) findViewById(R.id.im_debug_git_commit_layout);
        imDebugSvnCommit = (TextView) findViewById(R.id.im_debug_svn_commit);
        imDebugGitCommit = (TextView) findViewById(R.id.im_debug_git_commit);
        imDebugUserId = (TextView) findViewById(R.id.im_debug_user_id);
        imDebugUserName = (TextView) findViewById(R.id.im_debug_user_name);
        imDebugSwitchServer = (TextView) findViewById(R.id.im_debug_switch_server);
        imDebugCleanData = (TextView) findViewById(R.id.im_debug_clean_data);
        imDebugRestart = (TextView) findViewById(R.id.im_debug_restart);
        imDebugServerUrl = (TextView) findViewById(R.id.im_debug_server_url);
        imDebugCurrentServer = (TextView) findViewById(R.id.im_debug_current_server);
        imDebugServerResponse = (TextView) findViewById(R.id.im_debug_server_response);
    }

    /**
     * 初始化数据
     */
    private void initData(){
        // 设置Debug版本号
        imDebugVersion.setText("V" + BuildConfig.VERSION_NAME);
        // 设置App名称
        imDebugAppName.setText(PackageUtils.getPackageName(this));
        // 设置App版本
        imDebugAppVersion.setText(PackageUtils.getVersion(this));

        // GIT Commit
        String gitCommit = BuildConfig.GIT_COMMIT;
        if(!TextUtils.isEmpty(gitCommit)){
            imDebugGitCommitLayout.setVisibility(View.VISIBLE);
            imDebugGitCommit.setText(gitCommit);
        }else{
            imDebugGitCommitLayout.setVisibility(View.GONE);
        }

        // SVN Commit
        String svnCommit = BuildConfig.SVN_COMMIT;
        if(!TextUtils.isEmpty(svnCommit)){
            imDebugSvnCommitLayout.setVisibility(View.VISIBLE);
            imDebugSvnCommit.setText(svnCommit);
        }else{
            imDebugSvnCommitLayout.setVisibility(View.GONE);
        }

        // TODO:后续封装IMSDK后，从IMSDK中获取，暂从SP中拿
        // 用户ID
        imDebugUserId.setText(getLoginUserId() + "");
        // 用户名称
        imDebugUserName.setText(getLoginUserName());

        // 切换消息服务器
        boolean isOfficial = DebuggerLib.isOfficialEnv();
        String text = isOfficial ? "(线上)" : "(线下)";
        imDebugSwitchServer.setText("切换服务器"+text);
        imDebugSwitchServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示切换消息服务器Popup
                showPopupWindow();
            }
        });

        // 清空数据
        imDebugCleanData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCleanUtils.cleanApplicationData(DebugActivity.this);
                toast("清理成功");
            }
        });

        // 重启
        imDebugRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageUtils.restart(DebugActivity.this);
            }
        });

        // 消息服务器链接情况
        // TODO:后续封装IMSDK后，从IMSDK中获取，暂从SP中拿
        imDebugServerUrl.setText(getLoginServerUrl());
        imDebugCurrentServer.setText(getConnectMsgServer());
        imDebugServerResponse.setText(getConnectMsgResponse());
    }

    /*************************** 从SP获取数据 ********************/

    private final String SP_LOGIN = "login.ini";
    private final String KEY_LOGIN_NAME = "loginName";
    private final String KEY_LOGIN_ID = "loginId";

    private String getLoginUserName(){
        return SharedPreferenceUtils.getString(this,SP_LOGIN,KEY_LOGIN_NAME, "");
    }

    private int getLoginUserId(){
        return SharedPreferenceUtils.getInt(this,SP_LOGIN,KEY_LOGIN_ID, 0);
    }

    private final String SP_LOGIN_SERVER = "systemconfig.ini";
    private final String LOGINSERVER = "LOGINSERVER";
    private final String REQ_SERVER_RESPONSE = "REQ_SERVER_RESPONSE";
    private final String CONNECT_MSG_SERVER = "CONNECT_MSG_SERVER";

    private String getLoginServerUrl(){
        return SharedPreferenceUtils.getString(this,SP_LOGIN_SERVER,LOGINSERVER, "");
    }

    private String getConnectMsgResponse(){
        return SharedPreferenceUtils.getString(this,SP_LOGIN_SERVER,REQ_SERVER_RESPONSE, "");
    }

    private String getConnectMsgServer(){
        return SharedPreferenceUtils.getString(this,SP_LOGIN_SERVER,CONNECT_MSG_SERVER, "");
    }

    /**
     * Toast
     * @param text
     */
    private void toast(String text){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示PopupWindow
     */
    private void showPopupWindow() {
        toast("切换之后，会重新启动");
        //设置contentView
        View contentView = LayoutInflater.from(DebugActivity.this).inflate(R.layout.im_debug_change_env_popup, null);
        final PopupWindow popWindow = new PopupWindow(contentView,LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popWindow.setContentView(contentView);
        //设置各个控件的点击响应
        TextView officialText = (TextView)contentView.findViewById(R.id.im_debug_change_dev_official);
        TextView devText = (TextView)contentView.findViewById(R.id.im_debug_change_dev_dev);
        officialText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 线上环境
                SharedPreferenceUtils.putBoolean(DebugActivity.this, DebuggerLib.SP_DEBUG_CONFIG,DebuggerLib.SP_KEY_CHANGE_OFFICIAL, true);
                DataCleanUtils.cleanDataForChangeEnv(DebugActivity.this);

                PackageUtils.restart(DebugActivity.this);
                popWindow.dismiss();
            }
        });
        devText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 线下环境
                toast("重新启动后生效");
                SharedPreferenceUtils.putBoolean(DebugActivity.this, DebuggerLib.SP_DEBUG_CONFIG,DebuggerLib.SP_KEY_CHANGE_OFFICIAL, false);
                DataCleanUtils.cleanDataForChangeEnv(DebugActivity.this);
                PackageUtils.restart(DebugActivity.this);
                popWindow.dismiss();
            }
        });

        //显示PopupWindow
        popWindow.showAtLocation(imDebugSwitchServer, Gravity.CENTER_HORIZONTAL,0,0);

    }
}
