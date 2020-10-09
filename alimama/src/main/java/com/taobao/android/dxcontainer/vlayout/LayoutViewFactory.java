package com.taobao.android.dxcontainer.vlayout;

import android.content.Context;
import android.view.View;
import androidx.annotation.NonNull;

public interface LayoutViewFactory {
    View generateLayoutView(@NonNull Context context);
}
