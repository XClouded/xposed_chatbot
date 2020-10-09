package com.taobao.weex.compontent;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import com.alipay.sdk.cons.c;
import com.taobao.android.nav.Nav;
import com.taobao.uikit.extend.R;
import com.taobao.uikit.extend.component.TBErrorView;
import com.taobao.uikit.extend.component.error.Error;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class WXErrorViewComponent extends WXComponent<TBErrorView> {
    public String errorCode;
    public String errorMsg;
    private WeakReference<WXSDKInstance> instance;
    public String mappingCode;
    public int responseCode;

    public WXErrorViewComponent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
        this.instance = new WeakReference<>(wXSDKInstance);
    }

    /* access modifiers changed from: protected */
    public TBErrorView initComponentHostView(@NonNull final Context context) {
        TBErrorView tBErrorView = new TBErrorView(context);
        try {
            if (!(this.instance == null || this.instance.get() == null || !((WXSDKInstance) this.instance.get()).getContainerInfo().containsKey("wml_feed_url"))) {
                final String str = ((WXSDKInstance) this.instance.get()).getContainerInfo().get("wml_feed_url");
                Button button = (Button) tBErrorView.findViewById(R.id.uik_errorButtonNag);
                if (button != null) {
                    button.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            if (!TextUtils.isEmpty(str) && context != null) {
                                Nav.from(context).toUri(str);
                            }
                        }
                    });
                }
            }
        } catch (Throwable unused) {
        }
        return tBErrorView;
    }

    @WXComponentProp(name = "error")
    public void setError(Map<String, Object> map) {
        if (map.containsKey("responsecode")) {
            this.responseCode = ((Integer) map.get("responsecode")).intValue();
        }
        if (map.containsKey("errormsg")) {
            this.errorMsg = map.get("errormsg").toString();
        }
        if (map.containsKey("mappingcode")) {
            this.mappingCode = (String) map.get("mappingcode");
        }
        if (map.containsKey("errorcode")) {
            this.errorCode = (String) map.get("errorcode");
        }
        Error fromMtopResponse = Error.Factory.fromMtopResponse(this.responseCode, this.mappingCode, this.errorCode, this.errorMsg);
        if (map.containsKey("url")) {
            fromMtopResponse.url = String.valueOf(map.get("url"));
        }
        if (map.containsKey(c.n)) {
            fromMtopResponse.apiName = String.valueOf(map.get(c.n));
        }
        ((TBErrorView) getHostView()).setError(fromMtopResponse);
    }

    @WXComponentProp(name = "status")
    public void setStatus(String str) {
        ((TBErrorView) getHostView()).setStatus(TBErrorView.Status.valueOf(str));
    }

    @WXComponentProp(name = "title")
    public void setTitle(String str) {
        ((TBErrorView) getHostView()).setTitle(str);
    }

    @WXComponentProp(name = "subtitle")
    public void setSubTitle(String str) {
        ((TBErrorView) getHostView()).setSubTitle(str);
    }

    @WXComponentProp(name = "icon")
    public void setIconUrl(String str) {
        ((TBErrorView) getHostView()).setIconUrl(str);
    }

    @WXComponentProp(name = "leftbutton")
    public void setLeftButton(Map<String, Object> map) {
        if (map != null && map.containsKey("text") && map.containsKey("visibility")) {
            ((TBErrorView) getHostView()).setButton(TBErrorView.ButtonType.BUTTON_LEFT, (String) map.get("text"), new View.OnClickListener() {
                public void onClick(View view) {
                    WXErrorViewComponent.this.getInstance().fireEvent(WXErrorViewComponent.this.getRef(), "onleftbuttonclick", new HashMap());
                }
            });
            ((TBErrorView) getHostView()).setButtonVisibility(TBErrorView.ButtonType.BUTTON_LEFT, mapVisibility((String) map.get("visibility")));
        }
    }

    @WXComponentProp(name = "rightbutton")
    public void setRightButton(Map<String, Object> map) {
        if (map != null && map.containsKey("visibility")) {
            ((TBErrorView) getHostView()).setButtonVisibility(TBErrorView.ButtonType.BUTTON_RIGHT, mapVisibility((String) map.get("visibility")));
        }
    }

    private int mapVisibility(String str) {
        if ("GONE".equals(str)) {
            return 8;
        }
        if ("INVISIBLE".equals(str)) {
            return 4;
        }
        boolean equals = "VISIBLE".equals(str);
        return 0;
    }
}
