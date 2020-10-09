package com.ali.user.mobile.common.api;

import android.os.Bundle;

public interface OnBindCaller {
    void onBindError(Bundle bundle);

    void onBindSuccess(Bundle bundle);
}
