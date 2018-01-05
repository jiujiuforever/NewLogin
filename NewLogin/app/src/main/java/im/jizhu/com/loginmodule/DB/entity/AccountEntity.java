package im.jizhu.com.loginmodule.DB.entity;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table Account.
 */
public class AccountEntity {

    private Long account_id;
    private int id;
    private int userid;
    private int type;
    /** Not-null value. */
    private String value;
    /** Not-null value. */
    private String account_text1;
    /** Not-null value. */
    private String account_text2;
    /** Not-null value. */
    private String account_text3;
    /** Not-null value. */
    private String status;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public AccountEntity() {
    }

    public AccountEntity(Long account_id) {
        this.account_id = account_id;
    }

    public AccountEntity(Long account_id, int id, int userid, int type, String value, String account_text1, String account_text2, String account_text3, String status) {
        this.account_id = account_id;
        this.id = id;
        this.userid = userid;
        this.type = type;
        this.value = value;
        this.account_text1 = account_text1;
        this.account_text2 = account_text2;
        this.account_text3 = account_text3;
        this.status = status;
    }

    public Long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(Long account_id) {
        this.account_id = account_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /** Not-null value. */
    public String getValue() {
        return value;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setValue(String value) {
        this.value = value;
    }

    /** Not-null value. */
    public String getAccount_text1() {
        return account_text1;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setAccount_text1(String account_text1) {
        this.account_text1 = account_text1;
    }

    /** Not-null value. */
    public String getAccount_text2() {
        return account_text2;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setAccount_text2(String account_text2) {
        this.account_text2 = account_text2;
    }

    /** Not-null value. */
    public String getAccount_text3() {
        return account_text3;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setAccount_text3(String account_text3) {
        this.account_text3 = account_text3;
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
