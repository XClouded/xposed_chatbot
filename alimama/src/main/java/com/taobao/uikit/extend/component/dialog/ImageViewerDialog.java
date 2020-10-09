package com.taobao.uikit.extend.component.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.taobao.uikit.extend.R;
import com.taobao.uikit.extend.feature.view.TZoomImageView;
import com.taobao.uikit.extend.feature.view.TZoomPagerItem;
import com.taobao.uikit.feature.features.ImageSaveFeature;
import com.taobao.uikit.feature.view.TImageView;

public class ImageViewerDialog extends Dialog {
    private MyAdapter mAdapter;
    /* access modifiers changed from: private */
    public boolean mHidePageNum;
    /* access modifiers changed from: private */
    public TextView mImageDescText;
    /* access modifiers changed from: private */
    public String[] mImageDescs;
    /* access modifiers changed from: private */
    public String[] mImageUrls;
    /* access modifiers changed from: private */
    public SparseArray<TZoomPagerItem> mPageList;
    /* access modifiers changed from: private */
    public TextView mPageNumText;
    /* access modifiers changed from: private */
    public boolean mSave;
    private ViewPager mViewPager;

    public ImageViewerDialog(Context context) {
        this(context, R.style.ImageViewerDialog);
    }

    public ImageViewerDialog(Context context, int i) {
        super(context, i);
        this.mPageList = new SparseArray<>();
        this.mSave = false;
        this.mHidePageNum = false;
        init();
    }

    @SuppressLint({"InflateParams"})
    private void init() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(getContext().getResources().getDisplayMetrics().widthPixels, -1);
        View inflate = getLayoutInflater().inflate(R.layout.uik_imageviewer_dialog, (ViewGroup) null);
        setContentView(inflate, layoutParams);
        this.mImageDescText = (TextView) inflate.findViewById(R.id.img_desc);
        this.mPageNumText = (TextView) inflate.findViewById(R.id.page_num);
        this.mViewPager = (ViewPager) inflate.findViewById(R.id.viewpager);
        this.mAdapter = new MyAdapter();
        this.mViewPager.setAdapter(this.mAdapter);
        this.mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int i) {
            }

            public void onPageScrolled(int i, float f, int i2) {
            }

            public void onPageSelected(int i) {
                TZoomImageView imageView;
                int size = ImageViewerDialog.this.mPageList.size();
                for (int i2 = 0; i2 < size; i2++) {
                    if (!(i2 == i || ImageViewerDialog.this.mPageList.get(i2) == null || (imageView = ((TZoomPagerItem) ImageViewerDialog.this.mPageList.get(i2)).getImageView()) == null)) {
                        imageView.reset();
                    }
                }
                if (ImageViewerDialog.this.mImageDescs != null && ImageViewerDialog.this.mImageDescs.length > i) {
                    ImageViewerDialog.this.mImageDescText.setText(ImageViewerDialog.this.mImageDescs[i]);
                }
                if (!ImageViewerDialog.this.mHidePageNum) {
                    TextView access$500 = ImageViewerDialog.this.mPageNumText;
                    access$500.setText((i + 1) + "/" + ImageViewerDialog.this.mImageUrls.length);
                }
            }
        });
    }

    public void setImageUrls(String[] strArr) {
        if (strArr != null) {
            this.mPageList.clear();
            this.mImageUrls = strArr;
            this.mAdapter.notifyDataSetChanged();
            int currentItem = this.mViewPager.getCurrentItem();
            if (!this.mHidePageNum) {
                TextView textView = this.mPageNumText;
                textView.setText((currentItem + 1) + "/" + this.mImageUrls.length);
            }
        }
    }

    public void setImageDescs(String[] strArr) {
        this.mImageDescs = strArr;
        int currentItem = this.mViewPager.getCurrentItem();
        if (this.mImageDescs.length > currentItem) {
            this.mImageDescText.setText(this.mImageDescs[currentItem]);
        }
        if (!this.mHidePageNum) {
            TextView textView = this.mPageNumText;
            textView.setText((currentItem + 1) + "/" + this.mImageUrls.length);
        }
    }

    public void show(int i) {
        if (this.mImageUrls != null && i >= 0 && i < this.mImageUrls.length) {
            this.mViewPager.setCurrentItem(i);
            show();
        }
    }

    public TImageView getCurrentImageView() {
        return this.mPageList.get(this.mViewPager.getCurrentItem()).getImageView();
    }

    class MyAdapter extends PagerAdapter {
        public int getItemPosition(Object obj) {
            return -2;
        }

        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        MyAdapter() {
        }

        public int getCount() {
            if (ImageViewerDialog.this.mImageUrls == null) {
                return 0;
            }
            return ImageViewerDialog.this.mImageUrls.length;
        }

        public Object instantiateItem(ViewGroup viewGroup, int i) {
            TZoomPagerItem tZoomPagerItem = (TZoomPagerItem) ImageViewerDialog.this.mPageList.get(i);
            if (tZoomPagerItem == null) {
                tZoomPagerItem = new TZoomPagerItem(ImageViewerDialog.this.getContext());
                TZoomImageView imageView = tZoomPagerItem.getImageView();
                if (ImageViewerDialog.this.mSave && imageView.findFeature(ImageSaveFeature.class) == null) {
                    imageView.addFeature(new ImageSaveFeature());
                    imageView.setLongClickable(true);
                }
                imageView.setImageUrl(ImageViewerDialog.this.mImageUrls[i]);
                imageView.setTag(Integer.valueOf(i));
                imageView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        ImageViewerDialog.this.dismiss();
                    }
                });
                ImageViewerDialog.this.mPageList.put(i, tZoomPagerItem);
            }
            viewGroup.addView(tZoomPagerItem);
            return tZoomPagerItem;
        }

        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            viewGroup.removeView((View) obj);
            ImageViewerDialog.this.mPageList.remove(i);
        }
    }

    public void enableSaveImage(boolean z) {
        this.mSave = z;
    }

    public void hidePageNum(boolean z) {
        this.mHidePageNum = z;
        this.mPageNumText.setVisibility(z ? 8 : 0);
    }
}
