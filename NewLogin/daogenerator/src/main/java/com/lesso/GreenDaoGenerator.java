package com.lesso;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Index;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

/**
 * @author : yingmu on 14-12-31.
 * @email : yingmu@mogujie.com.
 * 其中UserEntity、 GroupEntity 继承PeerEntity
 * 由于 UserEntity、 GroupEntity是自动生成，PeerEntity会有重复字段，所以每次生成之后要处理下成员变量。
 * PeerEntity成员变量名与子类统一。
 * <p>
 * 【备注】session表中的create与update字段没有特别的区分，主要是之前服务端的习惯。。。
 */
public class GreenDaoGenerator {
    private static String entityPath = "com.lesso.supercc.ccsdk.DB.entity";

    public static void main(String[] args) throws Exception {
        int dbVersion = 12;
        Schema schema = new Schema(dbVersion, "com.lesso.supercc.ccsdk.DB.dao");

        schema.enableKeepSectionsByDefault();
       /* addDepartment(schema);
        addUserInfo(schema);
        addGroupInfo(schema);
        addMessage(schema);
        addSessionInfo(schema);
        addContact(schema);
        addDiscover(schema);*/
        //addCollection(schema);
        //addSchedule(schema);
//        addApplication(schema);

        addAttach_super(schema);
//        addAttach_super(schema);
//        addAttachSpecify_super(schema);
//        addAttachKey_super(schema);
//        addAttachValue_super(schema);
        addUserInfo_super(schema);
        addUserKey_super(schema);
        addUserValue_super(schema);
        addUserName_super(schema);
        addDepart_super(schema);
        addAccount_super(schema);
        addStatus_super(schema);


        // todo 绝对路径,根据自己的路径设定， 例子如下
        String path = "d:/java";
        new DaoGenerator().generateAll(schema, path);
    }



    private static void addDepart_super(Schema schema) {
        Entity depart = schema.addEntity("DepartEntity");
        depart.setTableName("Depart");
        depart.setClassNameDao("DepartDao");
        depart.setJavaPackage(entityPath);

        depart.addLongProperty("depart_id").primaryKey().autoincrement();//自增字段
        depart.addStringProperty("guid").primaryKey().notNull();//主键
        depart.addIntProperty("container_id").notNull();//容器ID
        depart.addIntProperty("id").notNull();//部门ID
        depart.addStringProperty("name").notNull();//部门名称
        depart.addIntProperty("pid").notNull();//父部门ID
        depart.addIntProperty("sort").notNull();//排序
        depart.addStringProperty("depart_text1").notNull();
        depart.addStringProperty("depart_text2").notNull();
        depart.addStringProperty("depart_text3").notNull();
        depart.addStringProperty("status").notNull();//日志ID

        depart.setHasKeepSections(true);
    }

    private static void addUserInfo_super(Schema schema) {
        Entity userinfo = schema.addEntity("UserInfoEntity");
        userinfo.setTableName("UserInfo");
        userinfo.setClassNameDao("UserInfoDao");
        userinfo.setJavaPackage(entityPath);

        userinfo.addLongProperty("userinfo_id").primaryKey().autoincrement();//自增字段
        userinfo.addStringProperty("guid").notNull();//
        userinfo.addIntProperty("id").notNull();
        userinfo.addIntProperty("container_id").notNull();//容器ID
        userinfo.addIntProperty("name").notNull();
        userinfo.addStringProperty("avatar").notNull();
        userinfo.addIntProperty("attach").notNull();
        userinfo.addStringProperty("depart").notNull();
        userinfo.addStringProperty("uservalue_list").notNull();
        userinfo.addStringProperty("userinfo_text1").notNull();
        userinfo.addStringProperty("userinfo_text2").notNull();
        userinfo.addStringProperty("userinfo_text3").notNull();
        userinfo.addStringProperty("status").notNull();
        userinfo.setHasKeepSections(true);
    }

    private static void addUserKey_super(Schema schema) {
        Entity userkey = schema.addEntity("UserKeyEntity");
        userkey.setTableName("UserKey");
        userkey.setClassNameDao("UserKeyDao");
        userkey.setJavaPackage(entityPath);

        userkey.addLongProperty("userkey_id").primaryKey().autoincrement();//自增字段
        userkey.addIntProperty("id").primaryKey();//主键
        userkey.addIntProperty("type").notNull();//
        userkey.addStringProperty("key").notNull();
        userkey.addStringProperty("defualt_value").notNull();
        userkey.addStringProperty("desc").notNull();
        userkey.addStringProperty("userkey_text1").notNull();
        userkey.addStringProperty("userkey_text2").notNull();
        userkey.addStringProperty("status").notNull();
        userkey.setHasKeepSections(true);
    }

