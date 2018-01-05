package im.jizhu.com.loginmodule.imservice.event;

/**
 * Created by it026 on 2017/3/6.
 */
public class LoginDataEvent {

    private Event event;
    private String resultString;

    public LoginDataEvent(Event event,String resultString){
        this.event = event;
        this.resultString = resultString;
    }
    public enum Event{

        LOGIN_LEAVE_FAILED,
        LOGIN_VERSION_OLD
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getResultString() {
        return resultString;
    }

    public void setResultString(String resultString) {
        this.resultString = resultString;
    }
}
