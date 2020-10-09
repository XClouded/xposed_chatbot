package com.alimama.moon.features.home.view;

import android.net.Uri;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import com.alimama.moon.R;
import com.alimama.moon.features.home.item.HomeSliderBannerItem;
import com.alimama.unionwl.base.etaodrawee.EtaoDraweeView;
import com.alimama.unionwl.utils.UiUtils;
import in.srain.cube.views.banner.BannerAdapter;
import java.util.List;

public class HomeSliderBannerController {
    private InnerAdapter mBannerAdapter = new InnerAdapter();
    /* access modifiers changed from: private */
    public View.OnClickListener mClickItemListener = new View.OnClickListener() {
        public void onClick(View view) {
            HomeSliderBannerItem homeSliderBannerItem = (HomeSliderBannerItem) UiUtils.getTag(view, HomeSliderBannerItem.class);
            if (homeSliderBannerItem != null) {
                homeSliderBannerItem.onClick(view);
            }
        }
    };
    private HomeSliderBanner mSliderBanner;

    public void managerBanner(HomeSliderBanner homeSliderBanner) {
        homeSliderBanner.getViewPager().setPageTransformer(false, new ScaleTransformer());
        this.mSliderBanner = homeSliderBanner;
        homeSliderBanner.setAdapter(this.mBannerAdapter);
    }

    public void play(List<HomeSliderBannerItem> list) {
        this.mBannerAdapter.setData(list);
        this.mBannerAdapter.notifyDataSetChanged();
        this.mSliderBanner.setDotNum(list.size());
        this.mSliderBanner.beginPlay();
    }

    private class InnerAdapter extends BannerAdapter {
        private SparseArray<View> mCacheView = new SparseArray<>();
        private int mChildCount = 0;
        private List<HomeSliderBannerItem> mDataList;

        public InnerAdapter() {
        }

        public void setData(List<HomeSliderBannerItem> list) {
            this.mDataList = list;
        }

        public HomeSliderBannerItem getItem(int i) {
            if (this.mDataList == null || this.mDataList.size() == 0) {
                return null;
            }
            return this.mDataList.get(i % this.mDataList.size());
        }

        public int getPositionForIndicator(int i) {
            if (this.mDataList == null || this.mDataList.size() == 0) {
                return 0;
            }
            return i % this.mDataList.size();
        }

        public View getView(LayoutInflater layoutInflater, int i) {
            HomeSliderBannerItem item = getItem(i);
            View view = this.mCacheView.get(i);
            if (view == null) {
                view = layoutInflater.inflate(R.layout.home_banner_item, (ViewGroup) null);
            }
            EtaoDraweeView etaoDraweeView = (EtaoDraweeView) view.findViewById(R.id.home_banner_item_image);
            etaoDraweeView.setAdjustViewBounds(false);
            etaoDraweeView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            etaoDraweeView.setImageURI(Uri.parse(item.imgUrl));
            view.setTag(item);
            view.setOnClickListener(HomeSliderBannerController.this.mClickItemListener);
            this.mCacheView.put(i, view);
            return view;
        }

        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            super.destroyItem(viewGroup, i, obj);
        }

        public void notifyDataSetChanged() {
            this.mChildCount = getCount();
            super.notifyDataSetChanged();
        }

        public int getItemPosition(Object obj) {
            if (this.mChildCount <= 0) {
                return super.getItemPosition(obj);
            }
            this.mChildCount--;
            return -2;
        }

        public int getCount() {
            if (this.mDataList == null) {
                return 0;
            }
            return this.mDataList.size();
        }
    }

    static class ScaleTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.7f;

        ScaleTransformer() {
        }

        public void transformPage(@NonNull View view, float f) {
            if (f < -1.0f || f > 1.0f) {
                view.setScaleY(0.7f);
            } else if (f > 1.0f) {
            } else {
                if (f < 0.0f) {
                    float f2 = (0.3f * f) + 1.0f;
                    view.setScaleX(f2);
                    view.setScaleY(f2);
                    view.setPivotX(((float) view.getWidth()) * (0.05f - f));
                    return;
                }
                float f3 = 1.0f - (0.3f * f);
                view.setScaleX(f3);
                view.setScaleY(f3);
                view.setPivotX(((float) view.getWidth()) * (0.95f - f));
            }
        }
    }
}
