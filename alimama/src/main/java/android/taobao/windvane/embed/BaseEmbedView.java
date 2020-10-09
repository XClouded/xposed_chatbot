package android.taobao.windvane.embed;

import android.app.Activity;
import android.content.Context;
import android.content.MutableContextWrapper;
import android.graphics.Bitmap;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.webview.IWVWebView;
import android.text.TextUtils;
import android.view.View;

import com.uc.webview.export.extension.EmbedViewConfig;
import com.uc.webview.export.extension.IEmbedView;
import com.uc.webview.export.extension.IEmbedViewContainer;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseEmbedView extends WVApiPlugin implements IEmbedView, IEmbedViewContainer.OnParamChangedListener, IEmbedViewContainer.OnStateChangedListener, IEmbedViewContainer.OnVisibilityChangedListener {
    Context context;
    public String id = "";
    public EmbedViewConfig params = null;
    public View view = null;
    public IWVWebView webView;

    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        return false;
    }

    /* access modifiers changed from: protected */
    public abstract View generateView(Context context2);

    public Bitmap getSnapShot() {
        return null;
    }

    /* access modifiers changed from: protected */
    public abstract String getViewType();

    public void onAttachedToWebView() {
    }

    public void onDetachedFromWebView() {
    }

    public void onParamChanged(String[] strArr, String[] strArr2) {
    }

    public void onVisibilityChanged(int i) {
    }

    public boolean init(String str, String str2, IWVWebView iWVWebView, EmbedViewConfig embedViewConfig) {
        String str3;
        if (!str2.equals(getViewType()) || iWVWebView == null) {
            return false;
        }
        if (TextUtils.isEmpty(str)) {
            str3 = "WVEmbedView";
        } else {
            str3 = "WVEmbedView_" + str;
        }
        iWVWebView.addJsObject(str3, this);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("bridgeId", str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        iWVWebView.evaluateJavascript(String.format("javascript:window.WindVane&&window.WindVane.fireEvent('WVEmbed.Ready',%s);", new Object[]{jSONObject.toString()}));
        this.context = iWVWebView.getContext();
        if (iWVWebView.getContext() instanceof MutableContextWrapper) {
            this.context = ((MutableContextWrapper) iWVWebView.getContext()).getBaseContext();
        }
        if (!(this.context instanceof Activity)) {
            this.context = null;
        }
        this.webView = iWVWebView;
        this.params = embedViewConfig;
        return true;
    }

    public View getView() {
        if (this.view != null) {
            return this.view;
        }
        if (this.context == null) {
            return null;
        }
        this.view = generateView(this.context);
        return this.view;
    }

    public void onDestroy() {
        this.view = null;
        this.context = null;
    }
}
