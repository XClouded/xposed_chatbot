package alimama.com.unwviewbase.pullandrefrsh.views;

import alimama.com.unwviewbase.pullandrefrsh.PullAdapter;
import alimama.com.unwviewbase.pullandrefrsh.PullBase;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

public class PtrScrollView extends ScrollView implements PullAdapter {
    public int getPullDirection() {
        return 0;
    }

    public void onPullAdapterAdded(PullBase pullBase) {
    }

    public void onPullAdapterRemoved(PullBase pullBase) {
    }

    public PtrScrollView(Context context) {
        super(context);
    }

    public PtrScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PtrScrollView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public boolean isReadyForPullStart() {
        return getScrollY() == 0;
    }

    public boolean isReadyForPullEnd() {
        View childAt;
        if (getChildCount() <= 0 || (childAt = getChildAt(0)) == null || getScrollY() < childAt.getHeight() - getHeight()) {
            return false;
        }
        return true;
    }
}
