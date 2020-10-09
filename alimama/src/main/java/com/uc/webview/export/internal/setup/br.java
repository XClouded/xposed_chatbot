package com.uc.webview.export.internal.setup;

import android.content.Context;
import android.util.Pair;
import com.taobao.weex.BuildConfig;
import com.uc.webview.export.cyclone.UCCyclone;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.internal.utility.Log;
import com.uc.webview.export.internal.utility.k;
import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/* compiled from: U4Source */
public class br {
    public static final String ASSETS_DIR = "assets";
    public static final String BROWSER_IF_DEX_FILE_USING_SO_SUFFIX = "libbrowser_if_jar_kj_uc.so";
    public static final String BROWSER_IF_FOR_EXPORT_FILE_USING_SO_SUFFIX = "libbrowser_if_for_export_jar_kj_uc.so";
    public static final String CORE_CLASS_LOADER_IMPL_CLASS = "com.uc.webkit.sdk.CoreClassPreLoaderImpl";
    public static final String CORE_FACTORY_IMPL_CLASS = "com.uc.webkit.sdk.CoreFactoryImpl";
    public static final String CORE_IMPL_DEX_FILE_USING_SO_SUFFIX = "libcore_jar_kj_uc.so";
    public static final String RES_PAKS_DIR_NAME = "paks";
    public static final String SDK_SHELL_DEX_FILE = "sdk_shell.jar";
    public static final String SDK_SHELL_DEX_FILE_USING_SO_SUFFIX = "libsdk_shell_jar_kj_uc.so";
    private static final String[] a = {"webviewuc"};
    private static final String[] b = {"imagehelper"};
    private Context c;
    public String coreCode = "";
    public final Pair<String, String> coreImplModule;
    private HashMap<String, String> d = new HashMap<>();
    public final String dataDir;
    public final String disabledFilePath;
    public boolean isFromDisk = false;
    public ClassLoader mCoreClassLoader = null;
    public ClassLoader mSdkShellClassLoader = null;
    public String mainLibrary = null;
    public final String pkgName;
    public final String resDirPath;
    public final Pair<String, String> sdkShellModule;
    public final String soDirPath;

    public br setInitInfo(String str, String str2) {
        synchronized (this.d) {
            this.d.put(str, str2);
        }
        return this;
    }

    public String getInitInfo(String str) {
        String str2;
        synchronized (this.d) {
            str2 = this.d.get(str);
        }
        return str2;
    }

    public br(Context context, String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8, boolean z, boolean z2) throws UCSetupException {
        Context context2 = context;
        String str9 = str;
        Log.d("UCMPackageInfo", "UCMPackageInfo, dataDir: " + str4 + ", pkgName: " + str9 + ", soDirPath: " + str2 + ", resDirPath: " + str3 + ", sdkShellPath: " + str5);
        String f = k.f(str2);
        String f2 = k.f(str3);
        String f3 = k.f(str4);
        String f4 = k.f(str5);
        String f5 = k.f(str7);
        String f6 = k.f(str8);
        this.c = context.getApplicationContext();
        this.pkgName = str9;
        this.soDirPath = f;
        f2 = z ? k.a(context2, f3, f2) : f2;
        if (f2 == null) {
            f2 = null;
        } else if (!f2.endsWith("/")) {
            f2 = f2 + "/";
        }
        this.resDirPath = f2;
        if (k.g()) {
            this.dataDir = f3;
            this.disabledFilePath = k.h();
            this.sdkShellModule = null;
            if (k.a(f5)) {
                this.coreImplModule = null;
            } else {
                this.coreImplModule = new Pair<>(f5, f6);
            }
        } else if (f3 != null) {
            File b2 = k.b(f6 == null ? k.a(context2, "odexs") : new File(f6), k.e(k.b(context2, f3)));
            this.dataDir = f3;
            this.disabledFilePath = f3 + "/e1df430e25e9dacb26ead508abb3413f";
            this.sdkShellModule = new Pair<>(z ? k.b(context2, f3, f4) : f4, b2.getAbsolutePath());
            this.coreImplModule = new Pair<>(z ? k.b(context2, f3, f5) : f5, b2.getAbsolutePath());
        } else {
            this.dataDir = null;
            this.disabledFilePath = k.h();
            this.sdkShellModule = null;
            this.coreImplModule = null;
        }
        String f7 = k.a(f) ? k.f(context.getApplicationInfo().nativeLibraryDir) : f;
        String str10 = null;
        for (String str11 : a) {
            File file = new File(f7, "lib" + str11 + ".so");
            if (file.exists() && (file.lastModified() <= 0 || file.lastModified() > 0)) {
                str10 = str11;
            }
        }
        if (str10 == null) {
            if (!k.a(f)) {
                throw new UCSetupException(3001, String.format("Main so file U4 [%s] not exists.", new Object[]{"webviewuc"}));
            }
        }
        if (!z2) {
            this.mainLibrary = str10;
            this.coreCode = "webviewuc".equals(this.mainLibrary) ? "u4" : this.mainLibrary == null ? BuildConfig.buildJavascriptFrameworkVersion : "error";
            if (this.coreCode.equals("u4")) {
                for (String str12 : b) {
                    File file2 = new File(context.getApplicationInfo().nativeLibraryDir, "lib" + str12 + ".so");
                    File file3 = new File(f7, "lib" + str12 + ".so");
                    if (file2.exists()) {
                        long lastModified = file2.lastModified();
                        if (!file3.exists() || file3.lastModified() < lastModified) {
                            k.a(file2, file3, file3);
                        }
                    }
                }
            }
        }
    }

