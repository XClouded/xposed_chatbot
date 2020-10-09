package com.ali.user.mobile.app.report.info;

import java.io.Serializable;
import java.util.Map;

public class Location implements Serializable {
    private static final long serialVersionUID = -3281452084459821133L;
    public double accuracy;
    public double altitude;
    public Map<String, String> extraInfos;
    public double latitude;
    public double longitude;
    public String os;
    public String source;
    public double speed;
}
