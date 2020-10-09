package com.ali.user.mobile.service;

import android.os.Handler;

public interface FingerprintService {
    void authenticate(Handler handler);

    void cancelIdentify();

    int getRetryTimeLeft();

    boolean hardwareDetected();

    boolean hasEnrolledFingerprints();

    void increaseErrorTime();

    boolean isKeyguardSecure();
}