    private static void addUserValue_super(Schema schema) {
        Entity uservalue = schema.addEntity("UserValueEntity");
        uservalue.setTableName("UserValue");
        uservalue.setClassNameDao("UserValueDao");
        uservalue.setJavaPackage(entityPath);

        uservalue.addLongProperty("uservalue_id").primaryKey().autoincrement();//自增字段
        uservalue.addStringProperty("guid").primaryKey().notNull();//
        uservalue.addIntProperty("key").notNull();//
        uservalue.addStringProperty("value").notNull();
        uservalue.addStringProperty("uservalue_text1").notNull();
        uservalue.addStringProperty("uservalue_text2").notNull();
        uservalue.addStringProperty("status").notNull();
        uservalue.setHasKeepSections(true);
    }

    private static void addUserName_super(Schema schema) {
        Entity username = schema.addEntity("UserNameEntity");
        username.setTableName("UserName");
        username.setClassNameDao("UserNameDao");
        username.setJavaPackage(entityPath);

        username.addLongProperty("username_id").primaryKey().autoincrement();//自增字段
        username.addIntProperty("id").primaryKey().notNull();
        username.addStringProperty("value").notNull();
        username.addStringProperty("english_name").notNull();
        username.addStringProperty("nick").notNull();
        username.addStringProperty("word_name").notNull();
        username.addStringProperty("number_name").notNull();
        username.addStringProperty("net_name").notNull();
        username.addStringProperty("pinying_name").notNull();
        username.addStringProperty("pinying_first_name").notNull();
        username.addStringProperty("pinying_nick").notNull();
        username.addStringProperty("username_text1").notNull();
        username.addStringProperty("username_text2").notNull();
        username.addStringProperty("status").notNull();
        username.setHasKeepSections(true);
    }

    //移动端？
    private static void addAttach_super(Schema schema) {
        Entity attach = schema.addEntity("AttachEntity");
        attach.setTableName("Attach");
        attach.setClassNameDao("AttachDao");
        attach.setJavaPackage(entityPath);

        attach.addLongProperty("attach_id").primaryKey().autoincrement();//自增字段
        attach.addStringProperty("voice").notNull();//语音
        attach.addStringProperty("photo").notNull();//图片
        attach.addStringProperty("file").notNull();//文件
        attach.addStringProperty("url").notNull();//地址
        attach.addStringProperty("capacity").notNull();//容量
        attach.addStringProperty("attach_text1").notNull();
        attach.addStringProperty("attach_text2").notNull();
        attach.addStringProperty("attach_text3").notNull();
        attach.addStringProperty("status").notNull();
        attach.setHasKeepSections(true);
    }

    private static void addAttachSpecify_super(Schema schema) {
        Entity attachspecify = schema.addEntity("AttachSpecifyEntity");
        attachspecify.setTableName("AttachSpecify");
        attachspecify.setClassNameDao("AttachSpecifyDao");
        attachspecify.setJavaPackage(entityPath);

        attachspecify.addLongProperty("attachspecify_id").primaryKey().autoincrement();//自增字段
        attachspecify.addStringProperty("id").primaryKey().notNull();
        attachspecify.addStringProperty("model").notNull();
        attachspecify.addStringProperty("key").notNull();
        attachspecify.addStringProperty("status").notNull();
        attachspecify.setHasKeepSections(true);
    }

    private static void addAttachKey_super(Schema schema) {
        Entity attachkey = schema.addEntity("AttachKeyEntity");
        attachkey.setTableName("AttachKey");
        attachkey.setClassNameDao("AttachKeyDao");
        attachkey.setJavaPackage(entityPath);

        attachkey.addLongProperty("attachkey_id").primaryKey().autoincrement();//自增字段
        attachkey.addIntProperty("id").notNull();
        attachkey.addIntProperty("type").notNull();
        attachkey.addStringProperty("key").notNull();
        attachkey.addStringProperty("defualt_value").notNull();
        attachkey.addStringProperty("desc").notNull();
        attachkey.addStringProperty("status").notNull();
        attachkey.setHasKeepSections(true);
    }

    private static void addAttachValue_super(Schema schema) {
        Entity attachvalue = schema.addEntity("AttachValueEntity");
        attachvalue.setTableName("AttachValue");
        attachvalue.setClassNameDao("AttachValueDao");
        attachvalue.setJavaPackage(entityPath);

        attachvalue.addLongProperty("attachvalue_id").primaryKey().autoincrement();//自增字段
        attachvalue.addStringProperty("guid").primaryKey().notNull();
        attachvalue.addStringProperty("attach").notNull();
        attachvalue.addStringProperty("key").notNull();
        attachvalue.addStringProperty("value").notNull();
        attachvalue.addStringProperty("status").notNull();
        attachvalue.setHasKeepSections(true);
    }

