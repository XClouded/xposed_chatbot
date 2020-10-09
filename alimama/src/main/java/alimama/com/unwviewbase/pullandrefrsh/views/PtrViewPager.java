package alimama.com.unwviewbase.pullandrefrsh.views;

import alimama.com.unwviewbase.pullandrefrsh.PullAdapter;
import alimama.com.unwviewbase.pullandrefrsh.PullBase;
import android.content.Context;
import android.util.AttributeSet;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class PtrViewPager extends ViewPager implements PullAdapter {
    public int getPullDirection() {
        return 1;
    }

    public void onPullAdapterAdded(PullBase pullBase) {
    }

    public void onPullAdapterRemoved(PullBase pullBase) {
    }

    public PtrViewPager(Context context) {
        super(context);
    }

    public PtrViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public boolean isReadyForPullStart() {
        if (getAdapter() == null || getCurrentItem() != 0) {
            return false;
        }
        return true;
    }

    public boolean isReadyForPullEnd() {
        PagerAdapter adapter = getAdapter();
        if (adapter == null || getCurrentItem() != adapter.getCount() - 1) {
            return false;
        }
        return true;
    }
}
