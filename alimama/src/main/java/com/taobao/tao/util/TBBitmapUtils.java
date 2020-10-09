package com.taobao.tao.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import com.taobao.uikit.actionbar.R;

public class TBBitmapUtils {
    private static String TAG = "TBBitmapUtils";

    public static Bitmap captureView(Activity activity, View view, int i, int i2) {
        long currentTimeMillis = System.currentTimeMillis();
        if (view == null || i <= 0 || i2 <= 0) {
            return null;
        }
        Bitmap createBitmap = Bitmap.createBitmap(Math.round(((float) view.getWidth()) * 0.1f), Math.round(((float) view.getHeight()) * 0.1f), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(createBitmap);
        canvas.scale(0.1f, 0.1f);
        view.draw(canvas);
        for (TextureView next : ViewUtils.find((ViewGroup) activity.findViewById(16908290), TextureView.class)) {
            View findViewById = ((ViewGroup) next.getParent()).findViewById(next.getContext().getResources().getIdentifier("dw_frontcover_cover", "id", next.getContext().getPackageName()));
            if (findViewById == null || findViewById.getParent() == null || ((View) findViewById.getParent()).getVisibility() != 0) {
                Rect rect = new Rect();
                next.getGlobalVisibleRect(rect);
                Bitmap bitmap = next.getBitmap(rect.right - rect.left, rect.bottom - rect.top);
                if (bitmap != null) {
                    canvas.drawBitmap(bitmap, (float) rect.left, (float) rect.top, new Paint());
                }
            } else {
                findViewById.draw(canvas);
            }
        }
        canvas.drawColor(view.getContext().getResources().getColor(R.color.uik_public_menu_item_new_bg), PorterDuff.Mode.SRC_OVER);
        String str = TAG;
        Log.d(str, "capture consumes " + (System.currentTimeMillis() - currentTimeMillis));
        return Bitmap.createScaledBitmap(createBitmap, Math.round(((float) i) * 0.1f), Math.round(((float) i2) * 0.1f), false);
    }

    @TargetApi(17)
    public static Bitmap blur(int i, RenderScript renderScript, Bitmap bitmap) {
        long currentTimeMillis = System.currentTimeMillis();
        Allocation createFromBitmap = Allocation.createFromBitmap(renderScript, bitmap);
        Allocation createTyped = Allocation.createTyped(renderScript, createFromBitmap.getType());
        ScriptIntrinsicBlur create = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        create.setRadius((float) i);
        create.setInput(createFromBitmap);
        create.forEach(createTyped);
        createTyped.copyTo(bitmap);
        String str = TAG;
        Log.d(str, "blur consumes " + (System.currentTimeMillis() - currentTimeMillis));
        return bitmap;
    }
}
