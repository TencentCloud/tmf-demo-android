package com.tencent.tmf.module.qapm.performance;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.HandlerThread;
import android.util.Log;
import com.tencent.tmf.common.BuildConfig;

/**
 * Created by toringzhang on 2017/12/15.
 */

public class GPSManager {

    private String logTAG = "GPSManager";
    private LocationManager mLocationManager;
    // flag for GPS status
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    HandlerThread ht;
    Location location; // location
    double latitude; // latitude
    double longitude; // longitud

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    private final Context mContext;

    public GPSManager(Context context) {
        this.mContext = context;
        this.ht = new HandlerThread("testgps");
        ht.start();
    }


    public Location getLocation() {
        if (BuildConfig.DEBUG) {
            Log.d("GPSManager", "getLocation");
        }
        if (Config.IS_FOR_PRIVACY_CHECK && (location != null || mLocationManager != null)) {
            return location;
        }
        try {
            mLocationManager = (LocationManager) mContext
                    .getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = mLocationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = mLocationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
                if (BuildConfig.DEBUG) {
                    Log.d("GPSManager", "cannot use gps.");
                }
            } else {
                this.canGetLocation = true;

                // First get location from Network Provider
                if (isNetworkEnabled) {

                    mLocationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener, ht.getLooper());
                    mLocationManager
                            .requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, ht.getLooper());
                    location = mLocationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        if (BuildConfig.DEBUG) {
                            Log.d(logTAG + "NETWORK", "latitude:" + latitude);
                        }
                        if (BuildConfig.DEBUG) {
                            Log.d(logTAG + "NETWORK", "longitude:" + longitude);
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    mLocationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener, ht.getLooper());
                    location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        if (BuildConfig.DEBUG) {
                            Log.d("GPSManager", "GPS Enabled");
                        }
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        if (BuildConfig.DEBUG) {
                            Log.d(logTAG + "GPS", "latitude:" + latitude);
                        }
                        if (BuildConfig.DEBUG) {
                            Log.d(logTAG + "GPS", "longitude:" + longitude);
                        }
                    }
                }
            }

        } catch (SecurityException e) {
            e.printStackTrace();
        }

        return location;
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (BuildConfig.DEBUG) {
                Log.d(logTAG, "onProviderDisabled.location = " + location);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (BuildConfig.DEBUG) {
                Log.d(logTAG, "onStatusChanged() called with " + "provider = [" + provider + "], status = [" + status
                        + "], extras = [" + extras + "]");
            }
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.i(logTAG, "AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.i(logTAG, "OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.i(logTAG, "TEMPORARILY_UNAVAILABLE");
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            if (BuildConfig.DEBUG) {
                Log.d(logTAG, "onProviderEnabled() called with " + "provider = [" + provider + "]");
            }
            try {
//                Location location = mLocationManager.getLastKnownLocation(provider);
                if (BuildConfig.DEBUG) {
                    Log.d(logTAG, "onProviderDisabled.location = " + location);
                }
            } catch (SecurityException e) {
                Log.d(logTAG, "onProviderDisabled.location err " + e);
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            if (BuildConfig.DEBUG) {
                Log.d(logTAG, "onProviderDisabled() called with " + "provider = [" + provider + "]");
            }
        }
    };

    public void stopLocationUpdates() {
        mLocationManager.removeUpdates(locationListener);
    }


}
