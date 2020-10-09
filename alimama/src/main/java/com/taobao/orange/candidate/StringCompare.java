package com.taobao.orange.candidate;

public class StringCompare extends DefCandidateCompare {
    public boolean equals(String str, String str2) {
        return str.equals(str2);
    }

    public boolean equalsNot(String str, String str2) {
        return !str.equals(str2);
    }

    public boolean fuzzy(String str, String str2) {
        return str.startsWith(str2);
    }

    public boolean fuzzyNot(String str, String str2) {
        return !str.startsWith(str2);
    }
}
