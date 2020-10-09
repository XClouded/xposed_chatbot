package com.uc.webview.export.internal.utility;

import android.content.Context;
import com.taobao.accs.common.Constants;
import com.taobao.weex.el.parse.Operators;
import com.uc.webview.export.cyclone.UCCyclone;
import com.uc.webview.export.internal.setup.UCSetupException;
import java.io.File;

/* compiled from: U4Source */
public final class s {
    private static String a(String str) {
        File file = new File(str);
        return UCCyclone.getSourceHash(UCCyclone.getDecompressSourceHash(str, file.length(), file.lastModified(), false));
    }

    public static boolean a(String str, Context context) {
        File a = k.a(context, Constants.KEY_FLAGS);
        String a2 = a(str);
        if (new File(a, a2 + "_n").exists()) {
            if (new File(a, a2 + "_y").exists()) {
                return false;
            }
            Log.d("VerifyUtils", "快速校验 Quick Failed [" + str + Operators.ARRAY_END_STR);
            throw new UCSetupException(3005, String.format("[%s] verifyQuick failed", new Object[]{str}));
        }
        if (!new File(a, a2 + "_y").exists()) {
            return false;
        }
        Log.d("VerifyUtils", "快速校验 Quick Success [" + str + Operators.ARRAY_END_STR);
        return true;
    }

    public static void a(String str, Context context, boolean z) {
        File a = k.a(context, Constants.KEY_FLAGS);
        String a2 = a(str);
        try {
            File file = new File(a, a2 + "_y");
            File file2 = new File(a, a2 + "_n");
            if (z) {
                file.createNewFile();
                file2.delete();
                return;
            }
            file.delete();
            file2.createNewFile();
        } catch (Exception unused) {
        }
    }
}
