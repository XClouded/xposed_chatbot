package com.taobao.uikit.extend.component.unify.Dialog;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ScrollView;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.uikit.extend.R;
import com.taobao.uikit.extend.component.unify.Dialog.TBMaterialDialog;
import com.taobao.uikit.extend.utils.ResourceUtils;

public class TBDialogRootLayout extends ViewGroup {
    private static final int INDEX_NEGATIVE = 1;
    private static final int INDEX_NEUTRAL = 0;
    private static final int INDEX_POSITIVE = 2;
    private ViewTreeObserver.OnScrollChangedListener mBottomOnScrollChangedListener;
    private int mButtonBarHeight;
    private GravityEnum mButtonGravity = GravityEnum.START;
    private int mButtonHorizontalEdgeMargin;
    private int mButtonPaddingFull;
    /* access modifiers changed from: private */
    public final TBDialogButton[] mButtons = new TBDialogButton[3];
    protected boolean mCardDialog = false;
    private View mCloseButton;
    private View mContent;
    private Paint mDividerPaint;
    private int mDividerWidth;
    /* access modifiers changed from: private */
    public boolean mDrawBottomDivider = false;
    /* access modifiers changed from: private */
    public boolean mDrawTopDivider = false;
    private boolean mForceStack = false;
    private boolean mIsStacked = false;
    private int mMaxBoundedHeight = 0;
    private int mMaxCardHeight = 0;
    private int mMaxCardWidth = 0;
    private boolean mNoTitleNoPadding;
    private int mNoTitlePaddingFull;
    private boolean mReducePaddingNoTitleNoButtons;
    private View mTitleBar;
    private ViewTreeObserver.OnScrollChangedListener mTopOnScrollChangedListener;
    private boolean mUseFullPadding = true;

    public TBDialogRootLayout(Context context) {
        super(context);
        init(context, (AttributeSet) null, 0);
    }

