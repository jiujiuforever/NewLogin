package im.jizhu.com.loginmodule.imservice.manager;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.protobuf.CodedInputStream;
//import com.igexin.sdk.PushManager;
import im.jizhu.com.loginmodule.DB.DBInterface;
import im.jizhu.com.loginmodule.DB.entity.UserEntity;
import im.jizhu.com.loginmodule.DB.entity.UserInfoEntity;
import im.jizhu.com.loginmodule.DB.sp.LoginSp;
import im.jizhu.com.loginmodule.DB.sp.SystemConfigSp;
import im.jizhu.com.loginmodule.imservice.callback.Packetlistener;
import im.jizhu.com.loginmodule.imservice.event.LoginDataEvent;
import im.jizhu.com.loginmodule.imservice.event.LoginEvent;
import im.jizhu.com.loginmodule.imservice.event.QueuueHandlerEvent;
import im.jizhu.com.loginmodule.protobuf.CCBaseDefine;
import im.jizhu.com.loginmodule.protobuf.CCLogin;
import im.jizhu.com.loginmodule.protobuf.IMBaseDefine;
import im.jizhu.com.loginmodule.protobuf.IMBuddy;
import im.jizhu.com.loginmodule.protobuf.IMLogin;
//import im.jizhu.com.loginmodule.protobuf.helper.ProtoBuf2JavaBean;
import im.jizhu.com.loginmodule.utils.Logger;
//import com.tencent.bugly.crashreport.CrashReport;

import java.io.IOException;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 很多情况下都是一种权衡
 * 登陆控制
 *
 * @yingmu
 */
public class IMLoginManager extends IMManager {

    private static final String TAG = "IMLoginManager";

    /**
     * 单例模式
     */
    private static IMLoginManager inst = new IMLoginManager();

    public static IMLoginManager instance() {
        return inst;
    }

    public IMLoginManager() {
        Logger.d(TAG,"login#creating IMLoginManager");
    }

    IMSocketManager imSocketManager = IMSocketManager.instance();

    /**
     * 登陆参数 以便重试
     */
    private String loginUserName;
    private String loginPwd;
    private int loginId;
    private String oaNum;
    private String sapNum;
    private String guid;

    private int loginType;//新增用于过滤部门显示
    private UserEntity loginInfo;
    private SharedPreferences sp;
    private int reLoginTime = 0;
    private String version ="";

    private String mainName;
    private int departmentId;

    /**
     * loginManger 自身的状态 todo 状态太多就采用enum的方式
     */
    private boolean identityChanged = false;
    private boolean isKickout = false;
    private boolean isPcOnline = false;
    //以前是否登陆过，用户重新登陆的判断
    private boolean everLogined = false;
    //本地包含登陆信息了[可以理解为支持离线登陆了]
    private boolean isLocalLogin = false;

    private LoginEvent loginStatus = LoginEvent.NONE;

    /**
     * -------------------------------功能方法--------------------------------------
     */

    @Override
    public void doOnStart() {
    }

    @Override
    public void reset() {
        loginUserName = null;
        loginPwd = null;
        loginId = -1;
        loginInfo = null;
        identityChanged = false;
        isKickout = false;
        isPcOnline = false;
        everLogined = false;
        loginStatus = LoginEvent.NONE;
        isLocalLogin = false;
    }

    /**
     * 实现自身的事件驱动
     *
     * @param event
     */
    public void triggerEvent(LoginEvent event) {
        loginStatus = event;
        EventBus.getDefault().postSticky(event);
    }

    /**
     * if not login, do nothing
     * send logOuting message, so reconnect won't react abnormally
     * but when reconnect start to work again?use isEverLogined
     * close the socket
     * send logOuteOk message
     * mainactivity jumps to login page
     */
    public void logOut() {
        Logger.d(TAG,"login#logOut");
        Logger.d(TAG,"login#stop reconnecting");
        //		everlogined is enough to stop reconnecting
        everLogined = false;
        isLocalLogin = false;
        reqLoginOut();
    }

