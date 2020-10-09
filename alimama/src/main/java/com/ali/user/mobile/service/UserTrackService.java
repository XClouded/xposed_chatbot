package com.ali.user.mobile.service;

import android.app.Activity;
import java.util.Properties;

public interface UserTrackService {
    void pageDisAppear(Activity activity);

    void sendControlUT(String str, String str2);

    void sendControlUT(String str, String str2, String str3);

    void sendUT(int i);

    void sendUT(String str);

    void sendUT(String str, String str2);

    void sendUT(String str, String str2, String str3, String str4, Properties properties);

    void sendUT(String str, String str2, String str3, Properties properties);

    void sendUT(String str, String str2, Properties properties);

    void sendUT(String str, Properties properties);

    void skipPage(Activity activity);

    void updatePageName(Activity activity, String str);
}
