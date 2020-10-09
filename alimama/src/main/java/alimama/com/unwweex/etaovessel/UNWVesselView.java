package alimama.com.unwweex.etaovessel;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.taobao.vessel.VesselView;
import com.taobao.vessel.base.VesselBaseView;
import com.taobao.vessel.local.VesselNativeView;
import com.taobao.vessel.model.VesselError;
import com.taobao.vessel.utils.Utils;
import com.taobao.vessel.utils.VesselType;

public class UNWVesselView extends VesselView {
    private VesselViewCallBack callBack;
    private Icreater icreater;

    public interface Icreater {
        VesselBaseView getNativeView(Context context);

        VesselBaseView getVesselWebView(Context context);

        VesselBaseView getVesselWeexView(Context context);
    }

    public UNWVesselView(Context context) {
        super(context);
    }

    public UNWVesselView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public UNWVesselView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void setCallBack(VesselViewCallBack vesselViewCallBack) {
        this.callBack = vesselViewCallBack;
    }

    public void setCreater(Icreater icreater2) {
        this.icreater = icreater2;
    }

    public void loadUrl(VesselType vesselType, String str, Object obj) {
        if (vesselType == null) {
            vesselType = Utils.getUrlType(str);
        }
        if (vesselType == null) {
            onLoadError(new VesselError());
            return;
        }
        this.mOriginUrl = str;
        this.mOriginParams = obj;
        this.mCurrentVesselType = vesselType;
        this.mProxyView = createView(getContext(), vesselType);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -1);
        if (this.mProxyView.getParent() == null) {
            addView(this.mProxyView, layoutParams);
        }
        if (this.mVesselViewCallback != null) {
            this.mProxyView.setVesselViewCallback(this.mVesselViewCallback);
        }
        this.mProxyView.mInstanceId = this.mInstanceId;
        this.mProxyView.setOnLoadListener(this);
        this.mProxyView.loadUrl(str, obj);
        this.mProxyView.setOnScrollViewListener(this);
    }

    private VesselBaseView createView(Context context, VesselType vesselType) {
        switch (vesselType) {
            case Weex:
                return getVesselWeexView(context);
            case Native:
                return getNativeView(context);
            default:
                return getVesselWebView(context);
        }
    }

    public VesselBaseView getVesselWebView(Context context) {
        if (this.icreater != null && this.icreater.getVesselWebView(context) != null) {
            return this.icreater.getVesselWebView(context);
        }
        UNWVesselWebview uNWVesselWebview = new UNWVesselWebview(context);
        if (this.callBack != null) {
            uNWVesselWebview.setCallBack(this.callBack);
        }
        return uNWVesselWebview;
    }

    public VesselBaseView getVesselWeexView(Context context) {
        if (this.icreater != null && this.icreater.getVesselWeexView(context) != null) {
            return this.icreater.getVesselWeexView(context);
        }
        UNWWeexView uNWWeexView = new UNWWeexView(context);
        if (this.callBack != null) {
            uNWWeexView.setCallBack(this.callBack);
        }
        return uNWWeexView;
    }

    public VesselBaseView getNativeView(Context context) {
        if (this.icreater == null || this.icreater.getNativeView(context) == null) {
            return new VesselNativeView(context);
        }
        return this.icreater.getNativeView(context);
    }
}
