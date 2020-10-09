package androidx.recyclerview.selection;

import android.graphics.Point;
import android.view.MotionEvent;
import androidx.annotation.NonNull;

final class MotionEvents {
    private static boolean hasBit(int i, int i2) {
        return (i & i2) != 0;
    }

    private MotionEvents() {
    }

    static boolean isMouseEvent(@NonNull MotionEvent motionEvent) {
        return motionEvent.getToolType(0) == 3;
    }

    static boolean isTouchEvent(@NonNull MotionEvent motionEvent) {
        return motionEvent.getToolType(0) == 1;
    }

    static boolean isActionMove(@NonNull MotionEvent motionEvent) {
        return motionEvent.getActionMasked() == 2;
    }

    static boolean isActionDown(@NonNull MotionEvent motionEvent) {
        return motionEvent.getActionMasked() == 0;
    }

    static boolean isActionUp(@NonNull MotionEvent motionEvent) {
        return motionEvent.getActionMasked() == 1;
    }

    static boolean isActionPointerUp(@NonNull MotionEvent motionEvent) {
        return motionEvent.getActionMasked() == 6;
    }

    static boolean isActionPointerDown(@NonNull MotionEvent motionEvent) {
        return motionEvent.getActionMasked() == 5;
    }

    static boolean isActionCancel(@NonNull MotionEvent motionEvent) {
        return motionEvent.getActionMasked() == 3;
    }

    static Point getOrigin(@NonNull MotionEvent motionEvent) {
        return new Point((int) motionEvent.getX(), (int) motionEvent.getY());
    }

    static boolean isPrimaryMouseButtonPressed(@NonNull MotionEvent motionEvent) {
        return isButtonPressed(motionEvent, 1);
    }

    static boolean isSecondaryMouseButtonPressed(@NonNull MotionEvent motionEvent) {
        return isButtonPressed(motionEvent, 2);
    }

    static boolean isTertiaryMouseButtonPressed(@NonNull MotionEvent motionEvent) {
        return isButtonPressed(motionEvent, 4);
    }

    private static boolean isButtonPressed(MotionEvent motionEvent, int i) {
        return i != 0 && (motionEvent.getButtonState() & i) == i;
    }

    static boolean isShiftKeyPressed(@NonNull MotionEvent motionEvent) {
        return hasBit(motionEvent.getMetaState(), 1);
    }

    static boolean isCtrlKeyPressed(@NonNull MotionEvent motionEvent) {
        return hasBit(motionEvent.getMetaState(), 4096);
    }

    static boolean isAltKeyPressed(@NonNull MotionEvent motionEvent) {
        return hasBit(motionEvent.getMetaState(), 2);
    }

    static boolean isTouchpadScroll(@NonNull MotionEvent motionEvent) {
        return isMouseEvent(motionEvent) && isActionMove(motionEvent) && motionEvent.getButtonState() == 0;
    }

    static boolean isPointerDragEvent(MotionEvent motionEvent) {
        return isPrimaryMouseButtonPressed(motionEvent) && isActionMove(motionEvent);
    }
}