    public TBDialogRootLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet, 0);
    }

    @TargetApi(11)
    public TBDialogRootLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet, i);
    }

    @TargetApi(21)
    public TBDialogRootLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context, attributeSet, i);
    }

    private void init(Context context, AttributeSet attributeSet, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.TBDialogRootLayout, i, 0);
        this.mReducePaddingNoTitleNoButtons = obtainStyledAttributes.getBoolean(R.styleable.TBDialogRootLayout_uik_mdReducePaddingNoTitleNoButtons, true);
        this.mCardDialog = obtainStyledAttributes.getBoolean(R.styleable.TBDialogRootLayout_uik_mdCardDialog, false);
        obtainStyledAttributes.recycle();
        this.mNoTitlePaddingFull = ResourceUtils.getDimensionPixelSize(getContext(), R.dimen.uik_mdTitleVerticalPadding);
        this.mButtonPaddingFull = ResourceUtils.getDimensionPixelSize(getContext(), R.dimen.uik_mdBttonFrameVerticalPadding);
        this.mButtonHorizontalEdgeMargin = ResourceUtils.getDimensionPixelSize(getContext(), R.dimen.uik_mdButtonPaddingFrameSide);
        this.mButtonBarHeight = ResourceUtils.getDimensionPixelSize(getContext(), R.dimen.uik_mdButtonHeight);
        this.mDividerPaint = new Paint();
        this.mDividerWidth = ResourceUtils.getDimensionPixelSize(getContext(), R.dimen.uik_mdDividerHeight);
        this.mDividerPaint.setColor(ResourceUtils.getColor(getContext(), R.color.uik_mdDividerColor));
        setWillNotDraw(false);
        this.mMaxBoundedHeight = ResourceUtils.getDimension(getContext(), R.dimen.uik_mdMaxBoundedHeight);
        this.mMaxCardHeight = ResourceUtils.getDimension(getContext(), R.dimen.uik_mdMaxCardHeight);
        this.mMaxCardWidth = ResourceUtils.getDimension(getContext(), R.dimen.uik_mdMaxCardWidth);
    }

    public void noTitleNoPadding() {
        this.mNoTitleNoPadding = true;
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt.getId() == R.id.uik_mdTitleFrame) {
                this.mTitleBar = childAt;
            } else if (childAt.getId() == R.id.uik_mdButtonDefaultNeutral) {
                this.mButtons[0] = (TBDialogButton) childAt;
            } else if (childAt.getId() == R.id.uik_mdButtonDefaultNegative) {
                this.mButtons[1] = (TBDialogButton) childAt;
            } else if (childAt.getId() == R.id.uik_mdButtonDefaultPositive) {
                this.mButtons[2] = (TBDialogButton) childAt;
            } else if (childAt.getId() == R.id.uik_mdButtonClose) {
                this.mCloseButton = childAt;
            } else {
                this.mContent = childAt;
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:56:0x0116  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0129  */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x0138  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x017d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onMeasure(int r12, int r13) {
        /*
            r11 = this;
            boolean r0 = r11.mCardDialog
            if (r0 == 0) goto L_0x006b
            int r0 = android.view.View.MeasureSpec.getSize(r12)
            int r1 = android.view.View.MeasureSpec.getSize(r13)
            int r2 = r11.mMaxCardHeight
            if (r2 <= 0) goto L_0x0022
            int r2 = r11.mMaxCardHeight
            if (r2 >= r1) goto L_0x0022
            int r13 = android.view.View.MeasureSpec.getMode(r13)
            int r1 = r11.mMaxCardHeight
            int r13 = android.view.View.MeasureSpec.makeMeasureSpec(r1, r13)
            int r1 = android.view.View.MeasureSpec.getSize(r13)
        L_0x0022:
            int r2 = r11.mMaxCardWidth
            if (r2 <= 0) goto L_0x0038
            int r2 = r11.mMaxCardWidth
            if (r2 >= r0) goto L_0x0038
            int r12 = android.view.View.MeasureSpec.getMode(r12)
            int r0 = r11.mMaxCardWidth
            int r12 = android.view.View.MeasureSpec.makeMeasureSpec(r0, r12)
            int r0 = android.view.View.MeasureSpec.getSize(r12)
        L_0x0038:
            android.view.View r2 = r11.mCloseButton
            r11.measureChild(r2, r12, r13)
            android.view.View r2 = r11.mCloseButton
            int r2 = r2.getMeasuredHeight()
            int r2 = r1 - r2
            android.content.res.Resources r3 = r11.getResources()
            int r4 = com.taobao.uikit.extend.R.dimen.uik_mdCardMarginBottom
            float r3 = r3.getDimension(r4)
            int r3 = (int) r3
            int r2 = r2 - r3
            android.view.View r3 = r11.mContent
            boolean r3 = isVisible(r3)
            if (r3 == 0) goto L_0x0065
            android.view.View r3 = r11.mContent
            r11.measureChild(r3, r12, r13)
            android.view.View r12 = r11.mContent
            int r12 = r12.getMeasuredHeight()
            int r2 = r2 - r12
        L_0x0065:
            int r1 = r1 - r2
            r11.setMeasuredDimension(r0, r1)
            goto L_0x0182
        L_0x006b:
            int r0 = android.view.View.MeasureSpec.getSize(r12)
            int r1 = android.view.View.MeasureSpec.getSize(r13)
            int r2 = r11.mMaxBoundedHeight
            if (r2 <= 0) goto L_0x0085
            int r2 = r11.mMaxBoundedHeight
            if (r2 >= r1) goto L_0x0085
            int r13 = android.view.View.MeasureSpec.getMode(r13)
            int r1 = r11.mMaxBoundedHeight
            int r13 = android.view.View.MeasureSpec.makeMeasureSpec(r1, r13)
        L_0x0085:
            int r1 = android.view.View.MeasureSpec.getSize(r13)
            r2 = 1
            r11.mUseFullPadding = r2
            boolean r3 = r11.mForceStack
            r4 = 0
            if (r3 != 0) goto L_0x00c6
            com.taobao.uikit.extend.component.unify.Dialog.TBDialogButton[] r3 = r11.mButtons
            int r5 = r3.length
            r6 = 0
            r7 = 0
            r8 = 0
        L_0x0097:
            if (r6 >= r5) goto L_0x00b2
            r9 = r3[r6]
            if (r9 == 0) goto L_0x00af
            boolean r10 = isVisible(r9)
            if (r10 == 0) goto L_0x00af
            r9.setStacked(r4, r4)
            r11.measureChild(r9, r12, r13)
            int r8 = r9.getMeasuredWidth()
            int r7 = r7 + r8
            r8 = 1
        L_0x00af:
            int r6 = r6 + 1
            goto L_0x0097
        L_0x00b2:
            android.content.Context r3 = r11.getContext()
            int r5 = com.taobao.uikit.extend.R.dimen.uik_mdNeutralButtonMargin
            int r3 = com.taobao.uikit.extend.utils.ResourceUtils.getDimensionPixelSize(r3, r5)
            int r3 = r3 * 2
            int r3 = r0 - r3
            if (r7 <= r3) goto L_0x00c4
            r3 = 1
            goto L_0x00c8
        L_0x00c4:
            r3 = 0
            goto L_0x00c8
        L_0x00c6:
            r3 = 1
            r8 = 0
        L_0x00c8:
            r11.mIsStacked = r3
            if (r3 == 0) goto L_0x00ec
            com.taobao.uikit.extend.component.unify.Dialog.TBDialogButton[] r3 = r11.mButtons
            int r5 = r3.length
            r6 = 0
            r7 = 0
        L_0x00d1:
            if (r6 >= r5) goto L_0x00ed
            r9 = r3[r6]
            if (r9 == 0) goto L_0x00e9
            boolean r10 = isVisible(r9)
            if (r10 == 0) goto L_0x00e9
            r9.setStacked(r2, r4)
            r11.measureChild(r9, r12, r13)
            int r8 = r9.getMeasuredHeight()
            int r7 = r7 + r8
            r8 = 1
        L_0x00e9:
            int r6 = r6 + 1
            goto L_0x00d1
        L_0x00ec:
            r7 = 0
        L_0x00ed:
            if (r8 == 0) goto L_0x0106
            boolean r12 = r11.mIsStacked
            if (r12 == 0) goto L_0x00fe
            int r12 = r1 - r7
            int r13 = r11.mButtonPaddingFull
            int r13 = r13 + r4
            int r3 = r11.mButtonPaddingFull
            int r3 = r3 * 2
            int r3 = r3 + r4
            goto L_0x010e
        L_0x00fe:
            int r12 = r11.mButtonBarHeight
            int r12 = r1 - r12
            int r13 = r11.mButtonPaddingFull
            int r13 = r13 + r4
            goto L_0x010d
        L_0x0106:
            int r12 = r11.mButtonPaddingFull
            int r12 = r12 * 2
            int r13 = r12 + 0
            r12 = r1
        L_0x010d:
            r3 = 0
        L_0x010e:
            android.view.View r5 = r11.mTitleBar
            boolean r5 = isVisible(r5)
            if (r5 == 0) goto L_0x0129
            android.view.View r5 = r11.mTitleBar
            r6 = 1073741824(0x40000000, float:2.0)
            int r6 = android.view.View.MeasureSpec.makeMeasureSpec(r0, r6)
            r5.measure(r6, r4)
            android.view.View r5 = r11.mTitleBar
            int r5 = r5.getMeasuredHeight()
            int r12 = r12 - r5
            goto L_0x0130
        L_0x0129:
            boolean r5 = r11.mNoTitleNoPadding
            if (r5 != 0) goto L_0x0130
            int r5 = r11.mNoTitlePaddingFull
            int r13 = r13 + r5
        L_0x0130:
            android.view.View r5 = r11.mContent
            boolean r5 = isVisible(r5)
            if (r5 == 0) goto L_0x017d
            android.view.View r5 = r11.mContent
            r6 = -2147483648(0xffffffff80000000, float:-0.0)
            int r7 = android.view.View.MeasureSpec.makeMeasureSpec(r0, r6)
            int r9 = r12 - r3
            int r6 = android.view.View.MeasureSpec.makeMeasureSpec(r9, r6)
            r5.measure(r7, r6)
            android.view.View r5 = r11.mContent
            int r5 = r5.getMeasuredHeight()
            int r6 = r12 - r13
            if (r5 > r6) goto L_0x017a
            boolean r5 = r11.mReducePaddingNoTitleNoButtons
            if (r5 == 0) goto L_0x016e
            android.view.View r5 = r11.mTitleBar
            boolean r5 = isVisible(r5)
            if (r5 != 0) goto L_0x016e
            if (r8 == 0) goto L_0x0162
            goto L_0x016e
        L_0x0162:
            r11.mUseFullPadding = r4
            android.view.View r13 = r11.mContent
            int r13 = r13.getMeasuredHeight()
            int r13 = r13 + r3
            int r4 = r12 - r13
            goto L_0x017e
        L_0x016e:
            r11.mUseFullPadding = r2
            android.view.View r2 = r11.mContent
            int r2 = r2.getMeasuredHeight()
            int r2 = r2 + r13
            int r4 = r12 - r2
            goto L_0x017e
        L_0x017a:
            r11.mUseFullPadding = r4
            goto L_0x017e
        L_0x017d:
            r4 = r12
        L_0x017e:
            int r1 = r1 - r4
            r11.setMeasuredDimension(r0, r1)
        L_0x0182:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.uikit.extend.component.unify.Dialog.TBDialogRootLayout.onMeasure(int, int):void");
    }

    private static boolean isVisible(View view) {
        boolean z = (view == null || view.getVisibility() == 8) ? false : true;
        if (!z || !(view instanceof TBDialogButton)) {
            return z;
        }
        return ((TBDialogButton) view).getText().toString().trim().length() > 0;
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mContent != null) {
            if (this.mDrawTopDivider) {
                int top = this.mContent.getTop();
                canvas.drawRect(0.0f, (float) (top - this.mDividerWidth), (float) getMeasuredWidth(), (float) top, this.mDividerPaint);
            }
            if (this.mDrawBottomDivider) {
                int bottom = this.mContent.getBottom();
                canvas.drawRect(0.0f, (float) bottom, (float) getMeasuredWidth(), (float) (bottom + this.mDividerWidth), this.mDividerPaint);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        if (isVisible(this.mTitleBar)) {
            int measuredHeight = this.mTitleBar.getMeasuredHeight() + i2;
            this.mTitleBar.layout(i, i2, i3, measuredHeight);
            i2 = measuredHeight;
        } else if (!this.mNoTitleNoPadding && this.mUseFullPadding) {
            i2 += this.mNoTitlePaddingFull;
        }
        if (isVisible(this.mContent)) {
            int i13 = i3 - i;
            this.mContent.layout((i13 - this.mContent.getMeasuredWidth()) / 2, i2, ((i13 - this.mContent.getMeasuredWidth()) / 2) + this.mContent.getMeasuredWidth(), this.mContent.getMeasuredHeight() + i2);
            i2 += this.mContent.getMeasuredHeight();
        }
        if (isVisible(this.mCloseButton)) {
            int i14 = i3 - i;
            this.mCloseButton.layout((i14 - this.mCloseButton.getMeasuredWidth()) / 2, ((int) getResources().getDimension(R.dimen.uik_mdCardMarginBottom)) + i2, ((i14 - this.mCloseButton.getMeasuredWidth()) / 2) + this.mCloseButton.getMeasuredWidth(), i2 + ((int) getResources().getDimension(R.dimen.uik_mdCardMarginBottom)) + this.mCloseButton.getMeasuredHeight());
        }
        if (this.mIsStacked) {
            int i15 = i4 - this.mButtonPaddingFull;
            for (TBDialogButton tBDialogButton : this.mButtons) {
                if (isVisible(tBDialogButton)) {
                    tBDialogButton.layout(i, i15 - tBDialogButton.getMeasuredHeight(), i3, i15);
                    i15 -= tBDialogButton.getMeasuredHeight();
                }
            }
        } else {
            if (this.mUseFullPadding) {
                i4 -= this.mButtonPaddingFull;
            }
            int i16 = i4 - this.mButtonBarHeight;
            int i17 = this.mButtonHorizontalEdgeMargin;
            if (isVisible(this.mButtons[2])) {
                if (this.mButtonGravity == GravityEnum.END) {
                    i12 = i + i17;
                    i11 = this.mButtons[2].getMeasuredWidth() + i12;
                    i5 = -1;
                } else {
                    i11 = i3 - i17;
                    i12 = i11 - this.mButtons[2].getMeasuredWidth();
                    i5 = i12;
                }
                this.mButtons[2].layout(i12, i16, i11, i4);
                i17 += this.mButtons[2].getMeasuredWidth();
            } else {
                i5 = -1;
            }
            if (isVisible(this.mButtons[1])) {
                if (this.mButtonGravity == GravityEnum.END) {
                    i10 = i17 + i;
                    i9 = this.mButtons[1].getMeasuredWidth() + i10;
                } else if (this.mButtonGravity == GravityEnum.START) {
                    i9 = i3 - i17;
                    i10 = i9 - this.mButtons[1].getMeasuredWidth();
                } else {
                    i10 = this.mButtonHorizontalEdgeMargin + i;
                    i9 = this.mButtons[1].getMeasuredWidth() + i10;
                    i6 = i9;
                    this.mButtons[1].layout(i10, i16, i9, i4);
                }
                i6 = -1;
                this.mButtons[1].layout(i10, i16, i9, i4);
            } else {
                i6 = -1;
            }
            if (isVisible(this.mButtons[0])) {
                if (this.mButtonGravity == GravityEnum.END) {
                    i7 = i3 - this.mButtonHorizontalEdgeMargin;
                    i8 = i7 - this.mButtons[0].getMeasuredWidth();
                } else if (this.mButtonGravity == GravityEnum.START) {
                    i8 = i + this.mButtonHorizontalEdgeMargin;
                    i7 = this.mButtons[0].getMeasuredWidth() + i8;
                } else {
                    if (i6 == -1 && i5 != -1) {
                        i6 = i5 - this.mButtons[0].getMeasuredWidth();
                    } else if (i5 == -1 && i6 != -1) {
                        i5 = i6 + this.mButtons[0].getMeasuredWidth();
                    } else if (i5 == -1) {
                        i6 = ((i3 - i) / 2) - (this.mButtons[0].getMeasuredWidth() / 2);
                        i5 = i6 + this.mButtons[0].getMeasuredWidth();
                    }
                    i8 = i6;
                    i7 = i5;
                }
                this.mButtons[0].layout(i8, i16, i7, i4);
            }
        }
        setUpDividersVisibility(this.mContent, true, true);
    }

    public void setForceStack(boolean z) {
        this.mForceStack = z;
        invalidate();
    }

    public void setDividerColor(int i) {
        this.mDividerPaint.setColor(i);
        invalidate();
    }

    public void setButtonGravity(GravityEnum gravityEnum) {
        this.mButtonGravity = gravityEnum;
        invertGravityIfNecessary();
    }

    private void invertGravityIfNecessary() {
        if (Build.VERSION.SDK_INT >= 17 && getResources().getConfiguration().getLayoutDirection() == 1) {
            switch (this.mButtonGravity) {
                case START:
                    this.mButtonGravity = GravityEnum.END;
                    return;
                case END:
                    this.mButtonGravity = GravityEnum.START;
                    return;
                default:
                    return;
            }
        }
    }

    public void setButtonStackedGravity(GravityEnum gravityEnum) {
        for (TBDialogButton tBDialogButton : this.mButtons) {
            if (tBDialogButton != null) {
                tBDialogButton.setStackedGravity(gravityEnum);
            }
        }
    }

    private void setUpDividersVisibility(final View view, final boolean z, final boolean z2) {
        if (view != null) {
            if (view instanceof ScrollView) {
                ScrollView scrollView = (ScrollView) view;
                if (canScrollViewScroll(scrollView)) {
                    addScrollListener(scrollView, z, z2);
                    return;
                }
                if (z) {
                    this.mDrawTopDivider = false;
                }
                if (z2) {
                    this.mDrawBottomDivider = false;
                }
            } else if (view instanceof AdapterView) {
                AdapterView adapterView = (AdapterView) view;
                if (canAdapterViewScroll(adapterView)) {
                    addScrollListener(adapterView, z, z2);
                    return;
                }
                if (z) {
                    this.mDrawTopDivider = false;
                }
                if (z2) {
                    this.mDrawBottomDivider = false;
                }
            } else if (view instanceof WebView) {
                view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    public boolean onPreDraw() {
                        if (view.getMeasuredHeight() == 0) {
                            return true;
                        }
                        if (!TBDialogRootLayout.canWebViewScroll((WebView) view)) {
                            if (z) {
                                boolean unused = TBDialogRootLayout.this.mDrawTopDivider = false;
                            }
                            if (z2) {
                                boolean unused2 = TBDialogRootLayout.this.mDrawBottomDivider = false;
                            }
                        } else {
                            TBDialogRootLayout.this.addScrollListener((ViewGroup) view, z, z2);
                        }
                        view.getViewTreeObserver().removeOnPreDrawListener(this);
                        return true;
                    }
                });
            } else if (view instanceof RecyclerView) {
                boolean canRecyclerViewScroll = canRecyclerViewScroll((RecyclerView) view);
                if (z) {
                    this.mDrawTopDivider = canRecyclerViewScroll;
                }
                if (z2) {
                    this.mDrawBottomDivider = canRecyclerViewScroll;
                }
            } else if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                View topView = getTopView(viewGroup);
                setUpDividersVisibility(topView, z, z2);
                View bottomView = getBottomView(viewGroup);
                if (bottomView != topView) {
                    setUpDividersVisibility(bottomView, false, true);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void addScrollListener(final ViewGroup viewGroup, final boolean z, final boolean z2) {
        if ((!z2 && this.mTopOnScrollChangedListener == null) || (z2 && this.mBottomOnScrollChangedListener == null)) {
            AnonymousClass2 r0 = new ViewTreeObserver.OnScrollChangedListener() {
                public void onScrollChanged() {
                    TBDialogButton[] access$400 = TBDialogRootLayout.this.mButtons;
                    int length = access$400.length;
                    boolean z = false;
                    int i = 0;
                    while (true) {
                        if (i < length) {
                            TBDialogButton tBDialogButton = access$400[i];
                            if (tBDialogButton != null && tBDialogButton.getVisibility() != 8) {
                                z = true;
                                break;
                            }
                            i++;
                        } else {
                            break;
                        }
                    }
                    if (viewGroup instanceof WebView) {
                        TBDialogRootLayout.this.invalidateDividersForWebView((WebView) viewGroup, z, z2, z);
                    } else {
                        TBDialogRootLayout.this.invalidateDividersForScrollingView(viewGroup, z, z2, z);
                    }
                    TBDialogRootLayout.this.invalidate();
                }
            };
            if (!z2) {
                this.mTopOnScrollChangedListener = r0;
                viewGroup.getViewTreeObserver().addOnScrollChangedListener(this.mTopOnScrollChangedListener);
            } else {
                this.mBottomOnScrollChangedListener = r0;
                viewGroup.getViewTreeObserver().addOnScrollChangedListener(this.mBottomOnScrollChangedListener);
            }
            r0.onScrollChanged();
        }
    }

    /* access modifiers changed from: private */
    public void invalidateDividersForScrollingView(ViewGroup viewGroup, boolean z, boolean z2, boolean z3) {
        boolean z4 = true;
        if (z && viewGroup.getChildCount() > 0) {
            this.mDrawTopDivider = (this.mTitleBar == null || this.mTitleBar.getVisibility() == 8 || viewGroup.getScrollY() + viewGroup.getPaddingTop() <= viewGroup.getChildAt(0).getTop()) ? false : true;
        }
        if (z2 && viewGroup.getChildCount() > 0) {
            if (!z3 || (viewGroup.getScrollY() + viewGroup.getHeight()) - viewGroup.getPaddingBottom() >= viewGroup.getChildAt(viewGroup.getChildCount() - 1).getBottom()) {
                z4 = false;
            }
            this.mDrawBottomDivider = z4;
        }
    }

    /* access modifiers changed from: private */
    public void invalidateDividersForWebView(WebView webView, boolean z, boolean z2, boolean z3) {
        boolean z4 = true;
        if (z) {
            this.mDrawTopDivider = (this.mTitleBar == null || this.mTitleBar.getVisibility() == 8 || webView.getScrollY() + webView.getPaddingTop() <= 0) ? false : true;
        }
        if (z2) {
            if (!z3 || ((float) ((webView.getScrollY() + webView.getMeasuredHeight()) - webView.getPaddingBottom())) >= ((float) webView.getContentHeight()) * webView.getScale()) {
                z4 = false;
            }
            this.mDrawBottomDivider = z4;
        }
    }

    public static boolean canRecyclerViewScroll(RecyclerView recyclerView) {
        if (recyclerView == null || recyclerView.getAdapter() == null || recyclerView.getLayoutManager() == null) {
            return false;
        }
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int itemCount = recyclerView.getAdapter().getItemCount();
        if (layoutManager instanceof LinearLayoutManager) {
            int findLastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            if (findLastVisibleItemPosition == -1) {
                return false;
            }
            if (!(findLastVisibleItemPosition == itemCount - 1) || (recyclerView.getChildCount() > 0 && recyclerView.getChildAt(recyclerView.getChildCount() - 1).getBottom() > recyclerView.getHeight() - recyclerView.getPaddingBottom())) {
                return true;
            }
            return false;
        }
        throw new TBMaterialDialog.NotImplementedException("Material Dialogs currently only supports LinearLayoutManager. Please report any new layout managers.");
    }

    private static boolean canScrollViewScroll(ScrollView scrollView) {
        if (scrollView.getChildCount() != 0 && (scrollView.getMeasuredHeight() - scrollView.getPaddingTop()) - scrollView.getPaddingBottom() < scrollView.getChildAt(0).getMeasuredHeight()) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public static boolean canWebViewScroll(WebView webView) {
        return ((float) webView.getMeasuredHeight()) < ((float) webView.getContentHeight()) * webView.getScale();
    }

    private static boolean canAdapterViewScroll(AdapterView adapterView) {
        if (adapterView.getLastVisiblePosition() == -1) {
            return false;
        }
        boolean z = adapterView.getFirstVisiblePosition() == 0;
        boolean z2 = adapterView.getLastVisiblePosition() == adapterView.getCount() - 1;
        if (!z || !z2 || adapterView.getChildCount() <= 0 || adapterView.getChildAt(0).getTop() < adapterView.getPaddingTop() || adapterView.getChildAt(adapterView.getChildCount() - 1).getBottom() > adapterView.getHeight() - adapterView.getPaddingBottom()) {
            return true;
        }
        return false;
    }

    @Nullable
    private static View getBottomView(ViewGroup viewGroup) {
        if (viewGroup == null || viewGroup.getChildCount() == 0) {
            return null;
        }
        for (int childCount = viewGroup.getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = viewGroup.getChildAt(childCount);
            if (childAt.getVisibility() == 0 && childAt.getBottom() == viewGroup.getMeasuredHeight()) {
                return childAt;
            }
        }
        return null;
    }

    @Nullable
    private static View getTopView(ViewGroup viewGroup) {
        if (viewGroup == null || viewGroup.getChildCount() == 0) {
            return null;
        }
        for (int childCount = viewGroup.getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = viewGroup.getChildAt(childCount);
            if (childAt.getVisibility() == 0 && childAt.getTop() == 0) {
                return childAt;
            }
        }
        return null;
    }
}
