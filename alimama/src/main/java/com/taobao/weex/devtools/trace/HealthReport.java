package com.taobao.weex.devtools.trace;

import android.util.Log;
import androidx.annotation.NonNull;
import com.alibaba.fastjson.annotation.JSONField;
import com.taobao.weex.el.parse.Operators;
import java.util.List;
import java.util.Map;

public class HealthReport {
    private static final String TAG = "Inspector-HearthReport";
    private String bundleUrl;
    public int componentCount;
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
    @JSONField(serialize = false)
    public int maxCellViewNum;
    @JSONField(name = "maxLayerOfVDom")
    public int maxLayer;
    public int maxLayerOfRealDom;

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
        Log.d(TAG, "health report(" + this.bundleUrl + Operators.BRACKET_END_STR);
        StringBuilder sb = new StringBuilder();
        sb.append("[health report] maxLayer:");
        sb.append(this.maxLayer);
        Log.d(TAG, sb.toString());
        Log.d(TAG, "[health report] maxLayerOfRealDom:" + this.maxLayerOfRealDom);
        Log.d(TAG, "[health report] hasList:" + this.hasList);
        Log.d(TAG, "[health report] hasScroller:" + this.hasScroller);
        Log.d(TAG, "[health report] hasBigCell:" + this.hasBigCell);
        Log.d(TAG, "[health report] maxCellViewNum:" + this.maxCellViewNum);
        if (this.listDescMap != null && !this.listDescMap.isEmpty()) {
            Log.d(TAG, "[health report] listNum:" + this.listDescMap.size());
            for (ListDesc next : this.listDescMap.values()) {
                Log.d(TAG, "[health report] listDesc: (ref:" + next.ref + ",cellNum:" + next.cellNum + ",totalHeight:" + next.totalHeight + "px)");
            }
        }
        Log.d(TAG, "[health report] hasEmbed:" + this.hasEmbed);
        if (this.embedDescList != null && !this.embedDescList.isEmpty()) {
            Log.d(TAG, "[health report] embedNum:" + this.embedDescList.size());
            for (EmbedDesc next2 : this.embedDescList) {
                Log.d(TAG, "[health report] embedDesc: (src:" + next2.src + ",layer:" + next2.actualMaxLayer + Operators.BRACKET_END_STR);
            }
        }
        Log.d(TAG, "[health report] estimateContentHeight:" + this.estimateContentHeight + "px,estimatePages:" + this.estimatePages);
        Log.d(TAG, "\n");
        if (this.extendProps != null) {
            for (Map.Entry next3 : this.extendProps.entrySet()) {
                Log.d(TAG, "[health report] " + ((String) next3.getKey()) + ":" + ((String) next3.getValue()) + Operators.BRACKET_END_STR);
            }
        }
    }
}
