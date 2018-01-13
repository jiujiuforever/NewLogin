package im.jizhu.com.loginmodule.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import im.jizhu.com.loginmodule.DB.dao.DaoMaster;
import im.jizhu.com.loginmodule.DB.dao.DaoSession;
import im.jizhu.com.loginmodule.DB.dao.UserInfoDao;
import im.jizhu.com.loginmodule.DB.entity.UserInfoEntity;
//import im.jizhu.com.loginmodule.DB.dao.DepartmentDao;
//import im.jizhu.com.loginmodule.DB.dao.GroupDao;
//import im.jizhu.com.loginmodule.DB.dao.MessageDao;
//import im.jizhu.com.loginmodule.DB.dao.SessionDao;
//import im.jizhu.com.loginmodule.DB.dao.UserDao;
//import im.jizhu.com.loginmodule.DB.dao.UserStatusInfoDao;
//import im.jizhu.com.loginmodule.DB.entity.DepartmentEntity;
//import im.jizhu.com.loginmodule.DB.entity.GroupEntity;
//import im.jizhu.com.loginmodule.DB.entity.MessageEntity;
//import im.jizhu.com.loginmodule.DB.entity.SessionEntity;
//import im.jizhu.com.loginmodule.DB.entity.UserEntity;
//import im.jizhu.com.loginmodule.DB.entity.UserStatusEntity;
//import im.jizhu.com.loginmodule.config.DBConstant;
//import im.jizhu.com.loginmodule.config.MessageConstant;
//import im.jizhu.com.loginmodule.imservice.entity.AudioMessage;
//import im.jizhu.com.loginmodule.imservice.entity.FileMessage;
//import im.jizhu.com.loginmodule.imservice.entity.ImageMessage;
//import im.jizhu.com.loginmodule.imservice.entity.MixMessage;
//import im.jizhu.com.loginmodule.imservice.entity.TextMessage;
//import im.jizhu.com.loginmodule.utils.Logger;
//
//import de.greenrobot.dao.query.DeleteQuery;
//import de.greenrobot.dao.query.Query;
//import de.greenrobot.dao.query.QueryBuilder;

/**
 * @author : yingmu on 15-1-5.
 * @email : yingmu@mogujie.com.
 * <p/>
 * 有两个静态标识可开启QueryBuilder的SQL和参数的日志输出：
 * QueryBuilder.LOG_SQL = true;
 * QueryBuilder.LOG_VALUES = true;
 */
public class DBInterface {

    private static final String TAG = "DBInterface";

    private static DBInterface dbInterface = null;
    private DaoMaster.DevOpenHelper openHelperLesso;
//    private DaoMaster.DevOpenHelper openHelperApp;
    private Context context = null;
    private int loginUserId = 0;

    public static DBInterface instance() {
        if (dbInterface == null) {
            synchronized (DBInterface.class) {
                if (dbInterface == null) {
                    dbInterface = new DBInterface();
                }
            }
        }
        return dbInterface;
    }

    private DBInterface() {
    }

    /**
     * 上下文环境的更新
     * 1. 环境变量的clear
     * check
     */
    public void close() {
//        if (openHelperLesso != null) {
//            openHelperLesso.close();
//            openHelperLesso = null;
//            context = null;
//            loginUserId = 0;
//        }
//        if (openHelperApp != null) {
//            openHelperApp.close();
//            openHelperApp = null;
//            context = null;
//            loginUserId = 0;
//        }
    }

    public void initDbHelp(Context ctx, int loginId) {

        if (ctx == null || loginId <= 0) {
            return;
            //throw new RuntimeException("#DBInterface# init DB exception!");
        }

        // 临时处理，为了解决离线登陆db实例初始化的过程
        if (context != ctx || loginUserId != loginId) {

            //close();//应该是赋值之前关
            context = ctx;
            loginUserId = loginId;
            // TODO: 2017/9/22  为什么加close
            //close();
            String DBName = "tt_" + loginId + ".db";
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(ctx, DBName, null);
            this.openHelperLesso = helper;
//
//            String DBName1 = "tt_" + loginId + "_app.db";
//            DaoMaster.DevOpenHelper helper1 = new DaoMaster.DevOpenHelper(ctx, DBName1, null);
//            this.openHelperApp= helper1;
        }
    }

