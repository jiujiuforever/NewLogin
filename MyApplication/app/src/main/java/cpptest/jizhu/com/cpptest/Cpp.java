package cpptest.jizhu.com.cpptest;

/**
 * Created by it031 on 2017/12/21.
 */

public class Cpp {
    //加载jni

    public Cpp() {
    }

    static {
        System.loadLibrary("nativeTest");
    }
    //声明native方法
    public native int sayhello(int t);
    public native String returnString();
}
