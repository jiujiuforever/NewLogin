package im.jizhu.com.loginmodule.imservice.network;

/**
 * 网络链接情况
 * Created by qzy on 2017/8/12.
 */
public enum NetState {
    NET_STATE_CONNECTED(0x00),  // 链接成功

    NET_STATE_CLOSED(0x01),     // 链接断开

    NET_STATE_CONNECTING(0x02), // 正在链接

    NET_STATE_INVALID_HANDLE(0x03); // 非法的链接句柄

    private int state;

    NetState(int value) {
        this.state = value;
    }

    public static NetState valueOf(int value) {
        if (value == 0) {
            return NET_STATE_CONNECTED;
        } else if (value == 1) {
            return NET_STATE_CLOSED;
        } else if (value == 2) {
            return NET_STATE_CONNECTING;
        } else {
            return NET_STATE_INVALID_HANDLE;
        }
    }

    public int getValue() {
        return state;
    }
}
