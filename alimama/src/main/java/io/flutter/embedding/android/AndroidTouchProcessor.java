package io.flutter.embedding.android;

import android.os.Build;
import android.view.InputDevice;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import io.flutter.embedding.engine.renderer.FlutterRenderer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class AndroidTouchProcessor {
    private static final int BYTES_PER_FIELD = 8;
    private static final int POINTER_DATA_FIELD_COUNT = 28;
    private static final int POINTER_DATA_FLAG_BATCHED = 1;
    private static final int _POINTER_BUTTON_PRIMARY = 1;
    @NonNull
    private final FlutterRenderer renderer;

    private @interface PointerChange {
        public static final int ADD = 1;
        public static final int CANCEL = 0;
        public static final int DOWN = 4;
        public static final int HOVER = 3;
        public static final int MOVE = 5;
        public static final int REMOVE = 2;
        public static final int UP = 6;
    }

    private @interface PointerDeviceKind {
        public static final int INVERTED_STYLUS = 3;
        public static final int MOUSE = 1;
        public static final int STYLUS = 2;
        public static final int TOUCH = 0;
        public static final int UNKNOWN = 4;
    }

    private @interface PointerSignalKind {
        public static final int NONE = 0;
        public static final int SCROLL = 1;
        public static final int UNKNOWN = 2;
    }

    @PointerChange
    private int getPointerChangeForAction(int i) {
        if (i == 0) {
            return 4;
        }
        if (i == 1) {
            return 6;
        }
        if (i == 5) {
            return 4;
        }
        if (i == 6) {
            return 6;
        }
        if (i == 2) {
            return 5;
        }
        if (i == 7) {
            return 3;
        }
        if (i == 3) {
            return 0;
        }
        return i == 8 ? 3 : -1;
    }

    @PointerDeviceKind
    private int getPointerDeviceTypeForToolType(int i) {
        switch (i) {
            case 1:
                return 0;
            case 2:
                return 2;
            case 3:
                return 1;
            case 4:
                return 3;
            default:
                return 4;
        }
    }

    public AndroidTouchProcessor(@NonNull FlutterRenderer flutterRenderer) {
        this.renderer = flutterRenderer;
    }

    public boolean onTouchEvent(@NonNull MotionEvent motionEvent) {
        int pointerCount = motionEvent.getPointerCount();
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(pointerCount * 28 * 8);
        allocateDirect.order(ByteOrder.LITTLE_ENDIAN);
        int actionMasked = motionEvent.getActionMasked();
        int pointerChangeForAction = getPointerChangeForAction(motionEvent.getActionMasked());
        boolean z = actionMasked == 0 || actionMasked == 5;
        boolean z2 = !z && (actionMasked == 1 || actionMasked == 6);
        if (z) {
            addPointerForIndex(motionEvent, motionEvent.getActionIndex(), pointerChangeForAction, 0, allocateDirect);
        } else if (z2) {
            for (int i = 0; i < pointerCount; i++) {
                if (i != motionEvent.getActionIndex() && motionEvent.getToolType(i) == 1) {
                    addPointerForIndex(motionEvent, i, 5, 1, allocateDirect);
                }
            }
            addPointerForIndex(motionEvent, motionEvent.getActionIndex(), pointerChangeForAction, 0, allocateDirect);
        } else {
            for (int i2 = 0; i2 < pointerCount; i2++) {
                addPointerForIndex(motionEvent, i2, pointerChangeForAction, 0, allocateDirect);
            }
        }
        if (allocateDirect.position() % 224 == 0) {
            this.renderer.dispatchPointerDataPacket(allocateDirect, allocateDirect.position());
            return true;
        }
        throw new AssertionError("Packet position is not on field boundary");
    }

    public boolean onGenericMotionEvent(@NonNull MotionEvent motionEvent) {
        boolean z = Build.VERSION.SDK_INT >= 18 && motionEvent.isFromSource(2);
        boolean z2 = motionEvent.getActionMasked() == 7 || motionEvent.getActionMasked() == 8;
        if (!z || !z2) {
            return false;
        }
        int pointerChangeForAction = getPointerChangeForAction(motionEvent.getActionMasked());
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(motionEvent.getPointerCount() * 28 * 8);
        allocateDirect.order(ByteOrder.LITTLE_ENDIAN);
        addPointerForIndex(motionEvent, motionEvent.getActionIndex(), pointerChangeForAction, 0, allocateDirect);
        if (allocateDirect.position() % 224 == 0) {
            this.renderer.dispatchPointerDataPacket(allocateDirect, allocateDirect.position());
            return true;
        }
        throw new AssertionError("Packet position is not on field boundary.");
    }

    private void addPointerForIndex(MotionEvent motionEvent, int i, int i2, int i3, ByteBuffer byteBuffer) {
        long j;
        double d;
        double d2;
        InputDevice.MotionRange motionRange;
        MotionEvent motionEvent2 = motionEvent;
        int i4 = i;
        int i5 = i2;
        ByteBuffer byteBuffer2 = byteBuffer;
        if (i5 != -1) {
            int pointerDeviceTypeForToolType = getPointerDeviceTypeForToolType(motionEvent.getToolType(i));
            int i6 = motionEvent.getActionMasked() == 8 ? 1 : 0;
            byteBuffer2.putLong(motionEvent.getEventTime() * 1000);
            byteBuffer2.putLong((long) i5);
            byteBuffer2.putLong((long) pointerDeviceTypeForToolType);
            byteBuffer2.putLong((long) i6);
            byteBuffer2.putLong((long) motionEvent.getPointerId(i));
            byteBuffer2.putLong(0);
            byteBuffer2.putDouble((double) motionEvent.getX(i));
            byteBuffer2.putDouble((double) motionEvent.getY(i));
            byteBuffer2.putDouble(0.0d);
            byteBuffer2.putDouble(0.0d);
            if (pointerDeviceTypeForToolType == 1) {
                j = (long) (motionEvent.getButtonState() & 31);
                if (j == 0 && motionEvent.getSource() == 8194 && (i5 == 4 || i5 == 5)) {
                    j = 1;
                }
            } else {
                j = pointerDeviceTypeForToolType == 2 ? (long) ((motionEvent.getButtonState() >> 4) & 15) : 0;
            }
            byteBuffer2.putLong(j);
            byteBuffer2.putLong(0);
            byteBuffer2.putLong(0);
            byteBuffer2.putDouble((double) motionEvent.getPressure(i));
            double d3 = 1.0d;
            if (motionEvent.getDevice() == null || (motionRange = motionEvent.getDevice().getMotionRange(2)) == null) {
                d = 0.0d;
            } else {
                d = (double) motionRange.getMin();
                d3 = (double) motionRange.getMax();
            }
            byteBuffer2.putDouble(d);
            byteBuffer2.putDouble(d3);
            if (pointerDeviceTypeForToolType == 2) {
                byteBuffer2.putDouble((double) motionEvent2.getAxisValue(24, i4));
                d2 = 0.0d;
                byteBuffer2.putDouble(0.0d);
            } else {
                d2 = 0.0d;
                byteBuffer2.putDouble(0.0d);
                byteBuffer2.putDouble(0.0d);
            }
            byteBuffer2.putDouble((double) motionEvent.getSize(i));
            byteBuffer2.putDouble((double) motionEvent.getToolMajor(i));
            byteBuffer2.putDouble((double) motionEvent.getToolMinor(i));
            byteBuffer2.putDouble(d2);
            byteBuffer2.putDouble(d2);
            byteBuffer2.putDouble((double) motionEvent2.getAxisValue(8, i4));
            if (pointerDeviceTypeForToolType == 2) {
                byteBuffer2.putDouble((double) motionEvent2.getAxisValue(25, i4));
            } else {
                byteBuffer2.putDouble(d2);
            }
            byteBuffer2.putLong((long) i3);
            if (i6 == 1) {
                byteBuffer2.putDouble((double) (-motionEvent2.getAxisValue(10)));
                byteBuffer2.putDouble((double) (-motionEvent2.getAxisValue(9)));
                return;
            }
            byteBuffer2.putDouble(0.0d);
            byteBuffer2.putDouble(0.0d);
        }
    }
}
