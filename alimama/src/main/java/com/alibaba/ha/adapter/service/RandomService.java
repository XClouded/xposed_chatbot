package com.alibaba.ha.adapter.service;

import android.util.Log;
import com.alibaba.ha.adapter.AliHaAdapter;
import java.util.UUID;

public class RandomService {
    public String getRandomNum() {
        try {
            return UUID.randomUUID().toString().replace("-", "");
        } catch (Exception e) {
            Log.w(AliHaAdapter.TAG, "get random num failure", e);
            return null;
        }
    }
}
