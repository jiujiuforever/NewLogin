package im.jizhu.com.loginmodule.DB.sp;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * @author : yingmu on 15-1-6.
 * @email : yingmu@mogujie.com.
 * <p/>
 * todo need Encryption
 */
public class LoginSp {

    private final String fileName = "login.ini";
    private Context ctx;
    private final String KEY_LOGIN_NAME = "loginName";
    private final String KEY_PWD = "pwd";
    private final String KEY_LOGIN_ID = "loginId";
    private final String KEY_OA_NUM = "oanum";
    private final String KEY_SAP_NUM = "sapnum";
    private final String KEY_GUID = "guid";


    SharedPreferences sharedPreferences;

    private static LoginSp loginSp = null;

    public static LoginSp instance() {
        if (loginSp == null) {
            synchronized (LoginSp.class) {
                loginSp = new LoginSp();
            }
        }
        return loginSp;
    }

    private LoginSp() {
    }


    public void init(Context ctx) {
        this.ctx = ctx;
        sharedPreferences = ctx.getSharedPreferences
                (fileName, ctx.MODE_PRIVATE);
    }

    /**
     * 修改密码后将密码这条信息删除，防止用户收到PC端修改密码后将该程序强制关闭，使得密码一致存在，下次可以登录(密码是错误的，也登录进去了)
     */
    public void clearPW() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(KEY_PWD);
        //提交当前数据
        editor.commit();
    }

    public void setLoginInfo(String userName, String pwd, int loginId, String oanum, String sapnum, String guid) {
        // 横写
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LOGIN_NAME, userName);
        editor.putString(KEY_PWD, pwd);
        editor.putInt(KEY_LOGIN_ID, loginId);
        editor.putString(KEY_OA_NUM, oanum);
        editor.putString(KEY_SAP_NUM, sapnum);
        editor.putString(KEY_GUID, guid);
        //提交当前数据
        editor.commit();
    }

    public void setLoginInfo(String userName, String pwd, int loginId, String oanum, String sapnum) {
        // 横写
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_LOGIN_NAME, userName);
        editor.putString(KEY_PWD, pwd);
        editor.putInt(KEY_LOGIN_ID, loginId);
        editor.putString(KEY_OA_NUM, oanum);
        editor.putString(KEY_SAP_NUM, sapnum);
        //提交当前数据
        editor.commit();
    }

    public void setLoginGuid(String guid) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_GUID, guid);
        editor.commit();
    }

    public SpLoginIdentity getLoginIdentity() {

        if (ctx == null) {
            return null;
        }
        sharedPreferences = ctx.getSharedPreferences
                (fileName, ctx.MODE_PRIVATE);
        String userName = sharedPreferences.getString(KEY_LOGIN_NAME, null);
        String pwd = sharedPreferences.getString(KEY_PWD, null);
        int loginId = sharedPreferences.getInt(KEY_LOGIN_ID, 0);
        String oaNum = sharedPreferences.getString(KEY_OA_NUM, null);
        String sapNum = sharedPreferences.getString(KEY_SAP_NUM, null);
        String guid = sharedPreferences.getString(KEY_GUID, null);

        /**pwd不判空: loginOut的时候会将pwd清空*/
        if (TextUtils.isEmpty(userName) || loginId == 0) {
            return null;
        }
        return new SpLoginIdentity(userName, pwd, loginId, oaNum, sapNum, guid);
    }

    public class SpLoginIdentity {
        private String loginName;
        private String pwd;
        private int loginId;
        private String oaNum;
        private String sapNum;
        private String guid;

        public SpLoginIdentity(String mUserName, String mPwd, int mLoginId, String oaNum, String sapNum, String guid) {
            loginName = mUserName;
            pwd = mPwd;
            loginId = mLoginId;
            this.oaNum = oaNum;
            this.sapNum = sapNum;
            this.guid = guid;
        }

        public int getLoginId() {
            return loginId;
        }

        public void setLoginId(int loginId) {
            this.loginId = loginId;
        }

        public String getLoginName() {
            return loginName;
        }

        public String getPwd() {
            return pwd;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }

        public String getGuid() {
            return guid;
        }

        public void setOaNum(String oaNum) {
            this.oaNum = oaNum;
        }

        public String getOaNum() {
            return oaNum;
        }

        public void setSapNum(String sapNum) {
            this.sapNum = sapNum;
        }

        public String getSapNum() {
            return sapNum;
        }

    }
}
