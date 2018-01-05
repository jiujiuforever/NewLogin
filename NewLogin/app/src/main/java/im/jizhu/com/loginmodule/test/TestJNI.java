package im.jizhu.com.loginmodule.test;

/**
 * Created by it031 on 2017/12/7.
 */

public class TestJNI {
    public native boolean Init();
    public native int Add(int x, int y);
    public native void Destory();
}
