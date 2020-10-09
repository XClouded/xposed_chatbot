package com.taobao.weex.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.method.MovementMethod;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.EditText;
import com.taobao.weex.ui.view.gesture.WXGesture;
import com.taobao.weex.ui.view.gesture.WXGestureObservable;

@SuppressLint({"AppCompatCustomView"})
public class WXEditText extends EditText implements WXGestureObservable {
    private boolean mAllowCopyPaste = true;
    private boolean mAllowDisableMovement = true;
    private int mLines = 1;
    private WXGesture wxGesture;

    public WXEditText(Context context) {
        super(context);
        if (Build.VERSION.SDK_INT >= 16) {
            setBackground((Drawable) null);
        } else {
            setBackgroundDrawable((Drawable) null);
        }
    }

    public void registerGestureListener(WXGesture wXGesture) {
        this.wxGesture = wXGesture;
    }

    public WXGesture getGestureListener() {
        return this.wxGesture;
    }

    public void setLines(int i) {
        super.setLines(i);
        this.mLines = i;
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean onTouchEvent = super.onTouchEvent(motionEvent);
        if (this.wxGesture != null) {
            onTouchEvent |= this.wxGesture.onTouch(this, motionEvent);
        }
        ViewParent parent = getParent();
        if (parent != null) {
            int action = motionEvent.getAction() & 255;
            if (action != 3) {
                switch (action) {
                    case 0:
                        if (this.mLines < getLineCount()) {
                            parent.requestDisallowInterceptTouchEvent(true);
                            break;
                        }
                        break;
                    case 1:
                        break;
                }
            }
            parent.requestDisallowInterceptTouchEvent(false);
        }
        return onTouchEvent;
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        if (getLayout() != null) {
            int height = getLayout().getHeight();
            if (!this.mAllowDisableMovement || i2 >= height) {
                setMovementMethod(getDefaultMovementMethod());
            } else {
                setMovementMethod((MovementMethod) null);
            }
        }
    }

    public void setAllowDisableMovement(boolean z) {
        this.mAllowDisableMovement = z;
    }

    public void setAllowCopyPaste(boolean z) {
        this.mAllowCopyPaste = z;
        if (z) {
            setLongClickable(true);
            setCustomSelectionActionModeCallback((ActionMode.Callback) null);
            if (Build.VERSION.SDK_INT >= 23) {
                setCustomInsertionActionModeCallback((ActionMode.Callback) null);
                return;
            }
            return;
        }
        setLongClickable(false);
        AnonymousClass1 r3 = new ActionMode.Callback() {
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                return false;
            }

            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode actionMode) {
            }

            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }
        };
        if (Build.VERSION.SDK_INT >= 23) {
            setCustomInsertionActionModeCallback(r3);
        }
        setCustomSelectionActionModeCallback(r3);
    }

    public boolean onTextContextMenuItem(int i) {
        return !this.mAllowCopyPaste || super.onTextContextMenuItem(i);
    }
}
