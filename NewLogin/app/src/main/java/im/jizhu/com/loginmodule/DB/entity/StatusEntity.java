package im.jizhu.com.loginmodule.DB.entity;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table Status.
 */
public class StatusEntity {

    private Long status_id;
    /** Not-null value. */
    private String id;
    private int type;
    private int updated;
    private int status;
    /** Not-null value. */
    private String status_text1;
    /** Not-null value. */
    private String status_text2;
    /** Not-null value. */
    private String status_text3;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public StatusEntity() {
    }

    public StatusEntity(Long status_id, String id) {
        this.status_id = status_id;
        this.id = id;
    }

    public StatusEntity(Long status_id, String id, int type, int updated, int status, String status_text1, String status_text2, String status_text3) {
        this.status_id = status_id;
        this.id = id;
        this.type = type;
        this.updated = updated;
        this.status = status;
        this.status_text1 = status_text1;
        this.status_text2 = status_text2;
        this.status_text3 = status_text3;
    }

    public Long getStatus_id() {
        return status_id;
    }

    public void setStatus_id(Long status_id) {
        this.status_id = status_id;
    }

    /** Not-null value. */
    public String getId() {
        return id;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUpdated() {
        return updated;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    /** Not-null value. */
    public String getStatus_text1() {
        return status_text1;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setStatus_text1(String status_text1) {
        this.status_text1 = status_text1;
    }

    /** Not-null value. */
    public String getStatus_text2() {
        return status_text2;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setStatus_text2(String status_text2) {
        this.status_text2 = status_text2;
    }

    /** Not-null value. */
    public String getStatus_text3() {
        return status_text3;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setStatus_text3(String status_text3) {
        this.status_text3 = status_text3;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
