package com.taobao.weex.analyzer.core.scalpel;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.Deque;

public class ScalpelFrameLayout extends FrameLayout {
    private static final int CHILD_COUNT_ESTIMATION = 25;
    private static final int CHROME_COLOR = -7829368;
    private static final int CHROME_SHADOW_COLOR = -16777216;
    private static final boolean DEBUG = false;
    private static final int ROTATION_DEFAULT_X = -10;
    private static final int ROTATION_DEFAULT_Y = 15;
    private static final int ROTATION_MAX = 60;
    private static final int ROTATION_MIN = -60;
    private static final int SPACING_DEFAULT = 25;
    private static final int SPACING_MAX = 100;
    private static final int SPACING_MIN = 10;
    private static final int TEXT_OFFSET_DP = 2;
    private static final int TEXT_SIZE_DP = 10;
    private static final int TRACKING_HORIZONTALLY = -1;
    private static final int TRACKING_UNKNOWN = 0;
    private static final int TRACKING_VERTICALLY = 1;
    private static final float ZOOM_DEFAULT = 0.6f;
    private static final float ZOOM_MAX = 2.0f;
    private static final float ZOOM_MIN = 0.33f;
    private final Camera camera;
    private int chromeColor;
    private int chromeShadowColor;
    private final float density;
    private boolean drawIds;
    private boolean drawViews;
    private boolean enabled;
    private final SparseArray<String> idNames;
    private boolean isDrawViewNames;
    private float lastOneX;
    private float lastOneY;
    private float lastTwoX;
    private float lastTwoY;
    private final Pool<LayeredView> layeredViewPool;
    private final Deque<LayeredView> layeredViewQueue;
    private final int[] location;
    private OnDrawViewNameListener mListener;
    private final Matrix matrix;
    private int multiTouchTracking;
    private int pointerOne;
    private int pointerTwo;
    private final Resources res;
    private float rotationX;
    private float rotationY;
    private final float slop;
    private float spacing;
    private final float textOffset;
    private final float textSize;
    private final Paint viewBorderPaint;
    private final Rect viewBoundsRect;
    private final BitSet visibilities;
    private float zoom;

    public interface OnDrawViewNameListener {
        @Nullable
        String onDrawViewName(@NonNull View view, @NonNull String str);
    }

    private static void log(String str, Object... objArr) {
        Log.d("Scalpel", String.format(str, objArr));
    }

    private static class LayeredView {
        int layer;
        View view;

        private LayeredView() {
        }

        /* access modifiers changed from: package-private */
        public void set(View view2, int i) {
            this.view = view2;
            this.layer = i;
        }

        /* access modifiers changed from: package-private */
        public void clear() {
            this.view = null;
            this.layer = -1;
        }
    }

