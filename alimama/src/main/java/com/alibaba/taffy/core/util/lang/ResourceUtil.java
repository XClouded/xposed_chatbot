package com.alibaba.taffy.core.util.lang;

import android.content.Context;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ResourceUtil {
    public static int getResourceIdByName(String str, String str2, String str3) {
        try {
            Class[] classes = Class.forName(str + ".R").getClasses();
            Class cls = null;
            int length = classes.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                Class cls2 = classes[i];
                if (cls2.getName().split("\\$")[1].equals(str2)) {
                    cls = cls2;
                    break;
                }
                i++;
            }
            if (cls != null) {
                return cls.getField(str3).getInt(cls);
            }
            return 0;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return 0;
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
            return 0;
        } catch (SecurityException e3) {
            e3.printStackTrace();
            return 0;
        } catch (IllegalAccessException e4) {
            e4.printStackTrace();
            return 0;
        } catch (NoSuchFieldException e5) {
            e5.printStackTrace();
            return 0;
        }
    }

    public static String geTextFromAssets(Context context, String str) throws IOException {
        if (context == null || StringUtil.isBlank(str)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getResources().getAssets().open(str)));
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine == null) {
                return sb.toString();
            }
            sb.append(readLine);
        }
    }

    public static String geTextFromRaw(Context context, int i) throws IOException {
        if (context == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(i)));
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine == null) {
                return sb.toString();
            }
            sb.append(readLine);
        }
    }
}
