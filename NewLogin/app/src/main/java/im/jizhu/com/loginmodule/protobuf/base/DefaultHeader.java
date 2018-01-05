package im.jizhu.com.loginmodule.protobuf.base;


import im.jizhu.com.loginmodule.config.SysConstant;
import im.jizhu.com.loginmodule.support.SequenceNumberMaker;
import im.jizhu.com.loginmodule.utils.Logger;

public class DefaultHeader extends Header {

    private static final String TAG = "DefaultHeader";

    public DefaultHeader(int serviceId, int commandId) {
        setVersion((short) SysConstant.PROTOCOL_VERSION);
        setFlag((short) SysConstant.PROTOCOL_FLAG);
        setServiceId((short)serviceId);
        setCommandId((short)commandId);
        short seqNo = SequenceNumberMaker.getInstance().make();
        setSeqnum(seqNo);
        setReserved((short)SysConstant.PROTOCOL_RESERVED);

        Logger.d(TAG, "packet#construct Default Header -> serviceId:%d, commandId:%d, seqNo:%d", serviceId, commandId, seqNo);
    }
}
