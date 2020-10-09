package com.alimama.union.app.infrastructure.image.picPreviewer;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.taobao.windvane.util.ImageTool;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.view.PopupWindowController;
import android.view.View;
import android.widget.Toast;
import com.alimama.unionwl.utils.CommonUtils;

public class TBImageSaveView implements Handler.Callback {
    private static TBImageSaveView mPopupController;
    /* access modifiers changed from: private */
    public Context context = null;
    /* access modifiers changed from: private */
    public Handler mHandler = null;
    /* access modifiers changed from: private */
    public String mImageUrl = null;
    /* access modifiers changed from: private */
    public String[] mPopupMenuTags = {"保存到相册"};
    /* access modifiers changed from: private */
    public PopupWindowController mPopupView;
    private View.OnClickListener popupClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            if (TBImageSaveView.this.mHandler == null || TBImageSaveView.this.mImageUrl == null || TBImageSaveView.this.context == null) {
                TaoLog.e("TBImageSaveView", "save image param error");
                return;
            }
            if (!TBImageSaveView.this.mImageUrl.startsWith("http")) {
                TBImageSaveView tBImageSaveView = TBImageSaveView.this;
                String unused = tBImageSaveView.mImageUrl = CommonUtils.HTTP_PRE + TBImageSaveView.this.mImageUrl;
            }
            if (TBImageSaveView.this.mPopupMenuTags != null && TBImageSaveView.this.mPopupMenuTags.length > 0 && TBImageSaveView.this.mPopupMenuTags[0].equals(view.getTag())) {
                try {
                    ImageTool.saveImageToDCIM(TBImageSaveView.this.context.getApplicationContext(), TBImageSaveView.this.mImageUrl, TBImageSaveView.this.mHandler);
                } catch (Exception unused2) {
                    TBImageSaveView.this.mHandler.sendEmptyMessage(405);
                }
            }
            TBImageSaveView.this.mPopupView.hide();
        }
    };

    public static TBImageSaveView getInstance() {
        if (mPopupController == null) {
            synchronized (TBImageSaveView.class) {
                if (mPopupController == null) {
                    mPopupController = new TBImageSaveView();
                }
            }
        }
        return mPopupController;
    }

    public void save(String str, Context context2, View view) {
        this.context = context2;
        this.mHandler = new Handler(Looper.getMainLooper(), this);
        this.mImageUrl = str;
        this.mPopupView = new PopupWindowController(this.context, view, this.mPopupMenuTags, this.popupClickListener);
        this.mPopupView.show();
    }

    public boolean handleMessage(Message message) {
        switch (message.what) {
            case 404:
                try {
                    Toast.makeText(this.context, "图片保存到相册成功", 1).show();
                } catch (Exception e) {
                    TaoLog.e("TBImageSaveView", "NOTIFY_SAVE_IMAGE_SUCCESS fail " + e.getMessage());
                }
                return true;
            case 405:
                try {
                    Toast.makeText(this.context, "图片保存到相册失败", 1).show();
                } catch (Exception e2) {
                    TaoLog.e("TBImageSaveView", "NOTIFY_SAVE_IMAGE_FAIL fail " + e2.getMessage());
                }
                return true;
            default:
                return false;
        }
    }
}
