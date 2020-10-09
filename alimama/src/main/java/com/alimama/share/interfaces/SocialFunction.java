package com.alimama.share.interfaces;

import android.net.Uri;
import java.util.ArrayList;

public abstract class SocialFunction {
    public abstract void shareImage(Uri uri, Boolean bool, String str, String str2);

    public abstract void shareImages(ArrayList<Uri> arrayList, Boolean bool, String str, String str2);

    public abstract void shareText(String str, boolean z, String str2);
}
