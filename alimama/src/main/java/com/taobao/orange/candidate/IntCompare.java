package com.taobao.orange.candidate;

public class IntCompare extends DefCandidateCompare {
    public boolean equals(String str, String str2) {
        return Integer.parseInt(str) == Integer.parseInt(str2);
    }

    public boolean equalsNot(String str, String str2) {
        return Integer.parseInt(str) != Integer.parseInt(str2);
    }

    public boolean greater(String str, String str2) {
        return Integer.parseInt(str) > Integer.parseInt(str2);
    }

    public boolean greaterEquals(String str, String str2) {
        return Integer.parseInt(str) >= Integer.parseInt(str2);
    }

    public boolean less(String str, String str2) {
        return Integer.parseInt(str) < Integer.parseInt(str2);
    }

    public boolean lessEquals(String str, String str2) {
        return Integer.parseInt(str) <= Integer.parseInt(str2);
    }
}
