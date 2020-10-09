package com.taobao.android;

import android.app.Activity;
import android.net.Uri;
import android.view.View;
import com.alibaba.analytics.core.config.UTTPKItem;
import com.taobao.statistic.CT;
import com.taobao.statistic.TBS;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTPageStatus;
import com.ut.mini.UTTracker;
import java.util.Map;
import java.util.Properties;

public class AliUserTrackerImp implements AliUserTrackerInterface {
    private static final AliUserTrackerImp sInstance = new AliUserTrackerImp();
    private final UTTracker mUTTracker = UTAnalytics.getInstance().getDefaultTracker();

    public static AliUserTrackerImp getInstance() {
        return sInstance;
    }

    @Deprecated
    public void adv_keepKvs(String str, String... strArr) {
        TBS.Adv.keepKvs(str, strArr);
    }

    @Deprecated
    public void adv_unKeepKvs(String str, String... strArr) {
        TBS.Adv.unKeepKvs(str, strArr);
    }

    @Deprecated
    public void adv_putKvs(String str, Object obj) {
        TBS.Adv.putKvs(str, obj);
    }

    @Deprecated
    public void adv_destroy(String str, String... strArr) {
        TBS.Adv.destroy(str, strArr);
    }

    @Deprecated
    public void adv_enterWithPageName(String str, String str2, String... strArr) {
        TBS.Adv.enterWithPageName(str, str2, strArr);
    }

    @Deprecated
    public void adv_enter(String str, String... strArr) {
        TBS.Adv.enter(str, strArr);
    }

    public void adv_ctrlClicked(String str, AliUserTrackerCT aliUserTrackerCT, String str2, String... strArr) {
        TBS.Adv.ctrlClicked(str, getCt(aliUserTrackerCT), str2, strArr);
    }

    public void adv_ctrlClicked(AliUserTrackerCT aliUserTrackerCT, String str, String... strArr) {
        TBS.Adv.ctrlClicked(getCt(aliUserTrackerCT), str, strArr);
    }

    public void adv_ctrlClickedOnPage(String str, AliUserTrackerCT aliUserTrackerCT, String str2, String... strArr) {
        TBS.Adv.ctrlClickedOnPage(str, getCt(aliUserTrackerCT), str2, strArr);
    }

    public void adv_ctrlLongClicked(String str, AliUserTrackerCT aliUserTrackerCT, String str2, String... strArr) {
        TBS.Adv.ctrlLongClicked(str, getCt(aliUserTrackerCT), str2, strArr);
    }

    public void adv_ctrlLongClicked(AliUserTrackerCT aliUserTrackerCT, String str, String... strArr) {
        TBS.Adv.ctrlLongClicked(getCt(aliUserTrackerCT), str, strArr);
    }

    public void adv_ctrlLongClickedOnPage(String str, AliUserTrackerCT aliUserTrackerCT, String str2, String... strArr) {
        TBS.Adv.ctrlLongClickedOnPage(str, getCt(aliUserTrackerCT), str2, strArr);
    }

    public void adv_itemSelected(String str, AliUserTrackerCT aliUserTrackerCT, String str2, int i, String... strArr) {
        TBS.Adv.itemSelected(str, getCt(aliUserTrackerCT), str2, i, strArr);
    }

    public void adv_itemSelected(AliUserTrackerCT aliUserTrackerCT, String str, int i, String... strArr) {
        TBS.Adv.itemSelected(getCt(aliUserTrackerCT), str, i, strArr);
    }

    public void adv_itemSelectedOnPage(String str, AliUserTrackerCT aliUserTrackerCT, String str2, int i, String... strArr) {
        TBS.Adv.itemSelectedOnPage(str, getCt(aliUserTrackerCT), str2, i, strArr);
    }

    public void adv_ctrlClicked(String str, AliUserTrackerCT aliUserTrackerCT, String str2) {
        TBS.Adv.ctrlClicked(str, getCt(aliUserTrackerCT), str2);
    }

    public void adv_ctrlClickedOnPage(String str, AliUserTrackerCT aliUserTrackerCT, String str2) {
        TBS.Adv.ctrlClickedOnPage(str, getCt(aliUserTrackerCT), str2);
    }

    public void adv_ctrlLongClicked(String str, AliUserTrackerCT aliUserTrackerCT, String str2) {
        TBS.Adv.ctrlLongClicked(str, getCt(aliUserTrackerCT), str2);
    }

