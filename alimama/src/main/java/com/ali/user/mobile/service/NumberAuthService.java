package com.ali.user.mobile.service;

import com.ali.user.mobile.model.NumAuthCallback;

public interface NumberAuthService {
    void getToken();

    void init(NumAuthCallback numAuthCallback);
}
