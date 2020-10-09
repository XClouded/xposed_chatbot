package com.alibaba.aliweex.adapter.component;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.adapter.adapter.WXImgLoaderAdapter;
import com.taobao.phenix.cache.memory.ReleasableBitmapDrawable;
import com.taobao.phenix.intf.PhenixTicket;
import com.taobao.weex.common.Destroyable;
import com.taobao.weex.ui.view.WXImageView;

public class AliWXImageView extends WXImageView implements Destroyable {
    private ReleasableBitmapDrawable reference;

    public AliWXImageView(Context context) {
        super(context);
    }

    public void destroy() {
        try {
            if (getTag() instanceof PhenixTicket) {
                ((PhenixTicket) getTag()).cancel();
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        releaseDrawable();
    }

    public void setImageDrawable(@Nullable Drawable drawable) {
        releaseDrawable();
        super.setImageDrawable(drawable);
        if (drawable instanceof ReleasableBitmapDrawable) {
            String config = AliWeex.getInstance().getConfigAdapter().getConfig(WXImgLoaderAdapter.WX_IMAGE_RELEASE_CONFIG, WXImgLoaderAdapter.WX_ALLOW_RELEASE_DOMAIN, "");
            if (!TextUtils.isEmpty(config) && TextUtils.equals("true", config)) {
                this.reference = (ReleasableBitmapDrawable) drawable;
            }
        }
    }

    private void releaseDrawable() {
        if (this.reference != null) {
            this.reference.release();
            this.reference = null;
        }
    }
}
