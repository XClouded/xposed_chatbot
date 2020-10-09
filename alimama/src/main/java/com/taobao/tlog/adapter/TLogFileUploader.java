package com.taobao.tlog.adapter;

import android.content.Context;
import android.util.Log;
import com.taobao.tao.log.upload.FileUploadListener;
import com.taobao.tao.log.upload.LogFileUploadManager;
import com.uc.webview.export.extension.UCCore;
import java.util.HashMap;
import java.util.Map;

public class TLogFileUploader {
    private static final String KEY_TYPE = "type";
    private static boolean isValid = false;

    static {
        try {
            Class.forName("com.taobao.tao.log.TLog");
            isValid = true;
        } catch (ClassNotFoundException unused) {
            isValid = false;
        }
    }

    public static void uploadLogFile(Context context, String str, String str2, Map<String, String> map) {
        new LogFileUploadManager(context).uploadWithFilePrefix(str, str2, map, (FileUploadListener) new FileUploadListener() {
            public void onError(String str, String str2, String str3) {
                Log.w("TlogAdapter", "uploadWithFilePrefix failure!" + str2 + " msg:" + str3);
            }

            public void onSucessed(String str, String str2) {
                Log.w("TlogAdapter", "uploadWithFilePrefix success!");
            }
        });
    }

    public void uploadWithFilePath(Context context, String str, String str2, String str3, String str4, Map<String, String> map) {
        new LogFileUploadManager(context).uploadWithFilePath(str, str2, str3, str4, map, new FileUploadListener() {
            public void onError(String str, String str2, String str3) {
                Log.w("TlogAdapter", "uploadWithFilePath failure! " + str2 + " msg:" + str3);
            }

            public void onSucessed(String str, String str2) {
                Log.w("TlogAdapter", "uploadWithFilePath success!");
            }
        });
    }

    @Deprecated
    public static void uploadLogFile(Context context, String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("type", str);
        uploadLogFile(context, (Map<String, Object>) hashMap);
    }

    @Deprecated
    public static void uploadLogFile(Context context, Map<String, Object> map) {
        uploadLogFile(context, map, (String) null);
    }

    @Deprecated
    public static void uploadLogFile(Context context, Map<String, Object> map, String str) {
        if (isValid) {
            HashMap hashMap = new HashMap();
            if (map != null) {
                Object obj = map.get("type");
                if (!(obj instanceof String) || !UCCore.EVENT_EXCEPTION.equalsIgnoreCase((String) obj)) {
                    for (Map.Entry next : map.entrySet()) {
                        String str2 = (String) next.getKey();
                        if (next.getValue() instanceof String) {
                            hashMap.put(str2, (String) next.getValue());
                        }
                    }
                } else {
                    return;
                }
            }
            new LogFileUploadManager(context).uploadWithFilePrefix("FEEDBACK", "taobao4android_feedback_21646297", (Map<String, String>) hashMap, (FileUploadListener) new FileUploadListener() {
                public void onError(String str, String str2, String str3) {
                    Log.w("TlogAdapter", "uploadWithFilePrefix failure! " + str2 + " msg:" + str3);
                }

                public void onSucessed(String str, String str2) {
                    Log.w("TlogAdapter", "uploadWithFilePrefix success!");
                }
            });
        }
    }
}
