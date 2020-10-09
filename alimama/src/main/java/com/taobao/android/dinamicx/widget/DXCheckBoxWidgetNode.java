package com.taobao.android.dinamicx.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.CompoundButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import com.taobao.android.dinamic.R;
import com.taobao.android.dinamicx.expression.event.DXCheckBoxEvent;
import com.taobao.android.dinamicx.template.loader.binary.DXHashConstant;
import com.taobao.android.dinamicx.thread.DXRunnableManager;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import com.taobao.android.dinamicx.widget.utils.DXDrawableTools;
import java.lang.ref.WeakReference;

public class DXCheckBoxWidgetNode extends DXWidgetNode {
    public static final int ALREADY_INT_CHECK_IMG = R.id.already_int_check_img;
    public static final int ALREADY_INT_DIS_CHECK_IMG = R.id.already_int_dis_check_img;
    public static final int ALREADY_INT_DIS_UNCHECK_IMG = R.id.already_int_dis_uncheck_img;
    public static final int ALREADY_INT_UNCHECK_IMG = R.id.already_int_uncheck_img;
    public static final int NEED_INT_CHECK_IMG = R.id.need_int_check_img;
    public static final int NEED_INT_DIS_CHECK_IMG = R.id.need_int_dis_check_img;
    public static final int NEED_INT_DIS_UNCHECK_IMG = R.id.need_int_dis_uncheck_img;
    public static final int NEED_INT_UNCHECK_IMG = R.id.need_int_uncheck_img;
    private boolean asyncImageLoad;
    private String checkImg;
    private int checked;
    private String disCheckImg;
    private String disUnCheckImg;
    /* access modifiers changed from: private */
    public boolean isInitCheckState = false;
    private String uncheckImg;

    /* access modifiers changed from: protected */
    public boolean extraHandleDark() {
        return true;
    }

    public static class Builder implements IDXBuilderWidgetNode {
        public DXWidgetNode build(@Nullable Object obj) {
            return new DXCheckBoxWidgetNode();
        }
    }

    public DXWidgetNode build(@Nullable Object obj) {
        return new DXCheckBoxWidgetNode();
    }

    public DXCheckBoxWidgetNode() {
        this.accessibility = 1;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int mode = DXWidgetNode.DXMeasureSpec.getMode(i);
        int mode2 = DXWidgetNode.DXMeasureSpec.getMode(i2);
        boolean z = true;
        int i3 = 0;
        boolean z2 = mode == 1073741824;
        if (mode2 != 1073741824) {
            z = false;
        }
        int size = z2 ? DXWidgetNode.DXMeasureSpec.getSize(i) : 0;
        if (z) {
            i3 = DXWidgetNode.DXMeasureSpec.getSize(i2);
        }
        setMeasuredDimension(size, i3);
    }

    public void onClone(DXWidgetNode dXWidgetNode, boolean z) {
        super.onClone(dXWidgetNode, z);
        if (dXWidgetNode instanceof DXCheckBoxWidgetNode) {
            DXCheckBoxWidgetNode dXCheckBoxWidgetNode = (DXCheckBoxWidgetNode) dXWidgetNode;
            this.checked = dXCheckBoxWidgetNode.checked;
            this.enabled = dXCheckBoxWidgetNode.enabled;
            this.checkImg = dXCheckBoxWidgetNode.checkImg;
            this.uncheckImg = dXCheckBoxWidgetNode.uncheckImg;
            this.disCheckImg = dXCheckBoxWidgetNode.disCheckImg;
            this.disUnCheckImg = dXCheckBoxWidgetNode.disUnCheckImg;
            this.isInitCheckState = dXCheckBoxWidgetNode.isInitCheckState;
            this.asyncImageLoad = dXCheckBoxWidgetNode.asyncImageLoad;
        }
    }

    /* access modifiers changed from: protected */
    public void onSetIntAttribute(long j, int i) {
        if (DXHashConstant.DX_CHECKBOX_CHECKED == j) {
            this.checked = i;
        }
        if (-273786109416499313L == j) {
            this.asyncImageLoad = i != 0;
        } else {
            super.onSetIntAttribute(j, i);
        }
    }

