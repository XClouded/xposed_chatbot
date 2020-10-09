package com.alimama.moon.features.home.viewholder;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alimama.moon.R;
import com.alimama.moon.features.home.adapter.HomeCardItemAdapter;
import com.alimama.moon.features.home.item.HomeCardItem;
import com.alimama.moon.usertrack.UTHelper;
import com.alimama.union.app.pagerouter.MoonComponentManager;
import com.alimama.unionwl.base.etaodrawee.EtaoDraweeView;
import com.alimamaunion.common.listpage.CommonBaseViewHolder;

public class HomeCardViewHolder implements CommonBaseViewHolder<HomeCardItem> {
    private HomeCardItemAdapter mAdapter;
    private EtaoDraweeView mCardBgImg;
    private RecyclerView mCardContainer;
    private Context mContext;
    private View mTopBgContainer;

    public View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        View inflate = layoutInflater.inflate(R.layout.fragment_home_card, viewGroup, false);
        this.mContext = viewGroup.getContext();
        this.mCardBgImg = (EtaoDraweeView) inflate.findViewById(R.id.card_bg);
        this.mCardContainer = (RecyclerView) inflate.findViewById(R.id.card_container);
        this.mTopBgContainer = inflate.findViewById(R.id.top_bg_container);
        this.mCardContainer.setLayoutManager(new LinearLayoutManager(viewGroup.getContext(), 0, false));
        this.mAdapter = new HomeCardItemAdapter();
        this.mCardContainer.setAdapter(this.mAdapter);
        return inflate;
    }

    public void onBindViewHolder(int i, final HomeCardItem homeCardItem) {
        if (homeCardItem.itemList.isEmpty()) {
            this.mTopBgContainer.setVisibility(8);
            return;
        }
        this.mTopBgContainer.setVisibility(0);
        this.mAdapter.setItemList(homeCardItem.itemList);
        String str = (String) this.mCardBgImg.getTag();
        if (TextUtils.isEmpty(str)) {
            this.mCardBgImg.setAnyImageUrl(homeCardItem.imgUrl);
            this.mCardBgImg.setTag(homeCardItem.imgUrl);
        } else if (!TextUtils.equals(str, homeCardItem.imgUrl)) {
            this.mCardBgImg.setAnyImageUrl(homeCardItem.imgUrl);
            this.mCardBgImg.setTag(homeCardItem.imgUrl);
        }
        this.mTopBgContainer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                UTHelper.HomePage.clickCardMore();
                MoonComponentManager.getInstance().getPageRouter().gotoPage(homeCardItem.moreUrl);
            }
        });
    }
}
