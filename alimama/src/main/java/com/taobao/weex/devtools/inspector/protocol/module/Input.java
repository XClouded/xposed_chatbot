package com.taobao.weex.devtools.inspector.protocol.module;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.MotionEvent;
import com.taobao.weex.devtools.inspector.elements.android.ActivityTracker;
import com.taobao.weex.devtools.inspector.jsonrpc.JsonRpcPeer;
import com.taobao.weex.devtools.inspector.jsonrpc.JsonRpcResult;
import com.taobao.weex.devtools.inspector.protocol.ChromeDevtoolsDomain;
import com.taobao.weex.devtools.inspector.protocol.ChromeDevtoolsMethod;
import com.taobao.weex.devtools.inspector.screencast.ScreencastDispatcher;
import com.taobao.weex.devtools.json.ObjectMapper;
import com.taobao.weex.devtools.json.annotation.JsonProperty;
import org.json.JSONObject;

public class Input implements ChromeDevtoolsDomain {
    private static final String KEY_UP = "keyUp";
    private static final String MOUSE_BUTTON_LEFT = "left";
    private static final String MOUSE_BUTTON_RIGHT = "right";
    private static final String MOUSE_MOVED = "mouseMoved";
    private static final String MOUSE_PRESSED = "mousePressed";
    private static final String MOUSE_RELEASED = "mouseReleased";
    private static final String MOUSE_WHEEL = "mouseWheel";
    private long downTime;
    private Point lastPoint = new Point();
    private ObjectMapper mObjectMapper = new ObjectMapper();

    public static class DispatchKeyEventRequest {
        @JsonProperty
        public Boolean autoRepeat;
        @JsonProperty
        public String code;
        @JsonProperty
        public Boolean isKeypad;
        @JsonProperty
        public Boolean isSystemKey;
        @JsonProperty
        public String key;
        @JsonProperty
        public String keyIdentifier;
        @JsonProperty
        public Integer modifiers;
        @JsonProperty
        public Integer nativeVirtualKeyCode;
        @JsonProperty
        public String text;
        @JsonProperty
        public Double timestamp;
        @JsonProperty(required = true)
        public String type;
        @JsonProperty
        public String unmodifiedText;
        @JsonProperty
        public Integer windowsVirtualKeyCode;
    }

    public static class EmulateTouchFromMouseEventRequest {
        @JsonProperty(required = true)
        public String button;
        @JsonProperty
        public Integer clickCount;
        @JsonProperty
        public Double deltaX;
        @JsonProperty
        public Double deltaY;
        @JsonProperty
        public Integer modifiers;
        @JsonProperty(required = true)
        public double timestamp;
        @JsonProperty(required = true)
        public String type;
        @JsonProperty(required = true)
        public int x;
        @JsonProperty(required = true)
        public int y;
    }

