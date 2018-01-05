package im.jizhu.com.loginmodule.config;

import com.lesso.debug.BuildConfig;
import com.lesso.debug.DebuggerLib;


/**
 * @author : yingmu on 15-3-16.
 * @email : yingmu@mogujie.com.
 *
 */
public class UrlConstant {

    // 头像路径前缀
    public final static String AVATAR_URL_PREFIX = "";

    // access 地址
//    public final static String ACCESS_MSG_ADDRESS = "http://139.196.190.127:3120/msg_server";//线上测试
//    public final static String ACCESS_MSG_ADDRESS_RESERVE = "http://139.196.190.127:3120/msg_server";

//    public final static String ACCESS_MSG_ADDRESS = "http://120.76.24.174:6120/msg_server";  //深圳机房
//    public final static String ACCESS_MSG_ADDRESS_RESERVE = "http://120.76.222.118:6120/msg_server";  //正式备用IP

    //测试环境
    private final static String ACCESS_MSG_ADDRESS_DEV = "http://192.168.4.169:8080/msg_server";
    private final static String ACCESS_MSG_ADDRESS_RESERVE_DEV = "http://192.168.4.169:8080/msg_server";

    public final static String ACCESS_MSG_ADDRESS = "http://server1.lesso.com:6120/msg_server";  //深圳机房 域名地址
    public final static String ACCESS_MSG_ADDRESS_RESERVE = "http://server2.lesso.com:6120/msg_server";  //正式备用IP 域名地址 //正式备用IP 域名地址

    // public final static String ACCESS_MSG_ADDRESS = "http://120.25.177.9:5120/msg_server";//nca公司使用的ip
//    public final static String ACCESS_MSG_ADDRESS_RESERVE = "http://120.76.222.118:6120/msg_server";  //nca公司备用IP

    //public final static String ACCESS_MSG_ADDRESS = "http://kap.chinacloudapp.cn:5120/msg_server";

    // public final static String UPDATE_SERVER_ADDRESS = "http://121.12.253.157:6086/android/AndroidVersion.xml";//nca公司版本更新的地址
    //public final static String UPDATE_SERVER_ADDRESS = "http://120.76.24.174/cc/android/AndroidVersion.xml";
    public final static String UPDATE_SERVER_ADDRESS = "http://server1.lesso.com/cc/android/AndroidVersion.xml";//域名更新地址
    //public final static String UPDATE_SERVER_ADDRESS = "http://219.128.102.101:65155/lessocrm/installer/AndroidVersion.xml";//内测

    public final static String ACCESS_MSG_NOTICE_OPEN = "http://app.lesso.com/edfx/";//LESSO cc打开公告地址
//    public final static String ACCESS_MSG_NOTICE_OPEN = "http://app.lesso.com/edfx/themes/default/";//域名 cc打开公告地址
    //public final static String ACCESS_MSG_NOTICE_OPEN = "http://121.12.253.157:8000/edfx/themes/default/";//NCA打开公告地址
    /**
     * 正式环境中的应用所需的地址
     */
    public final static String REQ_GUID="http://app.lesso.com/edfx/WebService/mobileServer.asmx/LoginCheck";
    public final static String REQ_LIST="http://app.lesso.com/edfx/WebService/mobileServer.asmx/DownRight";
    /**
     * 测试环境中的应用所需的地址
     */
    //public final static String REQ_GUID="http://dev.lesso.com/edfx/WebService/mobileServer.asmx/LoginCheck";
    //public final static String REQ_LIST="http://dev.lesso.com/edfx/WebService/mobileServer.asmx/DownRight";

    public final static String OA_EMOBILE_WEB = "http://oa.liansu.com:89/verifyLogin.do?";
    public final static String OA_EMOBILE_WEB_FOLW ="http://oa.liansu.com:89/mobile/plugin/1/view.jsp?detailid=";

    //imapp.lesso.com 219.128.102.101:18080
    public final static String SMALL_APPLICATION_URL ="http://imapp.lesso.com/lessoWeb/apps.html";
    public final static String SMALL_APPLICATION_URL_TEST ="http://172.16.100.121/cc/apps.html";

