package com.fidenz.android_boilerplate.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.fidenz.android_boilerplate.R;
import com.fidenz.android_boilerplate.listeners.MapViewListener;
import com.fidenz.android_boilerplate.utility.MapUtility;
import com.fidenz.android_boilerplate.views.fragments.MapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class CommonMapActivity extends AppCompatActivity {

    private RelativeLayout fragmentContainer;
    private static MapViewListener listener;
    private static MapFragment fragment;
    private MapUtility mapUtility;
    private static CommonMapActivity commonMapActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        commonMapActivity = this;
        mapUtility = new MapUtility(this);

        fragmentContainer = findViewById(R.id.fragmentContainer);
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.fragmentContainer, new MapFragment());
//        transaction.commit();
    }

    private boolean checkMapIsReady() {
        if (fragment != null) {
            return true;
        } else {
            mapUtility.showMapLoadingToast();
            return false;
        }
    }

    public static void initMap(Activity activity, MapViewListener<CommonMapActivity, LatLng> listener) {

        activity.startActivity(new Intent(activity, CommonMapActivity.class));
        CommonMapActivity.listener = listener;




        MapFragment.initMap(false,commonMapActivity.getSupportFragmentManager().beginTransaction(),R.id.fragmentContainer,new MapViewListener<MapFragment, LatLng>() {
            @Override
            public void onMapInitialized(MapFragment fragment, GoogleMap googleMap) {
                CommonMapActivity.fragment = fragment;
                CommonMapActivity.listener.onMapInitialized(commonMapActivity,googleMap);
            }

            @Override
            public void onMapClickListener(LatLng location) {
                CommonMapActivity.listener.onMapClickListener(location);
            }

            @Override
            public void onCameraIdle() {

            }
        });
    }

    public static void initMap(Activity activity,boolean isZoomControlsEnabled, boolean isAllGesturesEnabled, boolean isMyLocationEnabled, int minZoomLevel,int mapType, MapViewListener<CommonMapActivity, LatLng> listener) {

        activity.startActivity(new Intent(activity, CommonMapActivity.class));
        CommonMapActivity.listener = listener;


        MapFragment.initMap(false,commonMapActivity.getSupportFragmentManager().beginTransaction(),R.id.fragmentContainer,isZoomControlsEnabled,isAllGesturesEnabled,isMyLocationEnabled,minZoomLevel,mapType,new MapViewListener<MapFragment, LatLng>() {
            @Override
            public void onMapInitialized(MapFragment fragment, GoogleMap googleMap) {
                CommonMapActivity.fragment = fragment;
                CommonMapActivity.listener.onMapInitialized(commonMapActivity,googleMap);
            }

            @Override
            public void onMapClickListener(LatLng location) {
                CommonMapActivity.listener.onMapClickListener(location);
            }

            @Override
            public void onCameraIdle() {

            }
        });
    }

    public void addMaker(LatLng location, String pinName, boolean clearOldMakers) {
        if (checkMapIsReady()) {
            fragment.addMarker(location, pinName, clearOldMakers);
        }
    }

    public void addMaker(LatLng location, String pinName, int icon, boolean clearOldMakers) {
        if (checkMapIsReady()) {
            fragment.addMarker(location, pinName, icon, clearOldMakers);
        }
    }

    public void markArea(final List<LatLng> locationList, String hexColorCode, boolean clearOldMakers) {
        if (checkMapIsReady()) {
            fragment.markArea(locationList, hexColorCode, clearOldMakers);
        }
    }

    public void drawPath(LatLng startLocation, String startPinName, int startLocationIcon, LatLng endLocation, String endPinName, int endLocationIcon) {
        if (checkMapIsReady()) {
            fragment.drawPath(startLocation, startPinName, startLocationIcon, endLocation, endPinName, endLocationIcon);
        }
    }

    public void drawPath(LatLng startLocation, String startPinName, int startLocationIcon, LatLng endLocation, String endPinName, int endLocationIcon, List<LatLng> wayPoints) {
        if (checkMapIsReady()) {
            fragment.drawPath(startLocation, startPinName, startLocationIcon, endLocation, endPinName, endLocationIcon, wayPoints);
        }
    }

    public void drawPath(List<LatLng> locations, String startPinName, int startLocationIcon, String endPinName, int endLocationIcon) {
        if (checkMapIsReady()) {
            fragment.drawPath(locations, startPinName, startLocationIcon, endPinName, endLocationIcon);
        }
    }

    public void centerScreen(LatLng startPoint, LatLng endPoint,List<LatLng> wayPoints) {
        if (checkMapIsReady()) {
            fragment.centerScreen(startPoint, endPoint,wayPoints);
        }
    }
}