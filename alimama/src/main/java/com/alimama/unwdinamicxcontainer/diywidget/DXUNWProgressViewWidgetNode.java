package com.alimama.unwdinamicxcontainer.diywidget;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.alimama.unwdinamicxcontainer.R;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import com.taobao.android.dinamicx.widget.IDXBuilderWidgetNode;

public class DXUNWProgressViewWidgetNode extends DXWidgetNode {
    public static final long DXUNWPROGRESSVIEW_PROGRESS = 5587939702916175485L;
    public static final long DXUNWPROGRESSVIEW_STATUS = 10152453284241450L;
    public static final long DXUNWPROGRESSVIEW_UNWPROGRESSVIEW = -7077938547156657671L;
    private String progress;
    private String status;

    public static class Builder implements IDXBuilderWidgetNode {
        public DXWidgetNode build(Object obj) {
            return new DXUNWProgressViewWidgetNode();
        }
    }

    public DXWidgetNode build(Object obj) {
        return new DXUNWProgressViewWidgetNode();
    }

    public void onClone(DXWidgetNode dXWidgetNode, boolean z) {
        if (dXWidgetNode != null && (dXWidgetNode instanceof DXUNWProgressViewWidgetNode)) {
            super.onClone(dXWidgetNode, z);
            DXUNWProgressViewWidgetNode dXUNWProgressViewWidgetNode = (DXUNWProgressViewWidgetNode) dXWidgetNode;
            this.progress = dXUNWProgressViewWidgetNode.progress;
            this.status = dXUNWProgressViewWidgetNode.status;
        }
    }

    /* access modifiers changed from: protected */
    public View onCreateView(Context context) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.progress_view_layout, (ViewGroup) null);
        ((DXCFlashSaleProgressBar) inflate.findViewById(R.id.process_bar)).setDelayTime(2);
        return inflate;
    }

    /* access modifiers changed from: protected */
    public void onRenderView(Context context, View view) {
        super.onRenderView(context, view);
        renderProgress(context, (DXCFlashSaleProgressBar) view.findViewById(R.id.process_bar));
    }

    private void renderProgress(Context context, DXCFlashSaleProgressBar dXCFlashSaleProgressBar) {
        dXCFlashSaleProgressBar.setMax(100);
        if (TextUtils.equals(this.status, "3")) {
            dXCFlashSaleProgressBar.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_bar_un_start));
        } else {
            dXCFlashSaleProgressBar.setCurProgress(Integer.valueOf(this.progress).intValue());
        }
    }

    /* access modifiers changed from: protected */
    public void onBindEvent(Context context, View view, long j) {
        super.onBindEvent(context, view, j);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
    }

    /* access modifiers changed from: protected */
    public void onSetStringAttribute(long j, String str) {
        if (j == DXUNWPROGRESSVIEW_PROGRESS) {
            this.progress = str;
        } else if (j == DXUNWPROGRESSVIEW_STATUS) {
            this.status = str;
        } else {
            super.onSetStringAttribute(j, str);
        }
    }
}