    /**
     * http://192.168.4.140/imapi
     * http://cc.lesso.com/imapi
     * http数据请求
     */
    public final static String USER_LOGIN_DATA = "http://cc.lesso.com/imapi/api/base/GetSessionData";//http://192.168.4.140/imapi/api/base/GetSessionData?

    public final static String USER_LOGIN_DATA_DEV = "http://imapp.lesso.com/IMAPI/api/base/GetSessionData";//http://192.168.4.140/imapi/api/base/GetSessionData?

    /**
     * 搜索用户  正式环境
     */
    public final static String USER_SEARCH_DATA = "http://cc.lesso.com/imapi/api/base/GetUserByKey";
    public final static String USER_BYID_SEARCH_DATA = "http://cc.lesso.com/imapi/api/base/GetUserByUserId";
    public final static String USER_BYDEPT_SEARCH_DATA = "http://cc.lesso.com/imapi/api/base/GetUserByDept";

    //测试环境
    public final static String USER_SEARCH_DATA_DEV = "http://imapp.lesso.com/imapi/api/base/GetUserByKey";
    public final static String USER_BYID_SEARCH_DATA_DEV = "http://imapp.lesso.com/imapi/api/base/GetUserByUserId";
    public final static String USER_BYDEPT_SEARCH_DATA_DEV = "http://imapp.lesso.com/imapi/api/base/GetUserByDept";


    public static String accessMsgAddress(){ //socket登录
        if(BuildConfig.DEBUG){
            if(DebuggerLib.isOfficialEnv()){
                return ACCESS_MSG_ADDRESS;
            }else{
                return ACCESS_MSG_ADDRESS_DEV;
            }
        }
        return ACCESS_MSG_ADDRESS;
    }

    public static String accessMsgAddressReserve(){//socket重连登录
        if(BuildConfig.DEBUG){
            if(DebuggerLib.isOfficialEnv()){
                return ACCESS_MSG_ADDRESS_RESERVE;
            }else{
                return ACCESS_MSG_ADDRESS_RESERVE_DEV;
            }
        }
        return ACCESS_MSG_ADDRESS_RESERVE;
    }

    public static String accessHttpAddressReserve(){//HTTP登录数据下发
        if(BuildConfig.DEBUG){
            if(DebuggerLib.isOfficialEnv()){
                return USER_LOGIN_DATA;
            }else{
                return USER_LOGIN_DATA_DEV;
            }
        }
        return USER_LOGIN_DATA;
    }

    public static String accessHttpSearchUserAddressReserve(){//HTTP搜索成员数据
        if(BuildConfig.DEBUG){
            if(DebuggerLib.isOfficialEnv()){
                return USER_SEARCH_DATA;
            }else{
                return USER_SEARCH_DATA_DEV;
            }
        }
        return USER_SEARCH_DATA;
    }
    public static String accessHttpSearchUserIdAddressReserve(){//HTTP搜索成员ID数据
        if(BuildConfig.DEBUG){
            if(DebuggerLib.isOfficialEnv()){
                return USER_BYID_SEARCH_DATA;
            }else{
                return USER_BYID_SEARCH_DATA_DEV;
            }
        }
        return USER_BYID_SEARCH_DATA;
    }
    public static String accessHttpSearchDeptAddressReserve(){//HTTP搜索部门数据
        if(BuildConfig.DEBUG){
            if(DebuggerLib.isOfficialEnv()){
                return USER_BYDEPT_SEARCH_DATA;
            }else{
                return USER_BYDEPT_SEARCH_DATA_DEV;
            }
        }
        return USER_BYDEPT_SEARCH_DATA;
    }

    public static String appliationAddressReserve(){//应用
        if(BuildConfig.DEBUG){
            if(DebuggerLib.isOfficialEnv()){
                return SMALL_APPLICATION_URL;
            }else{
                return SMALL_APPLICATION_URL_TEST;
            }
        }
        return SMALL_APPLICATION_URL;
    }

    //请求历史消息
    public final static String USER_SESSION_HISTORY_DATA = "http://imapp.lesso.com/imapi/api/base/GetSessionsHistoryMsgByPindex";

}
