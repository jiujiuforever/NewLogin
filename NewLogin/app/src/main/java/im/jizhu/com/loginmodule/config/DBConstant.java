package im.jizhu.com.loginmodule.config;

/**
 * @author : yingmu on 15-1-5.
 * @email : yingmu@mogujie.com.
 *
 */
public interface DBConstant {

    /**性别
     * 1. 男性 2.女性
     * */
    public final int SEX_MAILE = 1;
    public final int SEX_FEMALE = 2;

    /**sessionType*/
    public final int  SESSION_TYPE_SINGLE = 1;
    public final int  SESSION_TYPE_GROUP = 2;
    public final int SESSION_TYPE_ERROR= 3;

    /**msgType*/
    public final int  MSG_TYPE_SINGLE_TEXT = 0x01;
    public final int  MSG_TYPE_SINGLE_AUDIO = 0x02;
    public final int  MSG_TYPE_GROUP_TEXT = 0x11;
    public final int  MSG_TYPE_GROUP_AUDIO = 0x12;
    public final int  MSG_TYPE_GROUP_TRANSFER = 0x21;//群主转让类型


    /**msgDisplayType
     * 保存在DB中，与服务端一致，图文混排也是一条
     * 1. 最基础的文本信息
     * 2. 纯图片信息
     * 3. 语音
     * 4. 图文混排
     * */
    public final int SHOW_ORIGIN_TEXT_TYPE = 1;
    public final int  SHOW_IMAGE_TYPE = 2;
    public final int  SHOW_AUDIO_TYPE = 3;
    public final int  SHOW_MIX_TEXT = 4;
    public final int  SHOW_GIF_TYPE = 5;
    public final int  SHOW_FILE_TYPE = 6;
    /**群主转让*/
    public final int  SHOW_TYPE_GROUP_TRANSFER = 7;
    /**未读消息*/
    public final int  SHOW_UNREAD_TYPE = 8;

    public final String DISPLAY_FOR_IMAGE = "[图片]";
    public final String DISPLAY_FOR_MIX = "[图文消息]";
    public final String DISPLAY_FOR_AUDIO = "[语音]";
    public final String DISPLAY_FOR_ERROR = "[未知消息]";
    public final String DISPLAY_FOR_FILE = "[文件]";
    public final String DISPLAY_FOR_LOCATION = "[位置]";

}