    public int getDefaultValueForIntAttr(long j) {
        if (-273786109416499313L == j) {
            return 0;
        }
        return super.getDefaultValueForIntAttr(j);
    }

    /* access modifiers changed from: protected */
    public void onSetStringAttribute(long j, String str) {
        if (DXHashConstant.DX_CHECKBOX_CHECKIMG == j) {
            this.checkImg = str;
        } else if (DXHashConstant.DX_CHECKBOX_UNCHECKIMG == j) {
            this.uncheckImg = str;
        } else if (DXHashConstant.DX_CHECKBOX_DISCHECKIMG == j) {
            this.disCheckImg = str;
        } else if (DXHashConstant.DX_CHECKBOX_DISUNCHKIMG == j) {
            this.disUnCheckImg = str;
        } else {
            super.onSetStringAttribute(j, str);
        }
    }

    /* access modifiers changed from: protected */
    public View onCreateView(Context context) {
        return new AppCompatCheckBox(context);
    }

    /* access modifiers changed from: protected */
    public void onRenderView(Context context, View view) {
        if (view != null && (view instanceof AppCompatCheckBox)) {
            AppCompatCheckBox appCompatCheckBox = (AppCompatCheckBox) view;
            boolean z = true;
            appCompatCheckBox.setClickable(true);
            if (getMeasuredWidth() <= 0 || getMeasuredHeight() <= 0) {
                appCompatCheckBox.setButtonDrawable((Drawable) null);
            } else {
                setCheckDrawable(appCompatCheckBox);
            }
            if (this.checked != 1) {
                z = false;
            }
            setChecked(appCompatCheckBox, z);
        }
    }

    /* access modifiers changed from: protected */
    public void setChecked(AppCompatCheckBox appCompatCheckBox, boolean z) {
        if (appCompatCheckBox != null) {
            this.isInitCheckState = true;
            appCompatCheckBox.setChecked(z);
            this.isInitCheckState = false;
        }
    }

    private void setCheckDrawable(AppCompatCheckBox appCompatCheckBox) {
        AppCompatCheckBox appCompatCheckBox2 = appCompatCheckBox;
        String str = this.checkImg;
        String str2 = (String) appCompatCheckBox2.getTag(ALREADY_INT_CHECK_IMG);
        if (str == null) {
            str = "dinamicx_checked";
        }
        String str3 = this.uncheckImg;
        String str4 = (String) appCompatCheckBox2.getTag(ALREADY_INT_UNCHECK_IMG);
        if (str3 == null) {
            str3 = "dinamicx_uncheck";
        }
        String str5 = this.disCheckImg;
        String str6 = (String) appCompatCheckBox2.getTag(ALREADY_INT_DIS_CHECK_IMG);
        if (str5 == null) {
            str5 = "dinamicx_discheck";
        }
        String str7 = this.disUnCheckImg;
        String str8 = (String) appCompatCheckBox2.getTag(ALREADY_INT_DIS_UNCHECK_IMG);
        if (str7 == null) {
            str7 = "dinamicx_disunchk";
        }
        if (needHandleDark()) {
            str = "dark_" + str;
            str3 = "dark_" + str3;
            str5 = "dark_" + str5;
            str7 = "dark_" + str7;
        }
        String str9 = str;
        String str10 = str3;
        String str11 = str5;
        String str12 = str7;
        if (str2 == null && str4 == null && str6 == null && str8 == null) {
            appCompatCheckBox2.setButtonDrawable((Drawable) null);
        }
        if (!str9.equals(str2) || !str10.equals(str4) || !str11.equals(str6) || !str12.equals(str8)) {
            LoadDrawableTask loadDrawableTask = new LoadDrawableTask(appCompatCheckBox, str9, str10, str11, str12, getMeasuredWidth(), getMeasuredHeight(), needHandleDark());
            if (this.asyncImageLoad) {
                appCompatCheckBox2.setTag(NEED_INT_CHECK_IMG, str9);
                appCompatCheckBox2.setTag(NEED_INT_UNCHECK_IMG, str10);
                appCompatCheckBox2.setTag(NEED_INT_DIS_CHECK_IMG, str11);
                appCompatCheckBox2.setTag(NEED_INT_DIS_UNCHECK_IMG, str12);
                DXRunnableManager.scheduledAsyncTask(loadDrawableTask, new Void[0]);
                return;
            }
            Drawable[] access$000 = loadDrawableTask.loadDrawables();
            loadDrawableTask.updateInternalStatus(appCompatCheckBox, access$000[0], access$000[1], access$000[2], access$000[3]);
            appCompatCheckBox2.setTag(ALREADY_INT_CHECK_IMG, str9);
            appCompatCheckBox2.setTag(ALREADY_INT_UNCHECK_IMG, str10);
            appCompatCheckBox2.setTag(ALREADY_INT_DIS_CHECK_IMG, str11);
            appCompatCheckBox2.setTag(ALREADY_INT_DIS_UNCHECK_IMG, str12);
        }
    }

