package com.alibaba.taffy.core.util.lang;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import java.io.IOException;
import java.util.List;

public class LocationUtil {
    private static final String TAG = "LocationUtil";

    public static Location getLastLocation(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService("location");
        boolean isProviderEnabled = locationManager.isProviderEnabled("gps");
        boolean isProviderEnabled2 = locationManager.isProviderEnabled("network");
        if (isProviderEnabled) {
            return locationManager.getLastKnownLocation("gps");
        }
        if (isProviderEnabled2) {
            return locationManager.getLastKnownLocation("network");
        }
        return null;
    }

    public static String getLastCityName(Context context) {
        Location lastLocation = getLastLocation(context);
        if (lastLocation == null) {
            return null;
        }
        try {
            List<Address> fromLocation = new Geocoder(context).getFromLocation(lastLocation.getLatitude(), lastLocation.getLongitude(), 1);
            if (!ListUtil.isEmpty(fromLocation)) {
                return fromLocation.get(0).getLocality();
            }
            return null;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }
}