    /**
     * 退出登陆
     */
    private void reqLoginOut() {

//        int logoutType = SystemConfigSp.instance().getIntConfig(SystemConfigSp.SysCfgDimension.LOGOUT_TYPE);//登出状态
//
//        IMLogin.IMLogoutReq imLogoutReq = IMLogin.IMLogoutReq.newBuilder()
//                .setLoginoutType(logoutType)
//                .build();
//        int sid = IMBaseDefine.ServiceID.SID_LOGIN_VALUE;
//        int cid = IMBaseDefine.LoginCmdID.CID_LOGIN_REQ_LOGINOUT_VALUE;
//        try {
//            imSocketManager.sendRequest(imLogoutReq, sid, cid);
//        } catch (Exception e) {
//            Logger.e(TAG,"#reqLoginOut#sendRequest error,cause by" + e.toString());
//        } finally {
//            //让sp中用户信息的密码为空
//            LoginSp.instance().setLoginInfo(loginUserName, null, loginId, null, null, null);
//            //让sp中登陆IP为空
//            SystemConfigSp.instance().setStrConfig(SystemConfigSp.SysCfgDimension.LOGINSERVER, null);
//            //Log.e("test", "清掉IP了吗：" + SystemConfigSp.instance().getStrConfig(SystemConfigSp.SysCfgDimension.LOGINSERVER));
//            IMContactManager.instance().reset();
//            Logger.d(TAG,"login#send logout finish message");
//            triggerEvent(LoginEvent.LOGIN_OUT);
//        }
    }

    /**
     * 现在这种模式 req与rsp之间没有必然的耦合关系。是不是太松散了
     *
     * @param imLogoutRsp
     */
    public void onRepLoginOut(IMLogin.IMLogoutRsp imLogoutRsp) {

        int code = imLogoutRsp.getResultCode();//0表示成功，1表示失败
    }

    /**
     * 重新请求登陆 IMReconnectManager
     * 1. 检测当前的状态
     * 2. 请求msg server的地址
     * 3. 建立链接
     * 4. 验证登陆信息
     *
     * @return
     */
    public void relogin() {

        //if(reLoginTime<1) {
        //reLoginTime++;

        if (!TextUtils.isEmpty(loginUserName) && !TextUtils.isEmpty(loginPwd)) {
            Logger.d(TAG,"reconnect#login#relogin");

            SystemConfigSp.instance().setIntConfig(SystemConfigSp.SysCfgDimension.LOGIND_TYPE, 2);//重连登录状态

            imSocketManager.reqMsgServerAddrs();
        } else {
            Logger.d(TAG,"reconnect#login#userName or loginPwd is null!!");
            everLogined = false;
            triggerEvent(LoginEvent.LOGIN_AUTH_FAILED);
        }

        /*}else {
            //reLoginTime = 0;
            triggerEvent(LoginEvent.LOGIN_UPDATAPW_FAILED);
        }*/
    }

    // 自动登陆流程
    public void login(LoginSp.SpLoginIdentity identity) {
//        if (identity == null) {
//            triggerEvent(LoginEvent.LOGIN_AUTH_FAILED);
//            return;
//        }
//        loginUserName = identity.getLoginName();
//        loginPwd = identity.getPwd();
//        identityChanged = false;
//        int mLoginId = identity.getLoginId();
//        // 初始化数据库
//        DBInterface.instance().initDbHelp(ctx, mLoginId);
//
//        UserEntity loginEntity = DBInterface.instance().getByLoginId(mLoginId);
//
//        do {
//            if (loginEntity == null) {
//                break;
//            }
//            loginInfo = loginEntity;
//            loginId = loginEntity.getPeerId();
//            oaNum = loginEntity.getOanum();
//            loginType = loginEntity.getType();
//            mainName = loginEntity.getMainName();
//            departmentId = loginEntity.getDepartmentId();
//
//            // 这两个状态不要忘记掉
//            isLocalLogin = true;
//            everLogined = true;
//            triggerEvent(LoginEvent.LOCAL_LOGIN_SUCCESS);
//        } while (false);
//        // 开始请求网络
//
//        SystemConfigSp.instance().setIntConfig(SystemConfigSp.SysCfgDimension.LOGIND_TYPE, 1);//主动登录状态
//
//        imSocketManager.reqMsgServerAddrs();
    }


    public void login(String userName, String password) {
        Logger.i(TAG,"login#login -> userName:%s", userName);

        //test 使用
        LoginSp.SpLoginIdentity identity = LoginSp.instance().getLoginIdentity();
        if (identity != null && !TextUtils.isEmpty(identity.getPwd())) {
            if (identity.getPwd().equals(password) && identity.getLoginName().equals(userName)) {
                login(identity);
                return;
            }
        }
        //test end
        loginUserName = userName;
        loginPwd = password;
        identityChanged = true;

        SystemConfigSp.instance().setIntConfig(SystemConfigSp.SysCfgDimension.LOGIND_TYPE, 1);//主动登录状态

        imSocketManager.reqMsgServerAddrs();
    }