    public static class LoadDrawableTask extends AsyncTask<Void, Void, Drawable[]> {
        private WeakReference<AppCompatCheckBox> checkBoxRef;
        private Context context;
        int height;
        private boolean isDark;
        String newCheckedImage = null;
        String newDisCheckImage = null;
        String newDisUncheckImage = null;
        String newUncheckedImage = null;
        int width;

        public LoadDrawableTask(AppCompatCheckBox appCompatCheckBox, String str, String str2, String str3, String str4, int i, int i2, boolean z) {
            this.context = appCompatCheckBox.getContext().getApplicationContext();
            this.newCheckedImage = str;
            this.newUncheckedImage = str2;
            this.newDisCheckImage = str3;
            this.newDisUncheckImage = str4;
            this.width = i;
            this.height = i2;
            this.checkBoxRef = new WeakReference<>(appCompatCheckBox);
            this.isDark = z;
        }

        /* access modifiers changed from: protected */
        public Drawable[] doInBackground(Void... voidArr) {
            return loadDrawables();
        }

        /* access modifiers changed from: private */
        @NonNull
        public Drawable[] loadDrawables() {
            Drawable drawable = getDrawable(this.context, this.newCheckedImage);
            if (drawable == null) {
                if (this.isDark) {
                    drawable = getDefaultDrawable(this.context, R.drawable.dark_dinamicx_checked);
                } else {
                    drawable = getDefaultDrawable(this.context, R.drawable.dinamicx_checked);
                }
            }
            Drawable updateDrawableSize = updateDrawableSize(drawable, this.context, this.width, this.height);
            Drawable drawable2 = getDrawable(this.context, this.newUncheckedImage);
            if (drawable2 == null) {
                if (this.isDark) {
                    drawable2 = getDefaultDrawable(this.context, R.drawable.dark_dinamicx_uncheck);
                } else {
                    drawable2 = getDefaultDrawable(this.context, R.drawable.dinamicx_uncheck);
                }
            }
            Drawable updateDrawableSize2 = updateDrawableSize(drawable2, this.context, this.width, this.height);
            Drawable drawable3 = getDrawable(this.context, this.newDisCheckImage);
            if (drawable3 == null) {
                if (this.isDark) {
                    drawable3 = getDefaultDrawable(this.context, R.drawable.dark_dinamicx_discheck);
                } else {
                    drawable3 = getDefaultDrawable(this.context, R.drawable.dinamicx_discheck);
                }
            }
            Drawable updateDrawableSize3 = updateDrawableSize(drawable3, this.context, this.width, this.height);
            Drawable drawable4 = getDrawable(this.context, this.newDisUncheckImage);
            if (drawable4 == null) {
                if (this.isDark) {
                    drawable4 = getDefaultDrawable(this.context, R.drawable.dark_dinamicx_disunchk);
                } else {
                    drawable4 = getDefaultDrawable(this.context, R.drawable.dinamicx_disunchk);
                }
            }
            return new Drawable[]{updateDrawableSize, updateDrawableSize2, updateDrawableSize3, updateDrawableSize(drawable4, this.context, this.width, this.height)};
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Drawable[] drawableArr) {
            AppCompatCheckBox appCompatCheckBox = (AppCompatCheckBox) this.checkBoxRef.get();
            if (appCompatCheckBox != null) {
                String str = (String) appCompatCheckBox.getTag(DXCheckBoxWidgetNode.NEED_INT_CHECK_IMG);
                String str2 = (String) appCompatCheckBox.getTag(DXCheckBoxWidgetNode.NEED_INT_UNCHECK_IMG);
                String str3 = (String) appCompatCheckBox.getTag(DXCheckBoxWidgetNode.NEED_INT_DIS_CHECK_IMG);
                String str4 = (String) appCompatCheckBox.getTag(DXCheckBoxWidgetNode.NEED_INT_DIS_UNCHECK_IMG);
                if (str.equals(this.newCheckedImage) && str2.equals(this.newUncheckedImage) && str3.equals(this.newDisCheckImage) && str4.equals(this.newDisUncheckImage)) {
                    updateInternalStatus(appCompatCheckBox, drawableArr[0], drawableArr[1], drawableArr[2], drawableArr[3]);
                    appCompatCheckBox.setTag(DXCheckBoxWidgetNode.ALREADY_INT_CHECK_IMG, str);
                    appCompatCheckBox.setTag(DXCheckBoxWidgetNode.ALREADY_INT_UNCHECK_IMG, str2);
                    appCompatCheckBox.setTag(DXCheckBoxWidgetNode.ALREADY_INT_DIS_CHECK_IMG, str3);
                    appCompatCheckBox.setTag(DXCheckBoxWidgetNode.ALREADY_INT_DIS_UNCHECK_IMG, str4);
                }
            }
        }

