package alimama.com.unwviewbase.pullandrefrsh.views.abs.accessories;

import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;

public class AbsListViewDetector {
    public static boolean isFirstItemTotallyVisible(AbsListView absListView) {
        View childAt;
        if (absListView != null) {
            Adapter adapter = absListView.getAdapter();
            if (adapter == null || adapter.isEmpty()) {
                return true;
            }
            if (absListView.getFirstVisiblePosition() > 0 || (childAt = absListView.getChildAt(0)) == null || childAt.getTop() < 0) {
                return false;
            }
            return true;
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0011, code lost:
        r2 = r4.getCount() - 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean isLastItemTotallyVisible(android.widget.AbsListView r4) {
        /*
            r0 = 0
            r1 = 1
            if (r4 == 0) goto L_0x0032
            android.widget.Adapter r2 = r4.getAdapter()
            if (r2 == 0) goto L_0x0031
            boolean r2 = r2.isEmpty()
            if (r2 == 0) goto L_0x0011
            goto L_0x0031
        L_0x0011:
            int r2 = r4.getCount()
            int r2 = r2 - r1
            int r3 = r4.getLastVisiblePosition()
            if (r3 < r2) goto L_0x0032
            int r2 = r4.getFirstVisiblePosition()
            int r3 = r3 - r2
            android.view.View r2 = r4.getChildAt(r3)
            if (r2 == 0) goto L_0x0032
            int r2 = r2.getBottom()
            int r4 = r4.getHeight()
            if (r2 > r4) goto L_0x0032
        L_0x0031:
            r0 = 1
        L_0x0032:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: alimama.com.unwviewbase.pullandrefrsh.views.abs.accessories.AbsListViewDetector.isLastItemTotallyVisible(android.widget.AbsListView):boolean");
    }
}