    private static void addAccount_super(Schema schema) {
        Entity account = schema.addEntity("AccountEntity");
        account.setTableName("Account");
        account.setClassNameDao("AccountDao");
        account.setJavaPackage(entityPath);

        account.addLongProperty("account_id").primaryKey().autoincrement();//自增字段
        account.addIntProperty("id").notNull();
        account.addIntProperty("userid").notNull();
        account.addIntProperty("type").notNull();
        account.addStringProperty("value").notNull();
        account.addStringProperty("account_text1").notNull();
        account.addStringProperty("account_text2").notNull();
        account.addStringProperty("account_text3").notNull();
        account.addStringProperty("status").notNull();
        account.setHasKeepSections(true);
    }

    private static void addStatus_super(Schema schema) {
        Entity status = schema.addEntity("StatusEntity");
        status.setTableName("Status");
        status.setClassNameDao("StatusDao");
        status.setJavaPackage(entityPath);

        status.addLongProperty("status_id").primaryKey().autoincrement();//自增字段
        status.addStringProperty("id").primaryKey().notNull();
        status.addIntProperty("type").notNull();
        status.addIntProperty("updated").notNull();
        status.addIntProperty("status").notNull();
        status.addStringProperty("status_text1").notNull();
        status.addStringProperty("status_text2").notNull();
        status.addStringProperty("status_text3").notNull();
        status.setHasKeepSections(true);
    }



    private static void addApplication(Schema schema) {
        Entity schedule = schema.addEntity("AppEntity");
        schedule.setTableName("appinfo");
        schedule.setClassNameDao("AppDao");
        schedule.setJavaPackage(entityPath);
        schedule.addStringProperty("appID").notNull().primaryKey();//任务的主键
        schedule.addStringProperty("iconUrl");
        schedule.addStringProperty("zipURL");
        schedule.addDoubleProperty("appVen");
        schedule.addStringProperty("appName");
        schedule.addBooleanProperty("isDownloaded");
        schedule.addStringProperty("localSplash");
    }

    private static void addSchedule(Schema schema) {
        Entity schedule = schema.addEntity("ScheduleEntity");
        schedule.setTableName("ScheduleInfo");
        schedule.setClassNameDao("ScheduleDao");
        schedule.setJavaPackage(entityPath);
        schedule.addLongProperty("task_id").notNull().primaryKey();//任务的主键
        schedule.addIntProperty("dlid");
        schedule.addIntProperty("created");//任务创建的时间
        schedule.addIntProperty("received");
        schedule.addIntProperty("finished");//0表示未完成,1表示已完成
        schedule.addIntProperty("status");
        schedule.addIntProperty("isstar");//0表示不重要的任务,1表示重要的任务
        schedule.addStringProperty("content");
    }

    private static void addCollection(Schema schema) {
        Entity contactInfo = schema.addEntity("CollectionEntity");
        contactInfo.setTableName("CollectionInfo");
        contactInfo.setClassNameDao("CollectionInfoDao");
        contactInfo.setJavaPackage(entityPath);

        contactInfo.addLongProperty("id").notNull().primaryKey();
        contactInfo.addIntProperty("type").notNull();
        contactInfo.addStringProperty("name");
        contactInfo.addStringProperty("content");
        contactInfo.addStringProperty("avatar");
        contactInfo.addStringProperty("time");
        contactInfo.setHasKeepSections(true);
    }

    private static void addContact(Schema schema) {
        Entity contactInfo = schema.addEntity("ContactEntity");
        contactInfo.setTableName("ContactInfo");
        contactInfo.setClassNameDao("ContactInfoDao");
        contactInfo.setJavaPackage(entityPath);

        contactInfo.addIdProperty().autoincrement();
        contactInfo.addIntProperty("contactId").notNull().index();
        contactInfo.addIntProperty("type").notNull();
        contactInfo.addStringProperty("name").notNull();
        contactInfo.addStringProperty("realName").notNull();
        contactInfo.addStringProperty("avatar").notNull();

        contactInfo.setHasKeepSections(true);
    }

    private static void addDiscover(Schema schema) {
        Entity contactInfo = schema.addEntity("DiscoverEntity");
        contactInfo.setTableName("DiscoverInfo");
        contactInfo.setClassNameDao("DiscoverInfoDao");
        contactInfo.setJavaPackage(entityPath);

        contactInfo.addIdProperty().autoincrement();
        contactInfo.addIntProperty("discoverId").notNull().index();
        contactInfo.addStringProperty("icon_url").notNull();
        contactInfo.addStringProperty("to_url").notNull();
        contactInfo.addStringProperty("sys_name").notNull();

        contactInfo.setHasKeepSections(true);
    }

