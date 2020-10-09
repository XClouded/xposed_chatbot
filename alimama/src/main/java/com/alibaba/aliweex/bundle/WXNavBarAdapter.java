package com.alibaba.aliweex.bundle;

import android.app.Activity;
import android.net.Uri;
import android.text.TextUtils;
import androidx.fragment.app.FragmentActivity;
import com.alibaba.aliweex.adapter.INavigationBarModuleAdapter;
import com.taobao.weex.appfram.navigator.IActivityNavBarSetter;
import com.taobao.weex.utils.WXLogUtils;
import org.json.JSONObject;

public abstract class WXNavBarAdapter extends INavigationBarModuleAdapter implements IActivityNavBarSetter {
    private static final String TAG = "WXNavBarAdapter";
    private FragmentActivity mActivity;

    public boolean pop(String str) {
        return false;
    }

    public abstract void push(Activity activity, String str, JSONObject jSONObject);

    public WXNavBarAdapter(FragmentActivity fragmentActivity) {
        this.mActivity = fragmentActivity;
    }

    public boolean push(String str) {
        if (TextUtils.isEmpty(str)) {
            return true;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            String optString = jSONObject.optString("url", "");
            if (TextUtils.isEmpty(optString)) {
                return true;
            }
            Uri parse = Uri.parse(optString);
            String scheme = parse.getScheme();
            Uri.Builder buildUpon = parse.buildUpon();
            if (!TextUtils.equals(scheme, "http") && !TextUtils.equals(scheme, "https")) {
                buildUpon.scheme("http");
            }
            push(getFragmentActivity(), optString, jSONObject);
            return true;
        } catch (Exception e) {
            WXLogUtils.e(TAG, WXLogUtils.getStackTrace(e));
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public void destroy() {
        this.mActivity = null;
    }

    /* access modifiers changed from: protected */
    public FragmentActivity getFragmentActivity() {
        return this.mActivity;
    }
}
