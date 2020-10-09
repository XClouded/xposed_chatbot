package com.taobao.monitor.impl.trace;

import android.app.Activity;
import androidx.fragment.app.Fragment;

public interface FragmentFunctionListener {
    void onFunction(Activity activity, Fragment fragment, String str, long j);
}
