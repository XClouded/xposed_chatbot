package com.alibaba.motu.tbrest.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RestOrangeConfigure {
    private static final int DEFAULT_MESSAGE_COUNT = 50;
    private static final float DEFAULT_SAMPLE = 1.0f;
    private static final int DEFAULT_SIZE = 40960;
    private static final int MAX_MESSAGE_COUNT = 500;
    private static final int MAX_SIZE = 1048576;
    private float allSample;
    private int dataSize;
    private final Map<String, Float> floatHashMap;
    private int messageCount;
    private boolean useOldLogic;

    private RestOrangeConfigure() {
        this.dataSize = DEFAULT_SIZE;
        this.allSample = 1.0f;
        this.floatHashMap = new ConcurrentHashMap();
        this.useOldLogic = false;
        this.messageCount = 50;
    }

    public static RestOrangeConfigure instance() {
        return Holder.INSTANCE;
    }

    public float getSampleByEventID(String str) {
        Float f = this.floatHashMap.get(str);
        if (f != null) {
            return Math.min(f.floatValue(), this.allSample);
        }
        return Math.min(1.0f, this.allSample);
    }

    public void setEventIDSample(String str, float f) {
        if (f < 0.0f || f > 1.0f) {
            this.floatHashMap.put(str, Float.valueOf(1.0f));
        } else {
            this.floatHashMap.put(str, Float.valueOf(f));
        }
    }

    public int getDataSize() {
        return (this.dataSize <= 0 || this.dataSize > 1048576) ? DEFAULT_SIZE : this.dataSize;
    }

    public void setDataSize(int i) {
        if (i <= 0 || i > 1048576) {
            this.dataSize = DEFAULT_SIZE;
        } else {
            this.dataSize = i;
        }
    }

    public float getAllSample() {
        if (this.allSample < 0.0f || this.allSample > 1.0f) {
            return 1.0f;
        }
        return this.allSample;
    }

    public void setAllSample(float f) {
        if (f < 0.0f || f > 1.0f) {
            this.allSample = 1.0f;
        } else {
            this.allSample = f;
        }
    }

    public boolean isUseOldLogic() {
        return this.useOldLogic;
    }

    public void setUseOldLogic(boolean z) {
        this.useOldLogic = z;
    }

    public int getMessageCount() {
        if (this.messageCount <= 0 || this.messageCount > 500) {
            return 50;
        }
        return this.messageCount;
    }

    public void setMessageCount(int i) {
        if (i <= 0 || i > 500) {
            this.messageCount = 50;
        } else {
            this.messageCount = i;
        }
    }

    private static class Holder {
        static final RestOrangeConfigure INSTANCE = new RestOrangeConfigure();

        private Holder() {
        }
    }
}