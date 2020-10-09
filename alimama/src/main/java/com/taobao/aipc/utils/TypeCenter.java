package com.taobao.aipc.utils;

import android.text.TextUtils;
import com.taobao.aipc.annotation.method.MethodName;
import com.taobao.aipc.annotation.type.ClassName;
import com.taobao.aipc.core.wrapper.BaseWrapper;
import com.taobao.aipc.core.wrapper.MethodWrapper;
import com.taobao.aipc.exception.IPCException;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class TypeCenter {
    private static volatile TypeCenter sInstance;
    private final ConcurrentHashMap<String, Class<?>> mAnnotatedClasses = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Class<?>, ConcurrentHashMap<String, Method>> mAnnotatedMethods = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Class<?>> mRawClasses = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Class<?>, ConcurrentHashMap<String, Method>> mRawMethods = new ConcurrentHashMap<>();

    private TypeCenter() {
    }

    public static TypeCenter getInstance() {
        if (sInstance == null) {
            synchronized (TypeCenter.class) {
                if (sInstance == null) {
                    sInstance = new TypeCenter();
                }
            }
        }
        return sInstance;
    }

    public void register(Class<?> cls) {
        TypeUtils.validateClass(cls);
        registerClass(cls);
        registerMethod(cls);
    }

    private void registerClass(Class<?> cls) {
        ClassName className = (ClassName) cls.getAnnotation(ClassName.class);
        if (className == null) {
            this.mRawClasses.putIfAbsent(cls.getName(), cls);
            return;
        }
        this.mAnnotatedClasses.putIfAbsent(className.value(), cls);
    }

    private void registerMethod(Class<?> cls) {
        for (Method method : cls.getDeclaredMethods()) {
            if (((MethodName) method.getAnnotation(MethodName.class)) != null || !method.isAccessible()) {
                method.setAccessible(true);
                this.mAnnotatedMethods.putIfAbsent(cls, new ConcurrentHashMap());
                this.mAnnotatedMethods.get(cls).putIfAbsent(TypeUtils.getMethodId(method), method);
            } else {
                this.mRawMethods.putIfAbsent(cls, new ConcurrentHashMap());
                this.mRawMethods.get(cls).putIfAbsent(TypeUtils.getMethodId(method), method);
            }
        }
    }

    public Method getMethod(Class<?> cls, MethodWrapper methodWrapper) throws IPCException {
        Method method;
        String name = methodWrapper.getName();
        ConcurrentHashMap concurrentHashMap = this.mAnnotatedMethods.get(cls);
        if (concurrentHashMap == null || (method = (Method) concurrentHashMap.get(name)) == null) {
            this.mRawMethods.putIfAbsent(cls, new ConcurrentHashMap());
            ConcurrentHashMap concurrentHashMap2 = this.mRawMethods.get(cls);
            Method method2 = (Method) concurrentHashMap2.get(name);
            if (method2 != null) {
                TypeUtils.methodReturnTypeMatch(method2, methodWrapper);
                return method2;
            }
            Method method3 = TypeUtils.getMethod(cls, name.substring(0, name.indexOf(40)), getClassTypes(methodWrapper.getParameterTypes()), getClassType(methodWrapper.getReturnType()));
            if (method3 != null) {
                concurrentHashMap2.put(name, method3);
                return method3;
            }
            throw new IPCException(17, "Method not found: " + name + " in class " + cls.getName());
        }
        TypeUtils.methodMatch(method, methodWrapper);
        return method;
    }

    public Class<?> getClassType(BaseWrapper baseWrapper) throws IPCException {
        Class<?> cls;
        String name = baseWrapper.getName();
        if (TextUtils.isEmpty(name)) {
            return null;
        }
        if (baseWrapper.isName()) {
            Class<?> cls2 = this.mRawClasses.get(name);
            if (cls2 != null) {
                return cls2;
            }
            char c = 65535;
            switch (name.hashCode()) {
                case -1325958191:
                    if (name.equals("double")) {
                        c = 7;
                        break;
                    }
                    break;
                case 104431:
                    if (name.equals("int")) {
                        c = 4;
                        break;
                    }
                    break;
                case 3039496:
                    if (name.equals("byte")) {
                        c = 1;
                        break;
                    }
                    break;
                case 3052374:
                    if (name.equals("char")) {
                        c = 2;
                        break;
                    }
                    break;
                case 3327612:
                    if (name.equals("long")) {
                        c = 5;
                        break;
                    }
                    break;
                case 3625364:
                    if (name.equals("void")) {
                        c = 8;
                        break;
                    }
                    break;
                case 64711720:
                    if (name.equals("boolean")) {
                        c = 0;
                        break;
                    }
                    break;
                case 97526364:
                    if (name.equals("float")) {
                        c = 6;
                        break;
                    }
                    break;
                case 109413500:
                    if (name.equals("short")) {
                        c = 3;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    cls = Boolean.TYPE;
                    break;
                case 1:
                    cls = Byte.TYPE;
                    break;
                case 2:
                    cls = Character.TYPE;
                    break;
                case 3:
                    cls = Short.TYPE;
                    break;
                case 4:
                    cls = Integer.TYPE;
                    break;
                case 5:
                    cls = Long.TYPE;
                    break;
                case 6:
                    cls = Float.TYPE;
                    break;
                case 7:
                    cls = Double.TYPE;
                    break;
                case 8:
                    cls = Void.TYPE;
                    break;
                default:
                    try {
                        cls = Class.forName(name);
                        break;
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        throw new IPCException(16, "Cannot find class " + name + ". Classes without ClassName annotation on it " + "should be located at the same package and have the same name, " + "EVEN IF the source code has been obfuscated by Proguard.");
                    }
            }
            this.mRawClasses.putIfAbsent(name, cls);
            return cls;
        }
        Class<?> cls3 = this.mAnnotatedClasses.get(name);
        if (cls3 == null) {
            try {
                cls3 = Class.forName(name);
                if (cls3 == null) {
                    throw new IPCException(16, "Cannot find class with ClassName annotation on it. ClassName = " + name + ". Please add the same annotation on the corresponding class in the remote process" + " and register it. Have you forgotten to register the class?");
                }
            } catch (ClassNotFoundException e2) {
                e2.printStackTrace();
                throw new IPCException(16, "Cannot find class " + name + ". Classes without ClassName annotation on it " + "should be located at the same package and have the same name, " + "EVEN IF the source code has been obfuscated by Proguard.");
            }
        }
        return cls3;
    }

    public Class<?>[] getClassTypes(BaseWrapper[] baseWrapperArr) throws IPCException {
        if (baseWrapperArr == null) {
            return new Class[0];
        }
        Class<?>[] clsArr = new Class[baseWrapperArr.length];
        for (int i = 0; i < baseWrapperArr.length; i++) {
            clsArr[i] = getClassType(baseWrapperArr[i]);
        }
        return clsArr;
    }
}
