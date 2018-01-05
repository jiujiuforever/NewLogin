package im.jizhu.com.loginmodule.imservice.manager;

import android.util.Log;

import com.google.protobuf.CodedInputStream;
import im.jizhu.com.loginmodule.protobuf.IMBaseDefine;
import im.jizhu.com.loginmodule.protobuf.IMBuddy;
import im.jizhu.com.loginmodule.protobuf.IMGroup;
import im.jizhu.com.loginmodule.protobuf.IMLogin;
import im.jizhu.com.loginmodule.protobuf.IMMessage;
import im.jizhu.com.loginmodule.utils.Logger;

import java.io.IOException;

/**
 * yingmu
 * 消息分发中心，处理消息服务器返回的数据包
 * 1. decode  header与body的解析
 * 2. 分发
 */
public class IMPacketDispatcher {

    private static final String TAG = "IMPacketDispatcher";

    /**
     * @param commandId
     * @param buffer    有没有更加优雅的方式
     */
    public static void loginPacketDispatcher(int commandId, CodedInputStream buffer) {
        try {
            switch (commandId) {
                case IMBaseDefine.LoginCmdID.CID_LOGIN_RES_USERLOGIN_VALUE:
                    IMLogin.IMLoginRes imLoginRes = IMLogin.IMLoginRes.parseFrom(buffer);
                    if(imLoginRes == null){
                        Logger.e(TAG,"CID_LOGIN_RES_USERLOGIN_VALUE#response is null");
                        return;
                    }
                    IMLoginManager.instance().onRepMsgServerLogin(imLoginRes);
                    return;
                case IMBaseDefine.LoginCmdID.CID_LOGIN_RES_LOGINOUT_VALUE:
                    //退出登录
                   IMLogin.IMLogoutRsp imLogoutRsp = IMLogin.IMLogoutRsp.parseFrom(buffer);
                    if(imLogoutRsp == null){
                        Logger.e(TAG,"CID_LOGIN_RES_LOGINOUT_VALUE#response is null");
                        return;
                    }
                    IMLoginManager.instance().onRepLoginOut(imLogoutRsp);
                    return;

                //有不同手机设备登录时
                case IMBaseDefine.LoginCmdID.CID_LOGIN_KICK_USER_VALUE:
                    IMLogin.IMKickUser imKickUser = IMLogin.IMKickUser.parseFrom(buffer);
                    if(imKickUser == null){
                        Logger.e(TAG,"CID_LOGIN_KICK_USER_VALUE#response is null");
                        return;
                    }
                    IMLoginManager.instance().onKickout(imKickUser);

                    return;

//                //修改密码返回广播（手机端修改）
//                case IMBaseDefine.LoginCmdID.CID_LOGIN_RES_MODIFY_PASS_VALUE:
//
//                    IMLogin.IMModifyPassRes imModifyPassRes = IMLogin.IMModifyPassRes.parseFrom(buffer);
//                    if(imModifyPassRes == null){
//                        Logger.e(TAG,"CID_LOGIN_RES_MODIFY_PASS_VALUE#response is null");
//                        return;
//                    }
//                    IMContactManager.instance().updatePsw(imModifyPassRes);

//                    return;

            }
        } catch (IOException e) {
            Logger.e(TAG,"loginPacketDispatcher# error,cid:%d", commandId);
        }
    }

