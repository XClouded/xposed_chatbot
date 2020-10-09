package com.ali.telescope.internal.plugins.systemcompoment;

import com.ali.telescope.util.StrictRuntime;
import com.ali.user.mobile.rpc.ApiConstants;

public class MessageConstants {
    public static int BIND_SERVICE = 0;
    public static int CALL_BACK_SERVICE_CONNECTION = (DYNAMICAL_RECEIVER + 1);
    public static int CREATE_SERVICE = 0;
    public static int DESTROY_ACTIVITY = 0;
    public static int DYNAMICAL_RECEIVER = 17767;
    public static int LAUNCH_ACTIVITY;
    public static int PAUSE_ACTIVITY;
    public static int PAUSE_ACTIVITY_FINISHING;
    public static int RECEIVER;
    public static int RESUME_ACTIVITY;
    public static int SERVICE_ARGS;
    public static int STOP_ACTIVITY_HIDE;
    public static int STOP_ACTIVITY_SHOW;
    public static int STOP_SERVICE;
    public static int UNBIND_SERVICE;

    static {
        try {
            Class<?> cls = Class.forName("android.app.ActivityThread$H");
            LAUNCH_ACTIVITY = loadValue(cls, "LAUNCH_ACTIVITY");
            PAUSE_ACTIVITY = loadValue(cls, "PAUSE_ACTIVITY");
            PAUSE_ACTIVITY_FINISHING = loadValue(cls, "PAUSE_ACTIVITY_FINISHING");
            STOP_ACTIVITY_SHOW = loadValue(cls, "STOP_ACTIVITY_SHOW");
            STOP_ACTIVITY_HIDE = loadValue(cls, "STOP_ACTIVITY_HIDE");
            RESUME_ACTIVITY = loadValue(cls, "RESUME_ACTIVITY");
            DESTROY_ACTIVITY = loadValue(cls, "DESTROY_ACTIVITY");
            RECEIVER = loadValue(cls, "RECEIVER");
            CREATE_SERVICE = loadValue(cls, "CREATE_SERVICE");
            SERVICE_ARGS = loadValue(cls, "SERVICE_ARGS");
            BIND_SERVICE = loadValue(cls, "BIND_SERVICE");
            UNBIND_SERVICE = loadValue(cls, "UNBIND_SERVICE");
            STOP_SERVICE = loadValue(cls, "STOP_SERVICE");
        } catch (Exception e) {
            StrictRuntime.onHandle(e);
        }
    }

    private static int loadValue(Class cls, String str) {
        try {
            return ((Integer) cls.getDeclaredField(str).get((Object) null)).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String readableString(int i) {
        if (i == LAUNCH_ACTIVITY) {
            return "LAUNCH_ACTIVITY";
        }
        if (i == PAUSE_ACTIVITY) {
            return "PAUSE_ACTIVITY";
        }
        if (i == PAUSE_ACTIVITY_FINISHING) {
            return "PAUSE_ACTIVITY_FINISHING";
        }
        if (i == STOP_ACTIVITY_SHOW) {
            return "STOP_ACTIVITY_SHOW";
        }
        if (i == STOP_ACTIVITY_HIDE) {
            return "STOP_ACTIVITY_HIDE";
        }
        if (i == RESUME_ACTIVITY) {
            return "RESUME_ACTIVITY";
        }
        if (i == DESTROY_ACTIVITY) {
            return "DESTROY_ACTIVITY";
        }
        if (i == RECEIVER) {
            return "RECEIVER";
        }
        if (i == CREATE_SERVICE) {
            return "CREATE_SERVICE";
        }
        if (i == SERVICE_ARGS) {
            return "SERVICE_ARGS";
        }
        if (i == BIND_SERVICE) {
            return "BIND_SERVICE";
        }
        if (i == UNBIND_SERVICE) {
            return "UNBIND_SERVICE";
        }
        if (i == STOP_SERVICE) {
            return "STOP_SERVICE";
        }
        if (i == DYNAMICAL_RECEIVER) {
            return "DYNAMICAL_RECEIVE";
        }
        return i == CALL_BACK_SERVICE_CONNECTION ? "CALL_BACK_SERVICE_CONNECTION" : ApiConstants.ResultActionType.OTHER;
    }
}
