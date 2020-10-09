package com.taobao.tao.log;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import com.alibaba.android.umbrella.link.export.UMLLCons;
import com.taobao.android.dinamic.DinamicConstant;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TLogUtils {
    public static String DATE_TO_STRING_SHORT_PATTERN = "yyyyMMdd";

    public static boolean checkNetworkIsWifi(Context context) {
        if (((ConnectivityManager) context.getSystemService("connectivity")).getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        return false;
    }

    public static String getNetWorkType(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            return "NOT_NETWORK";
        }
        int type = activeNetworkInfo.getType();
        if (type == 1) {
            return "WIFI";
        }
        if (type != 0) {
            return null;
        }
        switch (activeNetworkInfo.getSubtype()) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
                return "2G";
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15:
                return "3G";
            case 13:
                return "4G";
            default:
                return null;
        }
    }

    public static boolean cleanDir(File file) {
        if (!file.exists()) {
            return false;
        }
        if (!file.isDirectory()) {
            return file.delete();
        }
        File[] listFiles = file.listFiles();
        boolean z = true;
        if (listFiles != null && listFiles.length > 0) {
            for (File cleanDir : listFiles) {
                z &= cleanDir(cleanDir);
            }
        }
        return z;
    }

    public static String[] getDays(Integer num) {
        if (num.intValue() == 0) {
            return null;
        }
        Long l = 86400000L;
        try {
            Long valueOf = Long.valueOf(System.currentTimeMillis());
            Long[] lArr = new Long[num.intValue()];
            String[] strArr = new String[num.intValue()];
            for (int i = 0; i < num.intValue(); i++) {
                lArr[i] = Long.valueOf(valueOf.longValue() - (((long) i) * l.longValue()));
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TO_STRING_SHORT_PATTERN);
            for (int i2 = 0; i2 < lArr.length; i2++) {
                strArr[i2] = simpleDateFormat.format(new Date(lArr[i2].longValue()));
            }
            return strArr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> getFilePath(String str, Integer num) {
        ArrayList arrayList = null;
        if (TextUtils.isEmpty(str) || TLogInitializer.getInstance().getFileDir() == null) {
            return null;
        }
        File file = new File(TLogInitializer.getInstance().getFileDir());
        if (file.exists()) {
            File[] listFiles = file.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                return null;
            }
            arrayList = new ArrayList();
            for (int i = 0; i < listFiles.length; i++) {
                String name = listFiles[i].getName();
                if (!name.endsWith("mmap2")) {
                    if (name.equals(str)) {
                        arrayList.add(listFiles[i].getAbsolutePath());
                    } else if (name.endsWith(str)) {
                        String parseDataInName = parseDataInName(name);
                        if (parseDataInName == null || arrayList.size() <= 0) {
                            arrayList.add(listFiles[i].getAbsolutePath());
                        } else {
                            int size = arrayList.size();
                            int i2 = 0;
                            while (true) {
                                if (i2 >= size) {
                                    break;
                                }
                                String parseDataInName2 = parseDataInName(new File((String) arrayList.get(i2)).getName());
                                if (parseDataInName2 != null) {
                                    if (parseDataInName.compareTo(parseDataInName2) <= 0) {
                                        arrayList.add(i2, listFiles[i].getAbsolutePath());
                                        break;
                                    } else if (i2 == size - 1) {
                                        arrayList.add(listFiles[i].getAbsolutePath());
                                        break;
                                    }
                                }
                                i2++;
                            }
                        }
                    }
                }
            }
        }
        if (num.intValue() > 0 && arrayList != null && arrayList.size() > num.intValue()) {
            int size2 = arrayList.size() - num.intValue();
            for (int i3 = 0; i3 < size2; i3++) {
                arrayList.remove(0);
            }
        }
        return arrayList;
    }

    public static List<String> getFilePath(String str, int i, String[] strArr) {
        ArrayList arrayList = null;
        if (TextUtils.isEmpty(str) || TLogInitializer.getInstance().getFileDir() == null) {
            return null;
        }
        File file = new File(TLogInitializer.getInstance().getFileDir());
        if (file.exists()) {
            File[] listFiles = file.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                return null;
            }
            arrayList = new ArrayList();
            for (int i2 = 0; i2 < listFiles.length; i2++) {
                String name = listFiles[i2].getName();
                if (!name.endsWith("mmap2")) {
                    if (name.equals(str)) {
                        arrayList.add(listFiles[i2].getAbsolutePath());
                    } else if (name.startsWith(str)) {
                        if (strArr != null) {
                            for (String contains : strArr) {
                                if (name.contains(contains)) {
                                    arrayList.add(listFiles[i2].getAbsolutePath());
                                }
                            }
                        } else {
                            String parseDataInName = parseDataInName(name);
                            if (parseDataInName == null || arrayList.size() <= 0) {
                                arrayList.add(listFiles[i2].getAbsolutePath());
                            } else {
                                int size = arrayList.size();
                                int i3 = 0;
                                while (true) {
                                    if (i3 >= size) {
                                        break;
                                    }
                                    String parseDataInName2 = parseDataInName(new File((String) arrayList.get(i3)).getName());
                                    if (parseDataInName2 != null) {
                                        if (parseDataInName.compareTo(parseDataInName2) <= 0) {
                                            arrayList.add(i3, listFiles[i2].getAbsolutePath());
                                            break;
                                        } else if (i3 == size - 1) {
                                            arrayList.add(listFiles[i2].getAbsolutePath());
                                            break;
                                        }
                                    }
                                    i3++;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (i > 0 && arrayList != null && arrayList.size() > i) {
            int size2 = arrayList.size() - i;
            for (int i4 = 0; i4 < size2; i4++) {
                arrayList.remove(0);
            }
        }
        return arrayList;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0017, code lost:
        r4 = r4[1];
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String parseDataInName(java.lang.String r4) {
        /*
            java.lang.String r0 = "_"
            java.lang.String[] r4 = r4.split(r0)
            r0 = 1
            if (r4 == 0) goto L_0x0010
            int r1 = r4.length
            r2 = 3
            if (r1 != r2) goto L_0x0010
            r4 = r4[r0]
            goto L_0x0028
        L_0x0010:
            r1 = 0
            if (r4 == 0) goto L_0x0029
            int r2 = r4.length
            r3 = 2
            if (r2 != r3) goto L_0x0029
            r4 = r4[r0]
            java.lang.String r0 = "."
            int r0 = r4.indexOf(r0)
            r2 = -1
            if (r0 != r2) goto L_0x0023
            return r1
        L_0x0023:
            r1 = 0
            java.lang.String r4 = r4.substring(r1, r0)
        L_0x0028:
            return r4
        L_0x0029:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.tao.log.TLogUtils.parseDataInName(java.lang.String):java.lang.String");
    }

    public static Map<String, LogLevel> makeModule(String str) {
        HashMap hashMap = null;
        if (TextUtils.isEmpty(str) || str.equalsIgnoreCase(TLogConstant.TLOG_MODULE_OFF)) {
            return null;
        }
        String[] split = str.split(",");
        if (split != null) {
            hashMap = new HashMap();
            for (String split2 : split) {
                String[] split3 = split2.split(DinamicConstant.DINAMIC_PREFIX_AT);
                if (split3 != null && split3.length > 0) {
                    if (split3.length == 1) {
                        hashMap.put(split3[0].toLowerCase(), LogLevel.E);
                    } else if (split3.length == 2) {
                        hashMap.put(split3[0].toLowerCase(), convertLogLevel(split3[1]));
                    }
                }
            }
        }
        return hashMap;
    }

    public static LogLevel convertLogLevel(String str) {
        LogLevel logLevel = LogLevel.L;
        if ("ERROR".equalsIgnoreCase(str)) {
            return LogLevel.E;
        }
        if ("WARN".equalsIgnoreCase(str)) {
            return LogLevel.W;
        }
        if ("INFO".equalsIgnoreCase(str)) {
            return LogLevel.I;
        }
        if (UMLLCons.FEATURE_TYPE_DEBUG.equalsIgnoreCase(str)) {
            return LogLevel.D;
        }
        return "VERBOSE".equalsIgnoreCase(str) ? LogLevel.V : logLevel;
    }
}
