package com.taobao.weex.ui.view.gesture;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.annotation.NonNull;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.weex.bridge.EventResult;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.WXEvent;
import com.taobao.weex.ui.component.Scrollable;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.view.gesture.WXGestureType;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WXGesture extends GestureDetector.SimpleOnGestureListener implements View.OnTouchListener {
    private static final int CUR_EVENT = -1;
    public static final String DOWN = "down";
    public static final String END = "end";
    public static final String LEFT = "left";
    public static final String MOVE = "move";
    public static final String RIGHT = "right";
    public static final String START = "start";
    private static final String TAG = "Gesture";
    public static final String UNKNOWN = "unknown";
    public static final String UP = "up";
    private WXComponent component;
    private Point globalEventOffset;
    private Point globalOffset;
    private Rect globalRect;
    private PointF locEventOffset;
    private PointF locLeftTop;
    private GestureDetector mGestureDetector;
    private boolean mIsPreventMoveEvent = false;
    private boolean mIsTouchEventConsumed = false;
    private int mParentOrientation = -1;
    private WXGestureType mPendingPan = null;
    private final List<View.OnTouchListener> mTouchListeners = new LinkedList();
    private long panDownTime = -1;
    private boolean requestDisallowInterceptTouchEvent = false;
    private int shouldBubbleCallRemainTimes = 0;
    private int shouldBubbleInterval = 0;
    private boolean shouldBubbleResult = true;
    private long swipeDownTime = -1;

    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    public WXGesture(WXComponent wXComponent, Context context) {
        this.component = wXComponent;
        this.globalRect = new Rect();
        this.globalOffset = new Point();
        this.globalEventOffset = new Point();
        this.locEventOffset = new PointF();
        this.locLeftTop = new PointF();
        this.mGestureDetector = new GestureDetector(context, this, new GestureHandler());
        Scrollable parentScroller = wXComponent.getParentScroller();
        if (parentScroller != null) {
            this.mParentOrientation = parentScroller.getOrientation();
        }
        this.shouldBubbleResult = WXUtils.getBoolean(wXComponent.getAttrs().get(Constants.Name.SHOULD_STOP_PROPAGATION_INIT_RESULT), true).booleanValue();
        this.shouldBubbleInterval = WXUtils.getNumberInt(wXComponent.getAttrs().get(Constants.Name.SHOULD_STOP_PROPAGATION_INTERVAL), 0);
    }

    private boolean isParentScrollable() {
        Scrollable parentScroller;
        if (this.component == null || (parentScroller = this.component.getParentScroller()) == null || parentScroller.isScrollable()) {
            return true;
        }
        return false;
    }

    private boolean hasSameOrientationWithParent() {
        if (this.mParentOrientation == 0 && this.component.containsGesture(WXGestureType.HighLevelGesture.HORIZONTALPAN)) {
            return true;
        }
        if (this.mParentOrientation != 1 || !this.component.containsGesture(WXGestureType.HighLevelGesture.VERTICALPAN)) {
            return false;
        }
        return true;
    }

    public void setPreventMoveEvent(boolean z) {
        this.mIsPreventMoveEvent = z;
    }

    public boolean isTouchEventConsumedByAdvancedGesture() {
        return this.mIsTouchEventConsumed;
    }

    public static boolean isStopPropagation(String str) {
        return Constants.Event.STOP_PROPAGATION.equals(str) || Constants.Event.STOP_PROPAGATION_RAX.equals(str);
    }

    public static boolean hasStopPropagation(WXComponent wXComponent) {
        WXEvent events = wXComponent.getEvents();
        if (events == null) {
            return false;
        }
        int size = events.size();
        int i = 0;
        while (i < size && i < events.size()) {
            if (isStopPropagation((String) events.get(i))) {
                return true;
            }
            i++;
        }
        return false;
    }

    private boolean shouldBubbleTouchEvent(MotionEvent motionEvent) {
        if (!hasStopPropagation(this.component)) {
            return true;
        }
        if (this.shouldBubbleInterval <= 0 || this.shouldBubbleCallRemainTimes <= 0) {
            Map<String, Object> createFireEventParam = createFireEventParam(motionEvent, -1, (String) null);
            createFireEventParam.put("type", "touch");
            if (motionEvent.getAction() == 0) {
                createFireEventParam.put("action", "start");
            } else if (motionEvent.getAction() == 3 || motionEvent.getAction() == 1) {
                createFireEventParam.put("action", "end");
            } else {
                createFireEventParam.put("action", MOVE);
            }
            String str = Constants.Event.STOP_PROPAGATION;
            if (!this.component.getEvents().contains(Constants.Event.STOP_PROPAGATION)) {
                str = Constants.Event.STOP_PROPAGATION_RAX;
            }
            EventResult fireEventWait = this.component.fireEventWait(str, createFireEventParam);
            if (fireEventWait.isSuccess() && fireEventWait.getResult() != null) {
                this.shouldBubbleResult = !WXUtils.getBoolean(fireEventWait.getResult(), Boolean.valueOf(!this.shouldBubbleResult)).booleanValue();
            }
            this.shouldBubbleCallRemainTimes = this.shouldBubbleInterval;
            return this.shouldBubbleResult;
        }
        this.shouldBubbleCallRemainTimes--;
        return this.shouldBubbleResult;
    }

    public void addOnTouchListener(View.OnTouchListener onTouchListener) {
        if (onTouchListener != null) {
            this.mTouchListeners.add(onTouchListener);
        }
    }

    public boolean removeTouchListener(View.OnTouchListener onTouchListener) {
        if (onTouchListener != null) {
            return this.mTouchListeners.remove(onTouchListener);
        }
        return false;
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouch(View view, MotionEvent motionEvent) {
        boolean z;
        ViewParent parent;
        if (this.requestDisallowInterceptTouchEvent) {
            this.requestDisallowInterceptTouchEvent = false;
            return false;
        }
        try {
            boolean onTouchEvent = this.mGestureDetector.onTouchEvent(motionEvent);
            if (this.mTouchListeners != null && !this.mTouchListeners.isEmpty()) {
                for (View.OnTouchListener onTouch : this.mTouchListeners) {
                    onTouchEvent |= onTouch.onTouch(view, motionEvent);
                }
            }
            switch (motionEvent.getActionMasked()) {
                case 0:
                case 5:
                    this.mIsTouchEventConsumed = false;
                    if (hasSameOrientationWithParent() && !isParentScrollable() && (parent = this.component.getRealView().getParent()) != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    onTouchEvent |= handleMotionEvent(WXGestureType.LowLevelGesture.ACTION_DOWN, motionEvent);
                    break;
                case 1:
                case 6:
                    finishDisallowInterceptTouchEvent(view);
                    onTouchEvent = onTouchEvent | handleMotionEvent(WXGestureType.LowLevelGesture.ACTION_UP, motionEvent) | handlePanMotionEvent(motionEvent);
                    break;
                case 2:
                    onTouchEvent |= handleMotionEvent(WXGestureType.LowLevelGesture.ACTION_MOVE, motionEvent);
                    break;
                case 3:
                    finishDisallowInterceptTouchEvent(view);
                    onTouchEvent = onTouchEvent | handleMotionEvent(WXGestureType.LowLevelGesture.ACTION_CANCEL, motionEvent) | handlePanMotionEvent(motionEvent);
                    break;
            }
            if (hasStopPropagation(this.component)) {
                ViewGroup viewGroup = (ViewGroup) view.getParent();
                if (viewGroup != null) {
                    z = !shouldBubbleTouchEvent(motionEvent);
                    viewGroup.requestDisallowInterceptTouchEvent(z);
                } else {
                    z = false;
                }
                if (this.component.getParent() != null) {
                    this.component.getParent().requestDisallowInterceptTouchEvent(z);
                }
                if (this.mIsTouchEventConsumed && WXUtils.getBoolean(this.component.getAttrs().get("cancelTouchOnConsume"), false).booleanValue()) {
                    motionEvent.setAction(3);
                }
            }
            return onTouchEvent;
        } catch (Exception e) {
            WXLogUtils.e("Gesture RunTime Error ", (Throwable) e);
            return false;
        }
    }

    private String getPanEventAction(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0:
                return "start";
            case 1:
                return "end";
            case 2:
                return MOVE;
            case 3:
                return "end";
            default:
                return "unknown";
        }
    }

    private void finishDisallowInterceptTouchEvent(View view) {
        if (view.getParent() != null) {
            view.getParent().requestDisallowInterceptTouchEvent(false);
        }
    }

    private boolean handlePanMotionEvent(MotionEvent motionEvent) {
        String str;
        if (this.mPendingPan == null) {
            return false;
        }
        if (this.mPendingPan == WXGestureType.HighLevelGesture.HORIZONTALPAN || this.mPendingPan == WXGestureType.HighLevelGesture.VERTICALPAN) {
            str = getPanEventAction(motionEvent);
        } else {
            str = null;
        }
        if (!this.component.containsGesture(this.mPendingPan)) {
            return false;
        }
        if (this.mIsPreventMoveEvent && MOVE.equals(str)) {
            return true;
        }
        for (Map<String, Object> fireEvent : createMultipleFireEventParam(motionEvent, str)) {
            this.component.fireEvent(this.mPendingPan.toString(), fireEvent);
        }
        if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            this.mPendingPan = null;
        }
        return true;
    }

    private boolean handleMotionEvent(WXGestureType wXGestureType, MotionEvent motionEvent) {
        if (!this.component.containsGesture(wXGestureType)) {
            return false;
        }
        for (Map<String, Object> fireEvent : createMultipleFireEventParam(motionEvent, (String) null)) {
            this.component.fireEvent(wXGestureType.toString(), fireEvent);
        }
        return true;
    }

    private List<Map<String, Object>> createMultipleFireEventParam(MotionEvent motionEvent, String str) {
        ArrayList arrayList = new ArrayList(motionEvent.getHistorySize() + 1);
        arrayList.add(createFireEventParam(motionEvent, -1, str));
        return arrayList;
    }

    private List<Map<String, Object>> getHistoricalEvents(MotionEvent motionEvent) {
        ArrayList arrayList = new ArrayList(motionEvent.getHistorySize());
        if (motionEvent.getActionMasked() == 2) {
            for (int i = 0; i < motionEvent.getHistorySize(); i++) {
                arrayList.add(createFireEventParam(motionEvent, i, (String) null));
            }
        }
        return arrayList;
    }

    private Map<String, Object> createFireEventParam(MotionEvent motionEvent, int i, String str) {
        JSONArray jSONArray = new JSONArray(motionEvent.getPointerCount());
        if (motionEvent.getActionMasked() == 2) {
            for (int i2 = 0; i2 < motionEvent.getPointerCount(); i2++) {
                jSONArray.add(createJSONObject(motionEvent, i, i2));
            }
        } else if (isPointerNumChanged(motionEvent)) {
            jSONArray.add(createJSONObject(motionEvent, -1, motionEvent.getActionIndex()));
        }
        HashMap hashMap = new HashMap();
        hashMap.put(WXGestureType.GestureInfo.HISTORICAL_XY, jSONArray);
        if (str != null) {
            hashMap.put("state", str);
        }
        return hashMap;
    }

    private boolean isPointerNumChanged(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0 || motionEvent.getActionMasked() == 5 || motionEvent.getActionMasked() == 1 || motionEvent.getActionMasked() == 6 || motionEvent.getActionMasked() == 3) {
            return true;
        }
        return false;
    }

    private boolean containsSimplePan() {
        return this.component.containsGesture(WXGestureType.HighLevelGesture.PAN_START) || this.component.containsGesture(WXGestureType.HighLevelGesture.PAN_MOVE) || this.component.containsGesture(WXGestureType.HighLevelGesture.PAN_END);
    }

    private JSONObject createJSONObject(MotionEvent motionEvent, int i, int i2) {
        PointF pointF;
        PointF pointF2;
        if (i == -1) {
            PointF eventLocInPageCoordinate = getEventLocInPageCoordinate(motionEvent, i2);
            pointF2 = eventLocInPageCoordinate;
            pointF = getEventLocInScreenCoordinate(motionEvent, i2);
        } else {
            pointF2 = getEventLocInPageCoordinate(motionEvent, i2, i);
            pointF = getEventLocInScreenCoordinate(motionEvent, i2, i);
        }
        JSONObject createJSONObject = createJSONObject(pointF, pointF2, (float) motionEvent.getPointerId(i2));
        float pressure = motionEvent.getPressure();
        if (pressure > 0.0f && pressure < 1.0f) {
            createJSONObject.put("force", (Object) Float.valueOf(motionEvent.getPressure()));
        }
        return createJSONObject;
    }

    @NonNull
    private JSONObject createJSONObject(PointF pointF, PointF pointF2, float f) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(WXGestureType.GestureInfo.PAGE_X, (Object) Float.valueOf(pointF2.x));
        jSONObject.put(WXGestureType.GestureInfo.PAGE_Y, (Object) Float.valueOf(pointF2.y));
        jSONObject.put(WXGestureType.GestureInfo.SCREEN_X, (Object) Float.valueOf(pointF.x));
        jSONObject.put(WXGestureType.GestureInfo.SCREEN_Y, (Object) Float.valueOf(pointF.y));
        jSONObject.put(WXGestureType.GestureInfo.POINTER_ID, (Object) Float.valueOf(f));
        return jSONObject;
    }

    private PointF getEventLocInScreenCoordinate(MotionEvent motionEvent, int i) {
        return getEventLocInScreenCoordinate(motionEvent, i, -1);
    }

    private PointF getEventLocInScreenCoordinate(MotionEvent motionEvent, int i, int i2) {
        float f;
        float f2;
        if (i2 == -1) {
            f = motionEvent.getX(i);
            f2 = motionEvent.getY(i);
        } else {
            float historicalX = motionEvent.getHistoricalX(i, i2);
            f2 = motionEvent.getHistoricalY(i, i2);
            f = historicalX;
        }
        return getEventLocInScreenCoordinate(f, f2);
    }

    @NonNull
    private PointF getEventLocInScreenCoordinate(float f, float f2) {
        this.globalRect.set(0, 0, 0, 0);
        this.globalOffset.set(0, 0);
        this.globalEventOffset.set((int) f, (int) f2);
        this.component.getRealView().getGlobalVisibleRect(this.globalRect, this.globalOffset);
        this.globalEventOffset.offset(this.globalOffset.x, this.globalOffset.y);
        return new PointF(WXViewUtils.getWebPxByWidth((float) this.globalEventOffset.x, this.component.getInstance().getInstanceViewPortWidth()), WXViewUtils.getWebPxByWidth((float) this.globalEventOffset.y, this.component.getInstance().getInstanceViewPortWidth()));
    }

    private PointF getEventLocInPageCoordinate(MotionEvent motionEvent, int i) {
        return getEventLocInPageCoordinate(motionEvent, i, -1);
    }

    private PointF getEventLocInPageCoordinate(MotionEvent motionEvent, int i, int i2) {
        float f;
        float f2;
        if (i2 == -1) {
            f = motionEvent.getX(i);
            f2 = motionEvent.getY(i);
        } else {
            float historicalX = motionEvent.getHistoricalX(i, i2);
            f2 = motionEvent.getHistoricalY(i, i2);
            f = historicalX;
        }
        return getEventLocInPageCoordinate(f, f2);
    }

    @NonNull
    private PointF getEventLocInPageCoordinate(float f, float f2) {
        this.locEventOffset.set(f, f2);
        this.locLeftTop.set(0.0f, 0.0f);
        this.component.computeVisiblePointInViewCoordinate(this.locLeftTop);
        this.locEventOffset.offset(this.locLeftTop.x, this.locLeftTop.y);
        return new PointF(WXViewUtils.getWebPxByWidth(this.locEventOffset.x, this.component.getInstance().getInstanceViewPortWidth()), WXViewUtils.getWebPxByWidth(this.locEventOffset.y, this.component.getInstance().getInstanceViewPortWidth()));
    }

    private static class GestureHandler extends Handler {
        public GestureHandler() {
            super(Looper.getMainLooper());
        }
    }

    public void onLongPress(MotionEvent motionEvent) {
        if (this.component.containsGesture(WXGestureType.HighLevelGesture.LONG_PRESS)) {
            List<Map<String, Object>> createMultipleFireEventParam = createMultipleFireEventParam(motionEvent, (String) null);
            this.component.getInstance().fireEvent(this.component.getRef(), WXGestureType.HighLevelGesture.LONG_PRESS.toString(), createMultipleFireEventParam.get(createMultipleFireEventParam.size() - 1));
            this.mIsTouchEventConsumed = true;
        }
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        WXGestureType.HighLevelGesture highLevelGesture;
        boolean z;
        boolean z2 = false;
        if (motionEvent == null || motionEvent2 == null) {
            return false;
        }
        if (Math.abs(motionEvent2.getX() - motionEvent.getX()) > Math.abs(motionEvent2.getY() - motionEvent.getY())) {
            highLevelGesture = WXGestureType.HighLevelGesture.HORIZONTALPAN;
        } else {
            highLevelGesture = WXGestureType.HighLevelGesture.VERTICALPAN;
        }
        if (this.mPendingPan == WXGestureType.HighLevelGesture.HORIZONTALPAN || this.mPendingPan == WXGestureType.HighLevelGesture.VERTICALPAN) {
            z = handlePanMotionEvent(motionEvent2);
        } else {
            if (this.component.containsGesture(highLevelGesture)) {
                ViewParent parent = this.component.getRealView().getParent();
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }
                if (this.mPendingPan != null) {
                    handleMotionEvent(this.mPendingPan, motionEvent2);
                }
                this.mPendingPan = highLevelGesture;
                this.component.fireEvent(highLevelGesture.toString(), createFireEventParam(motionEvent2, -1, "start"));
            } else if (containsSimplePan()) {
                if (this.panDownTime != motionEvent.getEventTime()) {
                    this.panDownTime = motionEvent.getEventTime();
                    this.mPendingPan = WXGestureType.HighLevelGesture.PAN_END;
                    this.component.fireEvent(WXGestureType.HighLevelGesture.PAN_START.toString(), createFireEventParam(motionEvent, -1, (String) null));
                } else {
                    this.component.fireEvent(WXGestureType.HighLevelGesture.PAN_MOVE.toString(), createFireEventParam(motionEvent2, -1, (String) null));
                }
            } else if (!this.component.containsGesture(WXGestureType.HighLevelGesture.SWIPE) || this.swipeDownTime == motionEvent.getEventTime()) {
                z = false;
            } else {
                this.swipeDownTime = motionEvent.getEventTime();
                List<Map<String, Object>> createMultipleFireEventParam = createMultipleFireEventParam(motionEvent2, (String) null);
                Map map = createMultipleFireEventParam.get(createMultipleFireEventParam.size() - 1);
                if (Math.abs(f) > Math.abs(f2)) {
                    map.put("direction", f > 0.0f ? "left" : "right");
                } else {
                    map.put("direction", f2 > 0.0f ? "up" : "down");
                }
                this.component.getInstance().fireEvent(this.component.getRef(), WXGestureType.HighLevelGesture.SWIPE.toString(), map);
            }
            z = true;
        }
        if (this.mIsTouchEventConsumed || z) {
            z2 = true;
        }
        this.mIsTouchEventConsumed = z2;
        return z;
    }

    public boolean isRequestDisallowInterceptTouchEvent() {
        return this.requestDisallowInterceptTouchEvent;
    }

    public void setRequestDisallowInterceptTouchEvent(boolean z) {
        this.requestDisallowInterceptTouchEvent = z;
    }
}
