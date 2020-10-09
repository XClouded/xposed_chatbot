package com.alimamaunion.common.listpage;

public interface IFooterProcess {
    boolean isFooter(CommonBaseViewHolder commonBaseViewHolder);

    void process(CommonBaseViewHolder commonBaseViewHolder, boolean z);
}
