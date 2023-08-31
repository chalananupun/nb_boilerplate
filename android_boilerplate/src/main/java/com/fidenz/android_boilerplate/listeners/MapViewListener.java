package com.fidenz.android_boilerplate.listeners;

import com.google.android.gms.maps.GoogleMap;

public interface MapViewListener<X, T> {
    void onMapInitialized(X fragment, GoogleMap googleMap);

    void onMapClickListener(T location);

    void onCameraIdle();
}
