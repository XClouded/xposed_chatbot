package com.taobao.aipc.utils;

import android.app.Activity;
import android.app.Application;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import com.taobao.aipc.annotation.WithinProcess;
import com.taobao.aipc.annotation.method.MethodName;
import com.taobao.aipc.annotation.method.Singleton;
import com.taobao.aipc.annotation.type.ClassName;
import com.taobao.aipc.core.wrapper.MethodWrapper;
import com.taobao.aipc.core.wrapper.ParameterWrapper;
import com.taobao.aipc.exception.IPCException;
import com.taobao.weex.el.parse.Operators;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;

public final class TypeUtils {
    private static final HashSet<Class<?>> CONTEXT_CLASSES = new HashSet<Class<?>>() {
        {
            add(Context.class);
            add(Activity.class);
            add(AppCompatActivity.class);
            add(Application.class);
            add(FragmentActivity.class);
            add(IntentService.class);
            add(Service.class);
        }
    };

    private TypeUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static String getClassId(Class<?> cls) {
        ClassName className = (ClassName) cls.getAnnotation(ClassName.class);
        if (className != null) {
            return className.value();
        }
        return cls.getName();
    }

    public static String getMethodId(Method method) {
        MethodName methodName = (MethodName) method.getAnnotation(MethodName.class);
        if (methodName != null) {
            return methodName.value() + '(' + getMethodParameters(method.getParameterTypes()) + ')';
        }
        return method.getName() + '(' + getMethodParameters(method.getParameterTypes()) + ')';
    }

    private static String getClassName(Class<?> cls) {
        if (cls == Boolean.class) {
            return "boolean";
        }
        if (cls == Byte.class) {
            return "byte";
        }
        if (cls == Character.class) {
            return "char";
        }
        if (cls == Short.class) {
            return "short";
        }
        if (cls == Integer.class) {
            return "int";
        }
        if (cls == Long.class) {
            return "long";
        }
        if (cls == Float.class) {
            return "float";
        }
        if (cls == Double.class) {
            return "double";
        }
        if (cls == Void.class) {
            return "void";
        }
        return cls.getName();
    }

    public static String getMethodParameters(Class<?>[] clsArr) {
        StringBuilder sb = new StringBuilder();
        int length = clsArr.length;
        if (length == 0) {
            return sb.toString();
        }
        sb.append(getClassName(clsArr[0]));
        for (int i = 1; i < length; i++) {
            sb.append(",");
            sb.append(getClassName(clsArr[i]));
        }
        return sb.toString();
    }

    public static Method getMethod(Class<?> cls, String str, Class<?>[] clsArr, Class<?> cls2) throws IPCException {
        Method method = null;
        for (Method method2 : cls.getMethods()) {
            if (method2.getName().equals(str) && classAssignable(method2.getParameterTypes(), clsArr)) {
                if (method == null) {
                    method = method2;
                } else {
                    throw new IPCException(8, "There are more than one method named " + str + " of the class " + cls.getName() + " matching the parameters!");
                }
            }
        }
        if (method == null) {
            return null;
        }
        if (method.getReturnType() == cls2) {
            return method;
        }
        throw new IPCException(10, "The method named " + str + " of the class " + cls.getName() + " matches the parameter types but not the return type. The return type is " + method.getReturnType().getName() + " but the required type is " + cls2.getName() + ". The method in the local interface must exactly " + "match the method in the remote class.");
    }

    public static Method getMethodForGettingInstance(Class<?> cls, String str, Class<?>[] clsArr) throws IPCException {
        Method method = null;
        for (Method method2 : cls.getMethods()) {
            String name = method2.getName();
            if (((str.equals("") && (name.equals("getInstance") || method2.isAnnotationPresent(Singleton.class))) || (!str.equals("") && name.equals(str))) && classAssignable(method2.getParameterTypes(), clsArr)) {
                if (method == null) {
                    method = method2;
                } else {
                    throw new IPCException(11, "When getting instance, there are more than one method named " + str + " of the class " + cls.getName() + " matching the parameters!");
                }
            }
        }
        if (method == null) {
            throw new IPCException(13, "When getting instance, the method named " + str + " of the class " + cls.getName() + " is not found. The class must have a method for getting instance.");
        } else if (method.getReturnType() == cls) {
            return method;
        } else {
            throw new IPCException(12, "When getting instance, the method named " + str + " of the class " + cls.getName() + " matches the parameter types but not the return type. The return type is " + method.getReturnType().getName() + " but the required type is " + cls.getName() + ".");
        }
    }

