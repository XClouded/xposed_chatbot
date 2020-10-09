package com.taobao.monitor.impl.trace;

import android.app.Activity;
import androidx.fragment.app.Fragment;
import com.taobao.monitor.impl.trace.AbsDispatcher;

public class FragmentFunctionDispatcher extends AbsDispatcher<FragmentFunctionListener> implements FragmentFunctionListener {
    public static final FragmentFunctionDispatcher INSTANCE = new FragmentFunctionDispatcher();

    public void onFunction(Activity activity, Fragment fragment, String str, long j) {
        final Activity activity2 = activity;
        final Fragment fragment2 = fragment;
        final String str2 = str;
        final long j2 = j;
        foreach(new AbsDispatcher.ListenerCaller<FragmentFunctionListener>() {
            public void callListener(FragmentFunctionListener fragmentFunctionListener) {
                fragmentFunctionListener.onFunction(activity2, fragment2, str2, j2);
            }
        });
    }
}
