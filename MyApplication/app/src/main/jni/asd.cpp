//
// Created by it031 on 2017/12/22.
//

#include "cpptest_jizhu_com_cpptest_Cpp.h"
/**
 * 上边的引用标签一定是.h的文件名家后缀，方法名一定要和.h文件中的方法名称一样
 */
JNIEXPORT jstring JNICALL Java_cpptest_jizhu_com_cpptest_Cpp_returnString
        (JNIEnv *env, jobject obj) {
    return (*env)->NewStringUTF(env, "这是我测试的jni");
}