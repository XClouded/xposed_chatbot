package com.alimama.union.app.pagerouter;

import alimama.com.unwbase.interfaces.IRouter;
import alimama.com.unwrouter.PageInfo;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class EmptyRouter implements IRouter<PageInfo> {
    public void finishAll() {
    }

    public ArrayList<WeakReference<Activity>> getBackStack() {
        return null;
    }

    public Activity getCurrentActivity() {
        return null;
    }

    public PageInfo getCurrentPageInfo() {
        return null;
    }

    public Class<?> getHomeClass() {
        return null;
    }

    public ArrayList<String> getOnResumeActiList() {
        return null;
    }

    public void gotoPage(PageInfo pageInfo) {
    }

    public void gotoPage(PageInfo pageInfo, int i) {
    }

    public void gotoPage(PageInfo pageInfo, Uri uri) {
    }

    public void gotoPage(PageInfo pageInfo, Bundle bundle) {
    }

    public void gotoPage(PageInfo pageInfo, Bundle bundle, int i) {
    }

    public void gotoPage(String str, boolean z) {
    }

    public boolean gotoPage(@Nullable String str) {
        return false;
    }

    public boolean gotoPage(String str, Bundle bundle) {
        return false;
    }

    public boolean gotoPage(@Nullable String str, Bundle bundle, int i) {
        return false;
    }

    public void gotoPageWithUrl(PageInfo pageInfo, Uri uri, Bundle bundle, int i, boolean z) {
    }

    public void init() {
    }

    public boolean isBackground() {
        return false;
    }

    public boolean isFirstEnterHome() {
        return false;
    }

    public boolean isLastPage() {
        return false;
    }

    public void onCreate(Activity activity) {
    }

    public void onDestroy(Activity activity) {
    }

    public void onPause(Activity activity) {
    }

    public void onResume(Activity activity) {
    }

    public void onStop(Activity activity) {
    }

    public void popUpLastActivity(Activity activity) {
    }

    public void setCurrent(Activity activity) {
    }

    public void setHomeActivity(Class<?> cls) {
    }

    public void turnToPage(PageInfo pageInfo, Uri uri, Bundle bundle, int i) {
    }
}
