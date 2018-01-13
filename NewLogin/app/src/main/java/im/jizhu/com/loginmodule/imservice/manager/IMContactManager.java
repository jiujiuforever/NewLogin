//package im.jizhu.com.loginmodule.imservice.manager;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.graphics.Bitmap;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.view.View;
//import android.widget.Toast;
//
//import im.jizhu.com.loginmodule.DB.DBInterface;
//import im.jizhu.com.loginmodule.DB.dao.DaoMaster;
//import im.jizhu.com.loginmodule.DB.dao.DepartmentDao;
//import im.jizhu.com.loginmodule.DB.dao.GroupDao;
//import im.jizhu.com.loginmodule.DB.dao.SessionDao;
//import im.jizhu.com.loginmodule.DB.dao.UserDao;
//import im.jizhu.com.loginmodule.DB.entity.DepartmentEntity;
//import im.jizhu.com.loginmodule.DB.entity.UserEntity;
//import im.jizhu.com.loginmodule.DB.entity.UserStatusEntity;
//import im.jizhu.com.loginmodule.DB.sp.LoginSp;
//import im.jizhu.com.loginmodule.DB.sp.TimeSaveSp;
//import im.jizhu.com.loginmodule.R;
//import im.jizhu.com.loginmodule.httpdata.HttpJavaBean;
//import im.jizhu.com.loginmodule.httpdata.HttpPostReq;
//import im.jizhu.com.loginmodule.imservice.event.OtherEvent;
//import im.jizhu.com.loginmodule.imservice.event.QueuueHandlerEvent;
//import im.jizhu.com.loginmodule.imservice.event.UserInfoEvent;
//import im.jizhu.com.loginmodule.protobuf.IMBaseDefine;
//import im.jizhu.com.loginmodule.protobuf.IMBuddy;
//import im.jizhu.com.loginmodule.protobuf.IMLogin;
//import im.jizhu.com.loginmodule.protobuf.helper.ProtoBuf2JavaBean;
//import im.jizhu.com.loginmodule.utils.IMUIHelper;
//import im.jizhu.com.loginmodule.utils.Logger;
//import im.jizhu.com.loginmodule.utils.pinyin.PinYin;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.assist.FailReason;
//import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
//
//import org.json.JSONArray;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.List;
//import java.util.Map;
//import java.util.Timer;
//import java.util.TimerTask;
//import java.util.concurrent.ConcurrentHashMap;
//
//import de.greenrobot.event.EventBus;
//
///**
// * 负责用户信息的请求
// * 为回话页面以及联系人页面提供服务
// * <p/>
// * 联系人信息管理
// * 普通用户的version  有总版本
// * 群组没有总version的概念， 每个群有version
// * 具体请参见 服务端具体的pd协议
// */
//public class IMContactManager extends IMManager {
//
//    private static final String TAG = "IMContactManager";
//
//    private SharedPreferences sp;
//    private int userType;
//    private int deparmentChange = 0;
//    private int userChange = 0;
//    private boolean deparmentFlag = false;
//    private String guid;
//    //private int flag = 0;
//    public boolean isownUpdata = false;
//    // 单例
//    private static IMContactManager inst = new IMContactManager();
//
//    public static IMContactManager instance() {
//        return inst;
//    }
//
//    private IMSocketManager imSocketManager = IMSocketManager.instance();
//    private DBInterface dbInterface = DBInterface.instance();
//
//    // 自身状态字段
//    private boolean userDataReady = false;
//    private Map<Integer, UserEntity> userMap = new ConcurrentHashMap<>();
//    private Map<Integer, DepartmentEntity> departmentMap = new ConcurrentHashMap<>();
//    private Map<Integer, UserStatusEntity> statusEntityMap = new ConcurrentHashMap<>();
//
//    // 新建组成员列表
//    private List<UserEntity>  userselectlist = new ArrayList<>();
//
//    @Override
//    public void doOnStart() {
//    }
//
//    /**
//     * 登陆成功触发
//     * auto自动登陆
//     */
//    public void onNormalLoginOk() {
//        Log.e("test", "onNormalLoginOk.......");
//        onLocalLoginOk();
//
////        onLocalNetOk();
//
//    }
//
//    /**
//     * 加载本地DB的状态
//     * 不管是离线还是在线登陆，loadFromDb 要运行的
//     */
//    public void onLocalLoginOk() {
//        Logger.d(TAG,"contact#loadAllUserInfo");
//
//        List<DepartmentEntity> deptlist = dbInterface.loadAllDept();
//        Logger.d(TAG,"contact#loadAllDept dbsuccess");
//
//        List<UserEntity> userlist = dbInterface.loadAllUsers();
//        Logger.d(TAG,"contact#loadAllUserInfo dbsuccess");
//
//        List<UserStatusEntity> statusEntityList = dbInterface.loadAllUsersStatus();
//        Logger.d(TAG,"userStatus#loadAllUsersStatus dbsuccess");
//
//        for (UserEntity userInfo : userlist) {
//            // todo DB的状态不包含拼音的，这个样每次都要加载啊
//            //服务器有返回拼音,注释掉
//            //T.C. 20160311
//            PinYin.getPinYin(userInfo.getMainName(), userInfo.getPinyinElement());
//            if (userInfo.getStatus() != 1) {
//                userMap.put(userInfo.getPeerId(), userInfo);
//            }
//        }
//        userType = IMLoginManager.instance().getLoginType();
//
//        for (DepartmentEntity deptInfo : deptlist) {
//            PinYin.getPinYin(deptInfo.getDepartName(), deptInfo.getPinyinElement());
//            if (deptInfo.getStatus() != 1) {
//
//                departmentMap.put(deptInfo.getDepartId(), deptInfo);
//            }
//        }
//
////        for (UserStatusEntity userStatusEntity : statusEntityList) {
////
////            statusEntityMap.put(userStatusEntity.getUserId(), userStatusEntity);
////        }
//        //reqAllDiscover();
//        triggerEvent(UserInfoEvent.USER_INFO_OK);
//    }
//
//    /**
//     * 网络链接成功，登陆之后请求
//     */
//    public void onLocalNetOk() {
//
//        int loginId = IMLoginManager.instance().getLoginId();
//        sp = ctx.getSharedPreferences("userTIme", Context.MODE_APPEND);
//        // 用户信息
//        //int updateTime = dbInterface.getUserInfoLastTime();
//        int updateTime = sp.getInt("LatestUpdate" + loginId + "Time", 0);
//        int lastUpdateTimeObject = sp.getInt("LatestUpdate" + loginId + "TimeObject", 0);
//        int latestUpdateTimeRole = sp.getInt("LatestUpdate" + loginId + "TimeRole", 0);
//        reqGetAllUsers(updateTime, lastUpdateTimeObject, latestUpdateTimeRole);
//        // 部门信息
////        int deptUpdateTime = sp.getInt("DepartmenLatest" + loginId + "UpdateTime", 0);
////        reqGetDepartment(deptUpdateTime);
//        //}
//
//        //获取所有发现
//        //reqAllDiscover();
//    }
//
//    /**
//     * http请求返回人员数据
//     * @param jsonArrayuser
//     * @param flag
//     */
//    public void reqAllUserData(JSONArray jsonArrayuser , JSONArray jsonArrayDept , int flag) {
//
//        try{
//            ArrayList<UserEntity> needDb = new ArrayList<>();
//            if (flag == 1 && userChange == 0) {
//
//                //清除表删数据
//                String dbName = "tt_" + LoginSp.instance().getLoginIdentity().getLoginId() + ".db";
//                DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(IMContactManager.instance().ctx, dbName, null);
//                UserDao.dropTable(helper.getWritableDatabase(), true);
//                DepartmentDao.dropTable(helper.getWritableDatabase(), true);
//                GroupDao.dropTable(helper.getWritableDatabase(), true);
//                SessionDao.dropTable(helper.getWritableDatabase(), true);
//                SessionDao.createTable(helper.getWritableDatabase(), false);
//                GroupDao.createTable(helper.getWritableDatabase(), false);
//                UserDao.createTable(helper.getWritableDatabase(), false);
//                DepartmentDao.createTable(helper.getWritableDatabase(), false);
//
//                for (int i = 0; i < jsonArrayuser.length(); i++) {
//                    UserEntity userEntity = HttpJavaBean.getUserEntity(jsonArrayuser,i);
//                    if (userEntity.getStatus() != 1) {
//                        userMap.put(userEntity.getPeerId(), userEntity);
//                    }
//                    if (userChange == 1 && userEntity.getStatus() == 1) {
//                        userMap.remove(userEntity.getPeerId());
//                    }
//                    needDb.add(userEntity);
//                }
//                dbInterface.batchInsertOrUpdateUser(needDb);
//                triggerEvent(UserInfoEvent.USER_INFO_OK);
//
//                //部门数据处理
//                /*deparmentFlag = true;
//                reqAllDepartment(jsonArrayDept,true);*/
//
//            }else if (flag == 1 && userChange == 1) {
//                updatePower();
//                userChange = 0;
//            } else {
//                for (int i = 0; i < jsonArrayuser.length(); i++) {
//                    UserEntity userEntity = HttpJavaBean.getUserEntity(jsonArrayuser,i);
//                    if (userEntity.getStatus() != 1) {
//                        userMap.put(userEntity.getPeerId(), userEntity);
//                    }
//                    if (userChange == 1 && userEntity.getStatus() == 1) {
//                        userMap.remove(userEntity.getPeerId());
//                    }
//                    needDb.add(userEntity);
//                }
//                dbInterface.batchInsertOrUpdateUser(needDb);
//                triggerEvent(UserInfoEvent.USER_INFO_OK);
//
//                //部门数据处理
//                //deparmentFlag = true;
//                //reqAllDepartment(jsonArrayDept);
//
//            }
//
//            userPCOnlineStatues();
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * http请求返回部门数据
//     * @param jsonArrayDept
//     */
//    public void reqAllDepartment(JSONArray jsonArrayDept,boolean deparmentFlag) {
//        try{
//
//            if (deparmentFlag) {
////                departmentMap.clear();
//                deparmentFlag = false;
//            }
//            ArrayList<DepartmentEntity> needDb = new ArrayList<>();
//            for (int i = 0; i < jsonArrayDept.length(); i++) {
//                DepartmentEntity entity = HttpJavaBean.getDepartEntity(jsonArrayDept,i);
//                if (entity.getStatus() != 1) {
//                    departmentMap.put(entity.getDepartId(), entity);
//                }
//                if (deparmentChange == 1 && entity.getStatus() == 1) {
//                    departmentMap.remove(entity.getDepartId());
//                }
//                needDb.add(entity);
//            }
//            dbInterface.batchInsertOrUpdateDepart(needDb);
//            triggerEvent(UserInfoEvent.USER_INFO_UPDATE);
//
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 人员数据插入
//     * @param userEntity
//     */
//    public void inserSearchUserData(UserEntity userEntity) {
//
//        if (userEntity == null) {
//            return;
//        }
//        userMap.put(userEntity.getPeerId(), userEntity);
//        dbInterface.insertOrUpdateUser(userEntity);
//    }
//
//    /**
//     * 会话无人员的情况下，插入人员数据
//     * @param needDb
//     */
//    public void inserSessionUserData(ArrayList<UserEntity> needDb,UserEntity user) {
//
//        userMap.put(user.getPeerId(), user);
//        dbInterface.batchInsertOrUpdateUser(needDb);
//        triggerEvent(UserInfoEvent.USER_INFO_OK);
//    }
//
//    @Override
//    public void reset() {
//        userDataReady = false;
//        userMap.clear();
//        departmentMap.clear();
//        statusEntityMap.clear();
//    }
//
//
//    /**
//     * @param event
//     */
//    public void triggerEvent(UserInfoEvent event) {
//        //先更新自身的状态
//        switch (event) {
//            case USER_INFO_OK:
//                userDataReady = true;
//                break;
//
//        }
//        EventBus.getDefault().postSticky(event);
//    }
//
//    public void triggerEvent(OtherEvent event) {
//
//        EventBus.getDefault().postSticky(event);
//    }
//
//    /**
//     * -----------------------事件驱动---end---------
//     */
//
//    private void reqGetAllUsers(int lastUpdateTime, int lastUpdateTimeObject, int latestUpdateTimeRole) {
//        Logger.i(TAG,"contact#reqGetAllUsers");
//        int userId = IMLoginManager.instance().getLoginId();
//
//        IMBuddy.IMAllUserReq imAllUserReq = IMBuddy.IMAllUserReq.newBuilder()
//                .setUserId(userId)
//                .setLatestUpdateTime(lastUpdateTime)
//                .setLatestUpdateTimeObject(lastUpdateTimeObject)
//                .setLatestUpdateTimeRole(latestUpdateTimeRole)
//                .build();
//
//        int sid = IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE;
//        int cid = IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_ALL_USER_REQUEST_VALUE;
//        imSocketManager.sendRequest(imAllUserReq, sid, cid);
//    }
//    Handler handler=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            Toast.makeText(ctx,"消息:"+msg.what,Toast.LENGTH_SHORT).show();
//        }
//    };
//
//    /**
//     * yingmu change id from string to int
//     *
//     * @param imAllUserRsp 1.请求所有用户的信息,总的版本号version
//     *                     2.匹配总的版本号，返回可能存在变更的
//     *                     3.选取存在变更的，请求用户详细信息
//     *                     4.更新DB，保存globalVersion 以及用户的信息
//     */
//    public void onRepAllUsers(IMBuddy.IMAllUserRsp imAllUserRsp) {
//        Logger.i(TAG,"contact#onRepAllUsers");
//        int userId = imAllUserRsp.getUserId();
//        int lastTime = imAllUserRsp.getLatestUpdateTime();
//        // lastTime 需要保存嘛? 不保存了
//        int count = imAllUserRsp.getUserListCount();
//        if (count <= 0) {
//            return;
//        }
//        int loginId = IMLoginManager.instance().getLoginId();
//        if (userId != loginId) {
//            Logger.e(TAG,"[fatal error] userId not equels loginId ,cause by onRepAllUsers");
//            return;
//        }
//        sp = ctx.getSharedPreferences("userTIme", Context.MODE_APPEND);
//        SharedPreferences.Editor editor = sp.edit();
//        int flag = imAllUserRsp.getFlag();
//
//        List<IMBaseDefine.UserInfo> changeList = imAllUserRsp.getUserListList();
//        ArrayList<UserEntity> needDb = new ArrayList<>();
//
//        if (flag == 1 && userChange == 0) {
//
//            editor.putInt("LatestUpdate" + loginId + "Time", imAllUserRsp.getLatestUpdateTime());
//            editor.putInt("LatestUpdate" + loginId + "TimeObject", imAllUserRsp.getLatestUpdateTimeObject());
//            editor.putInt("LatestUpdate" + loginId + "TimeRole", imAllUserRsp.getLatestUpdateTimeRole());
//            editor.commit();
//
//            String dbName = "tt_" + LoginSp.instance().getLoginIdentity().getLoginId() + ".db";
//            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(IMContactManager.instance().ctx, dbName, null);
//            UserDao.dropTable(helper.getWritableDatabase(), true);
//            DepartmentDao.dropTable(helper.getWritableDatabase(), true);
//            UserDao.createTable(helper.getWritableDatabase(), false);
//            DepartmentDao.createTable(helper.getWritableDatabase(), false);
//
//            for (IMBaseDefine.UserInfo userInfo : changeList) {
//                UserEntity entity = ProtoBuf2JavaBean.getUserEntity(userInfo);
//                //Log.e("test","所有状态值："+entity.getMainName()+"-"+entity.getPeerId()+"-statues:"+entity.getStatus());
//                if (entity.getStatus() != 1) {
//                    userMap.put(entity.getPeerId(), entity);
//                    //Log.e("test", "statues不为1状态值：" + entity.getMainName() + "-" + entity.getPeerId() + "-statues:" + entity.getStatus());
//                }
//                if (userChange == 1 && entity.getStatus() == 1) {
//                    userMap.remove(entity.getPeerId());
//                }
//                needDb.add(entity);
//            }
//
//            dbInterface.batchInsertOrUpdateUser(needDb);
//            triggerEvent(UserInfoEvent.USER_INFO_OK);
//            deparmentFlag = true;
//            reqGetDepartment(0);
//
//        } else if (flag == 1 && userChange == 1) {
//
//            updatePower();
//            userChange = 0;
//
//        } else {
//            editor.putInt("LatestUpdate" + loginId + "Time", imAllUserRsp.getLatestUpdateTime());
//            editor.putInt("LatestUpdate" + loginId + "TimeObject", imAllUserRsp.getLatestUpdateTimeObject());
//            editor.putInt("LatestUpdate" + loginId + "TimeRole", imAllUserRsp.getLatestUpdateTimeRole());
//            editor.commit();
//            for (IMBaseDefine.UserInfo userInfo : changeList) {
//                UserEntity entity = ProtoBuf2JavaBean.getUserEntity(userInfo);
//                if (entity.getStatus() != 1) {
//                    userMap.put(entity.getPeerId(), entity);
//                }
//                if (userChange == 1 && entity.getStatus() == 1) {
//                    userMap.remove(entity.getPeerId());
//                }
//                needDb.add(entity);
//            }
//
//            dbInterface.batchInsertOrUpdateUser(needDb);
//            triggerEvent(UserInfoEvent.USER_INFO_OK);
//
//        }
//        EventBus.getDefault().post(QueuueHandlerEvent.MESSAGE_FINISH);
//        userPCOnlineStatues();
//        userOnlineStatues(changeList);
//
//
//    }
//
//    /**
//     * 获取其他 用户在线状态 数据解析
//     */
//    public void onRepOnlineStatues(IMLogin.IMGetOtherUserStatusRes onLineUserStatusRes) {
//
//
//        List<IMBaseDefine.UserStatusInfo> changeList = onLineUserStatusRes.getUserStatusListList();
//        ArrayList<UserStatusEntity> needDb = new ArrayList<>();
//        for (IMBaseDefine.UserStatusInfo userStatusInfo : changeList) {
//
//            UserStatusEntity userStatusEntity = ProtoBuf2JavaBean.getUserStatusEntity(userStatusInfo);
//            statusEntityMap.put(userStatusEntity.getUserId(), userStatusEntity);
//            needDb.add(userStatusEntity);
//        }
//        dbInterface.batchInsertOrUpdateUserStatus(needDb);
//        triggerEvent(UserInfoEvent.USER_INFO_OK);
//        EventBus.getDefault().post(QueuueHandlerEvent.MESSAGE_FINISH);
//    }
//
//    /**
//     * 获取其他 用户在线状态 通知（数据解析）
//     */
//    public void onRepUserStatusUpdateNotify(IMBuddy.IMUserStatNotify userStatusUpdateNotify) {
//        int userId = userStatusUpdateNotify.getUserId();
//        int user_status = userStatusUpdateNotify.getUserStatus();
//        IMBaseDefine.ClientType clienttype = userStatusUpdateNotify.getClientType();
//        ArrayList<UserStatusEntity> needDb = new ArrayList<>();
//        for (UserStatusEntity userStatus : statusEntityMap.values()) {
//            if (userId == userStatus.getUserId()) {
//                int usertatus = userStatus.getUserStatus();
//                if (user_status == 1) {
//                    if (clienttype == IMBaseDefine.ClientType.CLIENT_TYPE_WINDOWS) {
//                        usertatus = usertatus | 4;
//                    } else if (clienttype == IMBaseDefine.ClientType.CLIENT_TYPE_IOS) {
//                        usertatus = usertatus | 2;
//                    } else if (clienttype == IMBaseDefine.ClientType.CLIENT_TYPE_ANDROID) {
//                        usertatus = usertatus | 1;
//                    }
//                } else if (user_status == 2) {
//                    if (clienttype == IMBaseDefine.ClientType.CLIENT_TYPE_WINDOWS) {
//                        usertatus = usertatus & ~4;
//                    } else if (clienttype == IMBaseDefine.ClientType.CLIENT_TYPE_IOS) {
//                        usertatus = usertatus & ~2;
//                    } else if (clienttype == IMBaseDefine.ClientType.CLIENT_TYPE_ANDROID) {
//                        usertatus = usertatus & ~1;
//                    }
//                }
//                int isonline = 0;
//                if (usertatus > 0) {
//                    isonline = 1;
//                } else {
//                    isonline = 0;
//                }
//                userStatus.setUserId(userId);
//                userStatus.setUserStatus(usertatus);
//                userStatus.setIsOnline(isonline);
//                statusEntityMap.put(userStatus.getUserId(), userStatus);
//                needDb.add(userStatus);
//            }
//        }
//        dbInterface.updateOnlineStateType(needDb);
//        triggerEvent(UserInfoEvent.USER_INFO_OK);
//        EventBus.getDefault().post(QueuueHandlerEvent.MESSAGE_FINISH);
//    }
//
//    /**
//     * 在线状态人员数据值 List<UserEntity> contactList = new ArrayList<>(userMap.values());
//     */
//    public List<UserStatusEntity> getUserStatusList() {
//
//        List<UserStatusEntity> userStatusList = new ArrayList<>();
//
//        for (UserStatusEntity userStatus : statusEntityMap.values()) {
//            userStatusList.add(userStatus);
//        }
//        return userStatusList;
//    }
//    public UserStatusEntity  getUserStatusEntity(int userId){
//
//        List<UserStatusEntity> userStatusList = new ArrayList<>();
//        for (UserStatusEntity userStatus : statusEntityMap.values()) {
//            userStatusList.add(userStatus);
//        }
//        UserStatusEntity statusEntity0 = new UserStatusEntity();
//        for (UserStatusEntity statusEntity : userStatusList) {
//
//            if (userId ==statusEntity.getUserId()){
//                statusEntity0 = statusEntity;
//                return statusEntity;
//            }
//        }
//        return statusEntity0;
//    }
//
//    /**
//     * 踢出PC端后数据修改
//     */
//    public void kickoutPcStatues() {
//
//        UserStatusEntity userStatus = new UserStatusEntity();
//        ArrayList<UserStatusEntity> needDb = new ArrayList<>();
//
//        userStatus.setUserId(LoginSp.instance().getLoginIdentity().getLoginId());
//        userStatus.setUserStatus(1);
//        userStatus.setIsOnline(1);
//        statusEntityMap.put(userStatus.getUserId(), userStatus);
//
//        needDb.add(userStatus);
//        statusEntityMap.put(userStatus.getUserId(), userStatus);
//
//    }
//
//    /**
//     * 修改头像AVATAR_CHANGED
//     */
//    public void updatAvater(IMBuddy.IMAvatarChangedNotify imAvatarChangedNotify) {
//
//        try{
//
//            UserEntity user = DBInterface.instance().getByLoginId(imAvatarChangedNotify.getChangedUserId());
//            ImageLoader.getInstance().getDiskCache().remove(user.getAvatar());
//            user.setAvatar(imAvatarChangedNotify.getAvatarUrl());
//            ArrayList<UserEntity> users = new ArrayList<UserEntity>(1);
//            users.add(user);
//            userMap.put(user.getPeerId(), user);
//            DBInterface.instance().batchInsertOrUpdateUser(users);
//            EventBus.getDefault().post(QueuueHandlerEvent.MESSAGE_FINISH);
//            DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnFail(R.drawable.tt_default_user_portrait_corner)
//                    .cacheOnDisk(true).build();
//            ImageLoader.getInstance().loadImage(imAvatarChangedNotify.getAvatarUrl(), options, new ImageLoadingListener() {
//                @Override
//                public void onLoadingStarted(String s, View view) {
//
//                }
//
//                @Override
//                public void onLoadingFailed(String s, View view, FailReason failReason) {
//
//                }
//
//                @Override
//                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                    triggerEvent(UserInfoEvent.USER_INFO_AVATER_UPDATE);
//                }
//
//                @Override
//                public void onLoadingCancelled(String s, View view) {
//
//                }
//            });
//
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    /**
//     * 修改资料
//     * @param imEditDataNotify
//     */
//    public void editData(IMBuddy.IMEditDataNotify imEditDataNotify) {
//
//        try{
//            UserEntity user = DBInterface.instance().getByLoginId(imEditDataNotify.getChangedUserId());
//            if (user != null) {
//                user.setGender(imEditDataNotify.getGender());
//                user.setSignInfo(imEditDataNotify.getSignature());
//                user.setEmail(imEditDataNotify.getEmail());
//                user.setPhone(imEditDataNotify.getTelephone());
//                user.setTelephone(imEditDataNotify.getPhone());
//                ArrayList<UserEntity> users = new ArrayList<UserEntity>(1);
//                users.add(user);
//                userMap.put(user.getPeerId(), user);
//                DBInterface.instance().batchInsertOrUpdateUser(users);
//                triggerEvent(UserInfoEvent.USER_INFO_DATA_UPDATE);//修改资料通知
//            }
//
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void updataAvatar(String result){
//
//        int loginId = IMLoginManager.instance().getLoginId();
//        IMBuddy.IMAvatarChangedNotify mdrproto = IMBuddy.IMAvatarChangedNotify.newBuilder()
//                .setChangedUserId(loginId)
//                .setAvatarUrl(result)
//                .build();
//        int sid = IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE;
//        int cid = IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_CHANGE_AVATAR_REQUEST_VALUE;
//        imSocketManager.sendRequest(mdrproto, sid, cid);
//    }
//
//    public void onRepUpdataAvatar(IMBuddy.IMAvatarChangedNotify imAvatarChangedNotify){
//        triggerEvent(UserInfoEvent.USER_INFO_AVATER_UPDATE);
//        UserEntity user = DBInterface.instance().getByLoginId(imAvatarChangedNotify.getChangedUserId());
//        ImageLoader.getInstance().getDiskCache().remove(user.getAvatar());
//        user.setAvatar(imAvatarChangedNotify.getAvatarUrl());
//        ArrayList<UserEntity> users = new ArrayList<UserEntity>(1);
//        users.add(user);
//        userMap.put(user.getPeerId(), user);
//        DBInterface.instance().batchInsertOrUpdateUser(users);
//        EventBus.getDefault().post(QueuueHandlerEvent.MESSAGE_FINISH);
//    }
//    /**
//     * 修改个人资料
//     * @param currentUserId
//     * @param signature
//     * @param email
//     * @param userEntity
//     */
//    public void updataPersonalData(int currentUserId,String signature,String email,UserEntity userEntity){
//
//        IMBuddy.IMModifyDataReq mdrproto = IMBuddy.IMModifyDataReq.newBuilder()
//                .setChangedUserId(currentUserId)
//                .setGender(userEntity.getGender())
//                .setCsNickName(userEntity.getMainName())
//                .setEmail(email)
//                .setTelephone(userEntity.getPhone())
//                .setSignature(signature)
//                .build();
//        //int sid = IMBaseDefine.ServiceID.SID_BUDDY_LIST.getNumber();
//        int sid = IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE;
//        int cid = IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_MODIFY_DATA_REQUEST_VALUE;
//        imSocketManager.sendRequest(mdrproto, sid, cid);
//    }
//
//    public UserEntity findContact(int buddyId) {
//        if (buddyId > 0 && userMap.containsKey(buddyId)) {
//            return userMap.get(buddyId);
//        }
//        return null;
//    }
//
//    /**
//     * 请求用户详细信息
//     *
//     * @param userIds
//     */
//    public void reqGetDetaillUsers(ArrayList<Integer> userIds) {
//        Logger.i(TAG,"contact#contact#reqGetDetaillUsers");
//        if (null == userIds || userIds.size() <= 0) {
//            Logger.i(TAG,"contact#contact#reqGetDetaillUsers return,cause by null or empty");
//            return;
//        }
//        int loginId = IMLoginManager.instance().getLoginId();
//        IMBuddy.IMUsersInfoReq imUsersInfoReq = IMBuddy.IMUsersInfoReq.newBuilder()
//                .setUserId(loginId)
//                .addAllUserIdList(userIds)
//                .build();
//
//        int sid = IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE;
//        int cid = IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_USER_INFO_REQUEST_VALUE;
//        imSocketManager.sendRequest(imUsersInfoReq, sid, cid);
//    }
//
//    /**
//     * 获取用户详细的信息
//     *
//     * @param imUsersInfoRsp
//     */
//    public void onRepDetailUsers(IMBuddy.IMUsersInfoRsp imUsersInfoRsp) {
//        int loginId = imUsersInfoRsp.getUserId();
//        boolean needEvent = false;
//        List<IMBaseDefine.UserInfo> userInfoList = imUsersInfoRsp.getUserInfoListList();
//
//        ArrayList<UserEntity> dbNeed = new ArrayList<>();
//        for (IMBaseDefine.UserInfo userInfo : userInfoList) {
//            UserEntity userEntity = ProtoBuf2JavaBean.getUserEntity(userInfo);
//            int userId = userEntity.getPeerId();
//            if (userMap.containsKey(userId) && userMap.get(userId).equals(userEntity)) {
//                //没有必要通知更新
//            } else {
//                needEvent = true;
//                userMap.put(userEntity.getPeerId(), userEntity);
//                dbNeed.add(userEntity);
//                if (userInfo.getUserId() == loginId) {
//                    IMLoginManager.instance().setLoginInfo(userEntity);
//                }
//            }
//        }
//        // 负责userMap
//        dbInterface.batchInsertOrUpdateUser(dbNeed);
//        EventBus.getDefault().post(QueuueHandlerEvent.MESSAGE_FINISH);
//        // 判断有没有必要进行推送
//        if (needEvent) {
//            triggerEvent(UserInfoEvent.USER_INFO_UPDATE);
//        }
//    }
//
//
//    public DepartmentEntity findDepartment(int deptId) {
//        DepartmentEntity entity = departmentMap.get(deptId);
//        return entity;
//    }
//
//    public void updatePWByPC() {
//        //Toast.makeText(IMApplication.context, "修改了密码", Toast.LENGTH_LONG).show();
//        /*if (!isownUpdata) {
//            triggerEvent(UserInfoEvent.USER_INFO_UPDATE_PW_BY_PC);
//        }*/
//        triggerEvent(UserInfoEvent.USER_INFO_UPDATE_PW_BY_PC);
//        EventBus.getDefault().post(QueuueHandlerEvent.MESSAGE_FINISH);
//    }
//
//    public void updatePsw(IMLogin.IMModifyPassRes imModifyPassRes) {
//
//        if (imModifyPassRes.getStatus() == 0) {
//            triggerEvent(UserInfoEvent.USER_INFO_PSW_UPDATE);
//        } else {
//            triggerEvent(UserInfoEvent.USER_INFO_PSWFAIL_UPDATE);//密码修改失败
//        }
//        EventBus.getDefault().post(QueuueHandlerEvent.MESSAGE_FINISH);
//    }
//
//    public void updatePower() {
//
//        triggerEvent(UserInfoEvent.USER_INFO_POWER_UPDATE);
//    }
//
//    public int getDepartmentCountByParentId(int parentDepartmentId) {
//        int deptCount = 0;
//        for (DepartmentEntity dp : departmentMap.values()) {
//            if (dp.getParentDepartId() == parentDepartmentId) {
//                deptCount++;
//            }
//        }
//        return deptCount;
//    }
//
//    //按照父部门节点返回列表清单
//    public List<DepartmentEntity> getDepartmentListByParentId(int parentDepartmentId) {
//        List<DepartmentEntity> conditionList = new ArrayList<>();
//
//        for (DepartmentEntity dp : departmentMap.values()) {
//            if (dp.getParentDepartId() == parentDepartmentId) {
//                conditionList.add(dp);
//            }
//        }
//        // TODO: 2016/3/14 要做排序
//        Collections.sort(conditionList, new Comparator<DepartmentEntity>() {
//            @Override
//            public int compare(DepartmentEntity entity1, DepartmentEntity entity2) {
//
//                if (entity1.getPriority() == entity2.getPriority()) {
//                    if (entity1.getPinyinElement().pinyin == null) {
//                        PinYin.getPinYin(entity1.getDepartName(), entity1.getPinyinElement());
//                    }
//                    if (entity2.getPinyinElement().pinyin == null) {
//                        PinYin.getPinYin(entity2.getDepartName(), entity2.getPinyinElement());
//                    }
//
//                    if (entity1.getPinyinElement().pinyin.equals(entity2.getPinyinElement().pinyin)) {
//                        return 0;
//                    } else {
//                        return entity1.getPinyinElement().pinyin.compareToIgnoreCase(entity2.getPinyinElement().pinyin);
//                    }
//                } else {
//                    return entity1.getPriority() > entity2.getPriority() ? 1 : -1;
//                }
//
//            }
//        });
//
//        return conditionList;
//    }
//
//    /**
//     * @param departmentId 部门ID
//     * @return 部门下的人的数量
//     */
//    public int getContactCountByDepart(int departmentId) {
//        int userCount = 0;
//
//        for (UserEntity ue : userMap.values()) {
//            if (ue.getDepartmentId() == departmentId) {
//                userCount++;
//            }
//        }
//
//        return userCount;
//    }
//
//    /**
//     * @param departmentId 部门ID
//     * @return 返回人员列表
//     */
//    public List<UserEntity> getContactSortedListByDepart(int departmentId) {
//        List<UserEntity> contactList = new ArrayList<>();
//
//        for (UserEntity ue : userMap.values()) {
//            if (ue.getDepartmentId() == departmentId) {
//                contactList.add(ue);
//            }
//        }
//
//        // TODO: 2016/3/14 人员通过拼音排序
//        Collections.sort(contactList, new Comparator<UserEntity>() {
//            @Override
//            public int compare(UserEntity entity1, UserEntity entity2) {
//                if (entity1.getPinyinElement().pinyin == null) {
//                    PinYin.getPinYin(entity1.getMainName(), entity1.getPinyinElement());
//                }
//                if (entity2.getPinyinElement().pinyin == null) {
//                    PinYin.getPinYin(entity2.getMainName(), entity2.getPinyinElement());
//                }
//
//                if (entity2.getPinyinElement().pinyin.equals(entity1.getPinyinElement().pinyin)) {
//                    return 0;
//                } else if (entity2.getPinyinElement().pinyin.startsWith("#")) {
//                    return -1;
//                } else if (entity1.getPinyinElement().pinyin.startsWith("#")) {
//                    return 1;
//                } else {
//                    return entity1.getPinyinElement().pinyin.compareToIgnoreCase(entity2.getPinyinElement().pinyin);
//                }
//            }
//        });
//
//        return contactList;
//    }
//    /**
//     * @return 返回新建组的人员列表
//     */
//    public List<UserEntity> getUserselectlist() {
//        if (userselectlist == null) {
//            userselectlist = new ArrayList<>();
//        }
//        return userselectlist;
//    }
//    /**
//     * @return 设置新建组的人员列表
//     */
//    public void setUserselectlist(List<UserEntity> userselectlist) {
//        this.userselectlist = userselectlist;
//    }
//    /**
//     * @return 设置新建组的人员列表
//     */
//    public void clearUserselectlist() {
//        this.userselectlist = null;
//    }
//    /**
//     * @return 返回排序的人员列表
//     */
//    public List<UserEntity> getContactSortedList() {
//        // todo eric efficiency
//        List<UserEntity> contactList = new ArrayList<>(userMap.values());
//
//        Collections.sort(contactList, new Comparator<UserEntity>() {
//            @Override
//            public int compare(UserEntity entity1, UserEntity entity2) {
//
//                if (entity1.getPinyinElement().pinyin == null) {
//                    PinYin.getPinYin(entity1.getMainName(), entity1.getPinyinElement());
//                }
//                if (entity2.getPinyinElement().pinyin == null) {
//                    PinYin.getPinYin(entity2.getMainName(), entity2.getPinyinElement());
//                }
//
//                if (entity2.getPinyinElement().pinyin.equals(entity1.getPinyinElement().pinyin)) {
//                    return 0;
//                } else if (entity2.getPinyinElement().pinyin.startsWith("#")) {
//                    return -1;
//                } else if (entity1.getPinyinElement().pinyin.startsWith("#")) {
//                    return 1;
//                } else {
//                    return entity1.getPinyinElement().pinyin.compareToIgnoreCase(entity2.getPinyinElement().pinyin);
//                }
//            }
//        });
//        return contactList;
//    }
//
//    /**
//     *  群组成员 返回排序的人员列表
//     * @return
//     */
//    public List<UserEntity> getGroupSortedList(final List<UserEntity> contactList) {
//        // todo eric efficiency
//        Collections.sort(contactList, new Comparator<UserEntity>() {
//            @Override
//            public int compare(UserEntity entity1, UserEntity entity2) {
//
//                if (entity1.getPinyinElement().pinyin == null) {
//                    PinYin.getPinYin(entity1.getMainName(), entity1.getPinyinElement());
//                }
//                if (entity2.getPinyinElement().pinyin == null) {
//                    PinYin.getPinYin(entity2.getMainName(), entity2.getPinyinElement());
//                }
//
//                if (entity2.getPinyinElement().pinyin.equals(entity1.getPinyinElement().pinyin)) {
//                    return 0;
//                } else if (entity2.getPinyinElement().pinyin.startsWith("#")) {
//                    return -1;
//                } else if (entity1.getPinyinElement().pinyin.startsWith("#")) {
//                    return 1;
//                }
//                else {
//                    return  entity1.getPinyinElement().pinyin.compareToIgnoreCase(entity2.getPinyinElement().pinyin);
//                }
//
//            }
//        });
//        return contactList;
//    }
//
//    // 确实要将对比的抽离出来 Collections
//    public List<UserEntity> getSearchContactList(String key) {
//        List<UserEntity> searchList = new ArrayList<>();
//        for (Map.Entry<Integer, UserEntity> entry : userMap.entrySet()) {
//            UserEntity user = entry.getValue();
//            if (IMUIHelper.handleContactSearch(key, user)) {
//                searchList.add(user);
//            }
//        }
//        return searchList;
//    }
//
//    public List<DepartmentEntity> getSearchDepartList(String key) {
//        List<DepartmentEntity> searchList = new ArrayList<>();
//        for (Map.Entry<Integer, DepartmentEntity> entry : departmentMap.entrySet()) {
//            DepartmentEntity dept = entry.getValue();
//            if (IMUIHelper.handleDepartmentSearch(key, dept)) {
//                searchList.add(dept);
//            }
//        }
//        // TODO: 2016/3/14 要做排序
//        Collections.sort(searchList, new Comparator<DepartmentEntity>() {
//            @Override
//            public int compare(DepartmentEntity entity1, DepartmentEntity entity2) {
//
//                if (entity1.getPriority() == entity2.getPriority()) {
//                    if (entity1.getPinyinElement().pinyin == null) {
//                        PinYin.getPinYin(entity1.getDepartName(), entity1.getPinyinElement());
//                    }
//                    if (entity2.getPinyinElement().pinyin == null) {
//                        PinYin.getPinYin(entity2.getDepartName(), entity2.getPinyinElement());
//                    }
//
//                    if (entity1.getPinyinElement().pinyin.equals(entity2.getPinyinElement().pinyin)) {
//                        return 0;
//                    } else {
//                        return entity1.getPinyinElement().pinyin.compareToIgnoreCase(entity2.getPinyinElement().pinyin);
//                    }
//                } else {
//                    return entity1.getPriority() > entity2.getPriority() ? 1 : -1;
//                }
//
//            }
//        });
//        return searchList;
//    }
//
//    /**
//     * ------------------------部门相关的协议 start------------------------------
//     */
//
//    // 更新的方式与userInfo一直，根据时间点
//    public void reqGetDepartment(int lastUpdateTime) {
//        Logger.i(TAG,"contact#reqGetDepartment");
//        int userId = IMLoginManager.instance().getLoginId();
//
//        IMBuddy.IMDepartmentReq imDepartmentReq = IMBuddy.IMDepartmentReq.newBuilder()
//                .setUserId(userId)
//                .setLatestUpdateTime(lastUpdateTime).build();
//        int sid = IMBaseDefine.ServiceID.SID_BUDDY_LIST_VALUE;
//        int cid = IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_DEPARTMENT_REQUEST_VALUE;
//        imSocketManager.sendRequest(imDepartmentReq, sid, cid);
//    }
//
//
//    public void onRepDepartment(IMBuddy.IMDepartmentRsp imDepartmentRsp) {
//        Logger.i(TAG,"contact#onRepDepartment");
//        int userId = imDepartmentRsp.getUserId();
//
//        int lastTime = imDepartmentRsp.getLatestUpdateTime();
//        int count = imDepartmentRsp.getDeptListCount();
//        Logger.i(TAG,"contact#department cnt:%d", count);
//        // 如果用户找不到depart 那么部门显示未知
//        if (count <= 0) {
//            return;
//        }
//
//        int loginId = IMLoginManager.instance().getLoginId();
//        sp = ctx.getSharedPreferences("userTIme", Context.MODE_APPEND);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putInt("DepartmenLatest" + loginId + "UpdateTime", imDepartmentRsp.getLatestUpdateTime());
//        editor.commit();
//        if (userId != loginId) {
//            Logger.e(TAG,"[fatal error] userId not equels loginId ,cause by onRepDepartment");
//            return;
//        }
//        List<IMBaseDefine.DepartInfo> changeList = imDepartmentRsp.getDeptListList();
//        ArrayList<DepartmentEntity> needDb = new ArrayList<>();
//        if (deparmentFlag) {
//            departmentMap.clear();
//            deparmentFlag = false;
//        }
//        for (IMBaseDefine.DepartInfo departInfo : changeList) {
//            DepartmentEntity entity = ProtoBuf2JavaBean.getDepartEntity(departInfo);
//            if (entity.getStatus() != 1) {
//
//                departmentMap.put(entity.getDepartId(), entity);
//            }
//            if (deparmentChange == 1 && entity.getStatus() == 1) {
//
//                departmentMap.remove(entity.getDepartId());
//            }
//            needDb.add(entity);
//        }
//        // 部门信息更新
//        dbInterface.batchInsertOrUpdateDepart(needDb);
//        triggerEvent(UserInfoEvent.USER_INFO_UPDATE);
//        EventBus.getDefault().post(QueuueHandlerEvent.MESSAGE_FINISH);
//
//    }
//
//    /**
//     * 部门变更通知
//     *
//     * @param imDepartmentRsp
//     */
//    public void onRepDepartChangeNotify(IMBuddy.IMDepartmentRsp imDepartmentRsp) {
//
//        /*sp = ctx.getSharedPreferences("userTIme", Context.MODE_APPEND);
//        int loginId = IMLoginManager.instance().getLoginId();
//        int deptUpdateTime = sp.getInt("DepartmenLatest" + loginId + "UpdateTime", 0);
//        reqGetDepartment(deptUpdateTime);*/
//
//        TimeSaveSp.instance().init(ctx);
//        final int loginId = IMLoginManager.instance().getLoginId();
//        int deptTime = TimeSaveSp.instance().getRoleTimeValue("depttime" + loginId) ;
//        //请求部门数据
//        if (deptTime == 0) {
//
//            deparmentChange = 0;
//        } else {
//
//            deparmentChange = 1;
//        }
//
//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                HttpPostReq.reqDepartmentData(ctx);
//            }
//        }, 0);
//
//        EventBus.getDefault().post(QueuueHandlerEvent.MESSAGE_FINISH);
//    }
//
//    /**
//     * 人员变更
//     *
//     * @param imAllUserRspChangeNotify
//     */
//    public void reqRepAllUsersChangeNotify(IMBuddy.IMUsersInfoReq imAllUserRspChangeNotify) {
//
//        /*sp = ctx.getSharedPreferences("userTIme", Context.MODE_APPEND);
//        int loginId = IMLoginManager.instance().getLoginId();
//        int updateTime = sp.getInt("LatestUpdate" + loginId + "Time", 0);
//        int lastUpdateTimeObject = sp.getInt("LatestUpdate" + loginId + "TimeObject", 0);
//        int latestUpdateTimeRole = sp.getInt("LatestUpdate" + loginId + "TimeRole", 0);
//        reqGetAllUsers(updateTime, lastUpdateTimeObject, latestUpdateTimeRole);*/
//
//        //http处理方式
//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                HttpPostReq.reqAllDataList(ctx);
//            }
//        },0);
//
//        userChange = 1;
//        EventBus.getDefault().post(QueuueHandlerEvent.MESSAGE_FINISH);
//    }
//
//    public void userPCOnlineStatues() {
//
//        int userId = IMLoginManager.instance().getLoginId();
//        List<Integer> userInfoList = new ArrayList<>();
//        userInfoList.add(userId);
//
//        IMLogin.IMGetOtherUserStatusReq groupInfoListReq = IMLogin.IMGetOtherUserStatusReq.newBuilder()
//                .setUserId(userId)
//                .addAllUserList(userInfoList)
//                .build();
//        int sid = IMBaseDefine.ServiceID.SID_LOGIN_VALUE;
//        int cid = IMBaseDefine.OtherCmdID.CID_OTHER_GET_USER_STATUS_REQ_VALUE;
//        imSocketManager.sendRequest(groupInfoListReq, sid, cid);
//
//    }
//
//    /**
//     * 获取其他 用户在线状态 IMLogin.IMGetOtherUserStatusReq
//     */
//    public void userOnlineStatues(List<IMBaseDefine.UserInfo> UserChangeInfo) {
//
//
//        List<Integer> userInfoList = new ArrayList<>();
//
//        for (IMBaseDefine.UserInfo userInfo : UserChangeInfo) {
//            if (userInfo.getStatus() == 0) {
//                userInfoList.add(userInfo.getUserId());
//            }
//        }
//        int userId = IMLoginManager.instance().getLoginId();
//        IMLogin.IMGetOtherUserStatusReq groupInfoListReq = IMLogin.IMGetOtherUserStatusReq.newBuilder()
//                .setUserId(userId)
//                .addAllUserList(userInfoList)
//                .build();
//        int sid = IMBaseDefine.ServiceID.SID_LOGIN_VALUE;
//        int cid = IMBaseDefine.OtherCmdID.CID_OTHER_GET_USER_STATUS_REQ_VALUE;
//        imSocketManager.sendRequest(groupInfoListReq, sid, cid);
//
//    }
//    /**------------------------部门相关的协议 end------------------------------*/
//
//    /**
//     * -----------------------实体 get set 定义-----------------------------------
//     */
//
//    public Map<Integer, UserEntity> getUserMap() {
//        return userMap;
//    }
//
//    public Map<Integer, DepartmentEntity> getDepartmentMap() {
//        return departmentMap;
//    }
//
//    public Map<Integer, UserStatusEntity> getStatusEntityMap() {
//        return statusEntityMap;
//    }
//
//    public boolean isUserDataReady() {
//        return userDataReady;
//    }
//
//
//}
