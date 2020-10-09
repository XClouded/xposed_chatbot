package android.taobao.windvane.embed;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.internal.view.SupportMenu;

import org.json.JSONException;
import org.json.JSONObject;

public class EmbedViewDemo extends BaseEmbedView {
    TextView tv;

    /* access modifiers changed from: protected */
    public String getViewType() {
        return "demo";
    }

    /* access modifiers changed from: protected */
    public View generateView(Context context) {
        int i;
        RelativeLayout relativeLayout = new RelativeLayout(context);
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(-1, -2));
        int i2 = this.params.mWidth;
        int i3 = this.params.mHeight;
        if (context instanceof Activity) {
            Display defaultDisplay = ((Activity) context).getWindowManager().getDefaultDisplay();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            defaultDisplay.getMetrics(displayMetrics);
            i = displayMetrics.widthPixels;
            int i4 = displayMetrics.heightPixels;
        } else {
            i = 0;
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, (int) (((float) i3) * (((float) i) / ((float) i2))));
        layoutParams.addRule(13);
        this.tv = new TextView(context);
        this.tv.setBackgroundColor(-7829368);
        this.tv.setText("EmbedView DEMO");
        this.tv.setTextColor(SupportMenu.CATEGORY_MASK);
        this.tv.setTextSize(30.0f);
        this.tv.setGravity(17);
        relativeLayout.addView(this.tv, layoutParams);
        return relativeLayout;
    }

    public boolean execute(String str, String str2, WVCallBackContext wVCallBackContext) {
        if (str2 == null) {
            return false;
        }
        try {
            JSONObject jSONObject = new JSONObject(str2);
            if ("setText".equals(str)) {
                if (this.tv != null) {
                    this.tv.setText(jSONObject.getString("text"));
                    wVCallBackContext.success();
                }
                return true;
            } else if ("setTextSize".equals(str)) {
                if (this.tv != null) {
                    this.tv.setTextSize(Float.valueOf(jSONObject.getString("size")).floatValue());
                    wVCallBackContext.success();
                }
                return true;
            } else if ("setBackground".equals(str)) {
                if (this.tv != null) {
                    this.tv.setBackgroundColor(Color.parseColor(jSONObject.getString("color")));
                    wVCallBackContext.success();
                }
                return true;
            } else if (!"setTextColor".equals(str)) {
                return false;
            } else {
                if (this.tv != null) {
                    this.tv.setTextColor(Color.parseColor(jSONObject.getString("color")));
                    wVCallBackContext.success();
                }
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
}