    public static void otherPacketDispatcher(int commandId, CodedInputStream buffer) {
//        try {
//            switch (commandId) {
//               /* case IMBaseDefine.OtherCmdID.CID_OTHER_GET_DISCOVER_RES_VALUE:
//                    IMBuddy.DiscoverRsp imDiscoverRsp = IMBuddy.DiscoverRsp.parseFrom(buffer);
//                    IMDiscoverManager.instance().onRepAllDiscover(imDiscoverRsp);
//              /*      return;*//*
//                case IMBaseDefine.OtherCmdID.CID_OTHER_GET_EMAIL_TOKEN_RES_VALUE:
//                    IMLogin.IMGetEmailTokenRes imGetEmailTokenRes = IMLogin.IMGetEmailTokenRes.parseFrom(buffer);
//                    IMDiscoverManager.instance().onRepGetEmailToken(imGetEmailTokenRes);
//                    return;
//                case IMBaseDefine.OtherCmdID.CID_OTHER_SET_EMAIL_TOKEN_RES_VALUE:
//                    IMLogin.IMSetEmailTokenRes imSetEmailTokenRes = IMLogin.IMSetEmailTokenRes.parseFrom(buffer);
//                    IMDiscoverManager.instance().onRepSetEmailToken(imSetEmailTokenRes);
//                    return;
//                case IMBaseDefine.OtherCmdID.CID_OTHER_SET_APP_TOKEN_RES_VALUE:
//                    //响应APP TOKEN请求
//                    IMLogin.IMSetAppTokenRes imSetAppTokenRes = IMLogin.IMSetAppTokenRes.parseFrom(buffer);
//                    IMDiscoverManager.instance().onRepSetAppToken(imSetAppTokenRes);
//                    return;
//*/
//                //获取其他 用户在线状态
//                case IMBaseDefine.OtherCmdID.CID_OTHER_GET_USER_STATUS_RES_VALUE:
//                    IMLogin.IMGetOtherUserStatusRes imGetOtherUserStatusRes = IMLogin.IMGetOtherUserStatusRes.parseFrom(buffer);
//                    if(imGetOtherUserStatusRes == null){
//                        Logger.e(TAG,"CID_OTHER_GET_USER_STATUS_RES_VALUE#response is null");
//                        return;
//                    }
//                    IMContactManager.instance().onRepOnlineStatues(imGetOtherUserStatusRes);
//                    //Log.e("test", "状态信息：" + imGetOtherUserStatusRes);
//                    return;
//
//            }
//        } catch (IOException e) {
//            Logger.e(TAG,"otherPacketDispatcher# error,cid:%d", commandId);
//        }
    }

