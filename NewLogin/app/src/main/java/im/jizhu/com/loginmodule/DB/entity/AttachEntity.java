package im.jizhu.com.loginmodule.DB.entity;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table Attach.
 */
public class AttachEntity {

    private Long attach_id;
    /** Not-null value. */
    private String voice;
    /** Not-null value. */
    private String photo;
    /** Not-null value. */
    private String file;
    /** Not-null value. */
    private String url;
    /** Not-null value. */
    private String capacity;
    /** Not-null value. */
    private String attach_text1;
    /** Not-null value. */
    private String attach_text2;
    /** Not-null value. */
    private String attach_text3;
    /** Not-null value. */
    private String status;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public AttachEntity() {
    }

    public AttachEntity(Long attach_id) {
        this.attach_id = attach_id;
    }

    public AttachEntity(Long attach_id, String voice, String photo, String file, String url, String capacity, String attach_text1, String attach_text2, String attach_text3, String status) {
        this.attach_id = attach_id;
        this.voice = voice;
        this.photo = photo;
        this.file = file;
        this.url = url;
        this.capacity = capacity;
        this.attach_text1 = attach_text1;
        this.attach_text2 = attach_text2;
        this.attach_text3 = attach_text3;
        this.status = status;
    }

    public Long getAttach_id() {
        return attach_id;
    }

    public void setAttach_id(Long attach_id) {
        this.attach_id = attach_id;
    }

    /** Not-null value. */
    public String getVoice() {
        return voice;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setVoice(String voice) {
        this.voice = voice;
    }

    /** Not-null value. */
    public String getPhoto() {
        return photo;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    /** Not-null value. */
    public String getFile() {
        return file;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setFile(String file) {
        this.file = file;
    }

    /** Not-null value. */
    public String getUrl() {
        return url;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUrl(String url) {
        this.url = url;
    }

    /** Not-null value. */
    public String getCapacity() {
        return capacity;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    /** Not-null value. */
    public String getAttach_text1() {
        return attach_text1;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setAttach_text1(String attach_text1) {
        this.attach_text1 = attach_text1;
    }

    /** Not-null value. */
    public String getAttach_text2() {
        return attach_text2;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setAttach_text2(String attach_text2) {
        this.attach_text2 = attach_text2;
    }

    /** Not-null value. */
    public String getAttach_text3() {
        return attach_text3;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setAttach_text3(String attach_text3) {
        this.attach_text3 = attach_text3;
    }

    /** Not-null value. */
    public String getStatus() {
        return status;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setStatus(String status) {
        this.status = status;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}