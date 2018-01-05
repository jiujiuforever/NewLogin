package im.jizhu.com.loginmodule.DB.sp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by it026 on 2017/6/24.
 */
public class TimeSaveSp {

    private final String fileName = "timesave.ini";
    private Context ctx;
    SharedPreferences sharedPreferences;

    private TimeSaveSp(){

    }
    private static TimeSaveSp inst = null;

    public static TimeSaveSp instance(){

        if (inst==null){
            synchronized (TimeSaveSp.class) {
                inst = new TimeSaveSp();
            }
        }
        return inst;
    }

    public void init(Context context) {
        this.ctx = context.getApplicationContext();
        sharedPreferences = ctx.getSharedPreferences
                (fileName, ctx.MODE_PRIVATE);
    }

    public void setRoleTimeValue(String key,int value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        //提交当前数据
        editor.commit();
    }
    public int getRoleTimeValue(String key) {
        int intValue = sharedPreferences.getInt(key,0);
        return intValue;
    }

    public void setUserTimeValue(String key,int value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        //提交当前数据
        editor.commit();
    }
    public int getUserTimeValue(String key) {
        int intValue = sharedPreferences.getInt(key, 0);
        return intValue;
    }

    public void setDeptTimeValue(String key,int value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        //提交当前数据
        editor.commit();
    }
    public int getDeptTimeValue(String key) {
        int intValue = sharedPreferences.getInt(key,0);
        return intValue;
    }

    public void setGroupTimeValue(String key,int value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        //提交当前数据
        editor.commit();
    }
    public int getGroupTimeValue(String key) {
        int intValue = sharedPreferences.getInt(key,0);
        return intValue;
    }

    public void setSessionTimeValue(String key,int value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        //提交当前数据
        editor.commit();
    }
    public int getSessionTimeValue(String key) {
        int intValue = sharedPreferences.getInt(key,0);
        return intValue;
    }

    public void setFlagTimeValue(String key,int value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        //提交当前数据
        editor.commit();
    }
    public int getFlagTimeValue(String key) {
        int intValue = sharedPreferences.getInt(key,0);
        return intValue;
    }
    public void setCurrentTimeValue(String key,int value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        //提交当前数据
        editor.commit();
    }
    public int getCurrentTimeValue(String key) {
        int intValue = sharedPreferences.getInt(key,0);
        return intValue;
    }

}
