package com.taobao.ju.track.param;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import com.taobao.ju.track.JTrack;
import com.taobao.ju.track.constants.Constants;
import com.taobao.ju.track.impl.CtrlTrackImpl;
import java.util.Map;

public class JParamBuilder extends BaseParamBuilder {
    private static final String TAG = "JParamBuilder";

    @Deprecated
    public JParamBuilder setAsPreSpm() {
        return this;
    }

    private JParamBuilder() {
    }

    public static JParamBuilder make(Object obj) {
        return make(JTrack.Ctrl.getTrack(), (Object) String.valueOf(obj));
    }

    public static JParamBuilder make(Activity activity, Object obj) {
        return make(JTrack.Ctrl.getTrack(), activity, (Object) String.valueOf(obj));
    }

    public static JParamBuilder make(String str, Object obj) {
        return make(JTrack.Ctrl.getTrack(), str, (Object) String.valueOf(obj));
    }

    public static JParamBuilder make(CtrlTrackImpl ctrlTrackImpl, Object obj) {
        JParamBuilder jParamBuilder = (JParamBuilder) makeInternal(ctrlTrackImpl, new JParamBuilder(), obj);
        jParamBuilder.mParams.putAll(ctrlTrackImpl.getParamSpm(jParamBuilder.mCSVRowName));
        return jParamBuilder;
    }

    public static JParamBuilder make(CtrlTrackImpl ctrlTrackImpl, Activity activity, Object obj) {
        JParamBuilder jParamBuilder = (JParamBuilder) makeInternal(ctrlTrackImpl, new JParamBuilder(), obj);
        jParamBuilder.mParams.putAll(ctrlTrackImpl.getParamSpm(activity, jParamBuilder.mCSVRowName));
        return jParamBuilder;
    }

    public static JParamBuilder make(CtrlTrackImpl ctrlTrackImpl, String str, Object obj) {
        JParamBuilder jParamBuilder = (JParamBuilder) makeInternal(ctrlTrackImpl, new JParamBuilder(), obj);
        jParamBuilder.mParams.putAll(ctrlTrackImpl.getParamSpm(str, jParamBuilder.mCSVRowName));
        return jParamBuilder;
    }

    public JParamBuilder add(String str, Object obj) {
        if (this.mParams != null) {
            if (this.mParams.containsKey(str)) {
                String valueOf = String.valueOf(obj);
                if (!TextUtils.isEmpty(valueOf)) {
                    if (!(obj instanceof Number) || !Constants.isPosStartFromOne() || (!Constants.PARAM_POS.equals(str) && !Constants.PARAM_PAGER_POS.equals(str))) {
                        this.mParams.put(str, valueOf);
                    } else {
                        this.mParams.put(str, String.valueOf(((Number) obj).longValue() + 1));
                        return this;
                    }
                }
            } else {
                String str2 = TAG;
                Log.e(str2, "请先在 " + this.mCSVName + " 中配置，列_key为：" + this.mCSVRowName + "列" + str + "需要有值 否则打点会失败！");
            }
        }
        return this;
    }

    public JParamBuilder forceAdd(String str, Object obj) {
        if (this.mParams != null) {
            this.mParams.put(str, String.valueOf(obj));
        }
        return this;
    }

    public JParamBuilder add(Map<String, String> map) {
        if (map != null && map.size() > 0) {
            this.mParams.putAll(map);
        }
        return this;
    }

    public String getSpm() {
        return getParamValue("spm", Constants.PARAM_OUTER_SPM_NONE);
    }
}