    public void adv_ctrlLongClickedOnPage(String str, AliUserTrackerCT aliUserTrackerCT, String str2) {
        TBS.Adv.ctrlLongClickedOnPage(str, getCt(aliUserTrackerCT), str2);
    }

    public void adv_itemSelected(String str, AliUserTrackerCT aliUserTrackerCT, String str2, int i) {
        TBS.Adv.itemSelected(str, getCt(aliUserTrackerCT), str2, i);
    }

    public void adv_itemSelectedOnPage(String str, AliUserTrackerCT aliUserTrackerCT, String str2, int i) {
        TBS.Adv.itemSelectedOnPage(str, getCt(aliUserTrackerCT), str2, i);
    }

    @Deprecated
    public void adv_easyTraceEnter(String str, boolean z, String... strArr) {
        TBS.Adv.easyTraceEnter(str, z, strArr);
    }

    @Deprecated
    public void adv_easyTraceLeave(String str, boolean z, String... strArr) {
        TBS.Adv.easyTraceLeave(str, z, strArr);
    }

    @Deprecated
    public void adv_easyTraceInternalCtrlClick(String str, String str2, String... strArr) {
        TBS.Adv.easyTraceInternalCtrlClick(str, str2, strArr);
    }

    @Deprecated
    public void adv_leave(String str, String... strArr) {
        TBS.Adv.leave(str, strArr);
    }

    @Deprecated
    public void adv_turnOffLogFriendly() {
        TBS.Adv.turnOffLogFriendly();
    }

    @Deprecated
    public void adv_onCaughException(Throwable th) {
        TBS.Adv.onCaughException(th);
    }

    @Deprecated
    public void enter(String str) {
        TBS.Page.enter(str);
    }

    @Deprecated
    public void leave(String str) {
        TBS.Page.leave(str);
    }

    @Deprecated
    public void destroy(String str) {
        TBS.Page.destroy(str);
    }

    @Deprecated
    public void create(String str, String str2) {
        TBS.Page.create(str, str2);
    }

    @Deprecated
    public void enterWithPageName(String str, String str2) {
        TBS.Page.enterWithPageName(str, str2);
    }

    @Deprecated
    public void updatePageName(String str, String str2) {
        TBS.Page.updatePageName(str, str2);
    }

    @Deprecated
    public void create(String str) {
        TBS.Page.create(str);
    }

    public void ctrlClicked(AliUserTrackerCT aliUserTrackerCT, String str) {
        TBS.Page.ctrlClicked(getCt(aliUserTrackerCT), str);
    }

    private static CT getCt(AliUserTrackerCT aliUserTrackerCT) {
        return CT.valueOf(aliUserTrackerCT.toString());
    }

    public void buttonClicked(String str) {
        TBS.Page.buttonClicked(str);
    }

    public void ctrlLongClicked(AliUserTrackerCT aliUserTrackerCT, String str) {
        TBS.Page.ctrlLongClicked(getCt(aliUserTrackerCT), str);
    }

    public void itemSelected(AliUserTrackerCT aliUserTrackerCT, String str, int i) {
        TBS.Page.itemSelected(getCt(aliUserTrackerCT), str, i);
    }

    @Deprecated
    public void goBack() {
        TBS.Page.goBack();
    }

    @Deprecated
    public void updatePageProperties(String str, Properties properties) {
        TBS.Page.updatePageProperties(str, properties);
    }

    public void setGlobalProperty(String str, String str2) {
        this.mUTTracker.setGlobalProperty(str, str2);
    }

    public String getGlobalProperty(String str) {
        return this.mUTTracker.getGlobalProperty(str);
    }

    public void removeGlobalProperty(String str) {
        this.mUTTracker.removeGlobalProperty(str);
    }

    public void send(Map<String, String> map) {
        this.mUTTracker.send(map);
    }

    public void pageAppear(Object obj) {
        this.mUTTracker.pageAppear(obj);
    }

    public void pageAppear(Object obj, String str) {
        this.mUTTracker.pageAppear(obj, str);
    }

    public void pageAppearDonotSkip(Object obj) {
        this.mUTTracker.pageAppearDonotSkip(obj);
    }

    public void pageAppearDonotSkip(Object obj, String str) {
        this.mUTTracker.pageAppearDonotSkip(obj, str);
    }

    public void pageDisAppear(Object obj) {
        this.mUTTracker.pageDisAppear(obj);
    }

    public void updateNextPageProperties(Map<String, String> map) {
        this.mUTTracker.updateNextPageProperties(map);
    }

