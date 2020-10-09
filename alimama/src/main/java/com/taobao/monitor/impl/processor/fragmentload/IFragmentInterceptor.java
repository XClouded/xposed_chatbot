package com.taobao.monitor.impl.processor.fragmentload;

import androidx.fragment.app.Fragment;

public interface IFragmentInterceptor {
    boolean needPopFragment(Fragment fragment);
}
