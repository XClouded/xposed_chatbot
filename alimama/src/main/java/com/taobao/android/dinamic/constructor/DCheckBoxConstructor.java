package com.taobao.android.dinamic.constructor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.CompoundButton;
import androidx.appcompat.widget.AppCompatCheckBox;
import com.taobao.android.dinamic.DinamicTagKey;
import com.taobao.android.dinamic.R;
import com.taobao.android.dinamic.dinamic.DinamicViewAdvancedConstructor;
import com.taobao.android.dinamic.expressionv2.NumberUtil;
import com.taobao.android.dinamic.model.DinamicParams;
import com.taobao.android.dinamic.property.DAttrConstant;
import com.taobao.android.dinamic.property.DinamicEventHandlerWorker;
import com.taobao.android.dinamic.property.DinamicProperty;
import com.taobao.android.dinamic.property.ScreenTool;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Map;

public class DCheckBoxConstructor extends DinamicViewAdvancedConstructor {
    private static final String D_CHECKED = "dChecked";
    private static final String D_CHECK_IMG = "dCheckImg";
    private static final String D_DISCHECK_IMG = "dDisCheckImg";
    private static final String D_DISUNCHECK_IMG = "dDisUnCheckImg";
    private static final String D_HEIGHT = "dHeight";
    private static final String D_UNCHECK_IMG = "dUncheckImg";
    private static final String D_WIDTH = "dWidth";
    private static final String VIEW_EVENT_ON_CHANGE = "onChange";
    public static final String VIEW_TAG = "DCheckBox";

    public void setAttributes(View view, Map<String, Object> map, ArrayList<String> arrayList, DinamicParams dinamicParams) {
        int i;
        int i2;
        View view2 = view;
        Map<String, Object> map2 = map;
        ArrayList<String> arrayList2 = arrayList;
        super.setAttributes(view, map, arrayList, dinamicParams);
        AppCompatCheckBox appCompatCheckBox = view2 instanceof AppCompatCheckBox ? (AppCompatCheckBox) view2 : null;
        if (arrayList2.contains("dWidth") || arrayList2.contains("dHeight") || arrayList2.contains(D_CHECK_IMG) || arrayList2.contains(D_UNCHECK_IMG) || arrayList2.contains(D_DISCHECK_IMG) || arrayList2.contains(D_DISUNCHECK_IMG)) {
            int defaultSize = getDefaultSize(view.getContext());
            int defaultSize2 = getDefaultSize(view.getContext());
            Object obj = map2.get("dWidth");
            Object obj2 = map2.get("dHeight");
            int px = ScreenTool.getPx(view.getContext(), obj, -1);
            int px2 = ScreenTool.getPx(view.getContext(), obj2, -1);
            if (px == -1 || px2 == -1) {
                i = defaultSize;
                i2 = defaultSize2;
            } else {
                i2 = px;
                i = px2;
            }
            String str = (String) map2.get(D_CHECK_IMG);
            String str2 = (String) view2.getTag(DinamicTagKey.ALREADY_INT_CHECK_IMG);
            if (str == null) {
                str = "dinamicx_checked";
            }
            String str3 = str;
            String str4 = (String) map2.get(D_UNCHECK_IMG);
            String str5 = (String) view2.getTag(DinamicTagKey.ALREADY_INT_UNCHECK_IMG);
            if (str4 == null) {
                str4 = "dinamicx_uncheck";
            }
            String str6 = str4;
            String str7 = (String) map2.get(D_DISCHECK_IMG);
            String str8 = (String) view2.getTag(DinamicTagKey.ALREADY_INT_DIS_CHECK_IMG);
            if (str7 == null) {
                str7 = "dinamicx_discheck";
            }
            String str9 = str7;
            String str10 = (String) map2.get(D_DISUNCHECK_IMG);
            String str11 = (String) view2.getTag(DinamicTagKey.ALREADY_INT_DIS_UNCHECK_IMG);
            if (str10 == null) {
                str10 = "dinamicx_disunchk";
            }
            if (str2 == null && str5 == null && str8 == null && str11 == null) {
                appCompatCheckBox.setButtonDrawable((Drawable) null);
            }
            if (!str3.equals(str2) || !str6.equals(str5) || !str9.equals(str8) || !str10.equals(str11)) {
                view2.setTag(DinamicTagKey.NEED_INT_CHECK_IMG, str3);
                view2.setTag(DinamicTagKey.NEED_INT_UNCHECK_IMG, str6);
                view2.setTag(DinamicTagKey.NEED_INT_DIS_CHECK_IMG, str9);
                view2.setTag(DinamicTagKey.NEED_INT_DIS_UNCHECK_IMG, str10);
                new LoadDrawableTask(appCompatCheckBox, str3, str6, str9, str10, i2, i).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
            }
        }
        if (arrayList2.contains(D_CHECKED)) {
            setChecked(appCompatCheckBox, NumberUtil.parseBoolean((String) map2.get(D_CHECKED)));
        }
        if (arrayList2.contains(DAttrConstant.VIEW_ENABLED)) {
            String str12 = (String) map2.get(DAttrConstant.VIEW_ENABLED);
            if (!TextUtils.isEmpty(str12)) {
                setEnable(view2, NumberUtil.parseBoolean(str12));
            } else {
                setEnable(view2, true);
            }
        }
    }

