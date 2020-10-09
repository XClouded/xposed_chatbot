package com.taobao.weex.devtools.inspector.elements.android;

import android.app.Activity;
import android.view.View;
import android.view.Window;
import com.taobao.weex.devtools.common.Accumulator;
import com.taobao.weex.devtools.common.StringUtil;
import com.taobao.weex.devtools.common.android.FragmentCompat;
import com.taobao.weex.devtools.inspector.elements.AbstractChainedDescriptor;
import com.taobao.weex.devtools.inspector.elements.Descriptor;
import java.util.List;
import javax.annotation.Nullable;

final class ActivityDescriptor extends AbstractChainedDescriptor<Activity> implements HighlightableDescriptor {
    ActivityDescriptor() {
    }

    /* access modifiers changed from: protected */
    public String onGetNodeName(Activity activity) {
        return StringUtil.removePrefix(activity.getClass().getName(), "android.app.");
    }

    /* access modifiers changed from: protected */
    public void onGetChildren(Activity activity, Accumulator<Object> accumulator) {
        getDialogFragments(FragmentCompat.getSupportLibInstance(), activity, accumulator);
        getDialogFragments(FragmentCompat.getFrameworkInstance(), activity, accumulator);
        Window window = activity.getWindow();
        if (window != null) {
            accumulator.store(window);
        }
    }

    public View getViewForHighlighting(Object obj) {
        Descriptor.Host host = getHost();
        if (!(host instanceof AndroidDescriptorHost)) {
            return null;
        }
        return ((AndroidDescriptorHost) host).getHighlightingView(((Activity) obj).getWindow());
    }

    private static void getDialogFragments(@Nullable FragmentCompat fragmentCompat, Activity activity, Accumulator<Object> accumulator) {
        Object fragmentManager;
        List addedFragments;
        if (fragmentCompat != null && fragmentCompat.getFragmentActivityClass().isInstance(activity) && (fragmentManager = fragmentCompat.forFragmentActivity().getFragmentManager(activity)) != null && (addedFragments = fragmentCompat.forFragmentManager().getAddedFragments(fragmentManager)) != null) {
            int size = addedFragments.size();
            for (int i = 0; i < size; i++) {
                Object obj = addedFragments.get(i);
                if (fragmentCompat.getDialogFragmentClass().isInstance(obj)) {
                    accumulator.store(obj);
                }
            }
        }
    }
}
