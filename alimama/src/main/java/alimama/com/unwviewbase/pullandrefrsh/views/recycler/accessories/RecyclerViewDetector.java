package alimama.com.unwviewbase.pullandrefrsh.views.recycler.accessories;

import android.view.View;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class RecyclerViewDetector {
    public static int getLayoutOrientation(RecyclerView recyclerView) {
        if (recyclerView != null) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if ((layoutManager instanceof GridLayoutManager) || (layoutManager instanceof LinearLayoutManager)) {
                return ((LinearLayoutManager) layoutManager).getOrientation();
            }
            if (layoutManager instanceof StaggeredGridLayoutManager) {
                return ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            }
        }
        return 1;
    }

    private static boolean isChildTopVisible(RecyclerView recyclerView, int i) {
        View childAt;
        if (recyclerView == null || (childAt = recyclerView.getChildAt(i)) == null || childAt.getTop() < 0) {
            return false;
        }
        return true;
    }

    private static boolean isChildLeftVisible(RecyclerView recyclerView, int i) {
        View childAt;
        if (recyclerView == null || (childAt = recyclerView.getChildAt(i)) == null || childAt.getLeft() < 0) {
            return false;
        }
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0013, code lost:
        r5 = (androidx.recyclerview.widget.StaggeredGridLayoutManager) r5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int findFirstVisibleItemPosition(androidx.recyclerview.widget.RecyclerView r5) {
        /*
            if (r5 == 0) goto L_0x003d
            androidx.recyclerview.widget.RecyclerView$LayoutManager r5 = r5.getLayoutManager()
            boolean r0 = r5 instanceof androidx.recyclerview.widget.GridLayoutManager
            if (r0 != 0) goto L_0x0036
            boolean r0 = r5 instanceof androidx.recyclerview.widget.LinearLayoutManager
            if (r0 == 0) goto L_0x000f
            goto L_0x0036
        L_0x000f:
            boolean r0 = r5 instanceof androidx.recyclerview.widget.StaggeredGridLayoutManager
            if (r0 == 0) goto L_0x003d
            androidx.recyclerview.widget.StaggeredGridLayoutManager r5 = (androidx.recyclerview.widget.StaggeredGridLayoutManager) r5
            int r0 = r5.getSpanCount()
            if (r0 <= 0) goto L_0x003d
            int[] r0 = new int[r0]
            r5.findFirstVisibleItemPositions(r0)
            r5 = 0
            int r1 = r0.length
            r2 = 2147483647(0x7fffffff, float:NaN)
            r3 = 2147483647(0x7fffffff, float:NaN)
        L_0x0028:
            if (r5 >= r1) goto L_0x0033
            r4 = r0[r5]
            if (r4 >= r3) goto L_0x0030
            r3 = r0[r5]
        L_0x0030:
            int r5 = r5 + 1
            goto L_0x0028
        L_0x0033:
            if (r3 == r2) goto L_0x003d
            goto L_0x003e
        L_0x0036:
            androidx.recyclerview.widget.LinearLayoutManager r5 = (androidx.recyclerview.widget.LinearLayoutManager) r5
            int r3 = r5.findFirstVisibleItemPosition()
            goto L_0x003e
        L_0x003d:
            r3 = -1
        L_0x003e:
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: alimama.com.unwviewbase.pullandrefrsh.views.recycler.accessories.RecyclerViewDetector.findFirstVisibleItemPosition(androidx.recyclerview.widget.RecyclerView):int");
    }

    public static boolean isFirstItemTotallyVisible(RecyclerView recyclerView) {
        if (recyclerView != null) {
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (adapter == null || adapter.getItemCount() == 0) {
                return true;
            }
            int findFirstVisibleItemPosition = findFirstVisibleItemPosition(recyclerView);
            if (findFirstVisibleItemPosition != -1 && findFirstVisibleItemPosition <= 2) {
                switch (getLayoutOrientation(recyclerView)) {
                    case 0:
                        return isChildLeftVisible(recyclerView, findFirstVisibleItemPosition);
                    case 1:
                        return isChildTopVisible(recyclerView, findFirstVisibleItemPosition);
                }
            }
        }
        return false;
    }

    private static boolean isChildBottomVisible(RecyclerView recyclerView, int i) {
        View childAt;
        if (recyclerView == null || (childAt = recyclerView.getChildAt(i)) == null || childAt.getBottom() > recyclerView.getHeight()) {
            return false;
        }
        return true;
    }

    private static boolean isChildRightVisible(RecyclerView recyclerView, int i) {
        View childAt;
        if (recyclerView == null || (childAt = recyclerView.getChildAt(i)) == null || childAt.getRight() > recyclerView.getWidth()) {
            return false;
        }
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0014, code lost:
        r5 = (androidx.recyclerview.widget.StaggeredGridLayoutManager) r5;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int findLastVisibleItemPosition(androidx.recyclerview.widget.RecyclerView r5) {
        /*
            r0 = -1
            if (r5 == 0) goto L_0x0039
            androidx.recyclerview.widget.RecyclerView$LayoutManager r5 = r5.getLayoutManager()
            boolean r1 = r5 instanceof androidx.recyclerview.widget.GridLayoutManager
            if (r1 != 0) goto L_0x0033
            boolean r1 = r5 instanceof androidx.recyclerview.widget.LinearLayoutManager
            if (r1 == 0) goto L_0x0010
            goto L_0x0033
        L_0x0010:
            boolean r1 = r5 instanceof androidx.recyclerview.widget.StaggeredGridLayoutManager
            if (r1 == 0) goto L_0x0039
            androidx.recyclerview.widget.StaggeredGridLayoutManager r5 = (androidx.recyclerview.widget.StaggeredGridLayoutManager) r5
            int r1 = r5.getSpanCount()
            if (r1 <= 0) goto L_0x0039
            int[] r1 = new int[r1]
            r5.findLastVisibleItemPositions(r1)
            r5 = 0
            int r2 = r1.length
            r3 = -1
        L_0x0024:
            if (r5 >= r2) goto L_0x002f
            r4 = r1[r5]
            if (r4 <= r3) goto L_0x002c
            r3 = r1[r5]
        L_0x002c:
            int r5 = r5 + 1
            goto L_0x0024
        L_0x002f:
            if (r3 == r0) goto L_0x0039
            r0 = r3
            goto L_0x0039
        L_0x0033:
            androidx.recyclerview.widget.LinearLayoutManager r5 = (androidx.recyclerview.widget.LinearLayoutManager) r5
            int r0 = r5.findLastVisibleItemPosition()
        L_0x0039:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: alimama.com.unwviewbase.pullandrefrsh.views.recycler.accessories.RecyclerViewDetector.findLastVisibleItemPosition(androidx.recyclerview.widget.RecyclerView):int");
    }

    public static boolean isLastItemTotallyVisible(RecyclerView recyclerView) {
        if (recyclerView != null) {
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if (adapter == null || adapter.getItemCount() == 0) {
                return true;
            }
            int itemCount = adapter.getItemCount();
            int findLastVisibleItemPosition = findLastVisibleItemPosition(recyclerView);
            if (findLastVisibleItemPosition != -1 && findLastVisibleItemPosition >= itemCount) {
                int findFirstVisibleItemPosition = findLastVisibleItemPosition - findFirstVisibleItemPosition(recyclerView);
                switch (getLayoutOrientation(recyclerView)) {
                    case 0:
                        return isChildRightVisible(recyclerView, findFirstVisibleItemPosition);
                    case 1:
                        return isChildBottomVisible(recyclerView, findFirstVisibleItemPosition);
                }
            }
        }
        return false;
    }
}