        private Drawable updateDrawableSize(Drawable drawable, Context context2, int i, int i2) {
            if (!(drawable instanceof BitmapDrawable)) {
                return null;
            }
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
            BitmapDrawable bitmapDrawable = new BitmapDrawable(context2.getResources(), Bitmap.createScaledBitmap(bitmap, i, i2, true));
            bitmapDrawable.setTargetDensity(bitmap.getDensity());
            return bitmapDrawable;
        }

        public void updateInternalStatus(AppCompatCheckBox appCompatCheckBox, Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
            if (appCompatCheckBox != null) {
                appCompatCheckBox.setButtonDrawable((Drawable) getSelector(drawable, drawable2, drawable3, drawable4));
            }
        }

        private Drawable getDrawable(Context context2, String str) {
            try {
                return context2.getResources().getDrawable(context2.getResources().getIdentifier(str, "drawable", context2.getPackageName()));
            } catch (Throwable unused) {
                return null;
            }
        }

        private Drawable getDefaultDrawable(Context context2, int i) {
            return context2.getResources().getDrawable(i);
        }

        private StateListDrawable getSelector(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
            return DXDrawableTools.getCheckBoxSelector(drawable, drawable2, drawable3, drawable4);
        }
    }

    /* access modifiers changed from: protected */
    public void onBindEvent(Context context, View view, long j) {
        if (view != null && (view instanceof AppCompatCheckBox) && j == 5288679823228297259L) {
            ((AppCompatCheckBox) view).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    if (!DXCheckBoxWidgetNode.this.isInitCheckState) {
                        DXCheckBoxEvent dXCheckBoxEvent = new DXCheckBoxEvent(5288679823228297259L);
                        dXCheckBoxEvent.setChecked(z);
                        DXCheckBoxWidgetNode.this.postEvent(dXCheckBoxEvent);
                    }
                }
            });
        }
    }

    public int getChecked() {
        return this.checked;
    }

    public String getCheckImg() {
        return this.checkImg;
    }

    public String getUncheckImg() {
        return this.uncheckImg;
    }

    public String getDisCheckImg() {
        return this.disCheckImg;
    }

    public String getDisUnchkImg() {
        return this.disUnCheckImg;
    }

    public void setChecked(int i) {
        this.checked = i;
    }

    public void setCheckImg(String str) {
        this.checkImg = str;
    }

    public void setUncheckImg(String str) {
        this.uncheckImg = str;
    }

    public void setDisCheckImg(String str) {
        this.disCheckImg = str;
    }

    public void setDisUnCheckImg(String str) {
        this.disUnCheckImg = str;
    }

    public boolean isAsyncImageLoad() {
        return this.asyncImageLoad;
    }

    public void setAsyncImageLoad(boolean z) {
        this.asyncImageLoad = z;
    }

    public boolean isInitCheckState() {
        return this.isInitCheckState;
    }

    public void setInitCheckState(boolean z) {
        this.isInitCheckState = z;
    }
}
