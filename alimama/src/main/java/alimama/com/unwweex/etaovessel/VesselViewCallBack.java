package alimama.com.unwweex.etaovessel;

import android.view.View;
import com.taobao.vessel.model.VesselError;
import java.util.Map;

public interface VesselViewCallBack {
    void onDowngrade(VesselError vesselError, Map<String, Object> map);

    void onLoadError(VesselError vesselError);

    void onLoadFinish(View view, String str);

    void onLoadStart(View view, String str);

    boolean onOverrideUrl(View view, String str);
}
