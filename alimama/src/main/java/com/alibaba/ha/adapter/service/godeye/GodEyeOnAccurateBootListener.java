package com.alibaba.ha.adapter.service.godeye;

import com.ali.telescope.interfaces.OnAccurateBootListener;
import com.taobao.tao.log.godeye.GodeyeInitializer;
import java.util.HashMap;

public class GodEyeOnAccurateBootListener implements OnAccurateBootListener {
    public void OnAccurateBootFinished(HashMap<String, String> hashMap) {
        GodeyeInitializer.getInstance().onAccurateBootFinished(hashMap);
    }
}
