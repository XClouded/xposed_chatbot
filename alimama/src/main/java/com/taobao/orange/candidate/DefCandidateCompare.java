package com.taobao.orange.candidate;

import com.taobao.orange.ICandidateCompare;

public class DefCandidateCompare implements ICandidateCompare {
    public boolean equals(String str, String str2) {
        return false;
    }

    public boolean equalsNot(String str, String str2) {
        return false;
    }

    public boolean fuzzy(String str, String str2) {
        return false;
    }

    public boolean fuzzyNot(String str, String str2) {
        return false;
    }

    public boolean greater(String str, String str2) {
        return false;
    }

    public boolean greaterEquals(String str, String str2) {
        return false;
    }

    public boolean less(String str, String str2) {
        return false;
    }

    public boolean lessEquals(String str, String str2) {
        return false;
    }
}