    public static Constructor<?> getConstructor(Class<?> cls, Class<?>[] clsArr) throws IPCException {
        Constructor<?> constructor = null;
        for (Constructor<?> constructor2 : cls.getConstructors()) {
            if (classAssignable(constructor2.getParameterTypes(), clsArr)) {
                if (constructor == null) {
                    constructor = constructor2;
                } else {
                    throw new IPCException(14, "The class " + cls.getName() + " has too many constructors whose " + " parameter types match the required types.");
                }
            }
        }
        if (constructor != null) {
            return constructor;
        }
        throw new IPCException(15, "The class " + cls.getName() + " do not have a constructor whose " + " parameter types match the required types.");
    }

    public static ParameterWrapper[] objectToWrapper(Object[] objArr) throws IPCException {
        int i = 0;
        if (objArr == null) {
            objArr = new Object[0];
        }
        int length = objArr.length;
        ParameterWrapper[] parameterWrapperArr = new ParameterWrapper[length];
        while (i < length) {
            try {
                parameterWrapperArr[i] = new ParameterWrapper(objArr[i]);
                i++;
            } catch (IPCException e) {
                e.printStackTrace();
                int errorCode = e.getErrorCode();
                throw new IPCException(errorCode, "Error happens at parameter encoding, and parameter index is " + i + ". See the stack trace for more information.", e);
            }
        }
        return parameterWrapperArr;
    }

    public static void validateClass(Class<?> cls) {
        if (cls == null) {
            throw new IllegalArgumentException("Class object is null.");
        } else if (!cls.isPrimitive() && !cls.isInterface() && !cls.getName().startsWith(Operators.ARRAY_START_STR)) {
            if (cls.isAnnotationPresent(WithinProcess.class)) {
                throw new IllegalArgumentException("Error occurs when registering class " + cls.getName() + ". Class with a WithinProcess annotation presented on it cannot be accessed" + " from outside the process.");
            } else if (cls.isAnonymousClass()) {
                throw new IllegalArgumentException("Error occurs when registering class " + cls.getName() + ". Anonymous class cannot be accessed from outside the process.");
            } else if (cls.isLocalClass()) {
                throw new IllegalArgumentException("Error occurs when registering class " + cls.getName() + ". Local class cannot be accessed from outside the process.");
            } else if (!Context.class.isAssignableFrom(cls) && Modifier.isAbstract(cls.getModifiers())) {
                throw new IllegalArgumentException("Error occurs when registering class " + cls.getName() + ". Abstract class cannot be accessed from outside the process.");
            }
        }
    }

    public static void validateServiceInterface(Class<?> cls) {
        if (cls == null) {
            throw new IllegalArgumentException("Class object is null.");
        } else if (!cls.isInterface()) {
            throw new IllegalArgumentException("Only interfaces can be passed as the parameters.");
        }
    }

    public static boolean arrayContainsAnnotation(Annotation[] annotationArr, Class<? extends Annotation> cls) {
        if (annotationArr == null || cls == null) {
            return false;
        }
        for (Annotation isInstance : annotationArr) {
            if (cls.isInstance(isInstance)) {
                return true;
            }
        }
        return false;
    }

    public static Class<?> getContextClass(Class<?> cls) {
        while (true) {
            Class<? super Object> cls2 = cls;
            if (cls2 == Object.class) {
                throw new IllegalArgumentException("can not find context class!");
            } else if (CONTEXT_CLASSES.contains(cls2)) {
                return cls2;
            } else {
                cls2 = cls2.getSuperclass();
            }
        }
    }

    public static void validateAccessible(Class<?> cls) throws IPCException {
        if (cls.isAnnotationPresent(WithinProcess.class)) {
            throw new IPCException(19, "Class " + cls.getName() + " has a WithProcess annotation on it, " + "so it cannot be accessed from outside the process.");
        }
    }

