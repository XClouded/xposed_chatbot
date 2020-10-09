package com.taobao.monitor.impl.data;

import android.view.View;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class LineTreeCalculator {
    private static final boolean DELETE = Boolean.FALSE.booleanValue();
    private static final boolean INSERT = Boolean.TRUE.booleanValue();
    private static final String TAG = "LineTreeCoveredCalculator";
    private final int padding;

    public LineTreeCalculator(int i) {
        this.padding = i;
    }

    public float calculate(View view, List<ViewInfo> list, View view2) {
        if (list == null || list.size() == 0) {
            return 0.0f;
        }
        int[] absLocationInWindow = ViewUtils.getAbsLocationInWindow(view, view2);
        int max = Math.max(0, absLocationInWindow[1]);
        int min = Math.min(ViewUtils.screenHeight, absLocationInWindow[1] + view.getHeight());
        int max2 = Math.max(0, absLocationInWindow[0]);
        int min2 = Math.min(ViewUtils.screenWidth, absLocationInWindow[0] + view.getWidth());
        int i = min2 - max2;
        if (i <= 0) {
            i = 0;
        }
        int i2 = min - max;
        if (i2 <= 0) {
            i2 = 0;
        }
        int i3 = i * i2;
        if (i3 == 0) {
            return 0.0f;
        }
        List<Line> createLines = createLines(max, min, max2, min2, list);
        if (createLines.size() == 0) {
            return 0.0f;
        }
        Collections.sort(createLines, new LineComparator());
        float innerCalculate = (((float) innerCalculate(max, min, createLines)) * 1.0f) / ((float) i3);
        for (Line next : createLines) {
            if (next != null) {
                next.recycle();
            }
        }
        return innerCalculate;
    }

    private int innerCalculate(int i, int i2, List<Line> list) {
        int i3 = 0;
        Tree tree = new Tree(0, i, i2);
        int i4 = 0;
        for (Line next : list) {
            if (next.x > i3) {
                if (tree.totalLength > 1) {
                    i4 += (next.x - i3) * (tree.totalLength - 1);
                }
                i3 = next.x;
            }
            doTreeAction(tree, next, next.direction == 0 ? INSERT : DELETE);
        }
        return i4;
    }

    private List<Line> createLines(int i, int i2, int i3, int i4, List<ViewInfo> list) {
        ArrayList arrayList = new ArrayList();
        for (ViewInfo next : list) {
            int max = Math.max(i, next.top - this.padding);
            int min = Math.min(i2, next.bottom + this.padding);
            if (max <= min) {
                Line access$200 = Line.obtain(next.left - this.padding >= i3 ? next.left - this.padding : i3, max, min);
                access$200.direction = 0;
                int i5 = next.right + this.padding;
                if (i5 > i4) {
                    i5 = i4;
                }
                Line access$2002 = Line.obtain(i5, max, min);
                access$2002.direction = 1;
                arrayList.add(access$200);
                arrayList.add(access$2002);
            }
        }
        return arrayList;
    }

    private void doTreeAction(Tree tree, Line line, boolean z) {
        int i = tree.start;
        int i2 = tree.end;
        if (line.start > i || line.end < i2) {
            int i3 = (i + i2) / 2;
            if (i3 >= line.start) {
                if (tree.left == null) {
                    tree.left = new Tree(tree.count, tree.start, i3);
                }
                doTreeAction(tree.left, line, z);
            }
            if (i3 < line.end) {
                if (tree.right == null) {
                    tree.right = new Tree(tree.count, i3 + 1, tree.end);
                }
                doTreeAction(tree.right, line, z);
            }
            tree.count = getMinOverDraw(tree);
            if (tree.count > 0) {
                tree.totalLength = (i2 - i) + 1;
                return;
            }
            tree.totalLength = 0;
            if (tree.left != null) {
                tree.totalLength += tree.left.totalLength;
            }
            if (tree.right != null) {
                tree.totalLength += tree.right.totalLength;
                return;
            }
            return;
        }
        if (z) {
            tree.count++;
        } else {
            tree.count--;
        }
        if (tree.left != null) {
            doTreeAction(tree.left, line, z);
        }
        if (tree.right != null) {
            doTreeAction(tree.right, line, z);
        }
        if (tree.count > 0) {
            tree.totalLength = (i2 - i) + 1;
            return;
        }
        tree.totalLength = 0;
        if (tree.left != null) {
            tree.totalLength += tree.left.totalLength;
        }
        if (tree.right != null) {
            tree.totalLength += tree.right.totalLength;
        }
    }

    private int getMinOverDraw(Tree tree) {
        Tree tree2 = tree.left;
        Tree tree3 = tree.right;
        return Math.min(tree2 == null ? tree.count : tree2.count, tree3 == null ? tree.count : tree3.count);
    }

    private static class Line {
        private static final int LEFT = 0;
        private static final int POOL_SIZE = 100;
        private static final int RIGHT = 1;
        private static Queue<Line> queue = new LinkedList();
        int direction;
        int end;
        int start;
        int x;

        private Line() {
        }

        /* access modifiers changed from: private */
        public static Line obtain(int i, int i2, int i3) {
            Line poll = queue.poll();
            if (poll == null) {
                poll = new Line();
            }
            poll.x = i;
            poll.start = i2;
            poll.end = i3;
            return poll;
        }

        /* access modifiers changed from: private */
        public void recycle() {
            if (queue.size() < 100) {
                queue.add(this);
            }
        }
    }

    private static class LineComparator implements Comparator<Line> {
        private LineComparator() {
        }

        public int compare(Line line, Line line2) {
            if (line.x < line2.x) {
                return -1;
            }
            if (line.x != line2.x) {
                return 1;
            }
            if (line.direction == line2.direction) {
                return 0;
            }
            if (line.direction == 0) {
                return -1;
            }
            return 1;
        }
    }

    private static class Tree {
        int count;
        int end;
        Tree left = null;
        Tree right = null;
        int start;
        int totalLength;

        Tree(int i, int i2, int i3) {
            if (i > 0) {
                this.totalLength = (i3 - i2) + 1;
            }
            this.count = i;
            this.start = i2;
            this.end = i3;
        }
    }
}
