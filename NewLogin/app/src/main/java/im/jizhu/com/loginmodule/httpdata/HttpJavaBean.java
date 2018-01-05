package im.jizhu.com.loginmodule.httpdata;



import org.json.JSONArray;

import java.util.ArrayList;

import im.jizhu.com.loginmodule.DB.entity.DepartmentEntity;
import im.jizhu.com.loginmodule.DB.entity.GroupEntity;
import im.jizhu.com.loginmodule.DB.entity.MessageEntity;
import im.jizhu.com.loginmodule.DB.entity.SessionEntity;
import im.jizhu.com.loginmodule.DB.entity.UserEntity;
import im.jizhu.com.loginmodule.config.DBConstant;
import im.jizhu.com.loginmodule.utils.pinyin.PinYin;

/**
 * Created by it026 on 2017/6/24.
 */
public class HttpJavaBean {


    /**
     * 人员实体数据
     * @param jsonArrayuser
     * @return
     */
    public static UserEntity getUserEntity(JSONArray jsonArrayuser, int i) {

        try{
            UserEntity userEntity = new UserEntity();
            int timeNow = (int) (System.currentTimeMillis()/1000);
            userEntity.setStatus(Integer.parseInt(jsonArrayuser.getJSONObject(i).getString("status")));
            userEntity.setAvatar(jsonArrayuser.getJSONObject(i).getString("avatar"));
            userEntity.setCreated(timeNow);
            userEntity.setDepartmentId(Integer.parseInt(jsonArrayuser.getJSONObject(i).getString("dept")));
            userEntity.setEmail(jsonArrayuser.getJSONObject(i).getString("email"));
            userEntity.setGender(Integer.parseInt(jsonArrayuser.getJSONObject(i).getString("sex")));
            userEntity.setMainName(jsonArrayuser.getJSONObject(i).getString("nick"));
            userEntity.setPhone(jsonArrayuser.getJSONObject(i).getString("phone"));
            userEntity.setPinyinName(jsonArrayuser.getJSONObject(i).getString("domain"));
            userEntity.setRealName(jsonArrayuser.getJSONObject(i).getString("name"));
            userEntity.setUpdated(timeNow);
            userEntity.setSignInfo(jsonArrayuser.getJSONObject(i).getString("signfo"));
            userEntity.setTypeId(Integer.parseInt(jsonArrayuser.getJSONObject(i).getString("rtype")));
            userEntity.setPeerId(Integer.parseInt(jsonArrayuser.getJSONObject(i).getString("id")));
            userEntity.setTelephone(jsonArrayuser.getJSONObject(i).getString("tel"));
            userEntity.setSapVkorg(jsonArrayuser.getJSONObject(i).getString("sap"));
            userEntity.setOanum(jsonArrayuser.getJSONObject(i).getString("oa"));
            PinYin.getPinYin(userEntity.getMainName(), userEntity.getPinyinElement());
            return userEntity;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 部门实体数据
     * @param jsonArraydept
     * @param i
     * @return
     */
    public static DepartmentEntity getDepartEntity(JSONArray jsonArraydept, int i) {

        try{
            DepartmentEntity departmentEntity = new DepartmentEntity();
            int timeNow = (int) (System.currentTimeMillis()/1000);
            departmentEntity.setDepartId(Integer.parseInt(jsonArraydept.getJSONObject(i).getString("id")));
            departmentEntity.setDepartName(jsonArraydept.getJSONObject(i).getString("name"));
            departmentEntity.setParentDepartId(Integer.parseInt(jsonArraydept.getJSONObject(i).getString("pid")));
            departmentEntity.setPriority(Integer.parseInt(jsonArraydept.getJSONObject(i).getString("sort")));
            departmentEntity.setStatus(Integer.parseInt(jsonArraydept.getJSONObject(i).getString("status")));
            departmentEntity.setCreated(timeNow);
            departmentEntity.setUpdated(timeNow);
            departmentEntity.setType(Integer.parseInt(jsonArraydept.getJSONObject(i).getString("type")));//新增type值
            // 设定pinyin 相关
            PinYin.getPinYin(jsonArraydept.getJSONObject(i).getString("name"), departmentEntity.getPinyinElement());
            return departmentEntity;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 群组实体数据
     * @param jsonArraygroup
     * @param i
     * @return
     */
    public static GroupEntity getGroupEntity(JSONArray jsonArraygroup, int i) {
        try{
            GroupEntity groupEntity = new GroupEntity();
            int timeNow = (int) (System.currentTimeMillis()/1000);
            groupEntity.setUpdated(timeNow);
            groupEntity.setCreated(Integer.parseInt(jsonArraygroup.getJSONObject(i).getString("created")));
            groupEntity.setMainName(jsonArraygroup.getJSONObject(i).getString("name"));
            groupEntity.setAvatar(jsonArraygroup.getJSONObject(i).getString("avatar"));
            groupEntity.setCreatorId(Integer.parseInt(jsonArraygroup.getJSONObject(i).getString("creator")));
            groupEntity.setPeerId(Integer.parseInt(jsonArraygroup.getJSONObject(i).getString("id")));
            groupEntity.setGroupType(Integer.parseInt(jsonArraygroup.getJSONObject(i).getString("type")));
            //groupEntity.setStatus(Integer.parseInt(jsonArraygroup.getJSONObject(i).getString("status")));
            groupEntity.setStatuValue(Integer.parseInt(jsonArraygroup.getJSONObject(i).getString("status")));
            groupEntity.setUserCnt(Integer.parseInt(jsonArraygroup.getJSONObject(i).getString("usercnt")));
            //groupEntity.setVersion(Integer.parseInt(jsonArraygroup.getJSONObject(i).getString("pid")));
            String users = jsonArraygroup.getJSONObject(i).getString("users");
            //LogUtils.e("test","数据users："+users);
            if (users.isEmpty()) {
                return null;
            }
            String []usersSplit = users.split("\\|");
            ArrayList<String> array=new ArrayList<String>();
            for (int y=0;y<usersSplit.length;y++) {
                String usersString = usersSplit[y];
                String []user = usersString.split(",");
                String usersLast = user[0];
                String usersLast1 = user[1];
                if(usersLast1.equals("0")){
                    array.add(usersLast);
                }
            }
            if (array.size()<=0) {
                return null;
            }
            String packagesString="";
            for(String userstring:array){
                packagesString+=userstring+",";
            }
            if (packagesString.length()>0) {
                String userslist = packagesString.substring(0,packagesString.length()-1);
                groupEntity.setUserList(userslist);
            }
            //LogUtils.e("test",";;array："+array);
            PinYin.getPinYin(jsonArraygroup.getJSONObject(i).getString("name"), groupEntity.getPinyinElement());
            return groupEntity;

        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 最近
     * @param jsonArraysession
     * @param i
     * @return
     */
    public static SessionEntity getSessionEntity(JSONArray jsonArraysession, int i) {

        try {
            //LogUtils.e("test","jsonArraysession:"+jsonArraysession);
            SessionEntity sessionEntity = new SessionEntity();
            //Logger.e("消息类型数据接收","消息类型数据："+jsonArraysession.getJSONObject(i).getString("mtype")+";jsonArraysession:"+jsonArraysession.getJSONObject(i));
            int msgType = getJavaMsgType(Integer.parseInt(jsonArraysession.getJSONObject(i).getString("mtype")));
            //LogUtils.e("test","msgType:"+msgType);
            sessionEntity.setLatestMsgType(msgType);
            sessionEntity.setPeerType(getJavaSessionType(Integer.parseInt(jsonArraysession.getJSONObject(i).getString("type"))));
            sessionEntity.setPeerId(Integer.parseInt(jsonArraysession.getJSONObject(i).getString("sid")));
            sessionEntity.buildSessionKey();
            sessionEntity.setTalkId(Integer.parseInt(jsonArraysession.getJSONObject(i).getString("fuser")));
            sessionEntity.setLatestMsgId(Integer.parseInt(jsonArraysession.getJSONObject(i).getString("lmsgid")));
            sessionEntity.setCreated(Integer.parseInt(jsonArraysession.getJSONObject(i).getString("msgtime")));
            sessionEntity.setState(Integer.parseInt(jsonArraysession.getJSONObject(i).getString("mstatus")));
            String content  = jsonArraysession.getJSONObject(i).getString("lmsg");
            String desMessage = new String(com.lesso.cc.Security.getInstance().DecryptMsg(content));
            // 判断具体的类型是什么
            if(msgType == DBConstant.MSG_TYPE_GROUP_TEXT ||
                    msgType ==DBConstant.MSG_TYPE_SINGLE_TEXT){
                desMessage =  MsgAnalyzeEngine.analyzeMessageDisplay(desMessage);
            }

            if (msgType == 2 || msgType == 18) {
                sessionEntity.setLatestMsgData(DBConstant.DISPLAY_FOR_AUDIO);
            }else {
                sessionEntity.setLatestMsgData(desMessage);
            }
            sessionEntity.setUpdated(Integer.parseInt(jsonArraysession.getJSONObject(i).getString("msgtime")));
            return sessionEntity;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static int getJavaMsgType(int mtype) {
        //Logger.e("消息类型","消息类型数据："+mtype);
        switch(mtype) {
            case 1:
                return DBConstant.MSG_TYPE_SINGLE_TEXT;
            case 2:
                return DBConstant.MSG_TYPE_SINGLE_AUDIO;
            case 17:
                return DBConstant.MSG_TYPE_GROUP_TEXT;
            case 18:
                return DBConstant.MSG_TYPE_GROUP_AUDIO;
            case 33:
                return DBConstant.MSG_TYPE_GROUP_TRANSFER;

            case 7:
                return DBConstant.MSG_TYPE_SINGLE_TEXT;
            default:
                throw new IllegalArgumentException("msgType is illegal,cause by #getProtoMsgType#" +mtype);
        }
    }

    public static int getJavaSessionType(int sessionType){
        switch (sessionType){
            case 1:
                return DBConstant.SESSION_TYPE_SINGLE;
            case 2:
                return DBConstant.SESSION_TYPE_GROUP;
            default:
                throw new IllegalArgumentException("sessionType is illegal,cause by #getProtoSessionType#" +sessionType);
        }
    }


}
