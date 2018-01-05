package im.jizhu.com.loginmodule.imservice.event;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import im.jizhu.com.loginmodule.DB.entity.UserEntity;

/**
 * Created by it026 on 2017/7/1.
 */
public class UserEntityEvent {

    private List<UserEntity> userEntityList ;
    private Event Event;
    private int DeptUserCount;
    private int Deptid;
    private String errReturn;

    //统计部门下的人员数目
    public Map<String,Integer> getDeptUserCount() {
        Map<String,Integer> map = new HashMap<String,Integer>();
        if (Deptid != 0)
        map.put("Deptid",Deptid);
        map.put("DeptUserCount",DeptUserCount);
        return map;
    }

    public void setDeptUserCount(int deptid, int deptUserCount) {
        DeptUserCount = deptUserCount;
        Deptid = deptid;
    }

    public enum Event {

        USER_DATA_LIST,
        USER_DATA_NULL,
        USER_DEPT_LIST,
        USER_DEPT_NULL,
        //访问服务器部门下的成员数目返回结果
        DEPT_USER_LIST,
        DEPT_USER_NULL,
        //http请求失败处理
        HTTP_REQ_ERR,

    }

    public List<UserEntity> getUserEntityList() {
        return userEntityList;
    }

    public void setUserEntityList(List<UserEntity> userEntityList) {
        this.userEntityList = userEntityList;
    }

    public String getErrReturn() {
        return errReturn;
    }

    public void setErrReturn(String errReturn) {
        this.errReturn = errReturn;
    }

    public UserEntityEvent.Event getEvent() {
        return Event;
    }

    public void setEvent(UserEntityEvent.Event event) {
        Event = event;
    }
}
