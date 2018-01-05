package im.jizhu.com.loginmodule.config;

/**
 * @author : yingmu on 15-1-11.
 * @email : yingmu@mogujie.com.
 */
public interface MessageConstant {

    /**基础消息状态，表示网络层收发成功*/
    public final int   MSG_SENDING = 1;
    public final int    MSG_FAILURE = 2;
    public final int    MSG_SUCCESS = 3;

    /**图片消息状态，表示下载到本地、上传到服务器的状态*/
    public final int  IMAGE_UNLOAD=1;
    public final int  IMAGE_LOADING=2;
    public final int IMAGE_LOADED_SUCCESS =3;
    public final int IMAGE_LOADED_FAILURE =4;

    /**文件消息状态，表示下载到本地、上传到服务器的状态*/
    public final int  FILE_UNLOAD=11;
    public final int  FILE_LOADING=12;
    public final int FLIE_LOADED_SUCCESS =13;
    public final int FILE_LOADED_FAILURE =14;


    /**语音状态，未读与已读*/
    public final int   AUDIO_UNREAD =1;
    public final int   AUDIO_READED = 2;

    /**图片消息的前后常量*/
    public  final String IMAGE_MSG_START = "&$#@~^@[{:";
    public  final String IMAGE_MSG_END = ":}]&$~@#@";

    /** 公告消息常量格式 */
    public  final String NOTE_MSG_FLAG = "msgtype:7";

    /**消息@的前后常量*/
    public  final String TEXT_MSG_START = "@!$*&*(";
    public  final String TEXT_MSG_END = ")*&*$!@";

    public  final String LOCATION_MSG_FLAG = "%LO%}CA%TI!ON!";//位置

    /**PC端以A标签复杂消息格式*/
    public final String BEG_ALINK_FLAG = "[%A%Lin%!{";
    public final String END_ALINK_FLAG = "}%A%Lin%!]";

    /**PC端html标签处理*/
    public final String TEXT_HTML_BEG = "&lt;";
    public final String TEXT_HTML_END = "&gt;";
    public final String TEXT_HTML_SEMI = "&quot";

}
