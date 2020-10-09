package com.taobao.uikit.feature.features.internal.pullrefresh;

import android.view.View;

@Deprecated
public interface IViewEdgeJudge {
    boolean hasArrivedBottomEdge();

    boolean hasArrivedTopEdge();

    void keepBottom();

    void keepTop();

    void removeFooterView(View view);

    void removeHeaderView(View view);

    void setFooterView(View view);

    void setHeadView(View view);

    void trigger();
}