    public static void buddyPacketDispatcher(int commandId, CodedInputStream buffer) {
//        try {
//
//            switch (commandId) {
//                case IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_ALL_USER_RESPONSE_VALUE:
//                    IMBuddy.IMAllUserRsp imAllUserRsp = IMBuddy.IMAllUserRsp.parseFrom(buffer);
//                    if(imAllUserRsp == null){
//                        Logger.e(TAG,"CID_BUDDY_LIST_ALL_USER_RESPONSE_VALUE#response is null");
//                        return;
//                    }
//                    IMContactManager.instance().onRepAllUsers(imAllUserRsp);
//                    return;
//                case IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_USER_INFO_RESPONSE_VALUE:
//                    IMBuddy.IMUsersInfoRsp imUsersInfoRsp = IMBuddy.IMUsersInfoRsp.parseFrom(buffer);
//                    if(imUsersInfoRsp == null){
//                        Logger.e(TAG,"CID_BUDDY_LIST_USER_INFO_RESPONSE_VALUE#response is null");
//                        return;
//                    }
//                    IMContactManager.instance().onRepDetailUsers(imUsersInfoRsp);
//                    return;
//                //获取最近会话返回通知
//                case IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_RECENT_CONTACT_SESSION_RESPONSE_VALUE:
//                    IMBuddy.IMRecentContactSessionRsp recentContactSessionRsp = IMBuddy.IMRecentContactSessionRsp.parseFrom(buffer);
//                    if(recentContactSessionRsp == null){
//                        Logger.e(TAG,"CID_BUDDY_LIST_RECENT_CONTACT_SESSION_RESPONSE_VALUE#response is null");
//                        return;
//                    }
//                    IMSessionManager.instance().onRepRecentContacts(recentContactSessionRsp);
//                    return;
//                case IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_MODIFY_PASS_NOTIFY_VALUE:
//                    Log.e("test", "PC端修改");
//                    //PC端修改了密码
//                    IMContactManager.instance().updatePWByPC();
//                    return;
//                case IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_REMOVE_SESSION_RES_VALUE:
//                    IMBuddy.IMRemoveSessionRsp removeSessionRsp = IMBuddy.IMRemoveSessionRsp.parseFrom(buffer);
//                    if(removeSessionRsp == null){
//                        Logger.e(TAG,"CID_BUDDY_LIST_REMOVE_SESSION_RES_VALUE#response is null");
//                        return;
//                    }
//                    IMSessionManager.instance().onRepRemoveSession(removeSessionRsp);
//                    return;
//                //PC在线通知
//                case IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_PC_LOGIN_STATUS_NOTIFY_VALUE:
//                    IMBuddy.IMPCLoginStatusNotify statusNotify = IMBuddy.IMPCLoginStatusNotify.parseFrom(buffer);
//                    if(statusNotify == null){
//                        Logger.e(TAG,"CID_BUDDY_LIST_PC_LOGIN_STATUS_NOTIFY_VALUE#response is null");
//                        return;
//                    }
//                    IMLoginManager.instance().onLoginStatusNotify(statusNotify);
//                    return;
//
//                //other修改头像广播
//                case IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_AVATAR_CHANGED_NOTIFY_VALUE:
//                    //IMBuddy.IMUsersInfoRsp imUsersInfoRsp1 = IMBuddy.IMUsersInfoRsp.parseFrom(buffer);
//                    IMBuddy.IMAvatarChangedNotify imAvatarChangedNotify = IMBuddy.IMAvatarChangedNotify.parseFrom(buffer);
//                    if(imAvatarChangedNotify == null){
//                        Logger.e(TAG,"CID_BUDDY_LIST_AVATAR_CHANGED_NOTIFY_VALUE#response is null");
//                        return;
//                    }
//                    IMContactManager.instance().updatAvater(imAvatarChangedNotify);
//                    break;
//                //自己修改头像响应
//                case IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_CHANGE_AVATAR_RESPONSE_VALUE:
//                    Log.e("test","头像修改响应");
//                    IMBuddy.IMAvatarChangedNotify imAvatarChangedRep = IMBuddy.IMAvatarChangedNotify.parseFrom(buffer);
//                    if(imAvatarChangedRep == null){
//                        Logger.e(TAG,"CID_BUDDY_LIST_CHANGE_AVATAR_RESPONSE_VALUE#response is null");
//                        return;
//                    }
//                    IMContactManager.instance().onRepUpdataAvatar(imAvatarChangedRep);
//                    break;
//
//                //修改资料
//                case IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_EDIT_DATA_NOTIFY_VALUE:
//                    IMBuddy.IMEditDataNotify imEditDataNotify = IMBuddy.IMEditDataNotify.parseFrom(buffer);
//                    if(imEditDataNotify == null){
//                        Logger.e(TAG,"CID_BUDDY_LIST_EDIT_DATA_NOTIFY_VALUE#response is null");
//                        return;
//                    }
//                    IMContactManager.instance().editData(imEditDataNotify);
//                    break;
//
//                case IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_DEPARTMENT_RESPONSE_VALUE:
//                    IMBuddy.IMDepartmentRsp departmentRsp = IMBuddy.IMDepartmentRsp.parseFrom(buffer);
//                    if(departmentRsp == null){
//                        Logger.e(TAG,"CID_BUDDY_LIST_DEPARTMENT_RESPONSE_VALUE#response is null");
//                        return;
//                    }
//                    IMContactManager.instance().onRepDepartment(departmentRsp);
//
//                    return;
//
//                case IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_CHANGE_DEPARTMENT_NOTIFY_VALUE:
//                    IMBuddy.IMDepartmentRsp departmentChangeNotify = IMBuddy.IMDepartmentRsp.parseFrom(buffer);
//                    if(departmentChangeNotify == null){
//                        Logger.e(TAG,"CID_BUDDY_LIST_CHANGE_DEPARTMENT_NOTIFY_VALUE#response is null");
//                        return;
//                    }
//                    IMContactManager.instance().onRepDepartChangeNotify(departmentChangeNotify);
//                    return;
//
//                case IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_CHANGE_PERSONNEL_NOTIFY_VALUE:
//
//                    IMBuddy.IMUsersInfoReq imAllUserRspChangeNotify = IMBuddy.IMUsersInfoReq.parseFrom(buffer);
//                    if(imAllUserRspChangeNotify == null){
//                        Logger.e(TAG,"CID_BUDDY_LIST_CHANGE_PERSONNEL_NOTIFY_VALUE#response is null");
//                        return;
//                    }
//                    IMContactManager.instance().reqRepAllUsersChangeNotify(imAllUserRspChangeNotify);
//                    return;
//                //获取用户在线状态变更通知
//                case IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_STATUS_NOTIFY_VALUE:
//
//                    IMBuddy.IMUserStatNotify userStatusUpdateNotify = IMBuddy.IMUserStatNotify.parseFrom(buffer);
//                    if(userStatusUpdateNotify == null){
//                        Logger.e(TAG,"CID_BUDDY_LIST_STATUS_NOTIFY_VALUE#response is null");
//                        return;
//                    }
//                    IMContactManager.instance().onRepUserStatusUpdateNotify(userStatusUpdateNotify);
//                    return;
//                case IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_ADD_COLLECTION_RESPONSE_VALUE:
//                    //自己添加收藏的通知
//                    IMBuddy.addcollectionRsp rsp=IMBuddy.addcollectionRsp.parseFrom(buffer);
//                    if(rsp == null){
//                        Logger.e(TAG,"CID_BUDDY_LIST_ADD_COLLECTION_RESPONSE_VALUE#response is null");
//                        return;
//                    }
//                    IMMessageManager.instance().onRepCollectionAddNotify(rsp);
//                    return;
//                case IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_ADD_COLLECTION_NOTIFY_VALUE:
//                    //添加收藏的通知(其他端收藏)
//                    IMBuddy.addcollectionnotify rsp1=IMBuddy.addcollectionnotify.parseFrom(buffer);
//                    if(rsp1 == null){
//                        Logger.e(TAG,"CID_BUDDY_LIST_ADD_COLLECTION_NOTIFY_VALUE#response is null");
//                        return;
//                    }
//                    IMMessageManager.instance().onRepCollectionAddNotifyOther(rsp1);
//                    return;
//                case IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_GET_COLLECTION_RESPONSE_VALUE:
//                    //请求收藏列表
//                    IMBuddy.getcollectionRsp rsp2=IMBuddy.getcollectionRsp.parseFrom(buffer);
//                    if(rsp2 == null){
//                        Logger.e(TAG,"CID_BUDDY_LIST_GET_COLLECTION_RESPONSE_VALUE#response is null");
//                        return;
//                    }
//                    IMMessageManager.instance().onRepGetCollectionNotify(rsp2);
//                    return;
//                case IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_DEL_COLLECTION_RESPONSE_VALUE:
//                    //自己在android端删除了一条收藏
//                    IMBuddy.delcollectionRsp delcollectionRsp=IMBuddy.delcollectionRsp.parseFrom(buffer);
//                    if(delcollectionRsp == null){
//                        Logger.e(TAG,"CID_BUDDY_LIST_DEL_COLLECTION_RESPONSE_VALUE#response is null");
//                        return;
//                    }
//                    IMMessageManager.instance().onRepdelCollection(delcollectionRsp);
//                    return;
//                case IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_DEL_COLLECTION_NOTIFY_VALUE:
//                    //其他端删除了收藏的通知
//                    IMBuddy.delcollectionnotify delcollectionRsp1 = IMBuddy.delcollectionnotify.parseFrom(buffer);
//                    if(delcollectionRsp1 == null){
//                        Logger.e(TAG,"CID_BUDDY_LIST_DEL_COLLECTION_NOTIFY_VALUE#response is null");
//                        return;
//                    }
//                    IMMessageManager.instance().ondelCollectionNotify(delcollectionRsp1);
//                    return;
//                case IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_CREATE_SCHEDULETASK_RESPONSE_VALUE:
//                    //创建日程任务的响应
//                    IMBuddy.createscheduletaskRsp createscheduletaskRsp = IMBuddy.createscheduletaskRsp.parseFrom(buffer);
//                    if(createscheduletaskRsp == null){
//                        Logger.e(TAG,"CID_BUDDY_LIST_CREATE_SCHEDULETASK_RESPONSE_VALUE#response is null");
//                        return;
//                    }
//                    IMOtherManager.instance().rspScheduleCreate(createscheduletaskRsp);
//                    break;
//                case IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_GET_SCHEDULETASK_RESPONSE_VALUE:
//                    //请求日程列表
//                    IMBuddy.getscheduletaskRsp getscheduletaskRsp = IMBuddy.getscheduletaskRsp.parseFrom(buffer);
//                    if(getscheduletaskRsp == null){
//                        Logger.e(TAG,"CID_BUDDY_LIST_GET_SCHEDULETASK_RESPONSE_VALUE#response is null");
//                        return;
//                    }
//                    IMOtherManager.instance().rspScheduleList(getscheduletaskRsp);
//                    break;
//                case IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_DEL_SCHEDULETASK_RESPONSE_VALUE:
//                    //删除日程列表
//                    IMBuddy.delscheduletaskRsp delscheduletaskRsp = IMBuddy.delscheduletaskRsp.parseFrom(buffer);
//                    if(delscheduletaskRsp == null){
//                        Logger.e(TAG,"CID_BUDDY_LIST_DEL_SCHEDULETASK_RESPONSE_VALUE#response is null");
//                        return;
//                    }
//                    IMOtherManager.instance().rspScheduleDel(delscheduletaskRsp);
//                    break;
//                case IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_MODIFY_SCHEDULETASK_RESPONSE_VALUE:
//                    //日程状态修改
//                    IMBuddy.modifyscheduletaskRsp modifyscheduletaskRsp = IMBuddy.modifyscheduletaskRsp.parseFrom(buffer);
//                    if(modifyscheduletaskRsp == null){
//                        Logger.e(TAG,"CID_BUDDY_LIST_MODIFY_SCHEDULETASK_RESPONSE_VALUE#response is null");
//                        return;
//                    }
//                    IMOtherManager.instance().rspScheduleModify(modifyscheduletaskRsp);
//                    break;
//                case IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_CREATE_SCHEDULETASK_NOTIFY_VALUE:
//                    //PC端添加了日程
//                    IMBuddy.createscheduletasknotify createscheduletasknotify = IMBuddy.createscheduletasknotify.parseFrom(buffer);
//                    if(createscheduletasknotify == null){
//                        Logger.e(TAG,"CID_BUDDY_LIST_CREATE_SCHEDULETASK_NOTIFY_VALUE#response is null");
//                        return;
//                    }
//                    IMOtherManager.instance().rspPCScheduleCreate(createscheduletasknotify);
//                    break;
//                case IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_DEL_SCHEDULETASK_NOTIFY_VALUE:
//                    //PC端删除了日程
//                    IMBuddy.delscheduletasknotify delscheduletasknotify = IMBuddy.delscheduletasknotify.parseFrom(buffer);
//                    if(delscheduletasknotify == null){
//                        Logger.e(TAG,"CID_BUDDY_LIST_DEL_SCHEDULETASK_NOTIFY_VALUE#response is null");
//                        return;
//                    }
//                    IMOtherManager.instance().rspPCScheduleDel(delscheduletasknotify);
//                    break;
//                case IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_MODIFY_SCHEDULETASK_NOTIFY_VALUE:
//                    //PC端修改了日程状态
//                    IMBuddy.modifyscheduletasknotify modifyscheduletasknotify = IMBuddy.modifyscheduletasknotify.parseFrom(buffer);
//                    if(modifyscheduletasknotify == null){
//                        Logger.e(TAG,"CID_BUDDY_LIST_MODIFY_SCHEDULETASK_NOTIFY_VALUE#response is null");
//                        return;
//                    }
//                    IMOtherManager.instance().rspPCScheduleModifyType(modifyscheduletasknotify);
//                    break;
//
//                case IMBaseDefine.BuddyListCmdID.CID_BUDDY_LIST_GET_FILE_FORMAT_RESPONSE_VALUE:
//                    //文件格式获取
//                    IMBuddy.fileformatRsp fileformatRsp = IMBuddy.fileformatRsp.parseFrom(buffer);
//                    if(fileformatRsp == null){
//                        Logger.e(TAG,"CID_BUDDY_LIST_GET_FILE_FORMAT_RESPONSE_VALUE#response is null");
//                        return;
//                    }
//                    IMMessageManager.instance().onRepGetFileformatRsp(fileformatRsp);
//                    break;
//
//            }
//        } catch (IOException e) {
//            Logger.e(TAG,"buddyPacketDispatcher# error,cid:%d", commandId);
//        }
    }
    public static void msgPacketDispatcher(int commandId, CodedInputStream buffer) {
//        try {
//            switch (commandId) {
//                case IMBaseDefine.MessageCmdID.CID_MSG_DATA_ACK_VALUE:
//
//                    // have some problem  todo
//                    break;
//                case IMBaseDefine.MessageCmdID.CID_MSG_WITHDRAW_NOTIFY_VALUE://别人撤销消息通知
//                    IMMessage.IMWithdrawalMsgnotify imWithdrawalMsgnotify = IMMessage.IMWithdrawalMsgnotify.parseFrom(buffer);
//                    if(imWithdrawalMsgnotify == null){
//                        Logger.e(TAG,"CID_MSG_WITHDRAW_NOTIFY_VALUE#response is null");
//                        return;
//                    }
//                    IMMessageManager.instance().onReqRetracementMsgOther(imWithdrawalMsgnotify);
//                    break;
//                case IMBaseDefine.MessageCmdID.CID_MSG_WITHDRAW_RES_VALUE://自己撤销消息通知
//                    IMMessage.IMWithdrawalMsgRsp withdrawalMsgRsp = IMMessage.IMWithdrawalMsgRsp.parseFrom(buffer);
//                    if(withdrawalMsgRsp == null){
//                        Logger.e(TAG,"CID_MSG_WITHDRAW_RES_VALUE#response is null");
//                        return;
//                    }
//                    IMMessageManager.instance().onReqRetracementMsg(withdrawalMsgRsp);
//
//                    break;
//                case IMBaseDefine.MessageCmdID.CID_MSG_LIST_RESPONSE_VALUE: //拉取旧历史消息
//                    IMMessage.IMGetMsgListRsp rsp = IMMessage.IMGetMsgListRsp.parseFrom(buffer);
//                    if(rsp == null){
//                        Logger.e(TAG,"CID_MSG_LIST_RESPONSE_VALUE#response is null");
//                        return;
//                    }
//                    IMMessageManager.instance().onReqHistoryMsg(rsp);
//                    break;
//
//                //更新后启用
////                case IMBaseDefine.MessageCmdID.CID_MSG_GET_CUR_MSG_NUM_RESPONSE_VALUE: //拉取历史消息 最新
////
////                    IMMessage.IMGetCurPageMsgNumRsp rspmp = IMMessage.IMGetCurPageMsgNumRsp.parseFrom(buffer);
////                    IMMessageManager.instance().onReqHistoryMsg(rspmp);
////                    break;
//
//                case IMBaseDefine.MessageCmdID.CID_MSG_DATA_VALUE:
//                    IMMessage.IMMsgData imMsgData = IMMessage.IMMsgData.parseFrom(buffer);
//                    if(imMsgData == null){
//                        Logger.e(TAG,"CID_MSG_DATA_VALUE#response is null");
//                        return;
//                    }
//                    IMMessageManager.instance().onRecvMessage(imMsgData);
//                    break;
//
//                case IMBaseDefine.MessageCmdID.CID_MSG_READ_NOTIFY_VALUE:
//                    IMMessage.IMMsgDataReadNotify readNotify = IMMessage.IMMsgDataReadNotify.parseFrom(buffer);
//                    if(readNotify == null){
//                        Logger.e(TAG,"CID_MSG_READ_NOTIFY_VALUE#response is null");
//                        return;
//                    }
//                    IMUnreadMsgManager.instance().onNotifyRead(readNotify);
//                    break;
//                case IMBaseDefine.MessageCmdID.CID_MSG_UNREAD_CNT_RESPONSE_VALUE:
//
//                    IMMessage.IMUnreadMsgCntRsp unreadMsgCntRsp = IMMessage.IMUnreadMsgCntRsp.parseFrom(buffer);
//                    if(unreadMsgCntRsp == null){
//                        Logger.e(TAG,"CID_MSG_UNREAD_CNT_RESPONSE_VALUE#response is null");
//                        return;
//                    }
//                    IMUnreadMsgManager.instance().onRepUnreadMsgContactList(unreadMsgCntRsp);
//                    break;
//
//                case IMBaseDefine.MessageCmdID.CID_MSG_GET_BY_MSG_ID_RES_VALUE:
//
//                    IMMessage.IMGetMsgByIdRsp getMsgByIdRsp = IMMessage.IMGetMsgByIdRsp.parseFrom(buffer);
//                    if(getMsgByIdRsp == null){
//                        Logger.e(TAG,"CID_MSG_GET_BY_MSG_ID_RES_VALUE#response is null");
//                        return;
//                    }
//                    IMMessageManager.instance().onReqMsgById(getMsgByIdRsp);
//                    break;
                //未读消息，已读服务器返回 倒数第三已经有

//            }
//        } catch (IOException e) {
//            Logger.e(TAG,"msgPacketDispatcher# error,cid:%d", commandId);
//        }
    }

