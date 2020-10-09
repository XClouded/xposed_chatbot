package org.android.spdy;

import android.content.Context;
import android.os.Build;
import java.io.File;
import java.lang.reflect.Method;

public class SoInstallMgrSdk {
    private static final String ARMEABI = "armeabi";
    private static final int EventID_SO_INIT = 21033;
    static final String LOGTAG = "INIT_SO";
    private static final String MIPS = "mips";
    private static final String X86 = "x86";
    static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    public static boolean initSo(String str, int i) {
        return initSo(str, i, (ClassLoader) null);
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0040 A[SYNTHETIC, Splitter:B:16:0x0040] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean initSo(java.lang.String r7, int r8, java.lang.ClassLoader r9) {
        /*
            r0 = 1
            r1 = 0
            if (r9 != 0) goto L_0x000e
            java.lang.System.loadLibrary(r7)     // Catch:{ Exception -> 0x000c, UnsatisfiedLinkError -> 0x000a, Error -> 0x0008 }
            goto L_0x003e
        L_0x0008:
            r0 = move-exception
            goto L_0x0032
        L_0x000a:
            r0 = move-exception
            goto L_0x0036
        L_0x000c:
            r0 = move-exception
            goto L_0x003a
        L_0x000e:
            java.lang.Runtime r2 = java.lang.Runtime.getRuntime()     // Catch:{ Exception -> 0x000c, UnsatisfiedLinkError -> 0x000a, Error -> 0x0008 }
            r3 = 2
            java.lang.Class[] r4 = new java.lang.Class[r3]     // Catch:{ Exception -> 0x000c, UnsatisfiedLinkError -> 0x000a, Error -> 0x0008 }
            java.lang.Class<java.lang.String> r5 = java.lang.String.class
            r4[r1] = r5     // Catch:{ Exception -> 0x000c, UnsatisfiedLinkError -> 0x000a, Error -> 0x0008 }
            java.lang.Class<java.lang.ClassLoader> r5 = java.lang.ClassLoader.class
            r4[r0] = r5     // Catch:{ Exception -> 0x000c, UnsatisfiedLinkError -> 0x000a, Error -> 0x0008 }
            java.lang.Class<java.lang.Runtime> r5 = java.lang.Runtime.class
            java.lang.String r6 = "loadLibrary"
            java.lang.reflect.Method r4 = r5.getDeclaredMethod(r6, r4)     // Catch:{ Exception -> 0x000c, UnsatisfiedLinkError -> 0x000a, Error -> 0x0008 }
            r4.setAccessible(r0)     // Catch:{ Exception -> 0x000c, UnsatisfiedLinkError -> 0x000a, Error -> 0x0008 }
            java.lang.Object[] r3 = new java.lang.Object[r3]     // Catch:{ Exception -> 0x000c, UnsatisfiedLinkError -> 0x000a, Error -> 0x0008 }
            r3[r1] = r7     // Catch:{ Exception -> 0x000c, UnsatisfiedLinkError -> 0x000a, Error -> 0x0008 }
            r3[r0] = r9     // Catch:{ Exception -> 0x000c, UnsatisfiedLinkError -> 0x000a, Error -> 0x0008 }
            r4.invoke(r2, r3)     // Catch:{ Exception -> 0x000c, UnsatisfiedLinkError -> 0x000a, Error -> 0x0008 }
            goto L_0x003e
        L_0x0032:
            r0.printStackTrace()
            goto L_0x003d
        L_0x0036:
            r0.printStackTrace()
            goto L_0x003d
        L_0x003a:
            r0.printStackTrace()
        L_0x003d:
            r0 = 0
        L_0x003e:
            if (r0 != 0) goto L_0x0084
            boolean r2 = isExist(r7, r8)     // Catch:{ Exception -> 0x007f, UnsatisfiedLinkError -> 0x007a, Error -> 0x0075 }
            if (r2 == 0) goto L_0x0050
            boolean r2 = _loadUnzipSo(r7, r8, r9)     // Catch:{ Exception -> 0x007f, UnsatisfiedLinkError -> 0x007a, Error -> 0x0075 }
            if (r2 == 0) goto L_0x004d
            return r2
        L_0x004d:
            removeSoIfExit(r7, r8)     // Catch:{ Exception -> 0x007f, UnsatisfiedLinkError -> 0x007a, Error -> 0x0075 }
        L_0x0050:
            java.lang.String r2 = _cpuType()     // Catch:{ Exception -> 0x007f, UnsatisfiedLinkError -> 0x007a, Error -> 0x0075 }
            java.lang.String r3 = "mips"
            boolean r3 = r2.equalsIgnoreCase(r3)     // Catch:{ Exception -> 0x007f, UnsatisfiedLinkError -> 0x007a, Error -> 0x0075 }
            if (r3 != 0) goto L_0x0084
            java.lang.String r3 = "x86"
            boolean r2 = r2.equalsIgnoreCase(r3)     // Catch:{ Exception -> 0x007f, UnsatisfiedLinkError -> 0x007a, Error -> 0x0075 }
            if (r2 == 0) goto L_0x0065
            goto L_0x0084
        L_0x0065:
            boolean r7 = unZipSelectedFiles(r7, r8, r9)     // Catch:{ ZipException -> 0x0070, IOException -> 0x006b }
            r1 = r7
            goto L_0x0085
        L_0x006b:
            r7 = move-exception
            r7.printStackTrace()     // Catch:{ Exception -> 0x007f, UnsatisfiedLinkError -> 0x007a, Error -> 0x0075 }
            goto L_0x0084
        L_0x0070:
            r7 = move-exception
            r7.printStackTrace()     // Catch:{ Exception -> 0x007f, UnsatisfiedLinkError -> 0x007a, Error -> 0x0075 }
            goto L_0x0084
        L_0x0075:
            r7 = move-exception
            r7.printStackTrace()
            goto L_0x0085
        L_0x007a:
            r7 = move-exception
            r7.printStackTrace()
            goto L_0x0085
        L_0x007f:
            r7 = move-exception
            r7.printStackTrace()
            goto L_0x0085
        L_0x0084:
            r1 = r0
        L_0x0085:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.android.spdy.SoInstallMgrSdk.initSo(java.lang.String, int, java.lang.ClassLoader):boolean");
    }

    private static String _getFieldReflectively(Build build, String str) {
        try {
            return Build.class.getField(str).get(build).toString();
        } catch (Exception unused) {
            return "Unknown";
        }
    }

    private static String _cpuType() {
        String _getFieldReflectively = _getFieldReflectively(new Build(), "CPU_ABI");
        if (_getFieldReflectively == null || _getFieldReflectively.length() == 0 || _getFieldReflectively.equals("Unknown")) {
            _getFieldReflectively = ARMEABI;
        }
        return _getFieldReflectively.toLowerCase();
    }

    static String _targetSoFile(String str, int i) {
        Context context = mContext;
        if (context == null) {
            return "";
        }
        String str2 = "/data/data/" + context.getPackageName() + "/files";
        File filesDir = context.getFilesDir();
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

    static boolean _loadUnzipSo(String str, int i, ClassLoader classLoader) {
        try {
            if (isExist(str, i)) {
                if (classLoader == null) {
                    System.load(_targetSoFile(str, i));
                } else {
                    Runtime runtime = Runtime.getRuntime();
                    Method declaredMethod = Runtime.class.getDeclaredMethod("load", new Class[]{String.class, ClassLoader.class});
                    declaredMethod.setAccessible(true);
                    declaredMethod.invoke(runtime, new Object[]{_targetSoFile(str, i), classLoader});
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } catch (UnsatisfiedLinkError e2) {
            e2.printStackTrace();
            return false;
        } catch (Error e3) {
            e3.printStackTrace();
            return false;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:65:0x00db A[SYNTHETIC, Splitter:B:65:0x00db] */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x00e5 A[SYNTHETIC, Splitter:B:71:0x00e5] */
    /* JADX WARNING: Removed duplicated region for block: B:77:0x00ef A[SYNTHETIC, Splitter:B:77:0x00ef] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static boolean unZipSelectedFiles(java.lang.String r9, int r10, java.lang.ClassLoader r11) throws java.util.zip.ZipException, java.io.IOException {
        /*
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "lib/armeabi/lib"
            r0.append(r1)
            r0.append(r9)
            java.lang.String r1 = ".so"
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r1 = 0
            java.lang.String r2 = ""
            android.content.Context r3 = mContext     // Catch:{ IOException -> 0x00fc }
            if (r3 != 0) goto L_0x001e
            return r1
        L_0x001e:
            android.content.pm.ApplicationInfo r4 = r3.getApplicationInfo()     // Catch:{ IOException -> 0x00fc }
            if (r4 == 0) goto L_0x0026
            java.lang.String r2 = r4.sourceDir     // Catch:{ IOException -> 0x00fc }
        L_0x0026:
            java.util.zip.ZipFile r4 = new java.util.zip.ZipFile     // Catch:{ IOException -> 0x00fc }
            r4.<init>(r2)     // Catch:{ IOException -> 0x00fc }
            java.util.Enumeration r2 = r4.entries()     // Catch:{ IOException -> 0x00fc }
        L_0x002f:
            boolean r5 = r2.hasMoreElements()     // Catch:{ IOException -> 0x00fc }
            if (r5 == 0) goto L_0x0100
            java.lang.Object r5 = r2.nextElement()     // Catch:{ IOException -> 0x00fc }
            java.util.zip.ZipEntry r5 = (java.util.zip.ZipEntry) r5     // Catch:{ IOException -> 0x00fc }
            java.lang.String r6 = r5.getName()     // Catch:{ IOException -> 0x00fc }
            java.lang.String r7 = ".."
            boolean r7 = r6.contains(r7)     // Catch:{ IOException -> 0x00fc }
            if (r7 != 0) goto L_0x00fb
            java.lang.String r7 = "\\"
            boolean r7 = r6.contains(r7)     // Catch:{ IOException -> 0x00fc }
            if (r7 != 0) goto L_0x00fb
            java.lang.String r7 = "%"
            boolean r6 = r6.contains(r7)     // Catch:{ IOException -> 0x00fc }
            if (r6 == 0) goto L_0x0059
            goto L_0x00fb
        L_0x0059:
            java.lang.String r6 = r5.getName()     // Catch:{ IOException -> 0x00fc }
            boolean r6 = r6.startsWith(r0)     // Catch:{ IOException -> 0x00fc }
            if (r6 == 0) goto L_0x002f
            r0 = 0
            removeSoIfExit(r9, r10)     // Catch:{ all -> 0x00d6 }
            java.io.InputStream r2 = r4.getInputStream(r5)     // Catch:{ all -> 0x00d6 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ all -> 0x00d3 }
            r5.<init>()     // Catch:{ all -> 0x00d3 }
            java.lang.String r6 = "lib"
            r5.append(r6)     // Catch:{ all -> 0x00d3 }
            r5.append(r9)     // Catch:{ all -> 0x00d3 }
            java.lang.String r6 = "bk"
            r5.append(r6)     // Catch:{ all -> 0x00d3 }
            r5.append(r10)     // Catch:{ all -> 0x00d3 }
            java.lang.String r6 = ".so"
            r5.append(r6)     // Catch:{ all -> 0x00d3 }
            java.lang.String r5 = r5.toString()     // Catch:{ all -> 0x00d3 }
            java.io.FileOutputStream r3 = r3.openFileOutput(r5, r1)     // Catch:{ all -> 0x00d3 }
            java.nio.channels.FileChannel r5 = r3.getChannel()     // Catch:{ all -> 0x00d1 }
            r0 = 1024(0x400, float:1.435E-42)
            byte[] r0 = new byte[r0]     // Catch:{ all -> 0x00ce }
            r6 = 0
        L_0x0096:
            int r7 = r2.read(r0)     // Catch:{ all -> 0x00ce }
            if (r7 <= 0) goto L_0x00a5
            java.nio.ByteBuffer r8 = java.nio.ByteBuffer.wrap(r0, r1, r7)     // Catch:{ all -> 0x00ce }
            r5.write(r8)     // Catch:{ all -> 0x00ce }
            int r6 = r6 + r7
            goto L_0x0096
        L_0x00a5:
            if (r2 == 0) goto L_0x00af
            r2.close()     // Catch:{ Exception -> 0x00ab }
            goto L_0x00af
        L_0x00ab:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ IOException -> 0x00fc }
        L_0x00af:
            if (r5 == 0) goto L_0x00b9
            r5.close()     // Catch:{ Exception -> 0x00b5 }
            goto L_0x00b9
        L_0x00b5:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ IOException -> 0x00fc }
        L_0x00b9:
            if (r3 == 0) goto L_0x00c3
            r3.close()     // Catch:{ Exception -> 0x00bf }
            goto L_0x00c3
        L_0x00bf:
            r0 = move-exception
            r0.printStackTrace()     // Catch:{ IOException -> 0x00fc }
        L_0x00c3:
            r4.close()     // Catch:{ IOException -> 0x00fc }
            if (r6 <= 0) goto L_0x00cd
            boolean r9 = _loadUnzipSo(r9, r10, r11)     // Catch:{ IOException -> 0x00fc }
            return r9
        L_0x00cd:
            return r1
        L_0x00ce:
            r9 = move-exception
            r0 = r5
            goto L_0x00d9
        L_0x00d1:
            r9 = move-exception
            goto L_0x00d9
        L_0x00d3:
            r9 = move-exception
            r3 = r0
            goto L_0x00d9
        L_0x00d6:
            r9 = move-exception
            r2 = r0
            r3 = r2
        L_0x00d9:
            if (r2 == 0) goto L_0x00e3
            r2.close()     // Catch:{ Exception -> 0x00df }
            goto L_0x00e3
        L_0x00df:
            r10 = move-exception
            r10.printStackTrace()     // Catch:{ IOException -> 0x00fc }
        L_0x00e3:
            if (r0 == 0) goto L_0x00ed
            r0.close()     // Catch:{ Exception -> 0x00e9 }
            goto L_0x00ed
        L_0x00e9:
            r10 = move-exception
            r10.printStackTrace()     // Catch:{ IOException -> 0x00fc }
        L_0x00ed:
            if (r3 == 0) goto L_0x00f7
            r3.close()     // Catch:{ Exception -> 0x00f3 }
            goto L_0x00f7
        L_0x00f3:
            r10 = move-exception
            r10.printStackTrace()     // Catch:{ IOException -> 0x00fc }
        L_0x00f7:
            r4.close()     // Catch:{ IOException -> 0x00fc }
            throw r9     // Catch:{ IOException -> 0x00fc }
        L_0x00fb:
            return r1
        L_0x00fc:
            r9 = move-exception
            r9.printStackTrace()
        L_0x0100:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: org.android.spdy.SoInstallMgrSdk.unZipSelectedFiles(java.lang.String, int, java.lang.ClassLoader):boolean");
    }
}
