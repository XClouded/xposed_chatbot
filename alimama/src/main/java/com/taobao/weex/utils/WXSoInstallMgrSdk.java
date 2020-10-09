package com.taobao.weex.utils;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Build;
import android.text.TextUtils;
import com.alibaba.aliweex.utils.WXInitConfigManager;
import com.taobao.weex.IWXStatisticsListener;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.adapter.IWXSoLoaderAdapter;
import com.taobao.weex.adapter.IWXUserTrackAdapter;
import com.taobao.weex.common.WXErrorCode;
import dalvik.system.PathClassLoader;
import java.io.File;
import java.util.Locale;
import java.util.Map;

public class WXSoInstallMgrSdk {
    private static final String ARMEABI = "armeabi";
    static final String LOGTAG = "INIT_SO";
    private static final String MIPS = "mips";
    private static final String STARTUPSO = "/libweexjsb.so";
    private static final String STARTUPSOANDROID15 = "/libweexjst.so";
    private static final String X86 = "x86";
    private static String mAbi;
    static Application mContext;
    private static IWXSoLoaderAdapter mSoLoader;
    private static IWXStatisticsListener mStatisticsListener;

    public static void init(Application application, IWXSoLoaderAdapter iWXSoLoaderAdapter, IWXStatisticsListener iWXStatisticsListener) {
        mContext = application;
        mSoLoader = iWXSoLoaderAdapter;
        mStatisticsListener = iWXStatisticsListener;
    }

    public static boolean isX86() {
        return _cpuType().equalsIgnoreCase(X86);
    }

    public static boolean isCPUSupport() {
        return !_cpuType().equalsIgnoreCase(MIPS);
    }

    public static boolean initSo(String str, int i, IWXUserTrackAdapter iWXUserTrackAdapter) {
        String _cpuType = _cpuType();
        if (_cpuType.equalsIgnoreCase(MIPS)) {
            WXExceptionUtils.commitCriticalExceptionRT((String) null, WXErrorCode.WX_KEY_EXCEPTION_SDK_INIT, "initSo", "[WX_KEY_EXCEPTION_SDK_INIT_CPU_NOT_SUPPORT] for android cpuType is MIPS", (Map<String, String>) null);
            return false;
        }
        if (WXEnvironment.CORE_SO_NAME.equals(str)) {
            copyStartUpSo();
        }
        try {
            if (mSoLoader != null) {
                mSoLoader.doLoadLibrary("c++_shared");
            } else {
                System.loadLibrary("c++_shared");
            }
        } catch (Error | Exception e) {
            WXErrorCode wXErrorCode = WXErrorCode.WX_KEY_EXCEPTION_SDK_INIT;
            WXExceptionUtils.commitCriticalExceptionRT((String) null, wXErrorCode, "initSo", "load c++_shared failed Detail Error is: " + e.getMessage(), (Map<String, String>) null);
            if (WXEnvironment.isApkDebugable()) {
                throw e;
            }
        }
        try {
            if (mSoLoader != null) {
                mSoLoader.doLoadLibrary(str);
            } else {
                System.loadLibrary(str);
            }
            return true;
        } catch (Error | Exception e2) {
            if (_cpuType.contains(ARMEABI) || _cpuType.contains(X86)) {
                WXErrorCode wXErrorCode2 = WXErrorCode.WX_KEY_EXCEPTION_SDK_INIT;
                WXExceptionUtils.commitCriticalExceptionRT((String) null, wXErrorCode2, "initSo", str + "[WX_KEY_EXCEPTION_SDK_INIT_CPU_NOT_SUPPORT] for android cpuType is " + _cpuType + "\n Detail Error is: " + e2.getMessage(), (Map<String, String>) null);
            }
            if (!WXEnvironment.isApkDebugable()) {
                return false;
            }
            throw e2;
        }
    }

