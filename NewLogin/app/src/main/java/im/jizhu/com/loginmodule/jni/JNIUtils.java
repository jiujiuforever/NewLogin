package im.jizhu.com.loginmodule.jni;

/**
 * Created by it031 on 2017/12/21.
 */

public class JNIUtils {
    static {
        System.loadLibrary("huazict");
    }

    //java调C中的方法都需要用native声明且方法名必须和c的方法名一样
    public native String getString();

}