    /**
     * Query for readable DB
     */
    public DaoSession openReadableLessoDb() {
        if (openHelperLesso == null) {
            initDbHelp(context, loginUserId);
        }
        SQLiteDatabase db = openHelperLesso.getReadableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        return daoSession;
    }

//    //用户信息插入与更新
//    public long InsertOrUpdateUserInfo(UserInfoEntity userInfoEntity) {
//        UserInfoDao dao = openReadableLessoDb().getUserInfoDao();
//        long pkId = dao.insertOrReplace(userInfoEntity);
//        return pkId;
//    }

    /**
     * getReadableDatabase()并不是以只读方式打开数据库，而是先执行getWritableDatabase()，失败的情况下才调用。
     * getWritableDatabase()和getReadableDatabase()方法都可以获取一个用于操作数据库的SQLiteDatabase实例。
     * 但getWritableDatabase()方法以读写方式打开数据库，一旦数据库的磁盘空间满了，数据库就只能读而不能写，
     * getWritableDatabase()打开数据库就会出错。getReadableDatabase()方法先以读写方式打开数据库，
     * 倘若使用如果数据库的磁盘空间满了，就会打开失败，当打开失败后会继续尝试以只读方式打开数据库.
     */

    /**
     * Query for readable DB
     */
//    public DaoSession openReadableLessoDb() {
//        isInitOk();
//        SQLiteDatabase db = openHelperLesso.getReadableDatabase();
//        DaoMaster daoMaster = new DaoMaster(db);
//        DaoSession daoSession = daoMaster.newSession();
//        return daoSession;
//    }

    /**
     * Query for writable DB
     */
//    public DaoSession openWritableLessoDb() {
//        isInitOk();
//
//        SQLiteDatabase db = openHelperLesso.getWritableDatabase();
//        DaoMaster daoMaster = new DaoMaster(db);
//        DaoSession daoSession = daoMaster.newSession();
//        return daoSession;
//    }

