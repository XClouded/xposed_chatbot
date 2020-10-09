package com.airbnb.lottie.model;

public class Marker {
    private static String CARRIAGE_RETURN = "\r";
    public final float durationFrames;
    private final String name;
    public final float startFrame;

    public Marker(String str, float f, float f2) {
        this.name = str;
        this.durationFrames = f2;
        this.startFrame = f;
    }

    public boolean matchesName(String str) {
        if (this.name.equalsIgnoreCase(str)) {
            return true;
        }
        if (!this.name.endsWith(CARRIAGE_RETURN) || !this.name.substring(0, this.name.length() - 1).equalsIgnoreCase(str)) {
            return false;
        }
        return true;
    }
}
