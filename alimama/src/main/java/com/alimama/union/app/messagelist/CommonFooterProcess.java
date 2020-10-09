package com.alimama.union.app.messagelist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.alimama.moon.R;
import com.alimama.moon.view.design.FooterStatusView;
import com.alimamaunion.common.listpage.CommonBaseItem;
import com.alimamaunion.common.listpage.CommonBaseViewHolder;
import com.alimamaunion.common.listpage.IFooterProcess;

public class CommonFooterProcess implements CommonBaseViewHolder<CommonBaseItem>, IFooterProcess {
    private FooterStatusView mFooterStatusView;
    private View view;

    public void onBindViewHolder(int i, CommonBaseItem commonBaseItem) {
    }

    public boolean isFooter(CommonBaseViewHolder commonBaseViewHolder) {
        return commonBaseViewHolder instanceof CommonFooterProcess;
    }

    public void process(CommonBaseViewHolder commonBaseViewHolder, boolean z) {
        CommonFooterProcess commonFooterProcess = (CommonFooterProcess) commonBaseViewHolder;
        if (!z) {
            if (commonFooterProcess.mFooterStatusView != null) {
                commonFooterProcess.mFooterStatusView.onLoadingMore();
            }
        } else if (commonFooterProcess.mFooterStatusView != null) {
            commonFooterProcess.mFooterStatusView.onNoMoreItems();
        }
    }

    public View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        this.view = layoutInflater.inflate(R.layout.loadmore_footerview_layout, viewGroup, false);
        this.mFooterStatusView = (FooterStatusView) this.view.findViewById(R.id.footer_status_view);
        return this.view;
    }
}
