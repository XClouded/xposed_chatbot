package com.alimama.union.app.distributionCenter;

import alimama.com.unwrouter.PageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.taobao.windvane.WindVaneSDK;
import android.text.TextUtils;
import android.util.Log;
import com.alimama.moon.App;
import com.alimama.moon.R;
import com.alimama.moon.ui.BaseActivity;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.union.app.configcenter.ConfigKeyList;
import com.alimama.union.app.pagerouter.AppPageInfo;
import com.alimama.union.app.pagerouter.MoonComponentManager;
import com.alimamaunion.base.configcenter.EtaoConfigCenter;
import com.alimamaunion.base.safejson.SafeJSONArray;
import com.alimamaunion.base.safejson.SafeJSONObject;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class DistributionCenterActivity extends BaseActivity {
    private ArrayList<String> configRegexList;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_distribution_center);
        initLoginConfig();
        initData();
    }

    private void initLoginConfig() {
        App.getAppComponent().inject((BaseActivity) this);
        if (this.login != null) {
            this.login.registerReceiver();
            this.login.autoLogin();
        }
    }

    private void initData() {
        boolean z;
        String dataString = getIntent().getDataString();
        if (TextUtils.isEmpty(dataString)) {
            Log.d("DistributionCenter", "url  is  empty");
            finish();
            return;
        }
        UTHelper.DistributeCenterPage.trackControlClick("url", dataString, UTHelper.DistributeCenterPage.INVOKE_CONTROL_NAME);
        Uri parse = Uri.parse(dataString);
        if (parse == null || !TextUtils.equals(parse.getScheme(), "moon") || !TextUtils.equals(parse.getHost(), PageInfo.PAGE_WEBVIEW)) {
            MoonComponentManager.getInstance().getPageRouter().gotoPage(AppPageInfo.PAGE_HOME);
        } else {
            String queryParameter = parse.getQueryParameter("url");
            if (!isTrustConfigCenterRegex()) {
                z = WindVaneSDK.isTrustedUrl(queryParameter);
            } else {
                z = isMatchConfigCenterRegex(queryParameter);
            }
            if (z) {
                MoonComponentManager.getInstance().getPageRouter().gotoPage(queryParameter);
            } else {
                UTHelper.DistributeCenterPage.trackControlClick("url", dataString, UTHelper.DistributeCenterPage.INVOKE_FAIL_CONTROL_NAME);
                MoonComponentManager.getInstance().getPageRouter().gotoPage(AppPageInfo.PAGE_HOME);
            }
        }
        finish();
    }

    private boolean isTrustConfigCenterRegex() {
        return EtaoConfigCenter.getInstance().getSwitch(ConfigKeyList.UNION_SWITCH, "sheme_url_judge", false);
    }

    private boolean isMatchConfigCenterRegex(String str) {
        this.configRegexList = new ArrayList<>();
        parseUnionSchemeRegex();
        for (int i = 0; i < this.configRegexList.size(); i++) {
            if (Pattern.compile(this.configRegexList.get(i)).matcher(str).find()) {
                return true;
            }
        }
        return false;
    }

    public void parseUnionSchemeRegex() {
        String configResult = EtaoConfigCenter.getInstance().getConfigResult(ConfigKeyList.UNION_SCHEME_REGEX);
        if (!TextUtils.isEmpty(configResult)) {
            try {
                SafeJSONArray optJSONArray = new SafeJSONObject(new SafeJSONObject(configResult).optJSONObject("data").optString("schemeRegexJson")).optJSONArray("itemUrl");
                for (int i = 0; i < optJSONArray.length(); i++) {
                    this.configRegexList.add(optJSONArray.optString(i));
                }
            } catch (Exception unused) {
            }
        }
    }
}