    public void loginReserveIP() {

        SystemConfigSp.instance().setIntConfig(SystemConfigSp.SysCfgDimension.LOGIND_TYPE, 1);//主动登录状态

        imSocketManager.reqMsgServerAddrs();
    }

    /**
     * 链接成功之后
     */
    public void reqLoginMsgServer() {
        Logger.i(TAG,"login#reqLoginMsgServer");
        triggerEvent(LoginEvent.LOGINING);
        /** 加密 */
//        String desPwd = new String(im.jizhu.com.loginmodule.Security.getInstance().EncryptPass(loginPwd));
        String desPwd = "d0dcbf0d12a6b1e7fbfa2ce5848f3eff";
        try {
//            ctx = get
//            // 获取packagemanager的实例
//            PackageManager packageManager = ctx.getPackageManager();
//            // getPackageName()是你当前类的包名，0代表是获取版本信息
//            PackageInfo packInfo = packageManager.getPackageInfo(ctx.getPackageName(),0);
//            version = packInfo.versionName;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }

//        int loginType = SystemConfigSp.instance().getIntConfig(SystemConfigSp.SysCfgDimension.LOGIND_TYPE);//登入状态
        Log.e("test","登入状态："+loginType);
        loginUserName = "00045857";
        loginType = 1;

//        CCBaseDefine.IPInfo ipInfo = CCBaseDefine.IPInfo.newBuilder()
//                .setType(1)
//                .setIp("168.17.8.996").build();
//        CCBaseDefine.DeviceInfo deviceInfo = CCBaseDefine.DeviceInfo.newBuilder()
//                .setName("HTC-666")
//                .setType(2)
//                .setMeid("cwl666")
//                .setOs("Android9.0")
//                .setMemory(1014)
//                .setDiskCapacity(4056).build();
//
//        CCLogin.CCLoginReq ccLoginReq = CCLogin.CCLoginReq.newBuilder()
//                .setAccount("102171")
//                .setPassword("06ee7810be0e0b7c766391f07bad76fb")
//                .setDeviceinfo(deviceInfo)
//                .setIpinfo(ipInfo)
//                .setTenantId(1)
//                .setVersion("3.0.0.0").build();

        IMLogin.IMLoginReq imLoginReq = IMLogin.IMLoginReq.newBuilder()
                .setUserName(loginUserName)
                .setPassword(desPwd)
                .setLoginType(loginType)
                .setVersionNum(1)
                .setOnlineStatus(IMBaseDefine.UserStatType.USER_STATUS_ONLINE)
                .setClientType(IMBaseDefine.ClientType.CLIENT_TYPE_ANDROID)
                .setClientVersion("3.0.0.0").build();
////                .setClientVersion(version).build();
//
//        int sid = IMBaseDefine.ServiceID.SID_LOGIN_VALUE;
//        int cid = IMBaseDefine.LoginCmdID.CID_LOGIN_REQ_USERLOGIN_VALUE;

        int sid = CCBaseDefine.ServiceID.SID_LOGIN_VALUE;
        int cid = CCBaseDefine.LoginCmdID.LOGIN_USER_IN_REQ_VALUE;
        imSocketManager.sendRequest(imLoginReq, sid, cid, new Packetlistener() {
//        imSocketManager.sendRequest(ccLoginReq, sid, cid, new Packetlistener() {
            @Override
            public void onSuccess(Object response) {
                try {
                    CCLogin.CCLoginRes ccLoginRes = CCLogin.CCLoginRes.parseFrom((CodedInputStream) response);
                    onRepMsgServerLogin_new(ccLoginRes);
//                    IMLogin.IMLoginRes imLoginRes = IMLogin.IMLoginRes.parseFrom((CodedInputStream) response);
//                    onRepMsgServerLogin(imLoginRes);
                } catch (IOException e) {
                    triggerEvent(LoginEvent.LOGIN_INNER_FAILED);
                    Logger.e(TAG, "login failed,cause by %s", e.getCause());
                }
            }

            @Override
            public void onFaild() {
                triggerEvent(LoginEvent.LOGIN_INNER_FAILED);
            }

            @Override
            public void onTimeout() {
                triggerEvent(LoginEvent.LOGIN_INNER_FAILED);
            }
        });
    }


    /**
     * 验证登陆信息结果
     *
     * @param ccLoginRes
     */
    public void onRepMsgServerLogin_new(CCLogin.CCLoginRes ccLoginRes) {
        Logger.i(TAG,"login#onRepMsgServerLogin");

        if (ccLoginRes == null) {
            Logger.e(TAG,"login#decode LoginResponse failed");
            triggerEvent(LoginEvent.LOGIN_AUTH_FAILED);
            return;
        }
        String resultString = ccLoginRes.getResultMsg();
        CCBaseDefine.LoginResultCode code = ccLoginRes.getResultCode();
        switch (code) {
            case LOGIN_SUCCESS: {

                CCBaseDefine.UserInfo userInfo = ccLoginRes.getUserinfo();
                List<CCBaseDefine.AccountInfo> accountInfoList = ccLoginRes.getAccountlistList();
                CCBaseDefine.AccountInfo accountInfo = ccLoginRes.getAccountlist(0);
                InsertUserInfoEntity(userInfo);
//                InsertUserNameEntity(userInfo);
//                InsertUserValueEntity(userInfo);
//                InsertDepartEntity(userInfo);
//                InsertAccountInfoEntity(accountInfoList);
//                InsertLoginUserAllStautus(userInfo);
//                InsertAttachEnity(userInfo);

            }
            break;

            case UNKNOWN_ERROR:{

                //未知错误
                Log.e("test","resultString"+resultString);
//                EventBus.getDefault().post();
            }
            break;

            case LOGIN_FAILURE:{

                //登录失败：密码错误或版本过久
                Log.e("test","resultString"+resultString);
//                EventBus.getDefault().post();
            }
            break;

            case SERVICE_ERROR:{

                //服务器异常
                Log.e("test","resultString"+resultString);
//                EventBus.getDefault().post();
            }
            break;

            default: {
                Logger.e(TAG,"login#login msg server inner failed, result:%s", code);
                triggerEvent(LoginEvent.LOGIN_INNER_FAILED);
            }
            break;
        }
        EventBus.getDefault().post(QueuueHandlerEvent.MESSAGE_FINISH);
    }

    private void InsertUserInfoEntity(CCBaseDefine.UserInfo userinfo) {

        UserInfoEntity userInfoEntity =new UserInfoEntity();
        userInfoEntity.setGuid(userinfo.getGuid());
        userInfoEntity.setId(userinfo.getId());
        userInfoEntity.setContainer_id(userinfo.getContainerId());
        userInfoEntity.setName(userinfo.getNameinfo().getId());
//        userInfoEntity.setAvatar(userinfo.g().getId());
        userInfoEntity.setDepart(userinfo.getDepartinfo().getGuid());
        userInfoEntity.setStatus(userinfo.getStatusinfo().getId());
//        DBInterface.instance().InsertOrUpdateUserInfo(userInfoEntity);
    }


    /**
     * 验证登陆信息结果
     *
     * @param loginRes
     */
    public void onRepMsgServerLogin(IMLogin.IMLoginRes loginRes) {
        Logger.i(TAG,"login#onRepMsgServerLogin");

        if (loginRes == null) {
            Logger.e(TAG,"login#decode LoginResponse failed");
            triggerEvent(LoginEvent.LOGIN_AUTH_FAILED);
            return;
        }
        String resultString = loginRes.getResultString();
        IMBaseDefine.ResultType code = loginRes.getResultCode();
        switch (code) {
            case REFUSE_REASON_NONE: {
                IMBaseDefine.UserStatType userStatType = loginRes.getOnlineStatus();
                IMBaseDefine.UserInfo userInfo = loginRes.getUserInfo();
                loginId = userInfo.getUserId();
                loginType = userInfo.getUserType();
                oaNum = userInfo.getOanum();
                mainName = userInfo.getUserNickName();
                departmentId = userInfo.getDepartmentId();
//                sapNum = userInfo.getSapgh();
//                loginInfo = ProtoBuf2JavaBean.getUserEntity(userInfo);
//
//                Log.d("IMLoginManager", "oanum == " + oaNum);
//                Log.d("IMLoginManager", "sapnum == " + sapNum);
//
//                // 设置Bugly上报用户名
//                if(ctx != null){
//                    CrashReport.setUserId(ctx, loginUserName);
//                }
//                LoginSp.instance().setLoginInfo(loginUserName, loginPwd, loginId, oaNum, sapNum);
//
//                onLoginOk();
            }
            break;

            case REFUSE_REASON_NO_DB_SERVER:{

                //离职账号相应登录提示
                Log.e("test","resultString"+resultString);
                EventBus.getDefault().post(new LoginDataEvent(LoginDataEvent.Event.LOGIN_LEAVE_FAILED,resultString));
            }
            break;

            case REFUSE_REASON_DB_VALIDATE_FAILED: {

                triggerEvent(LoginEvent.LOGIN_AUTH_FAILED);
                //PC修改密码，断网情况下，重连失败，注销登陆
                reqLoginOut();

            }
            break;

            case REFUSE_REASON_VERSION_TOO_OLD:{//版本过低，更新登录

                EventBus.getDefault().post(new LoginDataEvent(LoginDataEvent.Event.LOGIN_VERSION_OLD,"版本太旧！"));
            }
            break;
            //加上新增的只支持PC登陆
            case REFUSE_REASON_ONLY_PC_LOGIN:{
                //公告账号不可登陆
                triggerEvent(LoginEvent.LOGIN_COMMON_FAILED);
            }
            break;

            default: {
                Logger.e(TAG,"login#login msg server inner failed, result:%s", code);
                triggerEvent(LoginEvent.LOGIN_INNER_FAILED);
            }
            break;
        }
       EventBus.getDefault().post(QueuueHandlerEvent.MESSAGE_FINISH);
    }

    //SOCKET通信登录成功
    public void onLoginOk() {
        Logger.i(TAG,"login#onLoginOk");
        everLogined = true;
        isKickout = false;

        if (identityChanged) {
            LoginSp.instance().setLoginInfo(loginUserName, loginPwd, loginId, oaNum, sapNum);
            identityChanged = false;
        }

        // 判断登陆的类型
        if (isLocalLogin) {
            triggerEvent(LoginEvent.LOCAL_LOGIN_MSG_SERVICE);
        } else {

            isLocalLogin = true;
            triggerEvent(LoginEvent.LOGIN_OK);
        }

        // 发送token
//        reqDeviceToken();
        reqClientId();

    }

    /**
     * 发送token ClientId
     */
    private void reqClientId() {

//        String clientid = PushManager.getInstance().getClientid(ctx);
//
//        Log.e("test", "clientid:" + clientid);
//
//        if (clientid!=null ) {
//
//            IMLogin.IMDeviceTokenReq req = IMLogin.IMDeviceTokenReq.newBuilder()
//                    .setUserId(IMLoginManager.instance().getLoginId())
//                    .setClientType(IMBaseDefine.ClientType.CLIENT_TYPE_ANDROID)
//                    .setDeviceToken(clientid)
//                    .build();
//
//            int sid = IMBaseDefine.ServiceID.SID_LOGIN_VALUE;
//            int cid = IMBaseDefine.LoginCmdID.CID_LOGIN_REQ_DEVICETOKEN_VALUE;
//
//            IMSocketManager.instance().sendRequest(req, sid, cid);
//
//        }
    }

    private void reqDeviceToken() {
//        String token = PushManager.getInstance().getToken();
//        IMLogin.IMDeviceTokenReq req = IMLogin.IMDeviceTokenReq.newBuilder()
//                .setUserId(loginId)
//                .setClientType(IMBaseDefine.ClientType.CLIENT_TYPE_ANDROID)
//                .setDeviceToken(token)
//                .build();
//        int sid = IMBaseDefine.ServiceID.SID_LOGIN_VALUE;
//        int cid = IMBaseDefine.LoginCmdID.CID_LOGIN_REQ_DEVICETOKEN_VALUE;
//
//        imSocketManager.sendRequest(req,sid,cid,new Packetlistener() {
//            @Override
//            public void onSuccess(Object response) {
//                //?? nothing to do
////                try {
////                    IMLogin.IMDeviceTokenRsp rsp = IMLogin.IMDeviceTokenRsp.parseFrom((CodedInputStream) response);
////                    int userId = rsp.getUserId();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
//            }
//
//            @Override
//            public void onFaild() {}
//
//            @Override
//            public void onTimeout() {}
//        });
    }


    public void onKickout(IMLogin.IMKickUser imKickUser) {
        Logger.e(TAG,"login#onKickout");
        int kickUserId = imKickUser.getUserId();
        IMBaseDefine.KickReasonType reason = imKickUser.getKickReason();
        isKickout = true;
        //imSocketManager.onMsgServerDisconn();
        imSocketManager.onMsgDeviceLogin();

    }

    //DB变更退出imSocketManager
    public void onKickout(){
        isKickout = true;
        imSocketManager.onMsgServerDisconn();
    }

    // 收到PC端登陆的通知，另外登陆成功之后，如果PC端在线，也会立马收到该通知
    public void onLoginStatusNotify(IMBuddy.IMPCLoginStatusNotify statusNotify) {
        EventBus.getDefault().post(QueuueHandlerEvent.MESSAGE_FINISH);
        int userId = statusNotify.getUserId();
        if (userId != loginId) {
            Logger.i(TAG,"login#onLoginStatusNotify userId ≠ loginId");
            return;
        }

        if (isKickout) {
            Logger.i(TAG,"login#already isKickout");
            return;
        }

        switch (statusNotify.getLoginStat()) {
            case USER_STATUS_ONLINE: {
                isPcOnline = true;
                EventBus.getDefault().postSticky(LoginEvent.PC_ONLINE);
            }
            break;

            case USER_STATUS_OFFLINE: {
                isPcOnline = false;
                EventBus.getDefault().postSticky(LoginEvent.PC_OFFLINE);
            }
            break;
        }
    }

    //修改密码发送请求
    public void updataPSW(String oldPswString, String newPswString) {

        IMLogin.IMModifyPassReq imModifyPassReq = IMLogin.IMModifyPassReq.newBuilder()
                .setUserId(loginId)
                .setOldPass(oldPswString)
                .setNewPass(newPswString)
                .setClientType(2)
                .build();

        int sid = IMBaseDefine.ServiceID.SID_LOGIN_VALUE;
        int cid = IMBaseDefine.LoginCmdID.CID_LOGIN_REQ_MODIFY_PASS_VALUE;
        imSocketManager.sendRequest(imModifyPassReq, sid, cid);

    }

    // 踢出PC端登陆
    public void reqKickPCClient() {
//        IMLogin.IMKickPCClientReq req = IMLogin.IMKickPCClientReq.newBuilder()
//                .setUserId(loginId)
//                .build();
//        int sid = IMBaseDefine.ServiceID.SID_LOGIN_VALUE;
//        int cid = IMBaseDefine.LoginCmdID.CID_LOGIN_REQ_KICKPCCLIENT_VALUE;
//        imSocketManager.sendRequest(req, sid, cid, new Packetlistener() {
//            @Override
//            public void onSuccess(Object response) {
//                IMContactManager.instance().kickoutPcStatues();
//                triggerEvent(LoginEvent.KICK_PC_SUCCESS);
//            }
//
//            @Override
//            public void onFaild() {
//                triggerEvent(LoginEvent.KICK_PC_FAILED);
//            }
//
//            @Override
//            public void onTimeout() {
//                triggerEvent(LoginEvent.KICK_PC_FAILED);
//            }
//        });
    }

    /**
     * ------------------状态的 set  get------------------------------
     */
    public int getLoginId() {
        return loginId;
    }

    public void setLoginId(int loginId) {
        Logger.d(TAG,"login#setLoginId -> loginId:%d", loginId);
        this.loginId = loginId;

    }

    public String getOaNum() {
        return oaNum;
    }

    public void setOaNum(String oaNum) {
        this.oaNum = oaNum;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    public boolean isEverLogined() {
        return everLogined;
    }

    public void setEverLogined(boolean everLogined) {
        this.everLogined = everLogined;
    }

    public UserEntity getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(UserEntity loginInfo) {
        this.loginInfo = loginInfo;
    }

    public LoginEvent getLoginStatus() {
        return loginStatus;
    }

    public boolean isKickout() {
        return isKickout;
    }

    public void setKickout(boolean isKickout) {
        this.isKickout = isKickout;
    }

    public boolean isPcOnline() {
        return isPcOnline;
    }

    public void setPcOnline(boolean isPcOnline) {
        this.isPcOnline = isPcOnline;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getMainName() {
        return mainName;
    }

    public void setMainName(String mainName) {
        this.mainName = mainName;
    }

    public String getLoginUserName() {
        return loginUserName;
    }

    public void setLoginUserName(String loginUserName) {
        this.loginUserName = loginUserName;
    }
}