    private static void addDepartment(Schema schema) {
        Entity department = schema.addEntity("DepartmentEntity");
        department.setTableName("Department");
        department.setClassNameDao("DepartmentDao");
        department.setJavaPackage(entityPath);

        department.addIdProperty().autoincrement();
        department.addIntProperty("departId").unique().notNull().index();
        department.addStringProperty("departName").unique().notNull().index();
        department.addIntProperty("parentDepartId").notNull();
        department.addIntProperty("priority").notNull();
        department.addIntProperty("status").notNull();

        department.addIntProperty("created").notNull();
        department.addIntProperty("updated").notNull();

        department.setHasKeepSections(true);
    }

    private static void addUserInfo(Schema schema) {
        Entity userInfo = schema.addEntity("UserEntity");
        userInfo.setTableName("UserInfo");
        userInfo.setClassNameDao("UserDao");
        userInfo.setJavaPackage(entityPath);

        userInfo.addIdProperty().autoincrement();
        userInfo.addIntProperty("peerId").unique().notNull().index();
        userInfo.addIntProperty("gender").notNull();
        userInfo.addStringProperty("mainName").notNull();
        // 这个可以自动生成pinyin
        userInfo.addStringProperty("pinyinName").notNull();
        userInfo.addStringProperty("realName").notNull();
        userInfo.addStringProperty("avatar").notNull();
        userInfo.addStringProperty("phone").notNull();
        userInfo.addStringProperty("email").notNull();
        userInfo.addIntProperty("departmentId").notNull();

        userInfo.addIntProperty("status").notNull();
        userInfo.addIntProperty("created").notNull();
        userInfo.addIntProperty("updated").notNull();

        userInfo.setHasKeepSections(true);

        //todo 索引还没有设定
        // 一对一 addToOne 的使用
        // 支持protobuf
        // schema.addProtobufEntity();
    }

    private static void addGroupInfo(Schema schema) {
        Entity groupInfo = schema.addEntity("GroupEntity");
        groupInfo.setTableName("GroupInfo");
        groupInfo.setClassNameDao("GroupDao");
        groupInfo.setJavaPackage(entityPath);

        groupInfo.addIdProperty().autoincrement();
        groupInfo.addIntProperty("peerId").unique().notNull();
        groupInfo.addIntProperty("groupType").notNull();
        groupInfo.addStringProperty("mainName").notNull();
        groupInfo.addStringProperty("avatar").notNull();
        groupInfo.addIntProperty("creatorId").notNull();
        groupInfo.addIntProperty("userCnt").notNull();

        groupInfo.addStringProperty("userList").notNull();
        groupInfo.addIntProperty("version").notNull();
        groupInfo.addIntProperty("status").notNull();
        groupInfo.addIntProperty("created").notNull();
        groupInfo.addIntProperty("updated").notNull();

        groupInfo.setHasKeepSections(true);
    }

    private static void addMessage(Schema schema) {
        Entity message = schema.addEntity("MessageEntity");
        message.setTableName("Message");
        message.setClassNameDao("MessageDao");
        message.setJavaPackage(entityPath);

        message.implementsSerializable();
        message.addIdProperty().autoincrement();
        Property msgProId = message.addIntProperty("msgId").notNull().getProperty();
        message.addIntProperty("fromId").notNull();
        message.addIntProperty("toId").notNull();
        // 是不是需要添加一个sessionkey标示一下，登陆的用户在前面
        Property sessionPro = message.addStringProperty("sessionKey").notNull().getProperty();
        message.addStringProperty("content").notNull();
        message.addIntProperty("msgType").notNull();
        message.addIntProperty("displayType").notNull();
        message.addIntProperty("statusType").notNull();
        message.addIntProperty("status").notNull().index();
        message.addIntProperty("created").notNull().index();
        message.addIntProperty("updated").notNull();

        Index index = new Index();
        index.addProperty(msgProId);
        index.addProperty(sessionPro);
        index.makeUnique();
        message.addIndex(index);

        message.setHasKeepSections(true);
    }

    private static void addSessionInfo(Schema schema) {
        Entity sessionInfo = schema.addEntity("SessionEntity");
        sessionInfo.setTableName("Session");
        sessionInfo.setClassNameDao("SessionDao");
        sessionInfo.setJavaPackage(entityPath);

        //point to userId/groupId need sessionType 区分
        sessionInfo.addIdProperty().autoincrement();
        sessionInfo.addStringProperty("sessionKey").unique().notNull(); //.unique()
        sessionInfo.addIntProperty("peerId").notNull();
        sessionInfo.addIntProperty("peerType").notNull();

        sessionInfo.addIntProperty("latestMsgType").notNull();
        sessionInfo.addIntProperty("latestMsgId").notNull();
        sessionInfo.addStringProperty("latestMsgData").notNull();

        sessionInfo.addIntProperty("talkId").notNull();
        sessionInfo.addIntProperty("created").notNull();
        sessionInfo.addIntProperty("updated").notNull();

        sessionInfo.setHasKeepSections(true);
    }
}
