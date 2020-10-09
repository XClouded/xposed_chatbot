package com.taobao.android;

import com.taobao.statistic.TBS;
import java.util.Properties;

public class PageAdapter {
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

    @Deprecated
    public void updatePageProperties(String str, Properties properties) {
        TBS.Page.updatePageProperties(str, properties);
    }

    @Deprecated
    public void goBack() {
        TBS.Page.goBack();
    }
}
