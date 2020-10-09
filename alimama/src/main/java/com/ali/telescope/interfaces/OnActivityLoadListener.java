package com.ali.telescope.interfaces;

import android.app.Activity;
import java.util.HashMap;

public interface OnActivityLoadListener {
    void onActivityLoadFinish(Activity activity, HashMap<String, String> hashMap);

    void onActivityLoadStart(Activity activity, HashMap<String, String> hashMap);
}
