package com.taobao.vessel.callback;

import com.taobao.vessel.base.ResultCallback;
import java.util.Map;

public interface VesselViewCallback {
    void viewCall(Map<String, Object> map, ResultCallback resultCallback);
}
