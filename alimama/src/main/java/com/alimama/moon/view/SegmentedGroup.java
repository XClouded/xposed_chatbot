package com.alimama.moon.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.StateSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.alimama.moon.R;
import java.util.HashMap;

public class SegmentedGroup extends RadioGroup {
    /* access modifiers changed from: private */
    public RadioGroup.OnCheckedChangeListener mCheckedChangeListener;
    private int mCheckedTextColor;
    private Float mCornerRadius;
    /* access modifiers changed from: private */
    public HashMap<Integer, TransitionDrawable> mDrawableMap;
    /* access modifiers changed from: private */
    public int mLastCheckId;
    private LayoutSelector mLayoutSelector;
    private int mMarginDp;
    private int mTintColor;
    private int mUnCheckedTintColor;
    private Resources resources;

    public SegmentedGroup(Context context) {
        super(context);
        this.mCheckedTextColor = -1;
        this.resources = getResources();
        this.mTintColor = this.resources.getColor(R.color.radio_button_selected_color);
        this.mUnCheckedTintColor = this.resources.getColor(R.color.radio_button_unselected_color);
        this.mMarginDp = (int) getResources().getDimension(R.dimen.radio_button_stroke_border);
        this.mCornerRadius = Float.valueOf(getResources().getDimension(R.dimen.radio_button_conner_radius));
        this.mLayoutSelector = new LayoutSelector(this.mCornerRadius.floatValue());
    }

    public SegmentedGroup(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mCheckedTextColor = -1;
        this.resources = getResources();
        this.mTintColor = this.resources.getColor(R.color.radio_button_selected_color);
        this.mUnCheckedTintColor = this.resources.getColor(R.color.radio_button_unselected_color);
        this.mMarginDp = (int) getResources().getDimension(R.dimen.radio_button_stroke_border);
        this.mCornerRadius = Float.valueOf(getResources().getDimension(R.dimen.radio_button_conner_radius));
        initAttrs(attributeSet);
        this.mLayoutSelector = new LayoutSelector(this.mCornerRadius.floatValue());
    }

