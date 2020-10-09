package com.alibaba.aliweex.adapter.adapter;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import com.taobao.phenix.intf.Phenix;
import com.taobao.phenix.intf.event.IPhenixListener;
import com.taobao.phenix.intf.event.SuccPhenixEvent;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.DrawableStrategy;
import com.taobao.weex.adapter.IDrawableLoader;

public class PhenixBasedDrawableLoader implements IDrawableLoader {
    public void setDrawable(final String str, final IDrawableLoader.DrawableTarget drawableTarget, final DrawableStrategy drawableStrategy) {
        try {
            WXSDKManager.getInstance().postOnUiThread(new Runnable() {
                public void run() {
                    if (drawableTarget != null) {
                        if (!TextUtils.isEmpty(str) || !(drawableTarget instanceof IDrawableLoader.StaticTarget)) {
                            Phenix.instance().load(str).limitSize((View) null, drawableStrategy.width, drawableStrategy.height).notSharedDrawable(true).succListener(new DrawableLoadSuccess(drawableTarget)).fetch();
                        } else {
                            ((IDrawableLoader.StaticTarget) drawableTarget).setDrawable((Drawable) null, false);
                        }
                    }
                }
            }, 0);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private static class DrawableLoadSuccess implements IPhenixListener<SuccPhenixEvent> {
        private IDrawableLoader.DrawableTarget mDrawableTarget;

        DrawableLoadSuccess(IDrawableLoader.DrawableTarget drawableTarget) {
            this.mDrawableTarget = drawableTarget;
        }

        public boolean onHappen(SuccPhenixEvent succPhenixEvent) {
            BitmapDrawable drawable = succPhenixEvent.getDrawable();
            if (drawable != null && !succPhenixEvent.isIntermediate()) {
                drawable.setGravity(119);
                if (this.mDrawableTarget instanceof IDrawableLoader.StaticTarget) {
                    ((IDrawableLoader.StaticTarget) this.mDrawableTarget).setDrawable(drawable, true);
                }
            }
            return true;
        }
    }
}
