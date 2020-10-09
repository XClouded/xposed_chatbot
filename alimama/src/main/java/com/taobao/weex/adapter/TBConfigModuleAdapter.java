package com.taobao.weex.adapter;

import android.text.TextUtils;
import com.alibaba.aliweex.adapter.IConfigModuleAdapter;
import com.alibaba.aliweex.adapter.IConfigModuleListener;
import com.taobao.orange.OConfigListener;
import com.taobao.orange.OrangeConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TBConfigModuleAdapter extends TBConfigAdapter implements IConfigModuleAdapter {
    private Map<String, List<OConfigListenerExt>> mListeners = new HashMap();

    public String getConfig(String str, String str2, String str3) {
        if (TextUtils.isEmpty(str2)) {
            return "";
        }
        return super.getConfig(str, str2, str3);
    }

    public Map<String, String> getConfigs(String str) {
        return super.getConfigs(str);
    }

    public void registerListener(String[] strArr, IConfigModuleListener iConfigModuleListener) {
        if (strArr != null && iConfigModuleListener != null) {
            OConfigListenerExt oConfigListenerExt = new OConfigListenerExt(iConfigModuleListener);
            for (String str : strArr) {
                List list = this.mListeners.get(str);
                if (list == null) {
                    list = new ArrayList();
                    this.mListeners.put(str, list);
                }
                if (!list.contains(oConfigListenerExt)) {
                    list.add(oConfigListenerExt);
                }
            }
            OrangeConfig.getInstance().registerListener(strArr, oConfigListenerExt, true);
        }
    }

    public void unregisterListener(String[] strArr, IConfigModuleListener iConfigModuleListener) {
        int indexOf;
        if (strArr != null && iConfigModuleListener != null) {
            OConfigListenerExt oConfigListenerExt = new OConfigListenerExt(iConfigModuleListener);
            for (String str : strArr) {
                List list = this.mListeners.get(str);
                if (list != null && list.size() > 0 && (indexOf = list.indexOf(oConfigListenerExt)) >= 0) {
                    OrangeConfig.getInstance().unregisterListener(new String[]{str}, (OConfigListener) (OConfigListenerExt) list.remove(indexOf));
                }
            }
        }
    }

    public void destroy() {
        for (Map.Entry next : this.mListeners.entrySet()) {
            String str = (String) next.getKey();
            List<OConfigListenerExt> list = (List) next.getValue();
            if (list != null) {
                for (OConfigListenerExt unregisterListener : list) {
                    OrangeConfig.getInstance().unregisterListener(new String[]{str}, (OConfigListener) unregisterListener);
                }
            }
        }
        this.mListeners.clear();
    }

    static class OConfigListenerExt implements OConfigListener {
        private IConfigModuleListener mListener;

        OConfigListenerExt(IConfigModuleListener iConfigModuleListener) {
            this.mListener = iConfigModuleListener;
        }

        public void onConfigUpdate(String str, Map<String, String> map) {
            if (this.mListener != null) {
                this.mListener.onConfigUpdate(str, map);
            }
        }

        public boolean equals(Object obj) {
            if (this.mListener == null) {
                return false;
            }
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            return this.mListener.equals(((OConfigListenerExt) obj).mListener);
        }

        public int hashCode() {
            return this.mListener.hashCode();
        }
    }
}
