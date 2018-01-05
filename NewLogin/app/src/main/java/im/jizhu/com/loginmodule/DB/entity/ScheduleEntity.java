package im.jizhu.com.loginmodule.DB.entity;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table ScheduleInfo.
 */
public class ScheduleEntity {

    private long task_id;
    private Integer dlid;
    private Integer created;
    private Integer received;
    private Integer finished;
    private Integer status;
    private Integer isstar;
    private String content;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public ScheduleEntity() {
    }

    public ScheduleEntity(long task_id) {
        this.task_id = task_id;
    }

    public ScheduleEntity(long task_id, Integer dlid, Integer created, Integer received, Integer finished, Integer status, Integer isstar, String content) {
        this.task_id = task_id;
        this.dlid = dlid;
        this.created = created;
        this.received = received;
        this.finished = finished;
        this.status = status;
        this.isstar = isstar;
        this.content = content;
    }

    public long getTask_id() {
        return task_id;
    }

    public void setTask_id(long task_id) {
        this.task_id = task_id;
    }

    public Integer getDlid() {
        return dlid;
    }

    public void setDlid(Integer dlid) {
        this.dlid = dlid;
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }

    public Integer getReceived() {
        return received;
    }

    public void setReceived(Integer received) {
        this.received = received;
    }

    public Integer getFinished() {
        return finished;
    }

    public void setFinished(Integer finished) {
        this.finished = finished;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getIsstar() {
        return isstar;
    }

    public void setIsstar(Integer isstar) {
        this.isstar = isstar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
