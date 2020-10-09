package alimama.com.unwviewbase.pullandrefrsh.views;

import alimama.com.unwviewbase.pullandrefrsh.PullAdapter;
import alimama.com.unwviewbase.pullandrefrsh.PullBase;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

public class PtrHorizontalScrollView extends HorizontalScrollView implements PullAdapter {
    public int getPullDirection() {
        return 1;
    }

    public void onPullAdapterAdded(PullBase pullBase) {
    }

    public void onPullAdapterRemoved(PullBase pullBase) {
    }

    public PtrHorizontalScrollView(Context context) {
        super(context);
    }

    public PtrHorizontalScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PtrHorizontalScrollView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public boolean isReadyForPullStart() {
        return getScrollX() == 0;
    }

    public boolean isReadyForPullEnd() {
        View childAt;
        if (getChildCount() <= 0 || (childAt = getChildAt(0)) == null || getScrollX() < childAt.getWidth() - getWidth()) {
            return false;
        }
        return true;
    }
}
