package android.taobao.windvane.extra.uc;

import android.os.Build;
import android.os.Process;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.util.TaoLog;

import com.taobao.weex.el.parse.Operators;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import mtopsdk.common.util.SymbolExpUtil;

public class WVUCUtils {
    public static final String CONFIG_KEY = "ISX86";
    public static final String TAG = "WVUCUtils";
    public static final long VAL_ARM = 2;
    public static final long VAL_DEAFAULT = 0;
    public static final long VAL_X86 = 1;
    private static String sAbi;
    private static String sAbi2;
    private static String[] sAbiList;
    private static String sArch;
    private static String sCpuAbi;
    private static String[] sSupportedABIs;

    public static boolean isArchContains(String str) {
        String fromSystemProp;
        if (sArch == null) {
            sArch = System.getProperty("os.arch");
        }
        if (sArch != null && sArch.toLowerCase().contains(str)) {
            return true;
        }
        if (sAbi == null) {
            try {
                sAbi = Build.CPU_ABI;
                sAbi2 = Build.CPU_ABI2;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (sAbi != null && sAbi.toLowerCase().contains(str)) {
            return true;
        }
        if (sSupportedABIs == null && Build.VERSION.SDK_INT >= 21) {
            try {
                sSupportedABIs = (String[]) Build.class.getField("SUPPORTED_ABIS").get((Object) null);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        if (sSupportedABIs != null && sSupportedABIs.length > 0 && sSupportedABIs[0] != null && sSupportedABIs[0].toLowerCase().contains(str)) {
            return true;
        }
        if (sCpuAbi == null) {
            sCpuAbi = getFromSystemProp("ro.product.cpu.abi");
        }
        if (sCpuAbi != null && sCpuAbi.toLowerCase().contains(str)) {
            return true;
        }
        if (!(sAbiList != null || (fromSystemProp = getFromSystemProp("ro.product.cpu.abilist")) == null || fromSystemProp.length() == 0)) {
            sAbiList = fromSystemProp.split(",");
        }
        if (sAbiList == null || sAbiList.length <= 0 || sAbiList[0] == null || !sAbiList[0].toLowerCase().contains(str)) {
            return false;
        }
        return true;
    }

    public static boolean is64Bit() {
        if (Build.VERSION.SDK_INT >= 23) {
            boolean is64Bit = Process.is64Bit();
            TaoLog.i(TAG, "is 64 bit = [" + is64Bit + Operators.ARRAY_END_STR);
            return is64Bit;
        } else if (Build.VERSION.SDK_INT >= 21) {
            return isART64();
        } else {
            return false;
        }
    }

    private static boolean isART64() {
        try {
            ClassLoader classLoader = GlobalConfig.context.getClassLoader();
            Object invoke = ClassLoader.class.getDeclaredMethod("findLibrary", new Class[]{String.class}).invoke(classLoader, new Object[]{"art"});
            if (invoke != null) {
                return ((String) invoke).contains("lib64");
            }
            return false;
        } catch (Exception unused) {
            return is64bitCPU();
        }
    }

    private static boolean is64bitCPU() {
        String str;
        if (Build.VERSION.SDK_INT >= 21) {
            String[] strArr = Build.SUPPORTED_ABIS;
            str = strArr.length > 0 ? strArr[0] : null;
        } else {
            str = Build.CPU_ABI;
        }
        if (str == null || !str.contains("arm64")) {
            return false;
        }
        return true;
    }

    private static String getFromSystemProp(String str) {
        BufferedReader bufferedReader;
        FileInputStream fileInputStream;
        InputStreamReader inputStreamReader;
        try {
            fileInputStream = new FileInputStream(new File("/system/build.prop"));
            try {
                inputStreamReader = new InputStreamReader(fileInputStream);
            } catch (Throwable th) {
                th = th;
                inputStreamReader = null;
                bufferedReader = null;
                close(bufferedReader);
                close(inputStreamReader);
                close(fileInputStream);
                throw th;
            }
            try {
                bufferedReader = new BufferedReader(inputStreamReader);
                while (true) {
                    try {
                        String readLine = bufferedReader.readLine();
                        if (readLine == null) {
                            break;
                        } else if (readLine.contains(str)) {
                            String[] split = readLine.split(SymbolExpUtil.SYMBOL_EQUAL);
                            if (split.length == 2 && split[0].trim().equals(str)) {
                                String trim = split[1].trim();
                                close(bufferedReader);
                                close(inputStreamReader);
                                close(fileInputStream);
                                return trim;
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        th.printStackTrace();
                        close(bufferedReader);
                        close(inputStreamReader);
                        close(fileInputStream);
                        return null;
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                bufferedReader = null;
                close(bufferedReader);
                close(inputStreamReader);
                close(fileInputStream);
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
            inputStreamReader = null;
            fileInputStream = null;
            bufferedReader = null;
            close(bufferedReader);
            close(inputStreamReader);
            close(fileInputStream);
            throw th;
        }
        close(bufferedReader);
        close(inputStreamReader);
        close(fileInputStream);
        return null;
    }

    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
