#include <jni.h>
#include <android/log.h>

#define LOG_TAG "HamsterCore"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

bool g_AimBotEnabled = false;
bool g_EspEnabled = false;

extern "C" JNIEXPORT void JNICALL
Java_com_hamster_modmenu_ModMenuService_toggleFeatureNative(JNIEnv *env, jclass clazz, jint featureId, jboolean enable) {
    if (featureId == 1) {
        g_AimBotEnabled = enable;
        LOGI("AimBot status changed: %d", enable);
    } else if (featureId == 2) {
        g_EspEnabled = enable;
        LOGI("ESP status changed: %d", enable);
    }
}