    public void updateNextPageUtparam(String str) {
        this.mUTTracker.updateNextPageUtparam(str);
    }

    public void setPageStatusCode(Object obj, int i) {
        this.mUTTracker.setPageStatusCode(obj, i);
    }

    public void updatePageName(Object obj, String str) {
        this.mUTTracker.updatePageName(obj, str);
    }

    public void updatePageProperties(Object obj, Map<String, String> map) {
        this.mUTTracker.updatePageProperties(obj, map);
    }

    public Map<String, String> getPageProperties(Object obj) {
        return this.mUTTracker.getPageProperties(obj);
    }

    public void updatePageUtparam(Object obj, String str) {
        this.mUTTracker.updatePageUtparam(obj, str);
    }

    public void updatePageStatus(Object obj, UTPageStatus uTPageStatus) {
        this.mUTTracker.updatePageStatus(obj, uTPageStatus);
    }

    public void skipPageBack(Activity activity) {
        this.mUTTracker.skipPageBack(activity);
    }

    public void skipNextPageBack() {
        this.mUTTracker.skipNextPageBack();
    }

    public void skipPageBackForever(Activity activity, boolean z) {
        this.mUTTracker.skipPageBackForever(activity, z);
    }

    public String getPageSpmUrl(Activity activity) {
        return this.mUTTracker.getPageSpmUrl(activity);
    }

    public String getPageSpmPre(Activity activity) {
        return this.mUTTracker.getPageSpmPre(activity);
    }

    public String getPageScmPre(Activity activity) {
        return this.mUTTracker.getPageScmPre(activity);
    }

    public void updatePageUrl(Object obj, Uri uri) {
        this.mUTTracker.updatePageUrl(obj, uri);
    }

    public void addTPKItem(UTTPKItem uTTPKItem) {
        this.mUTTracker.addTPKItem(uTTPKItem);
    }

    public void addTPKCache(String str, String str2) {
        this.mUTTracker.addTPKCache(str, str2);
    }

    public void skipPage(Object obj) {
        this.mUTTracker.skipPage(obj);
    }

    public void setExposureTag(View view, String str, String str2, Map<String, String> map) {
        this.mUTTracker.setExposureTag(view, str, str2, map);
    }

    public void refreshExposureData() {
        this.mUTTracker.refreshExposureData();
    }

    public void refreshExposureData(String str) {
        this.mUTTracker.refreshExposureData(str);
    }

    public void refreshExposureDataByViewId(String str, String str2) {
        this.mUTTracker.refreshExposureDataByViewId(str, str2);
    }

    public Map<String, String> getPageAllProperties(Activity activity) {
        return this.mUTTracker.getPageAllProperties(activity);
    }

    public void commitEvent(String str, Properties properties) {
        TBS.Ext.commitEvent(str, properties);
    }

    public void commitEventBegin(String str, Properties properties) {
        TBS.Ext.commitEventBegin(str, properties);
    }

    public void commitEventEnd(String str, Properties properties) {
        TBS.Ext.commitEventEnd(str, properties);
    }

    public void commitEvent(String str, int i) {
        TBS.Ext.commitEvent(str, i);
    }

    public void commitEvent(String str, int i, Object obj) {
        TBS.Ext.commitEvent(str, i, obj);
    }

    public void commitEvent(String str, int i, Object obj, Object obj2) {
        TBS.Ext.commitEvent(str, i, obj, obj2);
    }

    public void commitEvent(String str, int i, Object obj, Object obj2, Object obj3) {
        TBS.Ext.commitEvent(str, i, obj, obj2, obj3);
    }

    public void commitEvent(String str, int i, Object obj, Object obj2, Object obj3, String... strArr) {
        TBS.Ext.commitEvent(str, i, obj, obj2, obj3, strArr);
    }

    public void commitEvent(int i) {
        TBS.Ext.commitEvent(i);
    }

    public void commitEvent(int i, Object obj) {
        TBS.Ext.commitEvent(i, obj);
    }

    public void commitEvent(int i, Object obj, Object obj2) {
        TBS.Ext.commitEvent(i, obj, obj2);
    }

    public void commitEvent(int i, Object obj, Object obj2, Object obj3) {
        TBS.Ext.commitEvent(i, obj, obj2, obj3);
    }

    public void commitEvent(int i, Object obj, Object obj2, Object obj3, String... strArr) {
        TBS.Ext.commitEvent(i, obj, obj2, obj3, strArr);
    }
}
