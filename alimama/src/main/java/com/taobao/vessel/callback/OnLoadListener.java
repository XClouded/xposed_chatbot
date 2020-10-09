package com.taobao.vessel.callback;

import android.view.View;
import com.taobao.vessel.model.VesselError;
import java.util.Map;

public interface OnLoadListener {
    void onDowngrade(VesselError vesselError, Map<String, Object> map);

    void onLoadError(VesselError vesselError);

    void onLoadFinish(View view);

    void onLoadStart();
}
