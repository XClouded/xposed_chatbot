package com.ali.telescope.internal.plugins.systemcompoment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ServiceInfo;
import android.os.IBinder;
import com.ali.telescope.util.StrictRuntime;
import com.ali.user.mobile.rpc.ApiConstants;
import java.lang.reflect.Field;

public class ActivityThreadData {
    private static Class activityThreadClz;

    static {
        try {
            activityThreadClz = Class.forName("android.app.ActivityThread");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static final class ReceiverData {
        private static Field mInfoField;
        private static Field mIntentField;
        public ActivityInfo info;
        public Intent intent;

        public static ReceiverData wrap(Object obj) {
            if (obj == null) {
                return null;
            }
            ReceiverData receiverData = new ReceiverData();
            if (mIntentField == null) {
                try {
                    Class<?> cls = Class.forName("android.app.ActivityThread$ReceiverData");
                    mIntentField = cls.getDeclaredField("intent");
                    mIntentField.setAccessible(true);
                    mInfoField = cls.getDeclaredField(ApiConstants.ApiField.INFO);
                    mInfoField.setAccessible(true);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    StrictRuntime.onHandle(e);
                } catch (NoSuchFieldException e2) {
                    e2.printStackTrace();
                    StrictRuntime.onHandle(e2);
                }
            }
            if (mIntentField != null) {
                try {
                    receiverData.intent = (Intent) mIntentField.get(obj);
                    receiverData.info = (ActivityInfo) mInfoField.get(obj);
                } catch (IllegalAccessException e3) {
                    e3.printStackTrace();
                }
            }
            return receiverData;
        }
    }

    public static final class ActivityClientRecord {
        private static Field mActivityField;
        private static Field mActivityInfoField;
        private static Field mIntentField;
        public Activity activity;
        public ActivityInfo activityInfo;
        public Intent intent;

        public static ActivityClientRecord wrap(Object obj) {
            if (obj == null) {
                return null;
            }
            ActivityClientRecord activityClientRecord = new ActivityClientRecord();
            if (mIntentField == null) {
                try {
                    Class<?> cls = Class.forName("android.app.ActivityThread$ActivityClientRecord");
                    mIntentField = cls.getDeclaredField("intent");
                    mIntentField.setAccessible(true);
                    mActivityField = cls.getDeclaredField("activity");
                    mActivityField.setAccessible(true);
                    mActivityInfoField = cls.getDeclaredField("activityInfo");
                    mActivityInfoField.setAccessible(true);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    StrictRuntime.onHandle(e);
                } catch (NoSuchFieldException e2) {
                    e2.printStackTrace();
                    StrictRuntime.onHandle(e2);
                }
            }
            if (mIntentField != null) {
                try {
                    activityClientRecord.intent = (Intent) mIntentField.get(obj);
                    activityClientRecord.activity = (Activity) mActivityField.get(obj);
                    activityClientRecord.activityInfo = (ActivityInfo) mActivityInfoField.get(obj);
                } catch (IllegalAccessException e3) {
                    e3.printStackTrace();
                }
            }
            return activityClientRecord;
        }
    }

    public static final class CreateServiceData {
        private static Field mInfoField;
        private static Field mIntentField;
        private static Field mTokenField;
        public ServiceInfo info;
        public Intent intent;
        public IBinder token;

        public static CreateServiceData wrap(Object obj) {
            if (obj == null) {
                return null;
            }
            CreateServiceData createServiceData = new CreateServiceData();
            if (mIntentField == null) {
                try {
                    Class<?> cls = Class.forName("android.app.ActivityThread$CreateServiceData");
                    mIntentField = cls.getDeclaredField("intent");
                    mIntentField.setAccessible(true);
                    mTokenField = cls.getDeclaredField("token");
                    mTokenField.setAccessible(true);
                    mInfoField = cls.getDeclaredField(ApiConstants.ApiField.INFO);
                    mInfoField.setAccessible(true);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    StrictRuntime.onHandle(e);
                } catch (NoSuchFieldException e2) {
                    e2.printStackTrace();
                    StrictRuntime.onHandle(e2);
                }
            }
            if (mIntentField != null) {
                try {
                    createServiceData.token = (IBinder) mTokenField.get(obj);
                    createServiceData.info = (ServiceInfo) mInfoField.get(obj);
                    createServiceData.intent = (Intent) mIntentField.get(obj);
                } catch (IllegalAccessException e3) {
                    e3.printStackTrace();
                }
            }
            return createServiceData;
        }
    }

    public static final class BindServiceData {
        private static Field mIntentField;
        private static Field mTokenField;
        public Intent intent;
        public IBinder token;

        public static BindServiceData wrap(Object obj) {
            if (obj == null) {
                return null;
            }
            BindServiceData bindServiceData = new BindServiceData();
            if (mIntentField == null) {
                try {
                    Class<?> cls = Class.forName("android.app.ActivityThread$BindServiceData");
                    mIntentField = cls.getDeclaredField("intent");
                    mIntentField.setAccessible(true);
                    mTokenField = cls.getDeclaredField("token");
                    mTokenField.setAccessible(true);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    StrictRuntime.onHandle(e);
                } catch (NoSuchFieldException e2) {
                    e2.printStackTrace();
                    StrictRuntime.onHandle(e2);
                }
            }
            if (mIntentField != null) {
                try {
                    bindServiceData.token = (IBinder) mTokenField.get(obj);
                    bindServiceData.intent = (Intent) mIntentField.get(obj);
                } catch (IllegalAccessException e3) {
                    e3.printStackTrace();
                }
            }
            return bindServiceData;
        }
    }

    public static final class ServiceArgsData {
        private static Field mArgsField;
        private static Field mTokenField;
        public Intent args;
        public IBinder token;

        public static ServiceArgsData wrap(Object obj) {
            if (obj == null) {
                return null;
            }
            ServiceArgsData serviceArgsData = new ServiceArgsData();
            if (mTokenField == null) {
                try {
                    Class<?> cls = Class.forName("android.app.ActivityThread$ServiceArgsData");
                    mTokenField = cls.getDeclaredField("token");
                    mTokenField.setAccessible(true);
                    mArgsField = cls.getDeclaredField("args");
                    mArgsField.setAccessible(true);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    StrictRuntime.onHandle(e);
                } catch (NoSuchFieldException e2) {
                    e2.printStackTrace();
                    StrictRuntime.onHandle(e2);
                }
            }
            if (mTokenField != null) {
                try {
                    serviceArgsData.token = (IBinder) mTokenField.get(obj);
                    serviceArgsData.args = (Intent) mArgsField.get(obj);
                } catch (IllegalAccessException e3) {
                    e3.printStackTrace();
                }
            }
            return serviceArgsData;
        }
    }
}
