package com.taobao.monitor.impl.data;

import android.view.View;
import android.widget.TextView;
import java.util.LinkedList;
import java.util.Queue;

public class ViewInfo {
    private static final int POOL_SIZE = 100;
    private static Queue<ViewInfo> queue = new LinkedList();
    public int bottom;
    public boolean isText;
    public int left;
    public int right;
    public int top;
    public View view;

    public void recycle() {
        if (queue.size() < 100) {
            queue.add(this);
        }
    }

    public static ViewInfo obtain(View view2, View view3) {
        ViewInfo poll = queue.poll();
        if (poll == null) {
            poll = new ViewInfo();
        }
        int[] absLocationInWindow = ViewUtils.getAbsLocationInWindow(view2, view3);
        boolean z = view2 instanceof TextView;
        int max = Math.max(0, absLocationInWindow[0]);
        int min = Math.min(ViewUtils.screenWidth, absLocationInWindow[0] + view2.getWidth());
        int max2 = Math.max(0, absLocationInWindow[1]);
        int min2 = Math.min(ViewUtils.screenHeight, absLocationInWindow[1] + view2.getHeight());
        poll.isText = z;
        poll.left = max;
        poll.right = min;
        poll.top = max2;
        poll.bottom = min2;
        return poll;
    }

    public String toString() {
        return "ViewInfo{top=" + this.top + ", bottom=" + this.bottom + ", left=" + this.left + ", right=" + this.right + '}';
    }
}
