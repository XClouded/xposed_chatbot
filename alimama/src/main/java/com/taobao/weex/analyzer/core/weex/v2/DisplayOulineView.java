package com.taobao.weex.analyzer.core.weex.v2;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.taobao.weex.analyzer.R;
import com.taobao.weex.analyzer.core.weex.v2.PerformanceV2Repository;
import com.taobao.weex.analyzer.utils.DeviceUtils;
import com.taobao.weex.analyzer.utils.SDKUtils;
import com.taobao.weex.analyzer.view.overlay.AbstractBizItemView;
import com.taobao.weex.performance.WXInstanceApm;
import java.util.Locale;
import java.util.Map;

public class DisplayOulineView extends AbstractBizItemView<PerformanceV2Repository.APMInfo> {
    private TextView applicationInfoView;
    private TextView bundleSizeView;
    private TextView bundleTypeView;
    private TextView bundleUrlView;
    private TextView containerInfoView;
    private TextView errorCodeView;
    private TextView jsfmVersionView;
    /* access modifiers changed from: private */
    public String mBundleUrl;
    private TextView requestTypeView;
    private TextView sdkVersionView;
    private TextView systemInfoView;

    public DisplayOulineView(Context context) {
        super(context);
    }

    public DisplayOulineView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DisplayOulineView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* access modifiers changed from: protected */
    public void prepareView() {
        this.bundleUrlView = (TextView) findViewById(R.id.bundle_url);
        this.sdkVersionView = (TextView) findViewById(R.id.sdk_version);
        this.jsfmVersionView = (TextView) findViewById(R.id.jsfm_version);
        this.bundleTypeView = (TextView) findViewById(R.id.bundle_type);
        this.requestTypeView = (TextView) findViewById(R.id.request_type);
        this.errorCodeView = (TextView) findViewById(R.id.error_code);
        this.bundleSizeView = (TextView) findViewById(R.id.bundle_size);
        this.systemInfoView = (TextView) findViewById(R.id.system_info);
        this.applicationInfoView = (TextView) findViewById(R.id.application_info);
        this.containerInfoView = (TextView) findViewById(R.id.container_name);
        this.bundleUrlView.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View view) {
                SDKUtils.copyToClipboard(view.getContext(), DisplayOulineView.this.mBundleUrl, true);
                return true;
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void render(@NonNull PerformanceV2Repository.APMInfo aPMInfo) {
        Map<String, Object> map = aPMInfo.propertyMap;
        if (!map.isEmpty()) {
            this.mBundleUrl = (String) map.get(WXInstanceApm.KEY_PAGE_PROPERTIES_BUBDLE_URL);
            if (TextUtils.isEmpty(this.mBundleUrl)) {
                this.mBundleUrl = (String) map.get(WXInstanceApm.KEY_PAGE_PROPERTIES_BIZ_ID);
            }
            String str = (String) map.get(WXInstanceApm.KEY_PAGE_PROPERTIES_WEEX_VERSION);
            String str2 = (String) map.get(WXInstanceApm.KEY_PAGE_PROPERTIES_JSLIB_VERSION);
            String str3 = (String) map.get(WXInstanceApm.KEY_PAGE_PROPERTIES_BUNDLE_TYPE);
            String str4 = (String) map.get(WXInstanceApm.KEY_PAGE_PROPERTIES_REQUEST_TYPE);
            String str5 = (String) map.get(WXInstanceApm.KEY_PROPERTIES_ERROR_CODE);
            String str6 = (String) map.get(WXInstanceApm.KEY_PAGE_PROPERTIES_CONTAINER_NAME);
            this.bundleUrlView.setText(TextUtils.isEmpty(this.mBundleUrl) ? "NA" : this.mBundleUrl);
            TextView textView = this.sdkVersionView;
            if (TextUtils.isEmpty(str)) {
                str = "NA";
            }
            textView.setText(str);
            TextView textView2 = this.jsfmVersionView;
            if (TextUtils.isEmpty(str2)) {
                str2 = "NA";
            }
            textView2.setText(str2);
            TextView textView3 = this.bundleTypeView;
            if (TextUtils.isEmpty(str3)) {
                str3 = "NA";
            }
            textView3.setText(str3);
            TextView textView4 = this.requestTypeView;
            if (TextUtils.isEmpty(str4)) {
                str4 = "NA";
            }
            textView4.setText(str4);
            TextView textView5 = this.errorCodeView;
            if (TextUtils.isEmpty(str5)) {
                str5 = "NA";
            }
            textView5.setText(str5);
            TextView textView6 = this.containerInfoView;
            if (TextUtils.isEmpty(str6)) {
                str6 = "NA";
            }
            textView6.setText(str6);
            this.systemInfoView.setText(String.format(Locale.getDefault(), "%s / %s", new Object[]{DeviceUtils.getDeviceModel(), DeviceUtils.getOSVersion()}));
            this.applicationInfoView.setText(String.format(Locale.getDefault(), "%s / %s", new Object[]{DeviceUtils.getAppName(getContext()), DeviceUtils.getAppVersion(getContext())}));
            Map<String, Object> map2 = aPMInfo.statsMap;
            if (!map2.isEmpty() && map2.get(WXInstanceApm.KEY_PAGE_STATS_BUNDLE_SIZE) != null) {
                Object obj = map2.get(WXInstanceApm.KEY_PAGE_STATS_BUNDLE_SIZE);
                if (obj instanceof Double) {
                    TextView textView7 = this.bundleSizeView;
                    textView7.setText(((Double) obj).doubleValue() + "KB");
                    return;
                }
                TextView textView8 = this.bundleSizeView;
                textView8.setText(obj.toString() + "KB");
            }
        }
    }

    /* access modifiers changed from: protected */
    public int getLayoutResId() {
        return R.layout.wxt_display_properties;
    }
}