    private static File _desSoCopyFile(String str) {
        String _cpuType = _cpuType();
        String copySoDesDir = WXEnvironment.copySoDesDir();
        if (TextUtils.isEmpty(copySoDesDir)) {
            return null;
        }
        return new File(copySoDesDir, str + "/" + _cpuType);
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:26:0x00aa */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x00b0 A[Catch:{ Throwable -> 0x00cc }] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00c1 A[Catch:{ Throwable -> 0x00cc }] */
    @android.annotation.SuppressLint({"SdCardPath"})
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void copyStartUpSo() {
        /*
            android.app.Application r0 = com.taobao.weex.WXEnvironment.getApplication()     // Catch:{ Throwable -> 0x00cc }
            java.lang.String r0 = r0.getPackageName()     // Catch:{ Throwable -> 0x00cc }
            android.app.Application r1 = com.taobao.weex.WXEnvironment.getApplication()     // Catch:{ Throwable -> 0x00cc }
            android.content.Context r1 = r1.getApplicationContext()     // Catch:{ Throwable -> 0x00cc }
            java.io.File r1 = r1.getCacheDir()     // Catch:{ Throwable -> 0x00cc }
            java.lang.String r1 = r1.getPath()     // Catch:{ Throwable -> 0x00cc }
            r2 = 1
            java.lang.String r3 = "weexjsb"
            java.lang.String r4 = "/libweexjsb.so"
            int r5 = android.os.Build.VERSION.SDK_INT     // Catch:{ Throwable -> 0x00cc }
            r6 = 16
            if (r5 >= r6) goto L_0x0028
            r2 = 0
            java.lang.String r3 = "weexjst"
            java.lang.String r4 = "/libweexjst.so"
        L_0x0028:
            java.io.File r5 = _desSoCopyFile(r3)     // Catch:{ Throwable -> 0x00cc }
            boolean r6 = r5.exists()     // Catch:{ Throwable -> 0x00cc }
            if (r6 != 0) goto L_0x0035
            r5.mkdirs()     // Catch:{ Throwable -> 0x00cc }
        L_0x0035:
            java.io.File r6 = new java.io.File     // Catch:{ Throwable -> 0x00cc }
            r6.<init>(r5, r4)     // Catch:{ Throwable -> 0x00cc }
            java.lang.String r4 = r6.getAbsolutePath()     // Catch:{ Throwable -> 0x00cc }
            com.taobao.weex.WXEnvironment.CORE_JSB_SO_PATH = r4     // Catch:{ Throwable -> 0x00cc }
            java.lang.String r4 = "-1"
            java.lang.String r4 = com.taobao.weex.WXEnvironment.getDefaultSettingValue(r3, r4)     // Catch:{ Throwable -> 0x00cc }
            boolean r5 = r6.exists()     // Catch:{ Throwable -> 0x00cc }
            if (r5 == 0) goto L_0x0057
            java.lang.String r5 = com.taobao.weex.WXEnvironment.getAppVersionName()     // Catch:{ Throwable -> 0x00cc }
            boolean r4 = android.text.TextUtils.equals(r5, r4)     // Catch:{ Throwable -> 0x00cc }
            if (r4 == 0) goto L_0x0057
            return
        L_0x0057:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00cc }
            r4.<init>()     // Catch:{ Throwable -> 0x00cc }
            java.lang.String r5 = "/data/data/"
            r4.append(r5)     // Catch:{ Throwable -> 0x00cc }
            r4.append(r0)     // Catch:{ Throwable -> 0x00cc }
            java.lang.String r0 = "/lib"
            r4.append(r0)     // Catch:{ Throwable -> 0x00cc }
            java.lang.String r0 = r4.toString()     // Catch:{ Throwable -> 0x00cc }
            if (r1 == 0) goto L_0x007f
            java.lang.String r4 = "/cache"
            int r4 = r1.indexOf(r4)     // Catch:{ Throwable -> 0x00cc }
            if (r4 <= 0) goto L_0x007f
            java.lang.String r0 = "/cache"
            java.lang.String r4 = "/lib"
            java.lang.String r0 = r1.replace(r0, r4)     // Catch:{ Throwable -> 0x00cc }
        L_0x007f:
            if (r2 == 0) goto L_0x0089
            java.io.File r1 = new java.io.File     // Catch:{ Throwable -> 0x00cc }
            java.lang.String r2 = "/libweexjsb.so"
            r1.<init>(r0, r2)     // Catch:{ Throwable -> 0x00cc }
            goto L_0x0090
        L_0x0089:
            java.io.File r1 = new java.io.File     // Catch:{ Throwable -> 0x00cc }
            java.lang.String r2 = "/libweexjst.so"
            r1.<init>(r0, r2)     // Catch:{ Throwable -> 0x00cc }
        L_0x0090:
            boolean r0 = r1.exists()     // Catch:{ Throwable -> 0x00cc }
            if (r0 != 0) goto L_0x00aa
            java.lang.Class<com.taobao.weex.utils.WXSoInstallMgrSdk> r0 = com.taobao.weex.utils.WXSoInstallMgrSdk.class
            java.lang.ClassLoader r0 = r0.getClassLoader()     // Catch:{ Throwable -> 0x00aa }
            dalvik.system.PathClassLoader r0 = (dalvik.system.PathClassLoader) r0     // Catch:{ Throwable -> 0x00aa }
            dalvik.system.PathClassLoader r0 = (dalvik.system.PathClassLoader) r0     // Catch:{ Throwable -> 0x00aa }
            java.lang.String r0 = r0.findLibrary(r3)     // Catch:{ Throwable -> 0x00aa }
            java.io.File r2 = new java.io.File     // Catch:{ Throwable -> 0x00aa }
            r2.<init>(r0)     // Catch:{ Throwable -> 0x00aa }
            r1 = r2
        L_0x00aa:
            boolean r0 = r1.exists()     // Catch:{ Throwable -> 0x00cc }
            if (r0 != 0) goto L_0x00bb
            java.lang.String r0 = com.taobao.weex.WXEnvironment.extractSo()     // Catch:{ Throwable -> 0x00cc }
            java.io.File r1 = new java.io.File     // Catch:{ Throwable -> 0x00cc }
            java.lang.String r2 = "/libweexjsb.so"
            r1.<init>(r0, r2)     // Catch:{ Throwable -> 0x00cc }
        L_0x00bb:
            boolean r0 = r1.exists()     // Catch:{ Throwable -> 0x00cc }
            if (r0 == 0) goto L_0x00c4
            com.taobao.weex.utils.WXFileUtils.copyFile(r1, r6)     // Catch:{ Throwable -> 0x00cc }
        L_0x00c4:
            java.lang.String r0 = com.taobao.weex.WXEnvironment.getAppVersionName()     // Catch:{ Throwable -> 0x00cc }
            com.taobao.weex.WXEnvironment.writeDefaultSettingsValue(r3, r0)     // Catch:{ Throwable -> 0x00cc }
            goto L_0x00d0
        L_0x00cc:
            r0 = move-exception
            r0.printStackTrace()
        L_0x00d0:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.utils.WXSoInstallMgrSdk.copyStartUpSo():void");
    }

    public static void copyJssRuntimeSo() {
        boolean checkGreyConfig = WXUtils.checkGreyConfig(WXInitConfigManager.WXAPM_CONFIG_GROUP, "use_runtime_api", "0");
        WXLogUtils.e("weex", "tryUseRunTimeApi ? " + checkGreyConfig);
        if (checkGreyConfig) {
            try {
                WXLogUtils.e("weex", "copyJssRuntimeSo: ");
                File _desSoCopyFile = _desSoCopyFile(WXEnvironment.CORE_JSS_SO_NAME);
                if (!_desSoCopyFile.exists()) {
                    _desSoCopyFile.mkdirs();
                }
                File file = new File(_desSoCopyFile, "libweexjss.so");
                String defaultSettingValue = WXEnvironment.getDefaultSettingValue("app_version_code_weex", "-1");
                if (file.exists()) {
                    if (!TextUtils.equals(WXEnvironment.getAppVersionName(), defaultSettingValue)) {
                        file.delete();
                    } else {
                        WXEnvironment.CORE_JSS_RUNTIME_SO_PATH = file.getAbsolutePath();
                        WXEnvironment.sUseRunTimeApi = true;
                        WXLogUtils.e("weex", "copyJssRuntimeSo exist:  return");
                        return;
                    }
                }
                String findLibrary = ((PathClassLoader) WXSoInstallMgrSdk.class.getClassLoader()).findLibrary("weexjssr");
                if (!TextUtils.isEmpty(findLibrary)) {
                    file.createNewFile();
                    WXFileUtils.copyFileWithException(new File(findLibrary), file);
                    WXEnvironment.CORE_JSS_RUNTIME_SO_PATH = file.getAbsolutePath();
                    WXEnvironment.writeDefaultSettingsValue("app_version_code_weex", WXEnvironment.getAppVersionName());
                    WXEnvironment.sUseRunTimeApi = true;
                    WXLogUtils.e("weex", "copyJssRuntimeSo: cp end and return ");
                }
            } catch (Throwable th) {
                th.printStackTrace();
                WXEnvironment.sUseRunTimeApi = false;
                WXLogUtils.e("weex", "copyJssRuntimeSo:  exception" + th);
            }
        }
    }

    private static String _getFieldReflectively(Build build, String str) {
        try {
            return Build.class.getField(str).get(build).toString();
        } catch (Exception unused) {
            return "Unknown";
        }
    }

    public static String _cpuType() {
        if (TextUtils.isEmpty(mAbi)) {
            try {
                mAbi = Build.CPU_ABI;
            } catch (Throwable th) {
                th.printStackTrace();
                mAbi = ARMEABI;
            }
            if (TextUtils.isEmpty(mAbi)) {
                mAbi = ARMEABI;
            }
            mAbi = mAbi.toLowerCase(Locale.ROOT);
        }
        return mAbi;
    }

    static boolean checkSoIsValid(String str, long j) {
        if (mContext == null) {
            return false;
        }
        try {
            long currentTimeMillis = System.currentTimeMillis();
            if (WXSoInstallMgrSdk.class.getClassLoader() instanceof PathClassLoader) {
                String findLibrary = ((PathClassLoader) WXSoInstallMgrSdk.class.getClassLoader()).findLibrary(str);
                if (TextUtils.isEmpty(findLibrary)) {
                    return false;
                }
                File file = new File(findLibrary);
                if (file.exists()) {
                    if (j != file.length()) {
                        return false;
                    }
                }
                WXLogUtils.w("weex so size check path :" + findLibrary + "   " + (System.currentTimeMillis() - currentTimeMillis));
                return true;
            }
        } catch (Throwable th) {
            WXErrorCode wXErrorCode = WXErrorCode.WX_KEY_EXCEPTION_SDK_INIT;
            WXExceptionUtils.commitCriticalExceptionRT((String) null, wXErrorCode, "checkSoIsValid", "[WX_KEY_EXCEPTION_SDK_INIT_CPU_NOT_SUPPORT] for weex so size check fail exception :" + th.getMessage(), (Map<String, String>) null);
            WXLogUtils.e("weex so size check fail exception :" + th.getMessage());
        }
        return true;
    }

    @SuppressLint({"SdCardPath"})
    static String _targetSoFile(String str, int i) {
        Application application = mContext;
        if (application == null) {
            return "";
        }
        String str2 = "/data/data/" + application.getPackageName() + "/files";
        File filesDir = application.getFilesDir();
        if (filesDir != null) {
            str2 = filesDir.getPath();
        }
        return str2 + "/lib" + str + "bk" + i + ".so";
    }

    static void removeSoIfExit(String str, int i) {
        File file = new File(_targetSoFile(str, i));
        if (file.exists()) {
            file.delete();
        }
    }

    static boolean isExist(String str, int i) {
        return new File(_targetSoFile(str, i)).exists();
    }

    @SuppressLint({"UnsafeDynamicallyLoadedCode"})
    static boolean _loadUnzipSo(String str, int i, IWXUserTrackAdapter iWXUserTrackAdapter) {
        try {
            if (isExist(str, i)) {
                if (mSoLoader != null) {
                    mSoLoader.doLoad(_targetSoFile(str, i));
                } else {
                    System.load(_targetSoFile(str, i));
                }
            }
            return true;
        } catch (Throwable th) {
            WXErrorCode wXErrorCode = WXErrorCode.WX_KEY_EXCEPTION_SDK_INIT_CPU_NOT_SUPPORT;
            WXExceptionUtils.commitCriticalExceptionRT((String) null, wXErrorCode, "_loadUnzipSo", "[WX_KEY_EXCEPTION_SDK_INIT_WX_ERR_COPY_FROM_APK] \n Detail Msg is : " + th.getMessage(), (Map<String, String>) null);
            WXLogUtils.e("", th);
            return false;
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v3, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v3, resolved type: java.nio.channels.FileChannel} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v4, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v5, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v4, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v6, resolved type: java.io.InputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v5, resolved type: java.nio.channels.FileChannel} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v8, resolved type: java.io.FileOutputStream} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v9, resolved type: java.io.FileOutputStream} */
    /* JADX WARNING: type inference failed for: r1v4, types: [java.util.Map, java.util.zip.ZipFile, java.lang.String] */
    /* JADX WARNING: type inference failed for: r2v2, types: [java.io.FileOutputStream] */
    /* JADX WARNING: type inference failed for: r2v7 */
    /* JADX WARNING: Code restructure failed: missing block: B:92:0x0114, code lost:
        if (r4 == null) goto L_0x0117;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Removed duplicated region for block: B:63:0x00c8 A[SYNTHETIC, Splitter:B:63:0x00c8] */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x00d2 A[SYNTHETIC, Splitter:B:69:0x00d2] */
    /* JADX WARNING: Removed duplicated region for block: B:75:0x00dc A[SYNTHETIC, Splitter:B:75:0x00dc] */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x011a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static boolean unZipSelectedFiles(java.lang.String r10, int r11, com.taobao.weex.adapter.IWXUserTrackAdapter r12) throws java.util.zip.ZipException, java.io.IOException {
        /*
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "lib/armeabi/lib"
            r0.append(r1)
            r0.append(r10)
            java.lang.String r1 = ".so"
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            java.lang.String r1 = ""
            android.app.Application r2 = mContext
            r3 = 0
            if (r2 != 0) goto L_0x001e
            return r3
        L_0x001e:
            android.content.pm.ApplicationInfo r4 = r2.getApplicationInfo()
            if (r4 == 0) goto L_0x0026
            java.lang.String r1 = r4.sourceDir
        L_0x0026:
            java.util.zip.ZipFile r4 = new java.util.zip.ZipFile
            r4.<init>(r1)
            r1 = 0
            java.util.Enumeration r5 = r4.entries()     // Catch:{ IOException -> 0x00f4 }
        L_0x0030:
            boolean r6 = r5.hasMoreElements()     // Catch:{ IOException -> 0x00f4 }
            if (r6 == 0) goto L_0x00ee
            java.lang.Object r6 = r5.nextElement()     // Catch:{ IOException -> 0x00f4 }
            java.util.zip.ZipEntry r6 = (java.util.zip.ZipEntry) r6     // Catch:{ IOException -> 0x00f4 }
            java.lang.String r7 = r6.getName()     // Catch:{ IOException -> 0x00f4 }
            boolean r7 = r7.startsWith(r0)     // Catch:{ IOException -> 0x00f4 }
            if (r7 == 0) goto L_0x0030
            removeSoIfExit(r10, r11)     // Catch:{ all -> 0x00c2 }
            java.io.InputStream r0 = r4.getInputStream(r6)     // Catch:{ all -> 0x00c2 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x00bf }
            r5.<init>()     // Catch:{ all -> 0x00bf }
            java.lang.String r6 = "lib"
            r5.append(r6)     // Catch:{ all -> 0x00bf }
            r5.append(r10)     // Catch:{ all -> 0x00bf }
            java.lang.String r6 = "bk"
            r5.append(r6)     // Catch:{ all -> 0x00bf }
            r5.append(r11)     // Catch:{ all -> 0x00bf }
            java.lang.String r6 = ".so"
            r5.append(r6)     // Catch:{ all -> 0x00bf }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x00bf }
            java.io.FileOutputStream r2 = r2.openFileOutput(r5, r3)     // Catch:{ all -> 0x00bf }
            java.nio.channels.FileChannel r5 = r2.getChannel()     // Catch:{ all -> 0x00bc }
            r6 = 1024(0x400, float:1.435E-42)
            byte[] r6 = new byte[r6]     // Catch:{ all -> 0x00ba }
            r7 = 0
        L_0x0078:
            int r8 = r0.read(r6)     // Catch:{ all -> 0x00ba }
            if (r8 <= 0) goto L_0x0087
            java.nio.ByteBuffer r9 = java.nio.ByteBuffer.wrap(r6, r3, r8)     // Catch:{ all -> 0x00ba }
            r5.write(r9)     // Catch:{ all -> 0x00ba }
            int r7 = r7 + r8
            goto L_0x0078
        L_0x0087:
            if (r0 == 0) goto L_0x0091
            r0.close()     // Catch:{ Exception -> 0x008d }
            goto L_0x0091
        L_0x008d:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ IOException -> 0x00f4 }
        L_0x0091:
            if (r5 == 0) goto L_0x009b
            r5.close()     // Catch:{ Exception -> 0x0097 }
            goto L_0x009b
        L_0x0097:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ IOException -> 0x00f4 }
        L_0x009b:
            if (r2 == 0) goto L_0x00a5
            r2.close()     // Catch:{ Exception -> 0x00a1 }
            goto L_0x00a5
        L_0x00a1:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ IOException -> 0x00f4 }
        L_0x00a5:
            r4.close()     // Catch:{ IOException -> 0x00f4 }
            if (r7 <= 0) goto L_0x00b4
            boolean r10 = _loadUnzipSo(r10, r11, r12)     // Catch:{ IOException -> 0x00eb, all -> 0x00e8 }
            if (r1 == 0) goto L_0x00b3
            r1.close()
        L_0x00b3:
            return r10
        L_0x00b4:
            if (r1 == 0) goto L_0x00b9
            r1.close()
        L_0x00b9:
            return r3
        L_0x00ba:
            r10 = move-exception
            goto L_0x00c6
        L_0x00bc:
            r10 = move-exception
            r5 = r1
            goto L_0x00c6
        L_0x00bf:
            r10 = move-exception
            r2 = r1
            goto L_0x00c5
        L_0x00c2:
            r10 = move-exception
            r0 = r1
            r2 = r0
        L_0x00c5:
            r5 = r2
        L_0x00c6:
            if (r0 == 0) goto L_0x00d0
            r0.close()     // Catch:{ Exception -> 0x00cc }
            goto L_0x00d0
        L_0x00cc:
            r11 = move-exception
            r11.printStackTrace()     // Catch:{ IOException -> 0x00f4 }
        L_0x00d0:
            if (r5 == 0) goto L_0x00da
            r5.close()     // Catch:{ Exception -> 0x00d6 }
            goto L_0x00da
        L_0x00d6:
            r11 = move-exception
            r11.printStackTrace()     // Catch:{ IOException -> 0x00f4 }
        L_0x00da:
            if (r2 == 0) goto L_0x00e4
            r2.close()     // Catch:{ Exception -> 0x00e0 }
            goto L_0x00e4
        L_0x00e0:
            r11 = move-exception
            r11.printStackTrace()     // Catch:{ IOException -> 0x00f4 }
        L_0x00e4:
            r4.close()     // Catch:{ IOException -> 0x00f4 }
            throw r10     // Catch:{ IOException -> 0x00eb, all -> 0x00e8 }
        L_0x00e8:
            r10 = move-exception
            r4 = r1
            goto L_0x0118
        L_0x00eb:
            r10 = move-exception
            r4 = r1
            goto L_0x00f5
        L_0x00ee:
            r4.close()
            goto L_0x0117
        L_0x00f2:
            r10 = move-exception
            goto L_0x0118
        L_0x00f4:
            r10 = move-exception
        L_0x00f5:
            r10.printStackTrace()     // Catch:{ all -> 0x00f2 }
            com.taobao.weex.common.WXErrorCode r11 = com.taobao.weex.common.WXErrorCode.WX_KEY_EXCEPTION_SDK_INIT_CPU_NOT_SUPPORT     // Catch:{ all -> 0x00f2 }
            java.lang.String r12 = "unZipSelectedFiles"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x00f2 }
            r0.<init>()     // Catch:{ all -> 0x00f2 }
            java.lang.String r2 = "[WX_KEY_EXCEPTION_SDK_INIT_unZipSelectedFiles] \n Detail msg is: "
            r0.append(r2)     // Catch:{ all -> 0x00f2 }
            java.lang.String r10 = r10.getMessage()     // Catch:{ all -> 0x00f2 }
            r0.append(r10)     // Catch:{ all -> 0x00f2 }
            java.lang.String r10 = r0.toString()     // Catch:{ all -> 0x00f2 }
            com.taobao.weex.utils.WXExceptionUtils.commitCriticalExceptionRT(r1, r11, r12, r10, r1)     // Catch:{ all -> 0x00f2 }
            if (r4 == 0) goto L_0x0117
            goto L_0x00ee
        L_0x0117:
            return r3
        L_0x0118:
            if (r4 == 0) goto L_0x011d
            r4.close()
        L_0x011d:
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.weex.utils.WXSoInstallMgrSdk.unZipSelectedFiles(java.lang.String, int, com.taobao.weex.adapter.IWXUserTrackAdapter):boolean");
    }
}
