package com.taobao.android;

import android.app.Activity;
import java.util.Map;

public interface AliUserTrackerInterface {
    void adv_ctrlClicked(String str, AliUserTrackerCT aliUserTrackerCT, String str2, String... strArr);

    void adv_ctrlClickedOnPage(String str, AliUserTrackerCT aliUserTrackerCT, String str2, String... strArr);

    void buttonClicked(String str);

    void commitEvent(int i, Object obj, Object obj2, Object obj3, String... strArr);

    void commitEvent(String str, int i, Object obj, Object obj2, Object obj3, String... strArr);

    void ctrlClicked(AliUserTrackerCT aliUserTrackerCT, String str);

    void ctrlLongClicked(AliUserTrackerCT aliUserTrackerCT, String str);

    Map<String, String> getPageAllProperties(Activity activity);

    void itemSelected(AliUserTrackerCT aliUserTrackerCT, String str, int i);

    void pageAppear(Object obj, String str);

    void pageDisAppear(Object obj);

    void updateNextPageProperties(Map<String, String> map);

    void updatePageName(Object obj, String str);

    void updatePageProperties(Object obj, Map<String, String> map);
}
