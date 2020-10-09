package com.taobao.orange;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.orange.aidl.OrangeCandidateCompareStub;
import com.taobao.orange.aidl.ParcelableCandidateCompare;
import com.taobao.orange.candidate.DefCandidateCompare;

public class OCandidate {
    private String clientVal;
    private ParcelableCandidateCompare compare;
    private String key;

    public OCandidate(@NonNull String str, @Nullable String str2, @NonNull ICandidateCompare iCandidateCompare) {
        if (TextUtils.isEmpty(str) || iCandidateCompare == null) {
            throw new IllegalArgumentException("key or compare is null");
        }
        this.key = str;
        this.clientVal = str2;
        this.compare = new OrangeCandidateCompareStub(iCandidateCompare);
    }

    public OCandidate(@NonNull String str, String str2, @NonNull Class<? extends ICandidateCompare> cls) {
        if (TextUtils.isEmpty(str) || cls == null) {
            throw new IllegalArgumentException("key or compare is null");
        }
        this.key = str;
        this.clientVal = str2;
        try {
            this.compare = new OrangeCandidateCompareStub((ICandidateCompare) cls.newInstance());
        } catch (Exception unused) {
            this.compare = new OrangeCandidateCompareStub(new DefCandidateCompare());
        }
    }

    public OCandidate(@NonNull String str, String str2, ParcelableCandidateCompare parcelableCandidateCompare) {
        if (TextUtils.isEmpty(str) || parcelableCandidateCompare == null) {
            throw new IllegalArgumentException("key or compare is null");
        }
        this.key = str;
        this.clientVal = str2;
        this.compare = parcelableCandidateCompare;
    }

    public String getKey() {
        return this.key;
    }

    public String getClientVal() {
        return this.clientVal;
    }

    public ParcelableCandidateCompare getCompare() {
        return this.compare;
    }

    public String toString() {
        return String.format("%s=%s %s", new Object[]{this.key, this.clientVal, this.compare instanceof OrangeCandidateCompareStub ? ((OrangeCandidateCompareStub) this.compare).getName() : null});
    }

    public boolean compare(OCandidate oCandidate) {
        if (oCandidate == null) {
            return false;
        }
        if (this == oCandidate) {
            return true;
        }
        if (!this.key.equals(oCandidate.key)) {
            return false;
        }
        if (this.clientVal == null ? oCandidate.clientVal != null : !this.clientVal.equals(oCandidate.clientVal)) {
            return false;
        }
        if (((OrangeCandidateCompareStub) this.compare).getRealClass() == ((OrangeCandidateCompareStub) oCandidate.compare).getRealClass()) {
            return true;
        }
        return false;
    }
}