    public ScalpelFrameLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public ScalpelFrameLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ScalpelFrameLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.viewBoundsRect = new Rect();
        this.viewBorderPaint = new Paint(1);
        this.camera = new Camera();
        this.matrix = new Matrix();
        this.location = new int[2];
        this.visibilities = new BitSet(25);
        this.idNames = new SparseArray<>();
        this.layeredViewQueue = new ArrayDeque();
        this.layeredViewPool = new Pool<LayeredView>(25) {
            /* access modifiers changed from: protected */
            public LayeredView newObject() {
                return new LayeredView();
            }
        };
        this.drawViews = true;
        this.pointerOne = -1;
        this.pointerTwo = -1;
        this.multiTouchTracking = 0;
        this.rotationY = 15.0f;
        this.rotationX = -10.0f;
        this.zoom = ZOOM_DEFAULT;
        this.spacing = 25.0f;
        this.res = context.getResources();
        this.density = context.getResources().getDisplayMetrics().density;
        this.slop = (float) ViewConfiguration.get(context).getScaledTouchSlop();
        this.textSize = this.density * 10.0f;
        this.textOffset = this.density * ZOOM_MAX;
        setChromeColor(CHROME_COLOR);
        this.viewBorderPaint.setStyle(Paint.Style.STROKE);
        this.viewBorderPaint.setTextSize(this.textSize);
        setChromeShadowColor(-16777216);
        if (Build.VERSION.SDK_INT >= 16) {
            this.viewBorderPaint.setTypeface(Typeface.create("sans-serif-condensed", 0));
        }
    }

    public void setChromeColor(int i) {
        if (this.chromeColor != i) {
            this.viewBorderPaint.setColor(i);
            this.chromeColor = i;
            invalidate();
        }
    }

    public void setOnDrawViewNameListener(OnDrawViewNameListener onDrawViewNameListener) {
        this.mListener = onDrawViewNameListener;
    }

    public int getChromeColor() {
        return this.chromeColor;
    }

    public void setChromeShadowColor(int i) {
        if (this.chromeShadowColor != i) {
            this.viewBorderPaint.setShadowLayer(1.0f, -1.0f, 1.0f, i);
            this.chromeShadowColor = i;
            invalidate();
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public int getChromeShadowColor() {
        return this.chromeShadowColor;
    }

    public void setLayerInteractionEnabled(boolean z) {
        if (this.enabled != z) {
            this.enabled = z;
            setWillNotDraw(!z);
            invalidate();
        }
    }

    public boolean isLayerInteractionEnabled() {
        return this.enabled;
    }

    public void setDrawViews(boolean z) {
        if (this.drawViews != z) {
            this.drawViews = z;
            invalidate();
        }
    }

    public boolean isDrawingViews() {
        return this.drawViews;
    }

    public void setDrawIds(boolean z) {
        if (this.drawIds != z) {
            this.drawIds = z;
            invalidate();
        }
    }

    public boolean isDrawingIds() {
        return this.drawIds;
    }

    public void setDrawViewNames(boolean z) {
        if (this.isDrawViewNames != z) {
            this.isDrawViewNames = z;
            invalidate();
        }
    }

    public boolean isDrawViewNames() {
        return this.isDrawViewNames;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return this.enabled || super.onInterceptTouchEvent(motionEvent);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int i;
        if (!this.enabled) {
            return super.onTouchEvent(motionEvent);
        }
        int actionMasked = motionEvent.getActionMasked();
        int i2 = 0;
        switch (actionMasked) {
            case 0:
            case 5:
                if (actionMasked != 0) {
                    i2 = motionEvent.getActionIndex();
                }
                if (this.pointerOne != -1) {
                    if (this.pointerTwo == -1) {
                        this.pointerTwo = motionEvent.getPointerId(i2);
                        this.lastTwoX = motionEvent.getX(i2);
                        this.lastTwoY = motionEvent.getY(i2);
                        break;
                    }
                } else {
                    this.pointerOne = motionEvent.getPointerId(i2);
                    this.lastOneX = motionEvent.getX(i2);
                    this.lastOneY = motionEvent.getY(i2);
                    break;
                }
                break;
            case 1:
            case 3:
            case 6:
                if (actionMasked != 6) {
                    i = 0;
                } else {
                    i = motionEvent.getActionIndex();
                }
                int pointerId = motionEvent.getPointerId(i);
                if (this.pointerOne != pointerId) {
                    if (this.pointerTwo == pointerId) {
                        this.pointerTwo = -1;
                        this.multiTouchTracking = 0;
                        break;
                    }
                } else {
                    this.pointerOne = this.pointerTwo;
                    this.lastOneX = this.lastTwoX;
                    this.lastOneY = this.lastTwoY;
                    this.pointerTwo = -1;
                    this.multiTouchTracking = 0;
                    break;
                }
                break;
            case 2:
                if (this.pointerTwo != -1) {
                    int findPointerIndex = motionEvent.findPointerIndex(this.pointerOne);
                    int findPointerIndex2 = motionEvent.findPointerIndex(this.pointerTwo);
                    float x = motionEvent.getX(findPointerIndex);
                    float y = motionEvent.getY(findPointerIndex);
                    float x2 = motionEvent.getX(findPointerIndex2);
                    float y2 = motionEvent.getY(findPointerIndex2);
                    float f = x - this.lastOneX;
                    float f2 = y - this.lastOneY;
                    float f3 = x2 - this.lastTwoX;
                    float f4 = y2 - this.lastTwoY;
                    if (this.multiTouchTracking == 0) {
                        float abs = Math.abs(f) + Math.abs(f3);
                        float abs2 = Math.abs(f2) + Math.abs(f4);
                        if (abs > this.slop * ZOOM_MAX || abs2 > this.slop * ZOOM_MAX) {
                            if (abs > abs2) {
                                this.multiTouchTracking = -1;
                            } else {
                                this.multiTouchTracking = 1;
                            }
                        }
                    }
                    if (this.multiTouchTracking == 1) {
                        if (y >= y2) {
                            this.zoom += (f2 / ((float) getHeight())) - (f4 / ((float) getHeight()));
                        } else {
                            this.zoom += (f4 / ((float) getHeight())) - (f2 / ((float) getHeight()));
                        }
                        this.zoom = Math.min(Math.max(this.zoom, ZOOM_MIN), ZOOM_MAX);
                        invalidate();
                    } else if (this.multiTouchTracking == -1) {
                        if (x >= x2) {
                            this.spacing += ((f / ((float) getWidth())) * 100.0f) - ((f3 / ((float) getWidth())) * 100.0f);
                        } else {
                            this.spacing += ((f3 / ((float) getWidth())) * 100.0f) - ((f / ((float) getWidth())) * 100.0f);
                        }
                        this.spacing = Math.min(Math.max(this.spacing, 10.0f), 100.0f);
                        invalidate();
                    }
                    if (this.multiTouchTracking != 0) {
                        this.lastOneX = x;
                        this.lastOneY = y;
                        this.lastTwoX = x2;
                        this.lastTwoY = y2;
                        break;
                    }
                } else {
                    int pointerCount = motionEvent.getPointerCount();
                    while (i2 < pointerCount) {
                        if (this.pointerOne == motionEvent.getPointerId(i2)) {
                            float x3 = motionEvent.getX(i2);
                            float y3 = motionEvent.getY(i2);
                            this.rotationY = Math.min(Math.max(this.rotationY + (((x3 - this.lastOneX) / ((float) getWidth())) * 90.0f), -60.0f), 60.0f);
                            this.rotationX = Math.min(Math.max(this.rotationX + (((-(y3 - this.lastOneY)) / ((float) getHeight())) * 90.0f), -60.0f), 60.0f);
                            this.lastOneX = x3;
                            this.lastOneY = y3;
                            invalidate();
                        }
                        i2++;
                    }
                    break;
                }
                break;
        }
        return true;
    }

    public void draw(Canvas canvas) {
        int id;
        if (!this.enabled) {
            super.draw(canvas);
            return;
        }
        getLocationInWindow(this.location);
        float f = (float) this.location[0];
        float f2 = (float) this.location[1];
        int save = canvas.save();
        float width = ((float) getWidth()) / ZOOM_MAX;
        float height = ((float) getHeight()) / ZOOM_MAX;
        this.camera.save();
        this.camera.rotate(this.rotationX, this.rotationY, 0.0f);
        this.camera.getMatrix(this.matrix);
        this.camera.restore();
        this.matrix.preTranslate(-width, -height);
        this.matrix.postTranslate(width, height);
        canvas.concat(this.matrix);
        canvas.scale(this.zoom, this.zoom, width, height);
        if (this.layeredViewQueue.isEmpty()) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                LayeredView obtain = this.layeredViewPool.obtain();
                obtain.set(getChildAt(i), 0);
                this.layeredViewQueue.add(obtain);
            }
            while (!this.layeredViewQueue.isEmpty()) {
                LayeredView removeFirst = this.layeredViewQueue.removeFirst();
                View view = removeFirst.view;
                int i2 = removeFirst.layer;
                removeFirst.clear();
                this.layeredViewPool.restore(removeFirst);
                boolean z = view instanceof ViewGroup;
                if (z) {
                    ViewGroup viewGroup = (ViewGroup) view;
                    this.visibilities.clear();
                    int childCount2 = viewGroup.getChildCount();
                    for (int i3 = 0; i3 < childCount2; i3++) {
                        View childAt = viewGroup.getChildAt(i3);
                        if (childAt.getVisibility() == 0) {
                            this.visibilities.set(i3);
                            childAt.setVisibility(4);
                        }
                    }
                }
                int save2 = canvas.save();
                float f3 = (float) i2;
                canvas.translate(this.spacing * f3 * this.density * (this.rotationY / 60.0f), -(f3 * this.spacing * this.density * (this.rotationX / 60.0f)));
                view.getLocationInWindow(this.location);
                canvas.translate(((float) this.location[0]) - f, ((float) this.location[1]) - f2);
                this.viewBoundsRect.set(0, 0, view.getWidth(), view.getHeight());
                canvas.drawRect(this.viewBoundsRect, this.viewBorderPaint);
                if (this.drawViews) {
                    view.draw(canvas);
                }
                if (this.drawIds && (id = view.getId()) != -1) {
                    canvas.drawText(nameForId(id), this.textOffset, this.textSize, this.viewBorderPaint);
                }
                if (this.isDrawViewNames && !this.drawIds) {
                    String simpleName = view.getClass().getSimpleName();
                    if (this.mListener != null) {
                        simpleName = this.mListener.onDrawViewName(view, simpleName);
                    }
                    if (!TextUtils.isEmpty(simpleName)) {
                        canvas.drawText(simpleName, this.textOffset, this.textSize, this.viewBorderPaint);
                    }
                }
                canvas.restoreToCount(save2);
                if (z) {
                    ViewGroup viewGroup2 = (ViewGroup) view;
                    int childCount3 = viewGroup2.getChildCount();
                    for (int i4 = 0; i4 < childCount3; i4++) {
                        if (this.visibilities.get(i4)) {
                            View childAt2 = viewGroup2.getChildAt(i4);
                            childAt2.setVisibility(0);
                            LayeredView obtain2 = this.layeredViewPool.obtain();
                            obtain2.set(childAt2, i2 + 1);
                            this.layeredViewQueue.add(obtain2);
                        }
                    }
                }
            }
            canvas.restoreToCount(save);
            return;
        }
        throw new AssertionError("View queue is not empty.");
    }

    private String nameForId(int i) {
        String str = this.idNames.get(i);
        if (str == null) {
            try {
                str = this.res.getResourceEntryName(i);
            } catch (Resources.NotFoundException unused) {
                str = String.format("0x%8x", new Object[]{Integer.valueOf(i)});
            }
            this.idNames.put(i, str);
        }
        return str;
    }

    private static abstract class Pool<T> {
        private final Deque<T> pool;

        /* access modifiers changed from: protected */
        public abstract T newObject();

        Pool(int i) {
            this.pool = new ArrayDeque(i);
            for (int i2 = 0; i2 < i; i2++) {
                this.pool.addLast(newObject());
            }
        }

        /* access modifiers changed from: package-private */
        public T obtain() {
            return this.pool.isEmpty() ? newObject() : this.pool.removeLast();
        }

        /* access modifiers changed from: package-private */
        public void restore(T t) {
            this.pool.addLast(t);
        }
    }
}