    private static int getStatusBarHeight(Context context) {
        if (!(context instanceof Activity)) {
            return 0;
        }
        Rect rect = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    @ChromeDevtoolsMethod
    public JsonRpcResult dispatchKeyEvent(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
        DispatchKeyEventRequest dispatchKeyEventRequest = (DispatchKeyEventRequest) this.mObjectMapper.convertValue(jSONObject, DispatchKeyEventRequest.class);
        if (KEY_UP.equals(dispatchKeyEventRequest.type)) {
            Instrumentation instrumentation = new Instrumentation();
            if (dispatchKeyEventRequest.nativeVirtualKeyCode.intValue() >= 48 && dispatchKeyEventRequest.nativeVirtualKeyCode.intValue() <= 57) {
                instrumentation.sendKeyDownUpSync((dispatchKeyEventRequest.nativeVirtualKeyCode.intValue() - 48) + 7);
                return null;
            } else if (dispatchKeyEventRequest.nativeVirtualKeyCode.intValue() >= 65 && dispatchKeyEventRequest.nativeVirtualKeyCode.intValue() <= 90) {
                instrumentation.sendKeyDownUpSync((dispatchKeyEventRequest.nativeVirtualKeyCode.intValue() - 65) + 29);
                return null;
            } else if (dispatchKeyEventRequest.nativeVirtualKeyCode.intValue() == 8) {
                instrumentation.sendKeyDownUpSync(67);
                return null;
            } else if (dispatchKeyEventRequest.nativeVirtualKeyCode.intValue() == 13) {
                instrumentation.sendKeyDownUpSync(66);
                return null;
            } else if (dispatchKeyEventRequest.nativeVirtualKeyCode.intValue() == 16) {
                instrumentation.sendKeyDownUpSync(59);
                return null;
            } else if (dispatchKeyEventRequest.nativeVirtualKeyCode.intValue() == 20) {
                instrumentation.sendKeyDownUpSync(115);
                return null;
            } else {
                instrumentation.sendCharacterSync((char) dispatchKeyEventRequest.nativeVirtualKeyCode.intValue());
            }
        }
        return null;
    }

    @ChromeDevtoolsMethod
    public JsonRpcResult emulateTouchFromMouseEvent(JsonRpcPeer jsonRpcPeer, JSONObject jSONObject) {
        EmulateTouchFromMouseEventRequest emulateTouchFromMouseEventRequest = (EmulateTouchFromMouseEventRequest) this.mObjectMapper.convertValue(jSONObject, EmulateTouchFromMouseEventRequest.class);
        try {
            if (MOUSE_PRESSED.equals(emulateTouchFromMouseEventRequest.type) && "left".equals(emulateTouchFromMouseEventRequest.button)) {
                this.lastPoint.x = emulateTouchFromMouseEventRequest.x;
                this.lastPoint.y = emulateTouchFromMouseEventRequest.y;
                down(emulateTouchFromMouseEventRequest.x, emulateTouchFromMouseEventRequest.y);
            } else if (MOUSE_RELEASED.equals(emulateTouchFromMouseEventRequest.type)) {
                if ("left".equals(emulateTouchFromMouseEventRequest.button)) {
                    this.lastPoint.x = emulateTouchFromMouseEventRequest.x;
                    this.lastPoint.y = emulateTouchFromMouseEventRequest.y;
                    up(emulateTouchFromMouseEventRequest.x, emulateTouchFromMouseEventRequest.y);
                } else if ("right".equals(emulateTouchFromMouseEventRequest.button) && ActivityTracker.get().getActivitiesView().size() > 1) {
                    new Instrumentation().sendKeyDownUpSync(4);
                }
            } else if (MOUSE_MOVED.equals(emulateTouchFromMouseEventRequest.type)) {
                move(emulateTouchFromMouseEventRequest.x, emulateTouchFromMouseEventRequest.y);
            } else {
                MOUSE_WHEEL.equals(emulateTouchFromMouseEventRequest.type);
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private synchronized void scroll(Point point, Point point2) {
        down(point.x, point.y);
        move(point2.x, point2.y);
        up(point2.x, point2.y);
        this.lastPoint.x = point2.x;
        this.lastPoint.y = point2.y;
    }

    private void move(int i, int i2) {
        final Activity tryGetTopActivity = ActivityTracker.get().tryGetTopActivity();
        if (tryGetTopActivity != null) {
            final MotionEvent obtain = MotionEvent.obtain(this.downTime, System.currentTimeMillis(), 2, ((float) i) / ScreencastDispatcher.getsBitmapScale(), (((float) i2) / ScreencastDispatcher.getsBitmapScale()) + ((float) getStatusBarHeight(tryGetTopActivity)), 0);
            tryGetTopActivity.runOnUiThread(new Runnable() {
                public void run() {
                    tryGetTopActivity.dispatchTouchEvent(obtain);
                }
            });
            obtain.recycle();
        }
    }

    private void down(int i, int i2) {
        final Activity tryGetTopActivity = ActivityTracker.get().tryGetTopActivity();
        if (tryGetTopActivity != null) {
            this.downTime = System.currentTimeMillis();
            final MotionEvent obtain = MotionEvent.obtain(this.downTime, this.downTime, 0, ((float) i) / ScreencastDispatcher.getsBitmapScale(), (((float) i2) / ScreencastDispatcher.getsBitmapScale()) + ((float) getStatusBarHeight(tryGetTopActivity)), 0);
            tryGetTopActivity.runOnUiThread(new Runnable() {
                public void run() {
                    tryGetTopActivity.dispatchTouchEvent(obtain);
                }
            });
            obtain.recycle();
        }
    }

    private void up(int i, int i2) {
        final Activity tryGetTopActivity = ActivityTracker.get().tryGetTopActivity();
        if (tryGetTopActivity != null) {
            final MotionEvent obtain = MotionEvent.obtain(this.downTime, System.currentTimeMillis(), 1, ((float) i) / ScreencastDispatcher.getsBitmapScale(), (((float) i2) / ScreencastDispatcher.getsBitmapScale()) + ((float) getStatusBarHeight(tryGetTopActivity)), 0);
            tryGetTopActivity.runOnUiThread(new Runnable() {
                public void run() {
                    tryGetTopActivity.dispatchTouchEvent(obtain);
                }
            });
            obtain.recycle();
        }
    }
}
