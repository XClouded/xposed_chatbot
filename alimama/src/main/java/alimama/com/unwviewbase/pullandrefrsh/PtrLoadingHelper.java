package alimama.com.unwviewbase.pullandrefrsh;

import alimama.com.unwviewbase.R;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PtrLoadingHelper implements PtrLoadingDelegate {
    private PtrLoadingDelegate mDelegate;
    private ProgressBar mHeaderProgress;
    private TextView mHeaderText;
    private int mOrientation;
    private PtrLayout mPtrController;
    private View mRoot;

    public PtrLoadingHelper(PtrLayout ptrLayout) {
        this.mPtrController = ptrLayout;
    }

    public View getLoadingView(ViewGroup viewGroup) {
        if (this.mOrientation != 1) {
            this.mRoot = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ptr_loading_vertical, viewGroup, false);
        } else {
            this.mRoot = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ptr_loading_horizontal, viewGroup, false);
        }
        if (this.mDelegate != null) {
            ViewGroup viewGroup2 = (ViewGroup) this.mRoot;
            viewGroup2.removeAllViews();
            this.mHeaderText = null;
            this.mHeaderProgress = null;
            viewGroup2.addView(this.mDelegate.getLoadingView(viewGroup2));
        } else {
            View findViewById = this.mRoot.findViewById(R.id.tv_ptr_label);
            if (findViewById instanceof TextView) {
                this.mHeaderText = (TextView) findViewById;
            }
            View findViewById2 = this.mRoot.findViewById(R.id.pb_ptr_progress);
            if (findViewById2 instanceof ProgressBar) {
                this.mHeaderProgress = (ProgressBar) findViewById2;
            }
            if (this.mPtrController != null) {
                setLoadingDrawable(this.mPtrController.getLoadingDrawable());
                setLoadingTextColor(this.mPtrController.getTextColor());
                initTextView(this.mPtrController.getPullLabel());
            }
        }
        return this.mRoot;
    }

    public void setTextSize(float f) {
        if (this.mHeaderText != null) {
            this.mHeaderText.setTextSize(f);
        }
    }

    public int getContentSize(int i) {
        int contentSize = this.mDelegate != null ? this.mDelegate.getContentSize(i) : 0;
        if (contentSize != 0) {
            return contentSize;
        }
        if (i == 1) {
            return this.mRoot != null ? this.mRoot.getWidth() : contentSize;
        }
        if (this.mRoot != null) {
            return this.mRoot.getHeight();
        }
        return contentSize;
    }

    private void initTextView(CharSequence charSequence) {
        if (this.mHeaderText != null && charSequence != null) {
            this.mHeaderText.setText(charSequence);
        }
    }

    public void setLoadingTextColor(ColorStateList colorStateList) {
        if (this.mHeaderText != null && colorStateList != null) {
            this.mHeaderText.setTextColor(colorStateList);
        }
    }

    public void setLoadingDrawable(Drawable drawable) {
        if (this.mHeaderProgress != null && drawable != null) {
            this.mHeaderProgress.setIndeterminateDrawable(drawable);
        }
    }

    public void setLoadingDelegate(PtrLoadingDelegate ptrLoadingDelegate) {
        this.mDelegate = ptrLoadingDelegate;
    }

    public void onUpdateDirection(int i) {
        this.mOrientation = i;
        if (this.mDelegate != null) {
            this.mDelegate.onUpdateDirection(i);
        }
    }

    private float calScale(float f) {
        int i = 1;
        if (this.mOrientation != 1) {
            if (this.mRoot != null) {
                i = this.mRoot.getHeight();
            }
        } else if (this.mRoot != null) {
            i = this.mRoot.getWidth();
        }
        return Math.abs(f) / ((float) i);
    }

    public void onPull(float f) {
        if (((double) calScale(f)) < 1.0d) {
            if (!(this.mHeaderText == null || this.mPtrController == null)) {
                this.mHeaderText.setText(this.mPtrController.getPullLabel());
            }
        } else if (!(this.mHeaderText == null || this.mPtrController == null)) {
            this.mHeaderText.setText(this.mPtrController.getReleaseLabel());
        }
        if (this.mHeaderProgress != null && this.mHeaderProgress.getVisibility() == 0) {
            this.mHeaderProgress.setVisibility(8);
        }
        if (this.mDelegate != null) {
            this.mDelegate.onPull(f);
        }
    }

    public void onRelease(float f) {
        if (this.mDelegate != null) {
            this.mDelegate.onRelease(f);
        }
    }

    public void onRefreshing() {
        if (!(this.mHeaderText == null || this.mPtrController == null)) {
            if (this.mHeaderText.getVisibility() == 8) {
                this.mHeaderText.setVisibility(0);
            }
            this.mHeaderText.setText(this.mPtrController.getRefreshingLabel());
        }
        if (this.mHeaderProgress != null && this.mHeaderProgress.getVisibility() == 8) {
            this.mHeaderProgress.setVisibility(0);
        }
        if (this.mDelegate != null) {
            this.mDelegate.onRefreshing();
        }
    }

    public void onCompleteUpdate(CharSequence charSequence) {
        if (this.mHeaderProgress != null) {
            this.mHeaderProgress.setVisibility(8);
        }
        if (this.mHeaderText != null) {
            this.mHeaderText.setText(charSequence);
        }
        if (this.mDelegate != null) {
            this.mDelegate.onCompleteUpdate(charSequence);
        }
    }

    public void onReset() {
        if (!(this.mHeaderText == null || this.mPtrController == null)) {
            if (this.mHeaderText.getVisibility() == 8) {
                this.mHeaderText.setVisibility(0);
            }
            this.mHeaderText.setText(this.mPtrController.getPullLabel());
        }
        if (this.mHeaderProgress != null && this.mHeaderProgress.getVisibility() == 0) {
            this.mHeaderProgress.setVisibility(8);
        }
        if (this.mDelegate != null) {
            this.mDelegate.onReset();
        }
    }

    public void onFreeze(boolean z, CharSequence charSequence) {
        if (z) {
            if (this.mHeaderProgress != null) {
                this.mHeaderProgress.setVisibility(8);
            }
            if (this.mHeaderText != null) {
                this.mHeaderText.setText(charSequence);
            }
        } else {
            if (this.mHeaderProgress != null) {
                this.mHeaderProgress.setVisibility(8);
            }
            if (!(this.mHeaderText == null || this.mPtrController == null)) {
                this.mHeaderText.setVisibility(0);
                this.mHeaderText.setText(this.mPtrController.getPullLabel());
            }
        }
        if (this.mDelegate != null) {
            this.mDelegate.onFreeze(z, charSequence);
        }
    }
}
