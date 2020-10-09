package com.taobao.tao.util;

import android.view.View;
import android.view.ViewGroup;

public class LayoutTraverser {
    private final Processor processor;

    public interface Processor {
        void process(View view);
    }

    private LayoutTraverser(Processor processor2) {
        this.processor = processor2;
    }

    public static LayoutTraverser build(Processor processor2) {
        return new LayoutTraverser(processor2);
    }

    public void traverse(ViewGroup viewGroup) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = viewGroup.getChildAt(i);
            this.processor.process(childAt);
            if (childAt instanceof ViewGroup) {
                traverse((ViewGroup) childAt);
            }
        }
    }
}
