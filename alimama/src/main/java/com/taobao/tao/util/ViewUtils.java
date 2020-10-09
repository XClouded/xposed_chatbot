package com.taobao.tao.util;

import android.view.View;
import android.view.ViewGroup;
import com.taobao.tao.util.LayoutTraverser;
import java.util.ArrayList;
import java.util.List;

public final class ViewUtils {
    private ViewUtils() {
    }

    public static <T extends View> List<T> find(ViewGroup viewGroup, Class<T> cls) {
        FinderByType finderByType = new FinderByType(cls);
        LayoutTraverser.build(finderByType).traverse(viewGroup);
        return finderByType.getViews();
    }

    private static class FinderByType<T extends View> implements LayoutTraverser.Processor {
        private final Class<T> type;
        private final List<T> views;

        private FinderByType(Class<T> cls) {
            this.type = cls;
            this.views = new ArrayList();
        }

        public void process(View view) {
            if (this.type.isInstance(view)) {
                this.views.add(view);
            }
        }

        public List<T> getViews() {
            return this.views;
        }
    }
}