    public static void groupPacketDispatcher(int commandId, CodedInputStream buffer) {
//        try {
//            switch (commandId) {
//                case IMBaseDefine.GroupCmdID.CID_GROUP_DISMISS_NOTIFY_VALUE:
//                    //这个删除表再建表是错误的做法
//                    //有一个群组解散了，我们先删除本地数据库中的group表，然后在获取所有的群组写入本地数据库，否则应用退出再进来时本地数据库中海油解散的群，会在页面显示的
//                    /*IMGroupManager.instance().getGroupMap().clear();
//                    String dbName = "tt_" + LoginSp.instance().getLoginIdentity().getLoginId() + ".db";
//                    DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(IMGroupManager.instance().ctx, dbName, null);
//                    GroupDao.dropTable(helper.getWritableDatabase(), true);
//                    GroupDao.createTable(helper.getWritableDatabase(), false);*/
//                    //IMGroupManager.instance().reqGetNormalGroupList();
//                    IMGroup.IMGroupDismissNotify groupDeleteInfoListRsp = IMGroup.IMGroupDismissNotify.parseFrom(buffer);
//                    if(groupDeleteInfoListRsp == null){
//                        Logger.e(TAG,"CID_GROUP_DISMISS_NOTIFY_VALUE#response is null");
//                        return;
//                    }
//                    IMGroupManager.instance().onRepGroupDeleteInfo(groupDeleteInfoListRsp);
//                    return;
//                //自己删除群组
//                case IMBaseDefine.GroupCmdID.CID_GROUP_DISMISS_CLIENT_RESPONSE_VALUE:
//                    IMGroup.IMGroupDismissNotify groupDeleteClientListRsp = IMGroup.IMGroupDismissNotify.parseFrom(buffer);
//                    if(groupDeleteClientListRsp == null){
//                        Logger.e(TAG,"CID_GROUP_DISMISS_CLIENT_RESPONSE_VALUE#response is null");
//                        return;
//                    }
//                    IMGroupManager.instance().onRepGroupDeleteInfo(groupDeleteClientListRsp);
//                    return;
//
//
//                case IMBaseDefine.GroupCmdID.CID_GROUP_CREATE_NOTIFY_VALUE:
//                    IMGroupManager.instance().reqGetNormalGroupList();
//                    return;
//
//                case IMBaseDefine.GroupCmdID.CID_GROUP_NORMAL_LIST_RESPONSE_VALUE:
//                    IMGroup.IMNormalGroupListRsp normalGroupListRsp = IMGroup.IMNormalGroupListRsp.parseFrom(buffer);
//                    if(normalGroupListRsp == null){
//                        Logger.e(TAG,"CID_GROUP_NORMAL_LIST_RESPONSE_VALUE#response is null");
//                        return;
//                    }
//                    IMGroupManager.instance().onRepNormalGroupList(normalGroupListRsp);
//                    return;
//
//                case IMBaseDefine.GroupCmdID.CID_GROUP_INFO_RESPONSE_VALUE:
//                    IMGroup.IMGroupInfoListRsp groupInfoListRsp = IMGroup.IMGroupInfoListRsp.parseFrom(buffer);
//                    if(groupInfoListRsp == null){
//                        Logger.e(TAG,"CID_GROUP_INFO_RESPONSE_VALUE#response is null");
//                        return;
//                    }
//                    IMGroupManager.instance().onRepGroupDetailInfo(groupInfoListRsp);
//                    return;
//
//                case IMBaseDefine.GroupCmdID.CID_GROUP_CHANGE_MEMBER_RESPONSE_VALUE:
//                    IMGroup.IMGroupChangeMemberRsp groupChangeMemberRsp = IMGroup.IMGroupChangeMemberRsp.parseFrom(buffer);
//                    if(groupChangeMemberRsp == null){
//                        Logger.e(TAG,"CID_GROUP_CHANGE_MEMBER_RESPONSE_VALUE#response is null");
//                        return;
//                    }
//                    IMGroupManager.instance().onReqChangeGroupMember(groupChangeMemberRsp);
//                    return;
//                //群主转让
//                case IMBaseDefine.GroupCmdID.CID_GROUP_TRANSFER_RESPONSE_VALUE:
//
//                    IMGroup.IMGroupTransferRes groupTransferRes = IMGroup.IMGroupTransferRes.parseFrom(buffer);
//                    if(groupTransferRes == null){
//                        Logger.e(TAG,"CID_GROUP_TRANSFER_RESPONSE_VALUE#response is null");
//                        return;
//                    }
//                    IMGroupManager.instance().onGroupTransferRes(groupTransferRes);
//                    return;
//
//                //群主变更，群成员收到通知
//                case IMBaseDefine.GroupCmdID.CID_GROUP_TRANSFER_NOTIFY_VALUE:
//
//                    IMGroup.IMGroupTransferNotify groupTransferNotify = IMGroup.IMGroupTransferNotify.parseFrom(buffer);
//                    if(groupTransferNotify == null){
//                        Logger.e(TAG,"CID_GROUP_TRANSFER_NOTIFY_VALUE#response is null");
//                        return;
//                    }
//                    IMGroupManager.instance().onGroupTransferNotify(groupTransferNotify);
//                    return;
//
//                //群成员变更 根据PC端修改为下面
////                case IMBaseDefine.GroupCmdID.CID_GROUP_CHANGE_MEMBER_NOTIFY_VALUE:
////                    IMGroup.IMGroupChangeMemberNotify notify = IMGroup.IMGroupChangeMemberNotify.parseFrom(buffer);
////                    IMGroupManager.instance().receiveGroupChangeMemberNotify(notify);
//
//                //群成员变更 群主加入踢人通知
//                case IMBaseDefine.GroupCmdID.CID_GROUP_CHANGE_MEMBER_NOTIFY_PC_VALUE:
//                    IMGroup.IMGroupChangeMemberNotifypc notify = IMGroup.IMGroupChangeMemberNotifypc.parseFrom(buffer);
//                    if(notify == null){
//                        Logger.e(TAG,"CID_GROUP_CHANGE_MEMBER_NOTIFY_PC_VALUE#response is null");
//                        return;
//                    }
//                    IMGroupManager.instance().receiveGroupChangeMemberNotify(notify);
//                    return;
//                case IMBaseDefine.GroupCmdID.CID_GROUP_SHIELD_GROUP_RESPONSE_VALUE:
//                    //todo
//                    return;
//
//            }
//        } catch (IOException e) {
//            Logger.e(TAG,"groupPacketDispatcher# error,cid:%d", commandId);
//        }
    }
}