    public static void validateAccessible(Method method) throws IPCException {
        if (method.isAnnotationPresent(WithinProcess.class)) {
            throw new IPCException(20, "Method " + method.getName() + " of class " + method.getDeclaringClass().getName() + " has a WithProcess annotation on it, so it cannot be accessed from " + "outside the process.");
        }
    }

    public static void validateAccessible(Constructor<?> constructor) throws IPCException {
        if (constructor.isAnnotationPresent(WithinProcess.class)) {
            throw new IPCException(20, "Constructor " + constructor.getName() + " of class " + constructor.getDeclaringClass().getName() + " has a WithProcess annotation on it, so it cannot be accessed from " + "outside the process.");
        }
    }

    public static void methodParameterTypeMatch(Method method, MethodWrapper methodWrapper) throws IPCException {
        Class[] classTypes = TypeCenter.getInstance().getClassTypes(methodWrapper.getParameterTypes());
        Class[] parameterTypes = method.getParameterTypes();
        if (classTypes.length == parameterTypes.length) {
            int length = classTypes.length;
            for (int i = 0; i < length; i++) {
                if (classTypes[i].isPrimitive() || parameterTypes[i].isPrimitive()) {
                    if (!primitiveMatch(classTypes[i], parameterTypes[i])) {
                        throw new IPCException(9, "The parameter type of method " + method + " do not match at index " + i + ".");
                    }
                } else if (classTypes[i] != parameterTypes[i] && !primitiveMatch(classTypes[i], parameterTypes[i])) {
                    throw new IPCException(9, "The parameter type of method " + method + " do not match at index " + i + ".");
                }
            }
            return;
        }
        throw new IPCException(9, "The number of method parameters do not match. Method " + method + " has " + parameterTypes.length + " parameters. " + "The required method has " + classTypes.length + " parameters.");
    }

    public static void methodReturnTypeMatch(Method method, MethodWrapper methodWrapper) throws IPCException {
        Class<?> returnType = method.getReturnType();
        Class<?> classType = TypeCenter.getInstance().getClassType(methodWrapper.getReturnType());
        if (returnType.isPrimitive() || classType.isPrimitive()) {
            if (!primitiveMatch(returnType, classType)) {
                throw new IPCException(10, "The return type of methods do not match. Method " + method + " return type: " + returnType.getName() + ". The required is " + classType.getName());
            }
        } else if (classType != returnType && !primitiveMatch(returnType, classType)) {
            throw new IPCException(10, "The return type of methods do not match. Method " + method + " return type: " + returnType.getName() + ". The required is " + classType.getName());
        }
    }

    public static void methodMatch(Method method, MethodWrapper methodWrapper) throws IPCException {
        methodParameterTypeMatch(method, methodWrapper);
        methodReturnTypeMatch(method, methodWrapper);
    }

    public static boolean primitiveMatch(Class<?> cls, Class<?> cls2) {
        if (!cls.isPrimitive() && !cls2.isPrimitive()) {
            return false;
        }
        if (cls == cls2) {
            return true;
        }
        if (cls.isPrimitive()) {
            return primitiveMatch(cls2, cls);
        }
        if (cls == Boolean.class && cls2 == Boolean.TYPE) {
            return true;
        }
        if (cls == Byte.class && cls2 == Byte.TYPE) {
            return true;
        }
        if (cls == Character.class && cls2 == Character.TYPE) {
            return true;
        }
        if (cls == Short.class && cls2 == Short.TYPE) {
            return true;
        }
        if (cls == Integer.class && cls2 == Integer.TYPE) {
            return true;
        }
        if (cls == Long.class && cls2 == Long.TYPE) {
            return true;
        }
        if (cls == Float.class && cls2 == Float.TYPE) {
            return true;
        }
        if (cls == Double.class && cls2 == Double.TYPE) {
            return true;
        }
        if (cls == Void.class && cls2 == Void.TYPE) {
            return true;
        }
        return false;
    }

    public static boolean classAssignable(Class<?>[] clsArr, Class<?>[] clsArr2) {
        if (clsArr.length != clsArr2.length) {
            return false;
        }
        int length = clsArr2.length;
        for (int i = 0; i < length; i++) {
            if (clsArr2[i] != null && !primitiveMatch(clsArr[i], clsArr2[i]) && !clsArr[i].isAssignableFrom(clsArr2[i])) {
                return false;
            }
        }
        return true;
    }
}
