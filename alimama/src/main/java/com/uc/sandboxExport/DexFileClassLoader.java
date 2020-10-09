package com.uc.sandboxExport;

import android.annotation.SuppressLint;
import android.os.ParcelFileDescriptor;
import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexFile;
import java.io.File;
import java.lang.reflect.Field;

@Api
/* compiled from: U4Source */
public class DexFileClassLoader extends BaseDexClassLoader {
    private DexFile a;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public DexFileClassLoader(String str, String str2, String str3, ClassLoader classLoader, ParcelFileDescriptor parcelFileDescriptor, String str4, DexFile dexFile) throws Throwable {
        super(parcelFileDescriptor != null ? str4 : str, parcelFileDescriptor == null ? new File(str2) : null, str3, classLoader);
        if (parcelFileDescriptor != null) {
            dexFile = dexFile == null ? new DexFile(str4) : dexFile;
            Object loadDexByFd = DexFileResolver.loadDexByFd(parcelFileDescriptor.getFd());
            if (loadDexByFd != null) {
                Field declaredField = dexFile.getClass().getDeclaredField("mCookie");
                declaredField.setAccessible(true);
                declaredField.set(dexFile, loadDexByFd);
                this.a = dexFile;
                return;
            }
            throw new Exception("cannot load DexFile!");
        }
    }

    public String findLibrary(String str) {
        String findLibrary = super.findLibrary(str);
        return (findLibrary != null || !(getParent() instanceof BaseDexClassLoader)) ? findLibrary : ((BaseDexClassLoader) getParent()).findLibrary(str);
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"NewApi"})
    public Class<?> findClass(String str) throws ClassNotFoundException {
        Class<?> loadClass = this.a != null ? this.a.loadClass(str, this) : null;
        try {
            loadClass = super.findClass(str);
        } catch (ClassNotFoundException unused) {
        }
        if (loadClass == null) {
            loadClass = findLoadedClass(str);
        }
        return loadClass == null ? getParent().loadClass(str) : loadClass;
    }

    /* access modifiers changed from: protected */
    public Class<?> loadClass(String str, boolean z) throws ClassNotFoundException {
        if (!str.startsWith("com.uc.") && !str.startsWith("org.chromium.")) {
            return super.loadClass(str, z);
        }
        Class<?> findLoadedClass = findLoadedClass(str);
        return findLoadedClass == null ? findClass(str) : findLoadedClass;
    }
}