    /**
     * Query for readable DB
     */
//    public DaoSession openReadableAppDb() {
//        isInitOk();
//        SQLiteDatabase db = openHelperApp.getReadableDatabase();
//        DaoMaster daoMaster = new DaoMaster(db);
//        DaoSession daoSession = daoMaster.newSession();
//        return daoSession;
//    }
//
//    /**
//     * Query for writable DB
//     */
//    public DaoSession openWritableAppDb(Context ctx, int loginId) {
//
//        if (openHelperLesso == null||openHelperApp==null) {
//            initDbHelp(ctx,loginId);
//        }
//        isInitOk();
//
//        SQLiteDatabase db = openHelperApp.getWritableDatabase();
//        DaoMaster daoMaster = new DaoMaster(db);
//        DaoSession daoSession = daoMaster.newSession();
//        return daoSession;
//    }
//
//
//    private void isInitOk() {
//
//        if (openHelperLesso == null || openHelperApp == null) {
//            //Logger.e(TAG,"DBInterface#isInit not success or start,cause by openHelper is null");
//            // 抛出异常 todo
//            //throw new RuntimeException("DBInterface#isInit not success or start,cause by openHelper is null");
//            initDbHelp(context, loginUserId);
//        }
//    }


//    /**
//     * -------------------------下面开始department 操作相关---------------------------------------
//     */
//    public void batchInsertOrUpdateDepart(List<DepartmentEntity> entityList) {
//        if (entityList.size() <= 0) {
//            return;
//        }
//        DepartmentDao dao = openReadableLessoDb().getDepartmentDao();
//        dao.insertOrReplaceInTx(entityList);
//    }
//
//    /**
//     * update
//     */
//    public int getDeptLastTime() {
//        DepartmentDao dao = openReadableLessoDb().getDepartmentDao();
//        DepartmentEntity entity = dao.queryBuilder()
//                .orderDesc(DepartmentDao.Properties.Updated)
//                .limit(1)
//                .unique();
//        if (entity == null) {
//            return 0;
//        } else {
//            return entity.getUpdated();
//        }
//    }
//
//    // 部门被删除的情况
//    public List<DepartmentEntity> loadAllDept() {
//        DepartmentDao dao = openReadableLessoDb().getDepartmentDao();
//        List<DepartmentEntity> result = dao.loadAll();
//        return result;
//    }
//
//    /**-------------------------下面开始User 操作相关---------------------------------------*/
//    /**
//     * @return todo  USER_STATUS_LEAVE
//     */
//    public List<UserEntity> loadAllUsers() {
//        UserDao dao = openReadableLessoDb().getUserDao();
//        List<UserEntity> result = dao.loadAll();
//        return result;
//    }
//
//    public List<UserStatusEntity> loadAllUsersStatus() {
//        UserStatusInfoDao dao = openReadableLessoDb().getUserStatusInfoDao();
//        List<UserStatusEntity> result = dao.loadAll();
//        return result;
//    }
//
//
//    public UserEntity getByUserName(String uName) {
//        UserDao dao = openReadableLessoDb().getUserDao();
//        UserEntity entity = dao.queryBuilder().where(UserDao.Properties.PinyinName.eq(uName)).unique();
//        return entity;
//    }
//
//    public UserEntity getByLoginId(int loginId) {
//        UserDao dao = openReadableLessoDb().getUserDao();
//        UserEntity entity = dao.queryBuilder().where(UserDao.Properties.PeerId.eq(loginId)).unique();
//        return entity;
//    }
//
//
//    public void insertOrUpdateUser(UserEntity entity) {
//        UserDao userDao = openReadableLessoDb().getUserDao();
//        long rowId = userDao.insertOrReplace(entity);
//        //LogUtils.e("test","搜索人员数据插入："+rowId);
//    }
//
//
//    public void batchInsertOrUpdateUser(List<UserEntity> entityList) {
//        if (entityList.size() <= 0) {
//            return;
//        }
//        UserDao userDao = openReadableLessoDb().getUserDao();
//        userDao.insertOrReplaceInTx(entityList);
//    }
//
//    public void batchInsertOrUpdateUserStatus(List<UserStatusEntity> UserStatusentityList) {
//        if (UserStatusentityList.size() <= 0) {
//            return;
//        }
//        UserStatusInfoDao userStatusInfoDao = openReadableLessoDb().getUserStatusInfoDao();
//        userStatusInfoDao.insertOrReplaceInTx(UserStatusentityList);
//    }
//
//    /**
//     * update
//     */
//    public int getUserInfoLastTime() {
//        UserDao sessionDao = openReadableLessoDb().getUserDao();
//        UserEntity userEntity = sessionDao.queryBuilder()
//                .orderDesc(UserDao.Properties.Updated)
//                .limit(1)
//                .unique();
//        if (userEntity == null) {
//            return 0;
//        } else {
//            return userEntity.getUpdated();
//        }
//    }
//
//    /**-------------------------下面开始Group 操作相关---------------------------------------*/
//    /**
//     * 载入Group的所有数据
//     *
//     * @return
//     */
//    public List<GroupEntity> loadAllGroup() {
//        GroupDao dao = openReadableLessoDb().getGroupDao();
//        List<GroupEntity> result = dao.loadAll();
//        return result;
//    }
//
//    public long insertOrUpdateGroup(GroupEntity groupEntity) {
//        GroupDao dao = openReadableLessoDb().getGroupDao();
//        long pkId = dao.insertOrReplace(groupEntity);
//        return pkId;
//    }
//
//    public void batchInsertOrUpdateGroup(List<GroupEntity> entityList) {
//        if (entityList.size() <= 0) {
//            return;
//        }
//        GroupDao dao = openReadableLessoDb().getGroupDao();
//        dao.insertOrReplaceInTx(entityList);
//    }
//
//    public void batchInsertDeleteGroup(List<GroupEntity> entityList) {
//        if (entityList.size() <= 0) {
//            return;
//        }
//        GroupDao dao = openReadableLessoDb().getGroupDao();
//        dao.deleteInTx(entityList);
//    }
//
//    /**-------------------------下面开始session 操作相关---------------------------------------*/
//    /**
//     * 载入session 表中的所有数据
//     *
//     * @return
//     */
//    public List<SessionEntity> loadAllSession() {
//        SessionDao dao = openReadableLessoDb().getSessionDao();
//        List<SessionEntity> result = dao.queryBuilder()
//                .orderDesc(SessionDao.Properties.Updated)
//                .list();
//        return result;
//    }
//
//    public long insertOrUpdateSession(SessionEntity sessionEntity) {
//        SessionDao dao = openReadableLessoDb().getSessionDao();
//        long pkId = dao.insertOrReplace(sessionEntity);
//        return pkId;
//    }
//
//    public void batchInsertOrUpdateSession(List<SessionEntity> entityList) {
//        if (entityList.size() <= 0) {
//            return;
//        }
//        SessionDao dao = openReadableLessoDb().getSessionDao();
//        dao.insertOrReplaceInTx(entityList);
//    }
//
//    public void deleteSession(String sessionKey) {
//        SessionDao sessionDao = openReadableLessoDb().getSessionDao();
//        DeleteQuery<SessionEntity> bd = sessionDao.queryBuilder()
//                .where(SessionDao.Properties.SessionKey.eq(sessionKey))
//                .buildDelete();
//
//        bd.executeDeleteWithoutDetachingEntities();
//    }
//
//    /**
//     * 获取最后回话的时间，便于获取联系人列表变化
//     * 问题: 本地消息发送失败，依旧会更新session的时间 [存在会话、不存在的会话]
//     * 本质上还是最后一条成功消息的时间
//     *
//     * @return
//     */
//    public int getSessionLastTime() {
//        int timeLine = 0;
//        MessageDao messageDao = openReadableLessoDb().getMessageDao();
//        String successType = String.valueOf(MessageConstant.MSG_SUCCESS);
//        String sql = "select created from Message where status=? order by created desc limit 1";
//        Cursor cursor = messageDao.getDatabase().rawQuery(sql, new String[]{successType});
//        try {
//            if (cursor != null && cursor.getCount() == 1) {
//                cursor.moveToFirst();
//                timeLine = cursor.getInt(0);
//            }
//        } catch (Exception e) {
//            Logger.e(TAG,"DBInterface#getSessionLastTime cursor 查询异常");
//        } finally {
//            cursor.close();
//        }
//        return timeLine;
//    }
//
//    /**
//     * 修改消息的标记是1（表示该条消息已经回撤）
//     *
//     * @param messageEntity
//     */
//    public void updateMessageStateType(MessageEntity messageEntity) {
//        messageEntity.setStatusType(1);
//        MessageDao messageDao = openReadableLessoDb().getMessageDao();
//        messageDao.insertOrReplace(messageEntity);
//
////        messageDao.update(messageEntity);
//
//    }
//
//    /**
//     * 更新离线在线状态数据
//     *
//     * @param UserStatusentityList
//     */
//    public void updateOnlineStateType(List<UserStatusEntity> UserStatusentityList) {
//        UserStatusInfoDao userStatusInfoDao = openReadableLessoDb().getUserStatusInfoDao();
//        userStatusInfoDao.insertOrReplaceInTx(UserStatusentityList);
////        userStatusInfoDao.updateInTx(UserStatusentityList);
//    }
//
//    /**
//     * 消息已读未读
//     *
//     * @param messageEntity
//     */
//    public void updateMessageRead(MessageEntity messageEntity) {
//        messageEntity.setReadStatus(1);
//        MessageDao messageDao = openReadableLessoDb().getMessageDao();
//        messageDao.insertOrReplace(messageEntity);// update
//    }
//
//    /**
//     * 修改回话界面的最后一条消息的状态
//     *
//     * @param sessionEntity
//     * @param state         目的状态（1表示最后一条消息是回撤的，0表示正常）
//     */
//    public void updateSessionState(SessionEntity sessionEntity, int state) {
//        sessionEntity.setState(state);
//        SessionDao sessionDao = openReadableLessoDb().getSessionDao();
//
//        sessionDao.insertOrReplace(sessionEntity);
//
////        sessionDao.update(sessionEntity);
//
//    }
//
//    /**
//     * -------------------------下面开始message 操作相关---------------------------------------
//     */
//
//    // where (msgId >= startMsgId and msgId<=lastMsgId) or
//    // (msgId=0 and status = 0)
//    // order by created desc
//    // limit count;
//    // 按照时间排序
//    public List<MessageEntity> getHistoryMsg(String chatKey, int lastMsgId, long lastCreateTime, int count) {
//        /**解决消息重复的问题*/
//        int preMsgId = lastMsgId + 1;
//        MessageDao dao = openReadableLessoDb().getMessageDao();
//        Log.e("test", "dao========" + dao);
//        List<MessageEntity> listMsg = dao.queryBuilder().where(MessageDao.Properties.Created.le(lastCreateTime)
//                , MessageDao.Properties.SessionKey.eq(chatKey)
//                , MessageDao.Properties.MsgId.notEq(preMsgId))
//                .whereOr(MessageDao.Properties.MsgId.le(lastMsgId),
//                        MessageDao.Properties.MsgId.gt(90000000))
//                .orderDesc(MessageDao.Properties.Created)
//                .orderDesc(MessageDao.Properties.MsgId)
//                .limit(count)
//                .list();
//
//        return formatMessage(listMsg);
//    }
//
//    /**
//     * IMGetLatestMsgIdReq 后去最后一条合法的msgid
//     */
//    public List<Integer> refreshHistoryMsgId(String chatKey, int beginMsgId, int lastMsgId) {
//        MessageDao dao = openReadableLessoDb().getMessageDao();
//
//        String sql = "select MSG_ID from Message where SESSION_KEY = ? and MSG_ID >= ? and MSG_ID <= ? order by MSG_ID asc";
//        Cursor cursor = dao.getDatabase().rawQuery(sql, new String[]{chatKey, String.valueOf(beginMsgId), String.valueOf(lastMsgId)});
//
//        List<Integer> msgIdList = new ArrayList<>();
//        try {
//            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
//                int msgId = cursor.getInt(0);
//                msgIdList.add(msgId);
//            }
//        } finally {
//            cursor.close();
//        }
//        return msgIdList;
//    }
//
//
//    public long insertOrUpdateMix(MessageEntity message) {
//        MessageDao dao = openReadableLessoDb().getMessageDao();
//        MessageEntity parent = dao.queryBuilder().where(MessageDao.Properties.MsgId.eq(message.getMsgId())
//                , MessageDao.Properties.SessionKey.eq(message.getSessionKey())).unique();
//
//        long resId = parent.getId();
//        if (parent.getDisplayType() != DBConstant.SHOW_MIX_TEXT) {
//            return resId;
//        }
//
//        boolean needUpdate = false;
//        MixMessage mixParent = (MixMessage) formatMessage(parent);
//        List<MessageEntity> msgList = mixParent.getMsgList();
//        for (int index = 0; index < msgList.size(); index++) {
//            if (msgList.get(index).getId() == message.getId()) {
//                msgList.set(index, message);
//                needUpdate = true;
//                break;
//            }
//        }
//
//        if (needUpdate) {
//            mixParent.setMsgList(msgList);
//            long pkId = dao.insertOrReplace(mixParent);
//            return pkId;
//        }
//        return resId;
//    }
//
//    /**
//     * 有可能是混合消息
//     * 批量接口{batchInsertOrUpdateMessage} 没有存在场景
//     */
//    public long insertOrUpdateMessage(MessageEntity message) {
//        if (message.getId() != null && message.getId() < 0) {
//            // mix消息
//            return insertOrUpdateMix(message);
//        }
//        MessageDao dao = openReadableLessoDb().getMessageDao();
//        long pkId = dao.insertOrReplace(message);
//        return pkId;
//    }
//
//    /**
//     * todo 这个地方调用存在特殊场景，如果list中包含Id为负Mix子类型，更新就有问题
//     * 现在的调用列表没有这个情景，使用的时候注意
//     */
//    public void batchInsertOrUpdateMessage(List<MessageEntity> entityList) {
//        MessageDao dao = openReadableLessoDb().getMessageDao();
//        dao.insertOrReplaceInTx(entityList);
//    }
//
//
//    public void deleteMessageById(long localId) {
//        if (localId <= 0) {
//            return;
//        }
//        Set<Long> setIds = new TreeSet<>();
//        setIds.add(localId);
//        batchDeleteMessageById(setIds);
//    }
//
//    public void batchDeleteMessageById(Set<Long> pkIds) {
//        if (pkIds.size() <= 0) {
//            return;
//        }
//        MessageDao dao = openReadableLessoDb().getMessageDao();
//        dao.deleteByKeyInTx(pkIds);
//    }
//
//
//    public void deleteMessageByMsgId(int msgId) {
//        if (msgId <= 0) {
//            return;
//        }
//        MessageDao messageDao = openReadableLessoDb().getMessageDao();
//        QueryBuilder<MessageEntity> qb = openReadableLessoDb().getMessageDao().queryBuilder();
//        DeleteQuery<MessageEntity> bd = qb.where(MessageDao.Properties.MsgId.eq(msgId)).buildDelete();
//        bd.executeDeleteWithoutDetachingEntities();
//    }
//
//    public MessageEntity getMessageByMsgId(int messageId) {
//        MessageDao dao = openReadableLessoDb().getMessageDao();
//        Query query = dao.queryBuilder().where(
//                MessageDao.Properties.Id.eq(messageId))
//                .build();
//        return formatMessage((MessageEntity) query.unique());
//    }
//
//    /**
//     * 根据主键查询
//     * not use
//     */
//    public MessageEntity getMessageById(long localId) {
//        MessageDao dao = openReadableLessoDb().getMessageDao();
//        MessageEntity messageEntity =
//                dao.queryBuilder().where(MessageDao.Properties.Id.eq(localId)).unique();
//        return formatMessage(messageEntity);
//    }
//
//
//    private MessageEntity formatMessage(MessageEntity msg) {
//        MessageEntity messageEntity = null;
//        int displayType = msg.getDisplayType();
//        switch (displayType) {
//            case DBConstant.SHOW_MIX_TEXT:
//                try {
//                    messageEntity = MixMessage.parseFromDB(msg);
//                } catch (JSONException e) {
//                    Logger.e(TAG,e.toString());
//                }
//                break;
//            case DBConstant.SHOW_AUDIO_TYPE:
//                messageEntity = AudioMessage.parseFromDB(msg);
//                break;
//            case DBConstant.SHOW_IMAGE_TYPE:
//                messageEntity = ImageMessage.parseFromDB(msg);
//                break;
//            case DBConstant.SHOW_FILE_TYPE:
//                messageEntity = FileMessage.parseFromDB(msg);
//                break;
//            case DBConstant.SHOW_ORIGIN_TEXT_TYPE:
//                messageEntity = TextMessage.parseFromDB(msg);
//                break;
//
//            case DBConstant.SHOW_TYPE_GROUP_TRANSFER://群主转让新增
//                messageEntity = TextMessage.parseFromDBTRANSFER(msg);
//                break;
//        }
//        return messageEntity;
//    }
//
//
//    public List<MessageEntity> formatMessage(List<MessageEntity> msgList) {
//        if (msgList.size() <= 0) {
//            return Collections.emptyList();
//        }
//        ArrayList<MessageEntity> newList = new ArrayList<>();
//        for (MessageEntity info : msgList) {
//            int displayType = info.getDisplayType();
//            switch (displayType) {
//                case DBConstant.SHOW_MIX_TEXT:
//                    try {
//                        newList.add(MixMessage.parseFromDB(info));
//                    } catch (JSONException e) {
//                        Logger.e(TAG,e.toString());
//                    }
//                    break;
//                case DBConstant.SHOW_AUDIO_TYPE:
//                    newList.add(AudioMessage.parseFromDB(info));
//                    break;
//                case DBConstant.SHOW_IMAGE_TYPE:
//                    newList.add(ImageMessage.parseFromDB(info));
//                    break;
//                case DBConstant.SHOW_FILE_TYPE:
//                    newList.add(FileMessage.parseFromDB(info));
//                    break;
//                case DBConstant.SHOW_ORIGIN_TEXT_TYPE:
//                    newList.add(TextMessage.parseFromDB(info));
//                    break;
//                case DBConstant.SHOW_TYPE_GROUP_TRANSFER://群主转让新增
//                    newList.add(TextMessage.parseFromDBTRANSFER(info));
//                    break;
//            }
//        }
//        return newList;
//    }

}
