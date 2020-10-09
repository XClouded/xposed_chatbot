package com.taobao.vessel.base;

import android.view.View;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class VesselCallbackManager {
    private static final VesselCallbackManager manager = new VesselCallbackManager();
    private static AtomicInteger sInstanceId = new AtomicInteger(0);
    private Map<Object, VesselBaseView> mMap = new HashMap();
    private Map<String, VesselBaseView> sViews = new HashMap();

    @Deprecated
    public void bindCallbackAndView(Object obj, String str) {
    }

    public void bindCallbackAndView(Object obj, VesselBaseView vesselBaseView) {
        this.mMap.put(obj, vesselBaseView);
    }

    public static VesselCallbackManager getInstance() {
        return manager;
    }

    public void notifyCallback(Object obj, Map<String, Object> map, ResultCallback resultCallback) {
        VesselBaseView vesselBaseView = this.mMap.get(obj);
        if (vesselBaseView != null && vesselBaseView.mVesselViewCallback != null) {
            vesselBaseView.mVesselViewCallback.viewCall(map, resultCallback);
        }
    }

    public void remove(Object obj) {
        if (this.mMap.get(obj) != null) {
            this.mMap.remove(obj);
        }
    }

    @Deprecated
    public String generateInstanceId() {
        return String.valueOf(sInstanceId.incrementAndGet());
    }

    @Deprecated
    public boolean removeVesselView(String str) {
        if (this.sViews.get(str) == null) {
            return false;
        }
        this.sViews.remove(str);
        return true;
    }

    @Deprecated
    public void addVesselView(String str, VesselBaseView vesselBaseView) {
        if (str != null && vesselBaseView != null) {
            this.sViews.put(str, vesselBaseView);
        }
    }

    @Deprecated
    public View getVesselView(String str) {
        if (str != null) {
            return this.sViews.get(str);
        }
        return null;
    }

    @Deprecated
    public Map<String, VesselBaseView> getAllViews() {
        return this.sViews;
    }
}
