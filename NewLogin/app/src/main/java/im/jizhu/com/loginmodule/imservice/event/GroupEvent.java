package im.jizhu.com.loginmodule.imservice.event;




import java.util.List;

import im.jizhu.com.loginmodule.DB.entity.GroupEntity;
import im.jizhu.com.loginmodule.DB.entity.UserEntity;

/**
 * @author : yingmu on 14-12-30.
 * @email : yingmu@mogujie.com.
 */
public class GroupEvent {

    private List<UserEntity> userEntityList ;

    private GroupEntity groupEntity;
    private Event event;

    /**很多的场景只是关心改变的类型以及change的Ids*/
    private int changeType;
    private List<Integer> changeList;
    private int groupId;

    public GroupEvent(Event event){
        this.event = event;
    }

    public GroupEvent(Event event,GroupEntity groupEntity){
        this.groupEntity = groupEntity;
        this.event = event;
    }

    public GroupEvent(Event event,int groupId){
        this.event = event;
        this.groupId = groupId;
    }

    public enum Event{
        NONE,

        GROUP_INFO_OK,
        GROUP_INFO_UPDATED,
        GROUP_INFO_DELETE,//删除群组
        GROUP_OWNER_TRANSFER,//群主转让

        CHANGE_GROUP_MEMBER_SUCCESS,
        CHANGE_GROUP_MEMBER_FAIL,
        CHANGE_GROUP_MEMBER_TIMEOUT,

        CREATE_GROUP_OK,
        CREATE_GROUP_FAIL,
        CREATE_GROUP_TIMEOUT,

        SHIELD_GROUP_OK,
        SHIELD_GROUP_TIMEOUT,
        SHIELD_GROUP_FAIL,

        USER_DATA_LIST,
        USER_DATA_NULL

    }

    public List<UserEntity> getUserEntityList() {
        return userEntityList;
    }

    public void setUserEntityList(List<UserEntity> userEntityList) {
        this.userEntityList = userEntityList;
    }


    public int getChangeType() {
        return changeType;
    }

    public void setChangeType(int changeType) {
        this.changeType = changeType;
    }

    public List<Integer> getChangeList() {
        return changeList;
    }

    public void setChangeList(List<Integer> changeList) {
        this.changeList = changeList;
    }

    public GroupEntity getGroupEntity() {
        return groupEntity;
    }
    public void setGroupEntity(GroupEntity groupEntity) {
        this.groupEntity = groupEntity;
    }

    public Event getEvent() {
        return event;
    }
    public void setEvent(Event event) {
        this.event = event;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
