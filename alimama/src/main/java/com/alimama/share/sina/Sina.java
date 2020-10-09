package com.alimama.share.sina;

import android.content.Context;
import android.net.Uri;
import com.alimama.share.interfaces.SocialFunction;
import java.util.ArrayList;

public class Sina extends SocialFunction {
    private Context context;

    public void shareImage(Uri uri, Boolean bool, String str, String str2) {
    }

    public void shareImages(ArrayList<Uri> arrayList, Boolean bool, String str, String str2) {
    }

    public void shareText(String str, boolean z, String str2) {
    }

    public Sina(Context context2) {
        this.context = context2;
    }
}