    private void initAttrs(AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = getContext().getTheme().obtainStyledAttributes(attributeSet, R.styleable.SegmentedGroup, 0, 0);
        try {
            this.mMarginDp = (int) obtainStyledAttributes.getDimension(1, getResources().getDimension(R.dimen.radio_button_stroke_border));
            this.mCornerRadius = Float.valueOf(obtainStyledAttributes.getDimension(0, getResources().getDimension(R.dimen.radio_button_conner_radius)));
            this.mTintColor = obtainStyledAttributes.getColor(2, getResources().getColor(R.color.radio_button_selected_color));
            this.mCheckedTextColor = obtainStyledAttributes.getColor(3, getResources().getColor(17170443));
            this.mUnCheckedTintColor = obtainStyledAttributes.getColor(4, getResources().getColor(R.color.radio_button_unselected_color));
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        updateBackground();
    }

    public void setTintColor(int i) {
        this.mTintColor = i;
        updateBackground();
    }

    public void setTintColor(int i, int i2) {
        this.mTintColor = i;
        this.mCheckedTextColor = i2;
        updateBackground();
    }

    public void setUnCheckedTintColor(int i, int i2) {
        this.mUnCheckedTintColor = i;
        updateBackground();
    }

    public void updateBackground() {
        this.mDrawableMap = new HashMap<>();
        int childCount = super.getChildCount();
        int i = 0;
        while (i < childCount) {
            View childAt = getChildAt(i);
            updateBackground(childAt);
            if (i != childCount - 1) {
                RadioGroup.LayoutParams layoutParams = (RadioGroup.LayoutParams) childAt.getLayoutParams();
                RadioGroup.LayoutParams layoutParams2 = new RadioGroup.LayoutParams(layoutParams.width, layoutParams.height, layoutParams.weight);
                if (getOrientation() == 0) {
                    layoutParams2.setMargins(0, 0, -this.mMarginDp, 0);
                } else {
                    layoutParams2.setMargins(0, 0, 0, -this.mMarginDp);
                }
                childAt.setLayoutParams(layoutParams2);
                i++;
            } else {
                return;
            }
        }
    }

    public void updateBackground(View view) {
        int selected = this.mLayoutSelector.getSelected();
        int unselected = this.mLayoutSelector.getUnselected();
        ((Button) view).setTextColor(new ColorStateList(new int[][]{new int[]{-16842912}, new int[]{16842912}}, new int[]{this.mTintColor, this.mCheckedTextColor}));
        Drawable mutate = this.resources.getDrawable(selected).mutate();
        Drawable mutate2 = this.resources.getDrawable(unselected).mutate();
        GradientDrawable gradientDrawable = (GradientDrawable) mutate;
        gradientDrawable.setColor(this.mTintColor);
        gradientDrawable.setStroke(this.mMarginDp, this.mTintColor);
        GradientDrawable gradientDrawable2 = (GradientDrawable) mutate2;
        gradientDrawable2.setStroke(this.mMarginDp, this.mTintColor);
        gradientDrawable2.setColor(this.mUnCheckedTintColor);
        gradientDrawable.setCornerRadii(this.mLayoutSelector.getChildRadii(view));
        gradientDrawable2.setCornerRadii(this.mLayoutSelector.getChildRadii(view));
        GradientDrawable gradientDrawable3 = (GradientDrawable) this.resources.getDrawable(unselected).mutate();
        gradientDrawable3.setStroke(this.mMarginDp, this.mTintColor);
        gradientDrawable3.setColor(this.mUnCheckedTintColor);
        gradientDrawable3.setCornerRadii(this.mLayoutSelector.getChildRadii(view));
        gradientDrawable3.setColor(Color.argb(50, Color.red(this.mTintColor), Color.green(this.mTintColor), Color.blue(this.mTintColor)));
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{mutate2, gradientDrawable3});
        TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{mutate2, mutate});
        if (((RadioButton) view).isChecked()) {
            transitionDrawable.reverseTransition(0);
        }
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{-16842912, 16842919}, layerDrawable);
        stateListDrawable.addState(StateSet.WILD_CARD, transitionDrawable);
        this.mDrawableMap.put(Integer.valueOf(view.getId()), transitionDrawable);
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(stateListDrawable);
        } else {
            view.setBackgroundDrawable(stateListDrawable);
        }
        super.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                TransitionDrawable transitionDrawable;
                ((TransitionDrawable) SegmentedGroup.this.mDrawableMap.get(Integer.valueOf(i))).reverseTransition(200);
                if (!(SegmentedGroup.this.mLastCheckId == 0 || (transitionDrawable = (TransitionDrawable) SegmentedGroup.this.mDrawableMap.get(Integer.valueOf(SegmentedGroup.this.mLastCheckId))) == null)) {
                    transitionDrawable.reverseTransition(200);
                }
                int unused = SegmentedGroup.this.mLastCheckId = i;
                if (SegmentedGroup.this.mCheckedChangeListener != null) {
                    SegmentedGroup.this.mCheckedChangeListener.onCheckedChanged(radioGroup, i);
                }
            }
        });
    }

    public void onViewRemoved(View view) {
        super.onViewRemoved(view);
        this.mDrawableMap.remove(Integer.valueOf(view.getId()));
    }

    public void removeAllViews() {
        this.mDrawableMap.clear();
        super.removeAllViews();
    }

    public void setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener onCheckedChangeListener) {
        this.mCheckedChangeListener = onCheckedChangeListener;
    }

    private class LayoutSelector {
        private final int SELECTED_LAYOUT = R.drawable.radio_checked;
        private final int UNSELECTED_LAYOUT = R.drawable.radio_unchecked;
        private int child = -1;
        private int children = -1;
        private float r;
        private final float r1 = TypedValue.applyDimension(1, 0.1f, SegmentedGroup.this.getResources().getDisplayMetrics());
        private final float[] rBot;
        private final float[] rDefault;
        private final float[] rLeft;
        private final float[] rMiddle;
        private final float[] rRight;
        private final float[] rTop;
        private float[] radii;

        public int getSelected() {
            return R.drawable.radio_checked;
        }

        public int getUnselected() {
            return R.drawable.radio_unchecked;
        }

        public LayoutSelector(float f) {
            this.r = f;
            this.rLeft = new float[]{this.r, this.r, this.r1, this.r1, this.r1, this.r1, this.r, this.r};
            this.rRight = new float[]{this.r1, this.r1, this.r, this.r, this.r, this.r, this.r1, this.r1};
            this.rMiddle = new float[]{this.r1, this.r1, this.r1, this.r1, this.r1, this.r1, this.r1, this.r1};
            this.rDefault = new float[]{this.r, this.r, this.r, this.r, this.r, this.r, this.r, this.r};
            this.rTop = new float[]{this.r, this.r, this.r, this.r, this.r1, this.r1, this.r1, this.r1};
            this.rBot = new float[]{this.r1, this.r1, this.r1, this.r1, this.r, this.r, this.r, this.r};
        }

        private int getChildren() {
            return SegmentedGroup.this.getChildCount();
        }

        private int getChildIndex(View view) {
            return SegmentedGroup.this.indexOfChild(view);
        }

        private void setChildRadii(int i, int i2) {
            if (this.children != i || this.child != i2) {
                this.children = i;
                this.child = i2;
                if (this.children == 1) {
                    this.radii = this.rDefault;
                } else if (this.child == 0) {
                    this.radii = SegmentedGroup.this.getOrientation() == 0 ? this.rLeft : this.rTop;
                } else if (this.child == this.children - 1) {
                    this.radii = SegmentedGroup.this.getOrientation() == 0 ? this.rRight : this.rBot;
                } else {
                    this.radii = this.rMiddle;
                }
            }
        }

        public float[] getChildRadii(View view) {
            setChildRadii(getChildren(), getChildIndex(view));
            return this.radii;
        }
    }
}
