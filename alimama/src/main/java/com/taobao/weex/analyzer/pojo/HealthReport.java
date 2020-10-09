package com.taobao.weex.analyzer.pojo;

import android.util.Log;
import androidx.annotation.NonNull;
import com.alibaba.fastjson.annotation.JSONField;
import com.taobao.weex.analyzer.core.lint.VDOMInfo;
import com.taobao.weex.el.parse.Operators;
import java.util.List;
import java.util.Map;

public class HealthReport {
    private String bundleUrl;
    @JSONField(serialize = false)
    public int componentNumOfBigCell;
    public List<EmbedDesc> embedDescList;
    public int estimateContentHeight;
    public String estimatePages;
    @JSONField(serialize = false)
    public Map<String, String> extendProps;
    public boolean hasBigCell;
    public boolean hasEmbed;
    public boolean hasList;
    public boolean hasScroller;
    public Map<String, ListDesc> listDescMap;
    public int maxCellViewNum;
    @JSONField(name = "maxLayerOfVDom")
    public int maxLayer;
    public int maxLayerOfRealDom;
    public VDOMInfo tree;

    public static class EmbedDesc {
        public int actualMaxLayer;
        public int beginLayer;
        public String src;
    }

    public static class ListDesc {
        public int cellNum;
        public String ref;
        public int totalHeight;
    }

    public HealthReport() {
    }

    public HealthReport(@NonNull String str) {
        this.bundleUrl = str;
    }

    public void writeToConsole() {
        Log.d("weex-analyzer", "health report(" + this.bundleUrl + Operators.BRACKET_END_STR);
        StringBuilder sb = new StringBuilder();
        sb.append("[health report] maxLayer:");
        sb.append(this.maxLayer);
        Log.d("weex-analyzer", sb.toString());
        Log.d("weex-analyzer", "[health report] maxLayerOfRealDom:" + this.maxLayerOfRealDom);
        Log.d("weex-analyzer", "[health report] hasList:" + this.hasList);
        Log.d("weex-analyzer", "[health report] hasScroller:" + this.hasScroller);
        Log.d("weex-analyzer", "[health report] hasBigCell:" + this.hasBigCell);
        Log.d("weex-analyzer", "[health report] maxCellViewNum:" + this.maxCellViewNum);
        if (this.listDescMap != null && !this.listDescMap.isEmpty()) {
            Log.d("weex-analyzer", "[health report] listNum:" + this.listDescMap.size());
            for (ListDesc next : this.listDescMap.values()) {
                Log.d("weex-analyzer", "[health report] listDesc: (ref:" + next.ref + ",cellNum:" + next.cellNum + ",totalHeight:" + next.totalHeight + "px)");
            }
        }
        Log.d("weex-analyzer", "[health report] hasEmbed:" + this.hasEmbed);
        if (this.embedDescList != null && !this.embedDescList.isEmpty()) {
            Log.d("weex-analyzer", "[health report] embedNum:" + this.embedDescList.size());
            for (EmbedDesc next2 : this.embedDescList) {
                Log.d("weex-analyzer", "[health report] embedDesc: (src:" + next2.src + ",layer:" + next2.actualMaxLayer + Operators.BRACKET_END_STR);
            }
        }
        Log.d("weex-analyzer", "[health report] estimateContentHeight:" + this.estimateContentHeight + "px" + ",estimatePages:" + this.estimatePages);
        Log.d("weex-analyzer", "\n");
        if (this.extendProps != null) {
            for (Map.Entry next3 : this.extendProps.entrySet()) {
                Log.d("weex-analyzer", "[health report] " + ((String) next3.getKey()) + ":" + ((String) next3.getValue()) + Operators.BRACKET_END_STR);
            }
        }
    }
}