    public String getDirAlias(Context context) {
        String str;
        if (this.coreImplModule == null || this.coreImplModule.first == null || (str = (String) this.coreImplModule.first) == null) {
            return "nul";
        }
        if (str.startsWith(k.a(context, "decompresses2").getAbsolutePath())) {
            return "dec";
        }
        if (str.startsWith(k.a(context, "updates").getAbsolutePath())) {
            return "upd";
        }
        if (str.startsWith(k.a(context, "kjlinks").getAbsolutePath())) {
            return "kjl";
        }
        if (str.startsWith(k.a(context, "kjcopies").getAbsolutePath())) {
            return "kjc";
        }
        return str.startsWith(k.a(context, "repairs").getAbsolutePath()) ? "rep" : "oth";
    }

    public Map<String, String> getFileHashs() {
        HashMap hashMap = new HashMap(16);
        if (this.coreImplModule == null || this.coreImplModule.first == null) {
            hashMap.put("core", BuildConfig.buildJavascriptFrameworkVersion);
        } else {
            File file = new File((String) this.coreImplModule.first);
            hashMap.put(file.getName(), UCCyclone.hashFileContents(file, UCCyclone.MessageDigestType.MD5));
        }
        if (this.sdkShellModule == null || this.sdkShellModule.first == null) {
            hashMap.put("sdk_shell", BuildConfig.buildJavascriptFrameworkVersion);
        } else {
            File file2 = new File((String) this.sdkShellModule.first);
            hashMap.put(file2.getName(), UCCyclone.hashFileContents(file2, UCCyclone.MessageDigestType.MD5));
        }
        String str = this.soDirPath;
        if (k.a(str)) {
            str = this.c.getApplicationInfo().nativeLibraryDir;
        }
        if (str != null) {
            File file3 = new File(str);
            if (file3.isDirectory()) {
                try {
                    if (this.mSdkShellClassLoader != null) {
                        Class<?> cls = Class.forName("com.uc.webview.browser.shell.NativeLibraries", true, this.mSdkShellClassLoader);
                        if (cls != null) {
                            Field declaredField = cls.getDeclaredField("LIBRARIES");
                            declaredField.setAccessible(true);
                            String[][] strArr = (String[][]) declaredField.get((Object) null);
                            if (strArr != null) {
                                for (String[] strArr2 : strArr) {
                                    String str2 = strArr2[0];
                                    k.d(strArr2[1]);
                                    String str3 = strArr2[2];
                                    String hashFileContents = UCCyclone.hashFileContents(new File(file3, str2), UCCyclone.MessageDigestType.MD5);
                                    if (k.a(str3) || str3.equals(hashFileContents)) {
                                        hashMap.put(str2, "ok");
                                    } else {
                                        hashMap.put(str2, hashFileContents);
                                    }
                                }
                            } else {
                                hashMap.put("NativeLibraries", BuildConfig.buildJavascriptFrameworkVersion);
                            }
                        }
                    } else {
                        hashMap.put("sdk_shell_cl", BuildConfig.buildJavascriptFrameworkVersion);
                    }
                } catch (Throwable unused) {
                    hashMap.put("NativeLibraries", UCCore.EVENT_EXCEPTION);
                }
            } else {
                hashMap.put("so_dir", BuildConfig.buildJavascriptFrameworkVersion);
            }
        } else {
            hashMap.put("so_path", BuildConfig.buildJavascriptFrameworkVersion);
        }
        return hashMap;
    }
}