    private void setEnable(View view, boolean z) {
        view.setEnabled(z);
    }

    public static class LoadDrawableTask extends AsyncTask<Void, Void, Drawable[]> {
        private WeakReference<AppCompatCheckBox> checkBoxRef;
        private Context context;
        int height;
        String newCheckedImage = null;
        String newDisCheckImage = null;
        String newDisUncheckImage = null;
        String newUncheckedImage = null;
        int width;

        public LoadDrawableTask(AppCompatCheckBox appCompatCheckBox, String str, String str2, String str3, String str4, int i, int i2) {
            this.context = appCompatCheckBox.getContext().getApplicationContext();
            this.newCheckedImage = str;
            this.newUncheckedImage = str2;
            this.newDisCheckImage = str3;
            this.newDisUncheckImage = str4;
            this.width = i;
            this.height = i2;
            this.checkBoxRef = new WeakReference<>(appCompatCheckBox);
        }

        /* access modifiers changed from: protected */
        public Drawable[] doInBackground(Void... voidArr) {
            Drawable drawable = getDrawable(this.context, this.newCheckedImage);
            if (drawable == null) {
                drawable = getDefaultDrawable(this.context, R.drawable.dinamicx_checked);
            }
            Drawable updateDrawableSize = updateDrawableSize(drawable, this.context, this.width, this.height);
            Drawable drawable2 = getDrawable(this.context, this.newUncheckedImage);
            if (drawable2 == null) {
                drawable2 = getDefaultDrawable(this.context, R.drawable.dinamicx_uncheck);
            }
            Drawable updateDrawableSize2 = updateDrawableSize(drawable2, this.context, this.width, this.height);
            Drawable drawable3 = getDrawable(this.context, this.newDisCheckImage);
            if (drawable3 == null) {
                drawable3 = getDefaultDrawable(this.context, R.drawable.dinamicx_discheck);
            }
            Drawable updateDrawableSize3 = updateDrawableSize(drawable3, this.context, this.width, this.height);
            Drawable drawable4 = getDrawable(this.context, this.newDisUncheckImage);
            if (drawable4 == null) {
                drawable4 = getDefaultDrawable(this.context, R.drawable.dinamicx_disunchk);
            }
            return new Drawable[]{updateDrawableSize, updateDrawableSize2, updateDrawableSize3, updateDrawableSize(drawable4, this.context, this.width, this.height)};
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Drawable[] drawableArr) {
            AppCompatCheckBox appCompatCheckBox = (AppCompatCheckBox) this.checkBoxRef.get();
            if (appCompatCheckBox != null) {
                String str = (String) appCompatCheckBox.getTag(DinamicTagKey.NEED_INT_CHECK_IMG);
                String str2 = (String) appCompatCheckBox.getTag(DinamicTagKey.NEED_INT_UNCHECK_IMG);
                String str3 = (String) appCompatCheckBox.getTag(DinamicTagKey.NEED_INT_DIS_CHECK_IMG);
                String str4 = (String) appCompatCheckBox.getTag(DinamicTagKey.NEED_INT_DIS_UNCHECK_IMG);
                if (str.equals(this.newCheckedImage) && str2.equals(this.newUncheckedImage) && str3.equals(this.newDisCheckImage) && str4.equals(this.newDisUncheckImage)) {
                    updateInternalStatus(appCompatCheckBox, drawableArr[0], drawableArr[1], drawableArr[2], drawableArr[3]);
                    appCompatCheckBox.setTag(DinamicTagKey.ALREADY_INT_CHECK_IMG, str);
                    appCompatCheckBox.setTag(DinamicTagKey.ALREADY_INT_UNCHECK_IMG, str2);
                    appCompatCheckBox.setTag(DinamicTagKey.ALREADY_INT_DIS_CHECK_IMG, str3);
                    appCompatCheckBox.setTag(DinamicTagKey.ALREADY_INT_DIS_UNCHECK_IMG, str4);
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

        private void updateInternalStatus(AppCompatCheckBox appCompatCheckBox, Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
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
            return DrawableTools.getCheckBoxSelector(drawable, drawable2, drawable3, drawable4);
        }
    }

    public void setEvents(View view, DinamicParams dinamicParams) {
        new DCheckBoxEventHandlerWorker().bindEventHandler(view, dinamicParams);
    }

    public void setChecked(AppCompatCheckBox appCompatCheckBox, boolean z) {
        if (appCompatCheckBox != null) {
            appCompatCheckBox.setTag(R.id.change_with_attribute, "true");
            appCompatCheckBox.setChecked(z);
            appCompatCheckBox.setTag(R.id.change_with_attribute, "false");
        }
    }

    private int getDefaultSize(Context context) {
        return (int) TypedValue.applyDimension(1, 17.0f, context.getResources().getDisplayMetrics());
    }

    public View initializeView(String str, Context context, AttributeSet attributeSet) {
        getDefaultSize(context);
        AppCompatCheckBox appCompatCheckBox = new AppCompatCheckBox(context, attributeSet);
        appCompatCheckBox.setClickable(true);
        return appCompatCheckBox;
    }

    private static class DCheckBoxEventHandlerWorker extends DinamicEventHandlerWorker {
        private DCheckBoxEventHandlerWorker() {
        }

        public void bindEventHandler(View view, DinamicParams dinamicParams) {
            bindSelfEvent(view, dinamicParams);
        }

        public void bindSelfEvent(View view, DinamicParams dinamicParams) {
            DinamicProperty dinamicProperty = (DinamicProperty) view.getTag(DinamicTagKey.PROPERTY_KEY);
            if (dinamicProperty != null) {
                Map<String, String> map = dinamicProperty.eventProperty;
                if (!map.isEmpty() && map.containsKey("onChange") && (view instanceof AppCompatCheckBox)) {
                    ((AppCompatCheckBox) view).setOnCheckedChangeListener(new OnChangeListener(this, dinamicParams, dinamicProperty, view));
                }
            }
        }
    }

    private static class OnChangeListener implements CompoundButton.OnCheckedChangeListener {
        private DinamicParams mDinamicParams;
        private DCheckBoxEventHandlerWorker mHandler;
        private String mOnChangeExpression;
        private DinamicProperty mProperty;
        private View mView;

        public OnChangeListener(DCheckBoxEventHandlerWorker dCheckBoxEventHandlerWorker, DinamicParams dinamicParams, DinamicProperty dinamicProperty, View view) {
            this.mHandler = dCheckBoxEventHandlerWorker;
            this.mDinamicParams = dinamicParams;
            this.mProperty = dinamicProperty;
            this.mView = view;
            Map<String, String> map = dinamicProperty.eventProperty;
            if (!map.isEmpty()) {
                this.mOnChangeExpression = map.get("onChange");
            }
        }

        public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            Object tag = compoundButton.getTag(R.id.change_with_attribute);
            if (!TextUtils.isEmpty(this.mOnChangeExpression) && !"true".equals(tag)) {
                ArrayList arrayList = new ArrayList(5);
                arrayList.add(Boolean.valueOf(compoundButton.isChecked()));
                this.mView.setTag(DinamicTagKey.VIEW_PARAMS, arrayList);
                DCheckBoxEventHandlerWorker dCheckBoxEventHandlerWorker = this.mHandler;
                DCheckBoxEventHandlerWorker.handleEvent(this.mView, this.mDinamicParams, this.mProperty, this.mOnChangeExpression);
            }
        }
    }
}
