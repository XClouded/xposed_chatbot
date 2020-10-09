package com.taobao.android.dxcontainer.render;

import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.android.dxcontainer.DXContainerEngine;
import com.taobao.android.dxcontainer.DXContainerModel;
import com.taobao.android.dxcontainer.DXContainerViewPager;

public class TabContentRender extends IDXContainerRender {
    public static final String RENDER_TYPE = "TabContentRender";
    private DXContainerEngine engine;

    public String getViewTypeId(DXContainerModel dXContainerModel) {
        return RENDER_TYPE;
    }

    public TabContentRender(DXContainerEngine dXContainerEngine) {
        super(dXContainerEngine);
        this.engine = dXContainerEngine;
    }

    public View createView(ViewGroup viewGroup, String str, Object obj) {
        DXContainerViewPager dXContainerViewPager = new DXContainerViewPager(viewGroup.getContext(), this.engine);
        resetHeight(dXContainerViewPager);
        return dXContainerViewPager;
    }

    public DXContainerRenderResult renderView(DXContainerModel dXContainerModel, View view, int i) {
        if (!(view instanceof DXContainerViewPager)) {
            return null;
        }
        ((DXContainerViewPager) view).bindData(dXContainerModel);
        resetHeight(view);
        return null;
    }

    private void resetHeight(View view) {
        int tabContentHeight = this.engine.getTabContentHeight();
        if (view.getLayoutParams() == null) {
            view.setLayoutParams(new RecyclerView.LayoutParams(-1, tabContentHeight));
        } else if (view.getLayoutParams().height != tabContentHeight) {
            view.getLayoutParams().height = tabContentHeight;
        }
    }
}
