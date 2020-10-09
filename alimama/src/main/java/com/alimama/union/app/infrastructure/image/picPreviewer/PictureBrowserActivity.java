package com.alimama.union.app.infrastructure.image.picPreviewer;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.viewpager.widget.ViewPager;
import com.alibaba.fastjson.JSON;
import com.alimama.moon.R;
import com.alimama.moon.ui.BaseActivity;
import com.alimama.moon.usertrack.PicBrowserUTHelper;
import com.alimama.moon.utils.ToastUtil;
import com.alimama.moon.windvane.jsbridge.model.PictureBrowserModel;
import com.alimama.union.app.infrastructure.executor.AsyncTaskManager;
import com.alimama.union.app.infrastructure.image.download.StoragePermissionValidator;
import com.alimama.union.app.infrastructure.image.download.UniversalImageDownloader;
import com.alimama.union.app.infrastructure.image.save.ExternalPublicStorageSaver;
import com.alimama.union.app.privacy.PermissionInterface;
import com.alimama.union.app.privacy.PrivacyPermissionManager;
import com.alimama.union.app.privacy.PrivacyUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.taobao.vessel.utils.Utils;
import java.util.ArrayList;

public class PictureBrowserActivity extends BaseActivity implements PermissionInterface {
    public static final String PIC_BROWSER_JSON_DATA_KEY = "pic_browser_json_data_key";
    /* access modifiers changed from: private */
    public String index = "0";
    private PictureBrowserPagerAdapter mAdapter = null;
    private RelativeLayout mCloseRl;
    private final UniversalImageDownloader mImageLoader = new UniversalImageDownloader(ImageLoader.getInstance(), ExternalPublicStorageSaver.getInstance(), AsyncTaskManager.getInstance());
    ArrayList<String> mImageUrls = new ArrayList<>();
    private TextView mNumberIndicator;
    private StoragePermissionValidator mPermissionValidator;
    private LinearLayout mSavePicLl;
    private ImageViewPager mViewPager;
    private PrivacyPermissionManager privacyPermissionManager;

    public void closePermissionRequest() {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView((int) R.layout.pic_browser_activity_layout);
        initView();
        initData();
    }

    private void initView() {
        this.mCloseRl = (RelativeLayout) findViewById(R.id.pic_preview_browser_close_ll);
        this.mCloseRl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PictureBrowserActivity.this.finish();
            }
        });
        this.mSavePicLl = (LinearLayout) findViewById(R.id.save_pic_ll);
        this.mSavePicLl.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PictureBrowserActivity.this.downLoadImage(PictureBrowserActivity.this.mImageUrls.get(Integer.parseInt(PictureBrowserActivity.this.index)));
            }
        });
        this.mViewPager = (ImageViewPager) findViewById(R.id.image_Horizontallist);
        this.mNumberIndicator = (TextView) findViewById(R.id.page_number_indicator_tv);
    }

    /* access modifiers changed from: private */
    public void downLoadImage(String str) {
        if (!PrivacyUtil.hasWriteExternalStorage(this)) {
            this.privacyPermissionManager.showReadWritePermissionDialog();
            return;
        }
        ToastUtil.showToast((Context) this, (int) R.string.save_image_in_progress);
        this.mImageLoader.download(urlPreProcess(str), new ImageDownloader(this));
    }

    public void openPermissionRequest() {
        this.mPermissionValidator.checkRequiredPermission(this);
    }

    private String urlPreProcess(String str) {
        if (!str.startsWith("//")) {
            return str;
        }
        return Utils.HTTPS_SCHEMA + str;
    }

    private void initData() {
        try {
            PictureBrowserModel pictureBrowserModel = (PictureBrowserModel) JSON.parseObject(getIntent().getStringExtra(PIC_BROWSER_JSON_DATA_KEY), PictureBrowserModel.class);
            this.index = pictureBrowserModel.getPos();
            if (TextUtils.isEmpty(this.index)) {
                this.index = "0";
            }
            this.mImageUrls.addAll(pictureBrowserModel.getImgList());
            PicBrowserUTHelper.viewImageOnPicBrowser(this.index);
        } catch (Exception unused) {
        }
        this.mAdapter = new PictureBrowserPagerAdapter(this);
        this.mAdapter.change(this.mImageUrls);
        this.mViewPager.setAdapter(this.mAdapter);
        try {
            this.mViewPager.setCurrentItem(Integer.parseInt(this.index));
        } catch (NumberFormatException unused2) {
            this.mViewPager.setCurrentItem(0);
        }
        updateNumberIndicator(Integer.parseInt(this.index));
        this.mViewPager.setOnPageChangeListener(new PageChangeListener());
        this.mPermissionValidator = new StoragePermissionValidator();
        this.privacyPermissionManager = new PrivacyPermissionManager((Context) this, (PermissionInterface) this);
    }

    /* access modifiers changed from: private */
    public void updateNumberIndicator(int i) {
        this.mNumberIndicator.setText(getString(R.string.page_number_indicator, new Object[]{String.valueOf(i + 1), String.valueOf(this.mImageUrls.size())}));
        this.index = String.valueOf(i);
    }

    public void onDestroy() {
        if (this.mAdapter != null) {
            this.mAdapter.clear();
        }
        this.mAdapter = null;
        super.onDestroy();
    }

    private class PageChangeListener implements ViewPager.OnPageChangeListener {
        public void onPageScrollStateChanged(int i) {
        }

        public void onPageScrolled(int i, float f, int i2) {
        }

        private PageChangeListener() {
        }

        public void onPageSelected(int i) {
            PictureBrowserActivity.this.updateNumberIndicator(i);
        }
    }
}
