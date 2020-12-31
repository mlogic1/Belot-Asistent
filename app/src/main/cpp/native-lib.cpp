#include <jni.h>
#include <string>
#include <iostream>
#include <BeloteController.h>

extern "C" JNIEXPORT jstring JNICALL
Java_com_merodyadt_belotasistent_MainActivity_stringFromJNI(JNIEnv* env, jobject /* this */)
{
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT void JNICALL
Java_com_merodyadt_belotasistent_MainActivity_InitBeloteController(JNIEnv* env, jobject /* this */)
{
    std::string hello = "Hello from C++";
	Belote::BeloteController* cntrl = new Belote::BeloteController();
    std::cout<<hello<<std::endl;
}