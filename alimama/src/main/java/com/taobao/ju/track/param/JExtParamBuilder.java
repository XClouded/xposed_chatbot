package com.taobao.ju.track.param;

import android.text.TextUtils;
import com.taobao.ju.track.JTrack;
import com.taobao.ju.track.constants.Constants;
import com.taobao.ju.track.impl.TrackImpl;
import java.util.Map;

public class JExtParamBuilder extends BaseParamBuilder {
    private static final String TAG = "JExtParamBuilder";

    private JExtParamBuilder() {
    }

    public static JExtParamBuilder make(Object obj) {
        return make(JTrack.Ext.getTrack(), String.valueOf(obj));
    }

    public static JExtParamBuilder make(TrackImpl trackImpl, Object obj) {
        return (JExtParamBuilder) makeInternal(trackImpl, new JExtParamBuilder(), obj);
    }

    public JExtParamBuilder add(String str, Object obj) {
        if (this.mParams != null) {
            String valueOf = String.valueOf(obj);
            if (!TextUtils.isEmpty(valueOf)) {
                if (!(obj instanceof Number) || !Constants.isPosStartFromOne() || (!Constants.PARAM_POS.equals(str) && !Constants.PARAM_PAGER_POS.equals(str))) {
                    this.mParams.put(str, valueOf);
                } else {
                    this.mParams.put(str, String.valueOf(((Number) obj).longValue() + 1));
                    return this;
                }
            }
        }
        return this;
    }

    public JExtParamBuilder forceAdd(String str, Object obj) {
        if (this.mParams != null) {
            this.mParams.put(str, String.valueOf(obj));
        }
        return this;
    }

    public JExtParamBuilder add(Map<String, String> map) {
        if (map != null && map.size() > 0) {
            this.mParams.putAll(map);
        }
        return this;
    }
}
