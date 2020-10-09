package com.alimama.moon.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.appcompat.widget.AppCompatEditText;
import com.alimama.moon.R;

public class ClearableEditTextView extends AppCompatEditText {
    private static final int CLEAR_DRAWABLE_HIT_AREA = 10;
    /* access modifiers changed from: private */
    public Drawable clearDrawable;

    public ClearableEditTextView(Context context) {
        super(context);
        initViews();
    }

    public ClearableEditTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initViews();
    }

    public ClearableEditTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews();
    }

    private void initViews() {
        this.clearDrawable = getResources().getDrawable(R.drawable.clear);
        addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable)) {
                    ClearableEditTextView.this.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, (Drawable) null, (Drawable) null);
                } else {
                    ClearableEditTextView.this.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, ClearableEditTextView.this.clearDrawable, (Drawable) null);
                }
            }
        });
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            Rect bounds = this.clearDrawable.getBounds();
            int width = getWidth();
            if (x >= (width - bounds.width()) - 10 && x <= (width - getPaddingRight()) + 10 && y >= getPaddingTop() - 10 && y <= (getHeight() - getPaddingBottom()) + 10) {
                setText("");
                return true;
            }
        }
        return super.onTouchEvent(motionEvent);
    }
}
