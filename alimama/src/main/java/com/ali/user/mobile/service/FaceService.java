package com.ali.user.mobile.service;

import com.ali.user.mobile.model.FaceVerifyCallback;

public interface FaceService {
    void activeFaceLogin(String str, FaceVerifyCallback faceVerifyCallback);

    String getDeviceInfo();

    void nativeLogin(String str, FaceVerifyCallback faceVerifyCallback);

    boolean userOpenFaceLogin();
}
