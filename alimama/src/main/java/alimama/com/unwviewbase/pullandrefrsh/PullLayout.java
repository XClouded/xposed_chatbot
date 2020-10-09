package alimama.com.unwviewbase.pullandrefrsh;

import alimama.com.unwviewbase.R;
import alimama.com.unwviewbase.pullandrefrsh.PullBase;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class PullLayout extends FrameLayout {
    private int mLayoutId = -1;
    private final PullBase.Mode mMode;
    private int mScrollDirection;

    /* access modifiers changed from: protected */
    public void onInit(Context context, PullBase.Mode mode, int i, AttributeSet attributeSet) {
    }

    /* access modifiers changed from: protected */
    public void onScrollDirectionUpdated(PullBase.Mode mode, int i) {
    }

    public PullLayout(Context context, PullBase.Mode mode, int i, AttributeSet attributeSet) {
        super(context);
        this.mMode = mode;
        this.mScrollDirection = i;
        init(context, mode, i, attributeSet);
    }

    /* access modifiers changed from: package-private */
    public void updateScrollDirection(PullBase.Mode mode, int i) {
        this.mScrollDirection = i;
        onScrollDirectionUpdated(mode, i);
    }

    /* renamed from: alimama.com.unwviewbase.pullandrefrsh.PullLayout$1  reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$alimama$com$unwviewbase$pullandrefrsh$PullBase$Mode = new int[PullBase.Mode.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(6:0|1|2|3|4|6) */
        /* JADX WARNING: Code restructure failed: missing block: B:7:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        static {
            /*
                alimama.com.unwviewbase.pullandrefrsh.PullBase$Mode[] r0 = alimama.com.unwviewbase.pullandrefrsh.PullBase.Mode.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$alimama$com$unwviewbase$pullandrefrsh$PullBase$Mode = r0
                int[] r0 = $SwitchMap$alimama$com$unwviewbase$pullandrefrsh$PullBase$Mode     // Catch:{ NoSuchFieldError -> 0x0014 }
                alimama.com.unwviewbase.pullandrefrsh.PullBase$Mode r1 = alimama.com.unwviewbase.pullandrefrsh.PullBase.Mode.PULL_FROM_END     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = $SwitchMap$alimama$com$unwviewbase$pullandrefrsh$PullBase$Mode     // Catch:{ NoSuchFieldError -> 0x001f }
                alimama.com.unwviewbase.pullandrefrsh.PullBase$Mode r1 = alimama.com.unwviewbase.pullandrefrsh.PullBase.Mode.PULL_FROM_START     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: alimama.com.unwviewbase.pullandrefrsh.PullLayout.AnonymousClass1.<clinit>():void");
        }
    }

    private void init(Context context, PullBase.Mode mode, int i, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.Pull);
        Drawable drawable = null;
        if (AnonymousClass1.$SwitchMap$alimama$com$unwviewbase$pullandrefrsh$PullBase$Mode[mode.ordinal()] != 1) {
            if (obtainStyledAttributes.hasValue(R.styleable.Pull_pullStartBackground)) {
                drawable = obtainStyledAttributes.getDrawable(R.styleable.Pull_pullStartBackground);
            }
            if (obtainStyledAttributes.hasValue(R.styleable.Pull_pullStartLayout)) {
                this.mLayoutId = obtainStyledAttributes.getResourceId(R.styleable.Pull_pullStartLayout, -1);
            }
        } else {
            if (obtainStyledAttributes.hasValue(R.styleable.Pull_pullEndBackground)) {
                drawable = obtainStyledAttributes.getDrawable(R.styleable.Pull_pullEndBackground);
            }
            if (obtainStyledAttributes.hasValue(R.styleable.Pull_pullEndLayout)) {
                this.mLayoutId = obtainStyledAttributes.getResourceId(R.styleable.Pull_pullEndLayout, -1);
            }
        }
        obtainStyledAttributes.recycle();
        setPullBackground(drawable);
        inflateChildLayout(this.mLayoutId);
        onInit(context, mode, i, attributeSet);
    }

    private void inflateChildLayout(int i) {
        if (i > 0) {
            LayoutInflater.from(getContext()).inflate(this.mLayoutId, this, true);
        }
    }

    public final void setHeight(int i) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            layoutParams.height = View.MeasureSpec.makeMeasureSpec(i, 1073741824);
            setLayoutParams(layoutParams);
            requestLayout();
        }
    }

    public final void setWidth(int i) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            layoutParams.width = View.MeasureSpec.makeMeasureSpec(i, 1073741824);
            setLayoutParams(layoutParams);
            requestLayout();
        }
    }

    public final int getLayoutId() {
        return this.mLayoutId;
    }

    public final PullBase.Mode getMode() {
        return this.mMode;
    }

    public final int getScrollDirection() {
        return this.mScrollDirection;
    }

    public final void setPullBackground(Drawable drawable) {
        if (drawable != null) {
            ViewCompat.setBackground(this, drawable);
        }
    }
}
