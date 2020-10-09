package com.taobao.android;

import java.lang.reflect.Method;

public class AliImageServiceFetcher {
    private static AliImageServiceInterface _serviceIMP;

    private static <T> T getDefaultServiceIMP(Class<T> cls) {
        String str;
        String name = cls.getName();
        if (name.endsWith("Interface")) {
            str = name.replace("Interface", "Imp");
        } else {
            str = name + "Imp";
        }
        try {
            Class<?> cls2 = Class.forName(str);
            try {
                Method declaredMethod = cls2.getDeclaredMethod("getInstance", new Class[0]);
                if (declaredMethod != null) {
                    return declaredMethod.invoke((Object) null, new Object[0]);
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    return cls2.newInstance();
                } catch (Exception unused) {
                    e.printStackTrace();
                    return null;
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static <T> T getAliInfrastructureService(Class<T> cls) {
        if (_serviceIMP != null) {
            return _serviceIMP;
        }
        T defaultServiceIMP = getDefaultServiceIMP(cls);
        if (defaultServiceIMP != null) {
            _serviceIMP = (AliImageServiceInterface) defaultServiceIMP;
        }
        return defaultServiceIMP;
    }

    public static AliImageServiceInterface getImageService() {
        Object aliInfrastructureService = getAliInfrastructureService(AliImageServiceInterface.class);
        if (aliInfrastructureService instanceof AliImageServiceInterface) {
            return (AliImageServiceInterface) aliInfrastructureService;
        }
        return null;
    }

    public static void setImageService(AliImageServiceInterface aliImageServiceInterface) {
        _serviceIMP = aliImageServiceInterface;
    }
}
