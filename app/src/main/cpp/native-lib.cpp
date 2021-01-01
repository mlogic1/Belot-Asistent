#include <jni.h>
#include <string>
#include <iostream>
#include <BeloteController.h>
#include <BeloteMatch.h>

Belote::BeloteController* beloteController = nullptr;

const char* jRoundDataClassPath = "com/merodyadt/belotasistent/data/RoundData";
jclass jRoundDataCls = nullptr;
jmethodID jRoundDataConstructor = nullptr;

// very nice resource on this topic
// https://www.developer.com/java/data/manipulating-java-objects-in-native-code.html


extern "C" JNIEXPORT void JNICALL
Java_com_merodyadt_belotasistent_MainActivity_InitBeloteControllerNew(JNIEnv* env, jobject /* this */, jint jtargetScore)
{
    const int targetScore = jtargetScore;
    beloteController = new Belote::BeloteController(targetScore);
}

extern "C" JNIEXPORT void JNICALL
Java_com_merodyadt_belotasistent_MainActivity_InitBeloteControllerData(JNIEnv* env, jobject /* this */, jstring data)
{
    std::string strData = env->GetStringUTFChars(data, 0);
    beloteController = new Belote::BeloteController(strData);
}

extern "C" JNIEXPORT jobject JNICALL Java_com_merodyadt_belotasistent_MainActivity_GetMatchRounds(JNIEnv* env, jobject /* this */)
{
    jRoundDataCls = env->FindClass(jRoundDataClassPath);
    jRoundDataConstructor = env->GetMethodID(jRoundDataCls, "<init>", "(III)V");

    jint A = 162, B = 182, i = 1;
    jobject jroundData = env->NewObject(jRoundDataCls, jRoundDataConstructor, i, A, B);

    const Belote::BeloteMatch& currentMatch = beloteController->GetCurrentMatch();
    const std::vector<Belote::BeloteRoundData>& matchRounds = currentMatch.GetRounds();

    for (const Belote::BeloteRoundData& roundData : matchRounds)
    {

    }

    return nullptr;
}