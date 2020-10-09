package com.alimama.union.app.infrastructure.image.picPreviewer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager.widget.PagerAdapter;
import com.taobao.phenix.intf.Phenix;
import com.taobao.tao.image.ImageStrategyConfig;
import java.util.ArrayList;

public class PictureBrowserPagerAdapter extends PagerAdapter {
    private String TAG = "Phenix123 HorizontalPagerAdapter";
    private ImageStrategyConfig mConfig = ImageStrategyConfig.newBuilderWithName("windvane", 98).build();
    private Context mContext;
    private ArrayList<String> mPaths;

    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    public PictureBrowserPagerAdapter(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public void change(ArrayList<String> arrayList) {
        this.mPaths = arrayList;
    }

    public int getCount() {
        return this.mPaths.size();
    }

    public Object instantiateItem(ViewGroup viewGroup, int i) {
        ImageTouchView imageTouchView = new ImageTouchView(this.mContext);
        imageTouchView.setMaxZoom(5.0f);
        Phenix.instance().load("", this.mPaths.get(i)).into(imageTouchView);
        ((ImageViewPager) viewGroup).addView(imageTouchView, 0);
        return imageTouchView;
    }

    public void setPrimaryItem(ViewGroup viewGroup, int i, Object obj) {
        super.setPrimaryItem(viewGroup, i, obj);
        ImageViewPager imageViewPager = (ImageViewPager) viewGroup;
        ImageTouchView imageTouchView = (ImageTouchView) obj;
        ImageTouchView currentView = imageViewPager.getCurrentView();
        if (currentView != imageTouchView) {
            if (currentView != null) {
                currentView.resetScale();
            }
            imageViewPager.setCurrentView(imageTouchView);
        }
    }

    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        viewGroup.removeView((View) obj);
    }

    public void clear() {
        this.mPaths = null;
    }
}
