package com.fidenz.android_boilerplate.views.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.amalbit.trail.RouteOverlayView;
import com.arsy.maps_library.MapRipple;
import com.fidenz.android_boilerplate.R;
import com.fidenz.android_boilerplate.google_location.FetchURL;
import com.fidenz.android_boilerplate.google_location.MapAnimator;
import com.fidenz.android_boilerplate.listeners.MapViewListener;
import com.fidenz.android_boilerplate.listeners.OnDistanceFetchedListener;
import com.fidenz.android_boilerplate.listeners.TaskCompleteListeners;
import com.fidenz.android_boilerplate.models.IconSize;
import com.fidenz.android_boilerplate.utility.LogUtility;
import com.fidenz.android_boilerplate.utility.MapUtility;
import com.fidenz.android_boilerplate.utility.StringUtility;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.gson.Gson;

import java.util.List;


public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnCameraIdleListener, TaskCompleteListeners {

    private static SupportMapFragment mapFragment;
    protected static GoogleMap mMap;
    private static MapViewListener listener;
    private static MapFragment activity;
    private Marker mMarker;
    private CameraUpdate mCamera;
    private static OnMapReadyCallback callback;
    private MapUtility mapUtility;
    private LatLng startLocation, endLocation;
    private static boolean isZoomControlsEnabled, isAllGesturesEnabled, isMyLocationEnabled;
    private static int minZoomLevel = 5, mapType;
    private static final String TAG = "MapFragment";
    private static MapStyleOptions mapStyleOptions;
    private LatLng mCurrentPosition;

    private static final int defaultZoomLevel = 14;
    private static final float defaultZoomLevelForMarkerUpdate = 15f;
    private static final int focusDelay = 3000;
    private static RouteOverlayView routeOverlayView;
    private static boolean haveDisplayMargins;
    private OnDistanceFetchedListener onDistanceFetchedListener;
    private CameraUpdate lastKnownCameraUpdate;


    public MapFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtility.debug(TAG, "onCreate");
        activity = this;
        mapUtility = new MapUtility(getActivity());
        callback = this;
        try {
            MapsInitializer.initialize(getContext());
        } catch (Exception e) {
            LogUtility.error(TAG, e);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LogUtility.debug(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        routeOverlayView = view.findViewById(R.id.mapOverlayView);
        return view;
    }

    private boolean checkMapIsReady() {
        if (mMap != null) {
            return true;
        } else {
            mapUtility.showMapLoadingToast();
            return false;
        }
    }


    public void centerToCurrentLocation(LatLng latLng) {
        if (mMap != null && latLng != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16), 500, null);
        }
    }


    public void centerToCurrentLocation() {
        if (mMap != null && mCurrentPosition != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mCurrentPosition, 16), 500, null);
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtility.debug(TAG, "onActivityCreated");

//        //this is  for set default map style
//        mapStyleOptions = MapUtility.getMapStyle(getActivity(),);


        if (getActivity() != null) {
            mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//            mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

            if (mapFragment != null) {
                LogUtility.debug(TAG, "mapfragment not null");
                mapFragment.getMapAsync(this);
            } else {
                LogUtility.debug(TAG, "map fragment null");
            }
        }
    }

    public static void initMap(boolean haveDisplayMargins, FragmentTransaction transaction, int layout, MapViewListener<MapFragment, LatLng> listener) {
        transaction.replace(layout, new MapFragment());
        transaction.commit();
        MapFragment.isZoomControlsEnabled = true;
        MapFragment.isAllGesturesEnabled = true;
        MapFragment.isMyLocationEnabled = true;
        MapFragment.haveDisplayMargins = haveDisplayMargins;
        MapFragment.mapType = GoogleMap.MAP_TYPE_TERRAIN;
        MapFragment.listener = listener;
        LogUtility.debug(TAG, "initMap");
    }

    public static void initMap(boolean haveDisplayMargins, FragmentTransaction transaction, int layout, boolean isMyLocationEnabled, MapViewListener<MapFragment, LatLng> listener) {
        transaction.replace(layout, new MapFragment());
        transaction.commit();
        MapFragment.isZoomControlsEnabled = true;
        MapFragment.isAllGesturesEnabled = true;
        MapFragment.isMyLocationEnabled = isMyLocationEnabled;
        MapFragment.haveDisplayMargins = haveDisplayMargins;
        MapFragment.mapType = GoogleMap.MAP_TYPE_TERRAIN;
        MapFragment.listener = listener;
        LogUtility.debug(TAG, "initMap");
    }


    public static void initMapWithoutMyLocation(boolean haveDisplayMargins, FragmentTransaction transaction, int layout, MapViewListener<MapFragment, LatLng> listener) {
        transaction.replace(layout, new MapFragment());
        transaction.commit();
        MapFragment.isZoomControlsEnabled = true;
        MapFragment.isAllGesturesEnabled = true;
        MapFragment.isMyLocationEnabled = false;
        MapFragment.haveDisplayMargins = haveDisplayMargins;
        MapFragment.mapType = GoogleMap.MAP_TYPE_TERRAIN;
        MapFragment.listener = listener;
        LogUtility.debug(TAG, "initMap");
    }

    public static void initMapWithoutZoomControls(boolean haveDisplayMargins, FragmentTransaction transaction, int layout, int minZoomLevel, MapViewListener<MapFragment, LatLng> listener) {
        transaction.replace(layout, new MapFragment());
        transaction.commit();
        MapFragment.isZoomControlsEnabled = false;
        MapFragment.isAllGesturesEnabled = true;
        MapFragment.isMyLocationEnabled = true;
        MapFragment.haveDisplayMargins = haveDisplayMargins;
        MapFragment.minZoomLevel = minZoomLevel;
        MapFragment.mapType = GoogleMap.MAP_TYPE_TERRAIN;
        MapFragment.listener = listener;
        LogUtility.debug(TAG, "initMap");
    }

    public static void initMap(boolean haveDisplayMargins, FragmentTransaction transaction, int layout, boolean enableGestures, MapStyleOptions mapStyleOptions, MapViewListener<MapFragment, LatLng> listener) {
        //map view
        transaction.replace(layout, new MapFragment());
        transaction.commit();
        MapFragment.haveDisplayMargins = haveDisplayMargins;
        MapFragment.isZoomControlsEnabled = enableGestures;
        MapFragment.isAllGesturesEnabled = enableGestures;
        MapFragment.isMyLocationEnabled = false;
        MapFragment.listener = listener;
        MapFragment.mapStyleOptions = mapStyleOptions;
        LogUtility.debug(TAG, "initMap");
    }


    public static void initMap(boolean haveDisplayMargins, FragmentTransaction transaction, int layout, boolean enableGestures, boolean isMyLocationEnabled, MapStyleOptions mapStyleOptions, MapViewListener<MapFragment, LatLng> listener) {
        //map view
        transaction.replace(layout, new MapFragment());
        transaction.commit();
        MapFragment.haveDisplayMargins = haveDisplayMargins;
        MapFragment.isZoomControlsEnabled = enableGestures;
        MapFragment.isAllGesturesEnabled = enableGestures;
        MapFragment.isMyLocationEnabled = isMyLocationEnabled;
        MapFragment.listener = listener;
        MapFragment.mapStyleOptions = mapStyleOptions;
        LogUtility.debug(TAG, "initMap");
    }

    public static void initMap(boolean haveDisplayMargins, FragmentTransaction transaction, int layout, boolean enableGestures, boolean isMyLocationEnabled, MapViewListener<MapFragment, LatLng> listener) {
        //map view
        transaction.replace(layout, new MapFragment());
        transaction.commit();
        MapFragment.haveDisplayMargins = haveDisplayMargins;
        MapFragment.isZoomControlsEnabled = enableGestures;
        MapFragment.isAllGesturesEnabled = enableGestures;
        MapFragment.mapType = GoogleMap.MAP_TYPE_TERRAIN;
        MapFragment.isMyLocationEnabled = isMyLocationEnabled;
        MapFragment.listener = listener;
        LogUtility.debug(TAG, "initMap");
    }

    public static void initMap(boolean haveDisplayMargins, FragmentTransaction transaction, int layout, boolean enableGestures, boolean enableZoomControls, boolean isMyLocationEnabled, MapStyleOptions mapStyleOptions, MapViewListener<MapFragment, LatLng> listener) {
        //map view
        transaction.replace(layout, new MapFragment());
        transaction.commit();
        MapFragment.haveDisplayMargins = haveDisplayMargins;
        MapFragment.isZoomControlsEnabled = enableZoomControls;
        MapFragment.isAllGesturesEnabled = enableGestures;
        MapFragment.isMyLocationEnabled = isMyLocationEnabled;
        MapFragment.listener = listener;
        MapFragment.mapStyleOptions = mapStyleOptions;
        LogUtility.debug(TAG, "initMap");
    }

    public static void initMap(boolean haveDisplayMargins, FragmentTransaction transaction, int layout, boolean isZoomControlsEnabled, boolean isAllGesturesEnabled, boolean isMyLocationEnabled, int minZoomLevel, int mapType, MapViewListener<MapFragment, LatLng> listener) {
        transaction.replace(layout, new MapFragment());
        transaction.commit();
        MapFragment.haveDisplayMargins = haveDisplayMargins;
        MapFragment.isZoomControlsEnabled = isZoomControlsEnabled;
        MapFragment.isAllGesturesEnabled = isAllGesturesEnabled;
        MapFragment.isMyLocationEnabled = isMyLocationEnabled;
        MapFragment.minZoomLevel = minZoomLevel;
        MapFragment.listener = listener;
        MapFragment.mapType = mapType;
        LogUtility.debug(TAG, "initMap");
    }


    public void startAnimation(LatLng latLng, int icon, Context context,int color,String markerText) {

        mMap.clear();

        mCurrentPosition = latLng;

        MapRipple mapRipple = new MapRipple(mMap, latLng, context);
        mapRipple.withNumberOfRipples(2);
        mapRipple.withFillColor(color);
        //  mapRipple.withStrokeColor(activity.getResources().getColor(R.color.colorPrimary));
        mapRipple.withStrokewidth(0);      // 10dp
        mapRipple.withDistance(1);      // 2000 metres radius
        mapRipple.withRippleDuration(5000);    //12000ms
        mapRipple.withTransparency(0.3f);

        mapRipple.startRippleMapAnimation();
        addMarker(latLng, markerText, icon, true);
        //  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, defaultZoomLevel), focusDelay, null);
    }


    public void addMarker(LatLng location, String pinName, boolean clearMakers) {
        if (checkMapIsReady()) {
            if (clearMakers) {
                mMap.clear();
            }
            mMap.addMarker(new MarkerOptions().position(location).title(pinName));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, defaultZoomLevel), focusDelay, null);
        }
    }


    public Marker addMarker(LatLng location, int icon, boolean clearMakers) {
        if (checkMapIsReady()) {
            if (clearMakers) {
                mMap.clear();
            }

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, defaultZoomLevel), focusDelay, null);
            return mMap.addMarker(mapUtility.createMarker(location, "", icon));
        }
        return null;
    }

    public void addMarkerFromRes(LatLng location, String icon, boolean clearMakers) {
        if (checkMapIsReady()) {
            if (clearMakers) {
                mMap.clear();
            }

            mMap.addMarker(mapUtility.createMarkerForCircle(location, "", icon));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 16), focusDelay, null);
        }
    }

    public void addMarkerFromRes(LatLng location, String icon, boolean clearMakers, int focusDelay) {
        if (checkMapIsReady()) {
            if (clearMakers) {
                mMap.clear();
            }

            mMap.addMarker(mapUtility.createMarkerForCircle(location, "", icon));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 16), focusDelay, null);
        }
    }

    public void addMarkerFromRes(LatLng location, String icon, boolean clearMakers, int focusDelay, int minZoomLevel) {
        if (checkMapIsReady()) {
            if (clearMakers) {
                mMap.clear();
            }

            mMap.addMarker(mapUtility.createMarkerForCircle(location, "", icon));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, minZoomLevel), focusDelay, null);
        }
    }

    public void addMarkerFromResWithoutMoveAnimation(LatLng location, String icon, boolean clearMakers) {
        if (checkMapIsReady()) {
            if (clearMakers) {
                mMap.clear();
            }

            mMap.addMarker(mapUtility.createMarkerForCircle(location, "", icon));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 16), 100, null);
        }
    }


    public void addMarker(LatLng location, String pinName, int icon, boolean clearMarkers) {

        if (checkMapIsReady()) {


            if (clearMarkers) {
                mMap.clear();
            }
            //update driver marker
            mMap.addMarker(mapUtility.createMarker(location, pinName, icon));
            mCamera = CameraUpdateFactory.newLatLngZoom(location, defaultZoomLevel);
            mMap.moveCamera(mCamera);
        }
    }


    public void addMarker(LatLng location, String pinName, int icon, boolean clearMarkers, IconSize iconSize) {

        if (checkMapIsReady()) {


            if (clearMarkers) {
                mMap.clear();
            }
            //update driver marker
            mMap.addMarker(mapUtility.createMarker(location, pinName, icon, iconSize));
            mCamera = CameraUpdateFactory.newLatLngZoom(location, defaultZoomLevel);
            mMap.moveCamera(mCamera);
        }
    }

    public void moveCamera(LatLng location, int zoomLevel) {
        mCamera = CameraUpdateFactory.newLatLngZoom(location, zoomLevel);
        mMap.moveCamera(mCamera);
    }

    public void moveCamera(LatLng location, int zoomLevel, int focusDelay) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel), focusDelay, null);
    }

    public void moveCamera(LatLng location) {
        mCamera = CameraUpdateFactory.newLatLngZoom(location, defaultZoomLevel);
        mMap.moveCamera(mCamera);
    }


    public void addUrlMarker(LatLng location, String pinName, String imageUrl, boolean clearMarkers,int placeholderIcon,int borderColor) {

        if (checkMapIsReady()) {


            if (clearMarkers) {
                mMap.clear();
            }
            //update driver marker

            try {
                //load image

//                mMap.addMarker(mapUtility.createWebUrlMarker(location, pinName, imageUrl));


                if (StringUtility.isNotNull(imageUrl)) {
                    mMap.addMarker(mapUtility.createWebUrlMarker(location, pinName, imageUrl, new IconSize(80, 80),borderColor));
                } else {
                    mMap.addMarker(mapUtility.createLocalCircularMarker(location, pinName, placeholderIcon, new IconSize(80, 80),borderColor));
                }

//                MarkerOptions markerOptions2 = new MarkerOptions()
//                        .position(location)
//                        .title(pinName)
//                        // .anchor(0,0)
//                        .icon(BitmapDescriptorFactory.fromBitmap(circular1));
//
//                mMap.addMarker(markerOptions2);

                mCamera = CameraUpdateFactory.newLatLngZoom(location, defaultZoomLevel);
                mMap.moveCamera(mCamera);
            } catch (Exception e) {
//                e.printStackTrace();
                //image fetching issue
                mMap.addMarker(mapUtility.createLocalCircularMarker(location, pinName, placeholderIcon, new IconSize(80, 80),borderColor));
                mCamera = CameraUpdateFactory.newLatLngZoom(location, defaultZoomLevel);
                mMap.moveCamera(mCamera);
            }
        }
    }


    public void addCircularMarker(LatLng location, String pinName, int icon, boolean clearMarkers, IconSize iconSize,int borderColor) {
        if (checkMapIsReady()) {

            if (clearMarkers) {
                mMap.clear();
            }
            //update driver marker
            mMap.addMarker(mapUtility.createLocalCircularMarker(location, pinName, icon, iconSize,borderColor));

//                MarkerOptions markerOptions2 = new MarkerOptions()
//                        .position(location)
//                        .title(pinName)
//                        // .anchor(0,0)
//                        .icon(BitmapDescriptorFactory.fromBitmap(circular1));
//
//                mMap.addMarker(markerOptions2);

            mCamera = CameraUpdateFactory.newLatLngZoom(location, defaultZoomLevel);
            mMap.moveCamera(mCamera);

        }
    }

    public void addCircularMarker(LatLng location, String pinName, int icon, boolean clearMarkers,int borderColor) {
        if (checkMapIsReady()) {

            if (clearMarkers) {
                mMap.clear();
            }
            //update driver marker
            mMap.addMarker(mapUtility.createLocalCircularMarker(location, pinName, icon, new IconSize(80, 80),borderColor));

//                MarkerOptions markerOptions2 = new MarkerOptions()
//                        .position(location)
//                        .title(pinName)
//                        // .anchor(0,0)
//                        .icon(BitmapDescriptorFactory.fromBitmap(circular1));
//
//                mMap.addMarker(markerOptions2);

            mCamera = CameraUpdateFactory.newLatLngZoom(location, defaultZoomLevel);
            mMap.moveCamera(mCamera);

        }
    }

    public void addUrlMarker(LatLng location, String pinName, String imageUrl, boolean clearMarkers, IconSize iconSize,int borderColor) {

        if (checkMapIsReady()) {


            if (clearMarkers) {
                mMap.clear();
            }
            //update driver marker

            try {
                //load image

                mMap.addMarker(mapUtility.createWebUrlMarker(location, pinName, imageUrl, iconSize,borderColor));

//                MarkerOptions markerOptions2 = new MarkerOptions()
//                        .position(location)
//                        .title(pinName)
//                        // .anchor(0,0)
//                        .icon(BitmapDescriptorFactory.fromBitmap(circular1));
//
//                mMap.addMarker(markerOptions2);

                mCamera = CameraUpdateFactory.newLatLngZoom(location, defaultZoomLevel);
                mMap.moveCamera(mCamera);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateCurrentLocation(LatLng location, int icon) {
        addMarker(location, "", icon, true);
    }


    public void markArea(final List<LatLng> locationList, String hexColorCode, boolean clearOldMakers) {

        if (checkMapIsReady()) {

            if (clearOldMakers) {
                mMap.clear();
            }

            int color = mapUtility.colorConverter(hexColorCode);
            mMap.addPolygon(new PolygonOptions()
                    .addAll(locationList)
                    .strokeColor(color)
                    .strokeWidth(5)
                    .fillColor(color));

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(MapUtility.getCenterPoint(locationList), defaultZoomLevel), focusDelay, null);

                }
            }, 500);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            LogUtility.debug(TAG, "onMapReady > permission denied");
            return;
        } else {
            LogUtility.debug(TAG, "onMapReady > permission granted");
        }

        LogUtility.debug(TAG, "onMapReady");
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(isZoomControlsEnabled);
        mMap.getUiSettings().setAllGesturesEnabled(isAllGesturesEnabled);
        mMap.setMyLocationEnabled(isMyLocationEnabled);


        mMap.setMinZoomPreference(minZoomLevel);
        if (mapStyleOptions != null) {
            mMap.setMapStyle(mapStyleOptions);
        }


        mMap.setOnCameraIdleListener(this);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                listener.onMapClickListener(latLng);
            }
        });
        listener.onMapInitialized(activity, googleMap);
    }

    @Override
    public void onCameraIdle() {
        listener.onCameraIdle();
    }

    public void drawPath(LatLng startLocation, String startPinName, int startLocationIcon, LatLng endLocation, String endPinName, int endLocationIcon) {

        if (checkMapIsReady()) {
            mMap.clear();
            this.startLocation = startLocation;
            this.endLocation = endLocation;

            mMap.addMarker(mapUtility.createMarker(startLocation, startPinName, startLocationIcon));
            mMap.addMarker(mapUtility.createMarker(endLocation, endPinName, endLocationIcon));

            String url = mapUtility.getDirectionsUrl(startLocation, endLocation, null, "driving");
            new FetchURL(this).execute(url, "walking");
        }

    }
//    public void drawPath(List<LatLng> wayPoints,LatLng startLocation, String startPinName, int startLocationIcon, LatLng endLocation, String endPinName, int endLocationIcon) {
//
//        if (checkMapIsReady()) {
//            mMap.clear();
//            this.startLocation = startLocation;
//            this.endLocation = endLocation;
//
//            mMap.addMarker(mapUtility.createMarker(startLocation, startPinName, startLocationIcon));
//            mMap.addMarker(mapUtility.createMarker(endLocation, endPinName, endLocationIcon));
//
//            String url = mapUtility.getDirectionsUrl(startLocation, endLocation, wayPoints, "driving");
//            new FetchURL(this).execute(url, "walking");
//        }
//
//    }


//    public Marker drawPath(LatLng startLocation, String startPinName, List<LatLng> wayPoints, int startLocationIcon, LatLng endLocation, String endPinName, int endLocationIcon) {
//
//        if (checkMapIsReady()) {
//            mMap.clear();
//            this.startLocation = startLocation;
//            this.endLocation = endLocation;
//
//            mMap.addMarker(mapUtility.createMarker(startLocation, startPinName, startLocationIcon));
//            //   mMap.addMarker(mapUtility.createMarker(endLocation, endPinName, endLocationIcon));
//
//            Marker destination = mMap.addMarker(mapUtility.createMarker(endLocation, endPinName, endLocationIcon));
//
//            String url = mapUtility.getDirectionsUrl(startLocation, endLocation, wayPoints, "driving");
//            new FetchURL(this).execute(url, "walking");
//
//            return destination;
//        }
//
//        return null;
//
//    }

    public Marker drawPathReturnStartMarker(LatLng startLocation, float startIconBearing, String startPinName, List<LatLng> wayPoints, int startLocationIcon, LatLng endLocation, String endPinName, int endLocationIcon) {

        if (checkMapIsReady()) {
            mMap.clear();
            this.startLocation = startLocation;
            this.endLocation = endLocation;

            Marker destination = mMap.addMarker(mapUtility.createMarkerWithBearing(startLocation, startPinName, startLocationIcon, startIconBearing));
            //   mMap.addMarker(mapUtility.createMarker(endLocation, endPinName, endLocationIcon));

            mMap.addMarker(mapUtility.createMarker(endLocation, endPinName, endLocationIcon));


            //waypoints add
//            if((wayPoints!=null) && (wayPoints.size()!=0)){
//                for (LatLng wayPoint : wayPoints) {
//                    if((startLocation.latitude!=wayPoint.latitude) && (startLocation.longitude!=wayPoint.longitude)){
//                        mMap.addMarker(mapUtility.createMarker(endLocation, "", R.drawable.ic_destination));
//                    }
//                }
//            }

            String url = mapUtility.getDirectionsUrl(startLocation, endLocation, wayPoints, "driving");
            new FetchURL(this).execute(url, "walking");

            return destination;
        }

        return null;

    }

    public Marker drawPathReturnStartMarker(LatLng startLocation, float startIconBearing, String startPinName, List<LatLng> wayPoints, int wayPointIcon, int startLocationIcon, LatLng endLocation, String endPinName, int endLocationIcon) {

        if (checkMapIsReady()) {
            mMap.clear();
            this.startLocation = startLocation;
            this.endLocation = endLocation;

            Marker destination = mMap.addMarker(mapUtility.createMarkerWithBearing(startLocation, startPinName, startLocationIcon, startIconBearing));
            //   mMap.addMarker(mapUtility.createMarker(endLocation, endPinName, endLocationIcon));

            mMap.addMarker(mapUtility.createMarker(endLocation, endPinName, endLocationIcon));


            //waypoints add
            if ((wayPoints != null) && (wayPoints.size() != 0)) {
                for (LatLng wayPoint : wayPoints) {
                    if ((startLocation.latitude != wayPoint.latitude) && (startLocation.longitude != wayPoint.longitude)) {
                        mMap.addMarker(mapUtility.createMarker(wayPoint, "", wayPointIcon));
                    }
                }
            }

            String url = mapUtility.getDirectionsUrl(startLocation, endLocation, wayPoints, "driving");
            new FetchURL(this).execute(url, "walking");

            return destination;
        }

        return null;

    }


    public Marker drawPathReturnStartMarker(LatLng startLocation, float startIconBearing, String startPinName, List<LatLng> wayPoints, int startLocationIcon, LatLng endLocation, String endPinName, int endLocationIcon, IconSize iconSize) {

        if (checkMapIsReady()) {
            mMap.clear();
            this.startLocation = startLocation;
            this.endLocation = endLocation;

            Marker destination = mMap.addMarker(mapUtility.createMarkerWithBearing(startLocation, startPinName, startLocationIcon, startIconBearing));
            //   mMap.addMarker(mapUtility.createMarker(endLocation, endPinName, endLocationIcon));

            mMap.addMarker(mapUtility.createMarker(endLocation, endPinName, endLocationIcon, iconSize));

            //waypoints add
//           if((wayPoints!=null) && (wayPoints.size()!=0)){
//                for (LatLng wayPoint : wayPoints) {
//                    if((endLocation.latitude!=wayPoint.latitude) && (endLocation.longitude!=wayPoint.longitude)){
//                        mMap.addMarker(mapUtility.createMarker(endLocation, "", R.drawable.ic_destination));
//                    }
//                }
//            }

            String url = mapUtility.getDirectionsUrl(startLocation, endLocation, wayPoints, "driving");
            new FetchURL(this).execute(url, "walking");

            return destination;
        }

        return null;

    }


    public Marker drawPathForTrackingEndCircular(LatLng startLocation, float startIconBearing, String startPinName,
                                                 List<LatLng> wayPoints, int startLocationIcon, LatLng endLocation,
                                                 String endPinName, String endImageUrl, IconSize iconSize,int placeholder,int borderColor) {

        if (checkMapIsReady()) {
            mMap.clear();
            this.startLocation = startLocation;
            this.endLocation = endLocation;

            Marker start = mMap.addMarker(mapUtility.createMarkerWithBearing(startLocation, startPinName, startLocationIcon, startIconBearing));
            //   mMap.addMarker(mapUtility.createMarker(endLocation, endPinName, endLocationIcon));

            if (StringUtility.isNotNull(endImageUrl)) {
                mMap.addMarker(mapUtility.createWebUrlMarker(endLocation, endPinName, endImageUrl, iconSize,borderColor));
            } else {
                mMap.addMarker(mapUtility.createLocalCircularMarker(endLocation, endPinName, placeholder, iconSize,borderColor));
            }

            //waypoints add
//           if((wayPoints!=null) && (wayPoints.size()!=0)){
//                for (LatLng wayPoint : wayPoints) {
//                    if((endLocation.latitude!=wayPoint.latitude) && (endLocation.longitude!=wayPoint.longitude)){
//                        mMap.addMarker(mapUtility.createMarker(endLocation, "", R.drawable.ic_destination));
//                    }
//                }
//            }

            String url = mapUtility.getDirectionsUrl(startLocation, endLocation, wayPoints, "driving");
            new FetchURL(this).execute(url, "walking");

            return start;
        }

        return null;

    }


    public void drawPathForTracking(LatLng startLocation,
                                    LatLng endLocation) {

        if (checkMapIsReady()) {

            this.startLocation = startLocation;
            this.endLocation = endLocation;

            LogUtility.debug(TAG, "drawPathForTracking :" + new Gson().toJson(startLocation));

            String url = mapUtility.getDirectionsUrl(startLocation, endLocation, null, "driving");
            new FetchURL(this).execute(url, "walking");


        }


    }


    public Marker drawPathForTrackingBothCircular(LatLng startLocation, float startIconBearing, String startPinName,
                                                  List<LatLng> wayPoints, String startImageUrl, LatLng endLocation,
                                                  String endPinName, String endImageUrl, IconSize iconSize,int placeholder,int borderColor) {

        if (checkMapIsReady()) {
            mMap.clear();
            this.startLocation = startLocation;
            this.endLocation = endLocation;

            Marker start = mMap.addMarker(mapUtility.createWebUrlMarker(startLocation, startPinName, startImageUrl, startIconBearing,borderColor));
            //   mMap.addMarker(mapUtility.createMarker(endLocation, endPinName, endLocationIcon));

            if (StringUtility.isNotNull(startImageUrl)) {
                mMap.addMarker(mapUtility.createWebUrlMarker(startLocation, startPinName, startImageUrl, iconSize,borderColor));
            } else {
                mMap.addMarker(mapUtility.createLocalCircularMarker(startLocation, startPinName,placeholder, iconSize,borderColor));
            }

            if (StringUtility.isNotNull(endImageUrl)) {
                mMap.addMarker(mapUtility.createWebUrlMarker(endLocation, endPinName, endImageUrl, iconSize,borderColor));
            } else {
                mMap.addMarker(mapUtility.createLocalCircularMarker(endLocation, endPinName, placeholder, iconSize,borderColor));
            }

            //waypoints add
//           if((wayPoints!=null) && (wayPoints.size()!=0)){
//                for (LatLng wayPoint : wayPoints) {
//                    if((endLocation.latitude!=wayPoint.latitude) && (endLocation.longitude!=wayPoint.longitude)){
//                        mMap.addMarker(mapUtility.createMarker(endLocation, "", R.drawable.ic_destination));
//                    }
//                }
//            }

            String url = mapUtility.getDirectionsUrl(startLocation, endLocation, wayPoints, "driving");
            new FetchURL(this).execute(url, "walking");

            return start;
        }

        return null;

    }

    public Marker drawPathForTrackingStartCircular(LatLng startLocation, float startIconBearing, String startPinName,
                                                   List<LatLng> wayPoints, String startImageUrl, LatLng endLocation,
                                                   String endPinName, int endIcon, IconSize iconSize,int placeholder,int borderColor) {

        if (checkMapIsReady()) {
            mMap.clear();
            this.startLocation = startLocation;
            this.endLocation = endLocation;

            if (StringUtility.isNotNull(startImageUrl)) {
                mMap.addMarker(mapUtility.createWebUrlMarker(startLocation, startPinName, startImageUrl, iconSize,borderColor));
            } else {
                mMap.addMarker(mapUtility.createLocalCircularMarker(startLocation, startPinName,placeholder, iconSize,borderColor));
            }

            Marker start = mMap.addMarker(mapUtility.createMarker(endLocation, endPinName, endIcon));
            //   mMap.addMarker(mapUtility.createMarker(endLocation, endPinName, endLocationIcon));


            //waypoints add
//           if((wayPoints!=null) && (wayPoints.size()!=0)){
//                for (LatLng wayPoint : wayPoints) {
//                    if((endLocation.latitude!=wayPoint.latitude) && (endLocation.longitude!=wayPoint.longitude)){
//                        mMap.addMarker(mapUtility.createMarker(endLocation, "", R.drawable.ic_destination));
//                    }
//                }
//            }

            String url = mapUtility.getDirectionsUrl(startLocation, endLocation, wayPoints, "driving");
            new FetchURL(this).execute(url, "walking");

            return start;
        }

        return null;

    }


    public Marker drawPathReturnStartMarker(LatLng startLocation, float startIconBearing, String startPinName, List<LatLng> wayPoints, String startImageUrl,
                                            LatLng endLocation, String endPinName, String endImageUrl, IconSize iconSize,int borderColor) {

        if (checkMapIsReady()) {
            mMap.clear();
            this.startLocation = startLocation;
            this.endLocation = endLocation;

            Marker destination = mMap.addMarker(mapUtility.createWebUrlMarker(startLocation, startPinName, startImageUrl, startIconBearing,borderColor));
            mMap.addMarker(mapUtility.createWebUrlMarker(endLocation, endPinName, endImageUrl, iconSize,borderColor));

            String url = mapUtility.getDirectionsUrl(startLocation, endLocation, wayPoints, "driving");
            new FetchURL(this).execute(url, "walking");

            return destination;
        }

        return null;

    }


    public void drawPath(LatLng startLocation, String startPinName, List<LatLng> wayPoints, int startLocationIcon, LatLng endLocation,
                         String endPinName, int endLocationIcon,int wayPointIcon) {

        if (checkMapIsReady()) {
            mMap.clear();
            this.startLocation = startLocation;
            this.endLocation = endLocation;

            Marker destination = mMap.addMarker(mapUtility.createMarker(startLocation, startPinName, startLocationIcon));
            //   mMap.addMarker(mapUtility.createMarker(endLocation, endPinName, endLocationIcon));

            mMap.addMarker(mapUtility.createMarker(endLocation, endPinName, endLocationIcon));
            if ((wayPoints != null) && (wayPoints.size() != 0)) {
                for (LatLng wayPoint : wayPoints) {
                    if ((endLocation.latitude != wayPoint.latitude) && (endLocation.longitude != wayPoint.longitude)) {
                        mMap.addMarker(mapUtility.createMarker(endLocation, "", wayPointIcon));
                    }
                }
            }


            String url = mapUtility.getDirectionsUrl(startLocation, endLocation, wayPoints, "driving");
            new FetchURL(this).execute(url, "walking");

        }

    }


    public Marker drawPathReturnDestinationMaker(LatLng startLocation, String startPinName, List<LatLng> wayPoints, int startLocationIcon, LatLng endLocation, String endPinName, int endLocationIcon) {

        if (checkMapIsReady()) {
            mMap.clear();
            this.startLocation = startLocation;
            this.endLocation = endLocation;

            mMap.addMarker(mapUtility.createMarker(startLocation, startPinName, startLocationIcon));
            //   mMap.addMarker(mapUtility.createMarker(endLocation, endPinName, endLocationIcon));

            Marker destination = mMap.addMarker(mapUtility.createMarker(endLocation, endPinName, endLocationIcon));

            String url = mapUtility.getDirectionsUrl(startLocation, endLocation, wayPoints, "driving");
            new FetchURL(this).execute(url, "walking");

            return destination;
        }

        return null;

    }

    public void drawPath(Context context, LatLng startLocation, String startPinName, int startLocationIcon, LatLng endLocation, String endPinName, int endLocationIcon) {

        if (checkMapIsReady()) {
            mMap.clear();
            this.startLocation = startLocation;
            this.endLocation = endLocation;

            mMap.addMarker(mapUtility.createMarker(startLocation, startPinName, startLocationIcon));
            mMap.addMarker(mapUtility.createMarker(endLocation, endPinName, endLocationIcon));

            String url = mapUtility.getDirectionsUrl(startLocation, endLocation, null, "driving");
            new FetchURL(this).execute(url, "walking");
        }

    }

//    public void drawPath(Context context, LatLng startLocation, String startPinName, int startLocationIcon, LatLng endLocation, String endPinName, int endLocationIcon, List<LatLng> wayPoints) {
//
//        if (checkMapIsReady()) {
//            mMap.clear();
//            this.startLocation = startLocation;
//            this.endLocation = endLocation;
//
//            mMap.addMarker(mapUtility.createMarker(startLocation, startPinName, startLocationIcon));
//            mMap.addMarker(mapUtility.createMarker(endLocation, endPinName, endLocationIcon));
//
//            if ((wayPoints != null) && (wayPoints.size() > 0)) {
//                for (LatLng wayPoint : wayPoints) {
//                    mMap.addMarker(mapUtility.createMarker(wayPoint,"",R.drawable.ic_destination));
//                }
//            }
//
//            String url = mapUtility.getDirectionsUrl(startLocation, endLocation, wayPoints, "driving");
//            new FetchURL(this).execute(url, "walking");
//        }
//
//    }

//    public void drawPath(Context context, LatLng startLocation, String startPinName, int startLocationIcon, LatLng endLocation, String endPinName, int endLocationIcon, List<SavedLocation> wayPoints) {
//
//        if (checkMapIsReady()) {
//            mMap.clear();
//            this.startLocation = startLocation;
//            this.endLocation = endLocation;
//
//            mMap.addMarker(mapUtility.createMarker(startLocation, startPinName, startLocationIcon));
//            mMap.addMarker(mapUtility.createMarker(endLocation, endPinName, endLocationIcon));
//
//            List<LatLng> points = new ArrayList<>();
//
//            if ((wayPoints != null) && (wayPoints.size() > 0)) {
//                for (SavedLocation wayPoint : wayPoints) {
//                    points.add(wayPoint.getLatLng());
//                    mMap.addMarker(mapUtility.createMarker(wayPoint.getLatLng(), wayPoint.getLocationText(), R.drawable.ic_destination));
//                }
//            }
//
//
//            String url = mapUtility.getDirectionsUrl(startLocation, endLocation, points, "driving");
//            new FetchURL(this).execute(url, "walking");
//        }
//
//    }


//    public void drawPath(Context context, LatLng startLocation, String startPinName, String startLocationIcon, LatLng endLocation, String endPinName, String endLocationIcon) {
//
//        if (checkMapIsReady()) {
//            mMap.clear();
//            this.startLocation = startLocation;
//            this.endLocation = endLocation;
//
//            mMap.addMarker(mapUtility.createMarkerForCircle(startLocation, startPinName, startLocationIcon));
//            mMap.addMarker(mapUtility.createMarkerForCircle(endLocation, endPinName, endLocationIcon));
//
//            String url = mapUtility.getDirectionsUrl(startLocation, endLocation, null, "driving");
//            new FetchURL(this).execute(url, "walking");
//        }
//
//    }

    public void drawPath(LatLng startLocation, String startPinName, int startLocationIcon, IconSize startIconSize, LatLng endLocation, String endPinName, int endLocationIcon, IconSize endIconSize) {

        if (checkMapIsReady()) {
            mMap.clear();
            this.startLocation = startLocation;
            this.endLocation = endLocation;

            mMap.addMarker(mapUtility.createMarker(startLocation, startPinName, startLocationIcon, startIconSize));
            mMap.addMarker(mapUtility.createMarker(endLocation, endPinName, endLocationIcon, endIconSize));
            String url = mapUtility.getDirectionsUrl(startLocation, endLocation, null, "driving");
            new FetchURL(this).execute(url, "walking");
        }

    }

    public void drawPath(LatLng startLocation, LatLng endLocation) {

        if (checkMapIsReady()) {
            mMap.clear();
            this.startLocation = startLocation;
            this.endLocation = endLocation;

            String url = mapUtility.getDirectionsUrl(startLocation, endLocation, null, "driving");
            new FetchURL(this).execute(url, "walking");
        }

    }

    public void drawPath(LatLng startLocation, LatLng endLocation, List<LatLng> wayPoint) {

        if (checkMapIsReady()) {
            mMap.clear();
            this.startLocation = startLocation;
            this.endLocation = endLocation;

            String url = mapUtility.getDirectionsUrl(startLocation, endLocation, wayPoint, "driving");
            LogUtility.debug(TAG, "WayPoint Req = " + url);
            new FetchURL(this).execute(url, "walking");
        }

    }


    public void drawPath(Context context, LatLng startLocation, String startPinName, String startLocationIcon, LatLng endLocation, String endPinName, int endLocationIcon) {

        if (checkMapIsReady()) {
            mMap.clear();
            this.startLocation = startLocation;
            this.endLocation = endLocation;

            mMap.addMarker(mapUtility.createMarkerForCircle(startLocation, startPinName, startLocationIcon));
            mMap.addMarker(mapUtility.createMarker(endLocation, endPinName, endLocationIcon));

            String url = mapUtility.getDirectionsUrl(startLocation, endLocation, null, "driving");
            new FetchURL(this).execute(url, "walking");
        }

    }


    public void drawPath(Context context, LatLng startLocation, String startPinName, int startLocationIcon, LatLng endLocation, String endPinName, String endLocationIcon) {

        if (checkMapIsReady()) {
            mMap.clear();
            this.startLocation = startLocation;
            this.endLocation = endLocation;

            mMap.addMarker(mapUtility.createMarker(startLocation, startPinName, startLocationIcon));
            mMap.addMarker(mapUtility.createMarkerForCircle(endLocation, endPinName, endLocationIcon));

            String url = mapUtility.getDirectionsUrl(startLocation, endLocation, null, "driving");
            new FetchURL(this).execute(url, "walking");
        }

    }

    public void drawPath(Context context, LatLng startLocation, String startPinName, String startLocationIcon, LatLng endLocation, String endPinName, String endLocationIcon) {

        if (checkMapIsReady()) {
            mMap.clear();
            this.startLocation = startLocation;
            this.endLocation = endLocation;

            mMap.addMarker(mapUtility.createMarkerForCircle(startLocation, startPinName, startLocationIcon));
            mMap.addMarker(mapUtility.createMarkerForCircle(endLocation, endPinName, endLocationIcon));


            String url = mapUtility.getDirectionsUrl(startLocation, endLocation, null, "driving");
            new FetchURL(this).execute(url, "walking");
        }

    }

    public void drawPath(Context context, LatLng startLocation, String startPinName, String startLocationIcon, IconSize startIconSize, LatLng endLocation, String endPinName, String endLocationIcon, IconSize endIconSize) {

        if (checkMapIsReady()) {
            mMap.clear();
            this.startLocation = startLocation;
            this.endLocation = endLocation;

            mMap.addMarker(mapUtility.createMarkerForCircle(startLocation, startPinName, startLocationIcon, startIconSize.getWidth(), startIconSize.getHeight()));
            mMap.addMarker(mapUtility.createMarkerForCircle(endLocation, endPinName, endLocationIcon, endIconSize.getWidth(), endIconSize.getHeight()));


            String url = mapUtility.getDirectionsUrl(startLocation, endLocation, null, "driving");
            new FetchURL(this).execute(url, "walking");
        }

    }

    public Marker drawPathAndReturnDestinationMarker(Context context, LatLng startLocation, String startPinName, String startLocationIcon, IconSize startIconSize, LatLng endLocation, String endPinName, String endLocationIcon, IconSize endIconSize) {

        if (checkMapIsReady()) {
            mMap.clear();
            this.startLocation = startLocation;
            this.endLocation = endLocation;

            mMap.addMarker(mapUtility.createMarkerForCircle(startLocation, startPinName, startLocationIcon, startIconSize.getWidth(), startIconSize.getHeight()));
            Marker marker = mMap.addMarker(mapUtility.createMarkerForCircle(endLocation, endPinName, endLocationIcon, endIconSize.getWidth(), endIconSize.getHeight()));


            String url = mapUtility.getDirectionsUrl(startLocation, endLocation, null, "driving");
            new FetchURL(this).execute(url, "walking");
            return marker;
        }

        return null;
    }

    public void drawPath(float rotation, LatLng startLocation, String startPinName, String startLocationIcon, IconSize startIconSize, LatLng endLocation, String endPinName,
                         String endLocationIcon, IconSize endIconSize) {

        if (checkMapIsReady()) {
            mMap.clear();
            this.startLocation = startLocation;
            this.endLocation = endLocation;

            mMap.addMarker(mapUtility.createMarker(startLocation, startPinName, startLocationIcon, startIconSize.getWidth(), startIconSize.getHeight(), rotation));
            mMap.addMarker(mapUtility.createMarker(endLocation, endPinName, endLocationIcon, endIconSize.getWidth(), endIconSize.getHeight()));

            String url = mapUtility.getDirectionsUrl(startLocation, endLocation, null, "driving");
            new FetchURL(this).execute(url, "walking");
        }

    }

//    public void drawPath(Context context, List<LatLng> locations, List<MarkerModel> markerModels) {
//
//        if (checkMapIsReady()) {
//            mMap.clear();
//            if (locations != null) {
//                if (locations.size() > 2) {
//
//                    this.startLocation = locations.get(0);
//                    this.endLocation = locations.get(locations.size() - 1);
//
//                    for (MarkerModel markerModel : markerModels) {
//                        mMap.addMarker(mapUtility.createMarker(markerModel.getLatLng(), markerModel.getName(), markerModel.getIcon(), markerModel.getWidth(), markerModel.getHeight()));
//                    }
//                    String url = mapUtility.getDirectionsUrl(startLocation, endLocation, null, "driving");
//                    new FetchURL(this).execute(url, "walking");
//                }
//            }
//        }
//
//    }

    public void drawPath(LatLng startLocation, String startPinName, int startLocationIcon, LatLng endLocation, String endPinName, int endLocationIcon, List<LatLng> wayPoints) {

        if (checkMapIsReady()) {
            mMap.clear();
            this.startLocation = startLocation;
            this.endLocation = endLocation;

            mMap.addMarker(mapUtility.createMarker(startLocation, startPinName, startLocationIcon));
            mMap.addMarker(mapUtility.createMarker(endLocation, endPinName, endLocationIcon));

            String url = mapUtility.getDirectionsUrl(startLocation, endLocation, wayPoints, "driving");
            new FetchURL(this).execute(url, "walking");
        }

    }

    public void drawPathWithWayPointMarker(LatLng startLocation, String startPinName, int startLocationIcon,
                                           LatLng endLocation, String endPinName, int endLocationIcon,
                                           List<LatLng> wayPoints,int wayPointIcon) {

        if (checkMapIsReady()) {
            mMap.clear();
            this.startLocation = startLocation;
            this.endLocation = endLocation;

            mMap.addMarker(mapUtility.createMarker(startLocation, startPinName, startLocationIcon));
            mMap.addMarker(mapUtility.createMarker(endLocation, endPinName, endLocationIcon));


            if (wayPoints != null && (wayPoints.size() > 0)) {
                for (LatLng wayPoint : wayPoints) {
                    mMap.addMarker(mapUtility.createMarker(wayPoint, endPinName,wayPointIcon));
                }
            }

            String url = mapUtility.getDirectionsUrl(startLocation, endLocation, wayPoints, "driving");
            new FetchURL(this).execute(url, "walking");
        }

    }

    public void drawPath(List<LatLng> locations, String startPinName, int startLocationIcon, String endPinName, int endLocationIcon) {

        if (checkMapIsReady()) {
            mMap.clear();
            if (locations != null) {
                if (locations.size() > 2) {
                    this.startLocation = locations.get(0);
                    this.endLocation = locations.get(locations.size() - 1);

                    mMap.addMarker(mapUtility.createMarker(startLocation, startPinName, startLocationIcon));
                    mMap.addMarker(mapUtility.createMarker(endLocation, endPinName, endLocationIcon));

                    drawAndCenter(startLocation, endLocation, locations);

                }
            }
        }

    }

    public Marker drawPath(Context context, List<LatLng> locations, String startPinName, int startLocationIcon, String endPinName, int endLocationIcon) {

        if (checkMapIsReady()) {
            mMap.clear();
            if (locations != null) {
                if (locations.size() > 1) {
                    this.startLocation = locations.get(0);
                    this.endLocation = locations.get(locations.size() - 1);
                } else {
                    this.startLocation = locations.get(0);
                    this.endLocation = locations.get(0);
                }
                mMap.addMarker(mapUtility.createMarker(startLocation, startPinName, startLocationIcon));
                Marker destination = mMap.addMarker(mapUtility.createMarker(endLocation, endPinName, endLocationIcon));
                drawAndCenter(startLocation, endLocation, locations);
                return destination;
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    public Marker drawPath(Context context, List<LatLng> locations, String startPinName, String startLocationIcon, String endPinName, int endLocationIcon) {

        if (checkMapIsReady()) {
            mMap.clear();
            if (locations != null) {
                if (locations.size() > 1) {
                    this.startLocation = locations.get(0);
                    this.endLocation = locations.get(locations.size() - 1);
                } else {
                    this.startLocation = locations.get(0);
                    this.endLocation = locations.get(0);
                }
                mMap.addMarker(mapUtility.createMarkerForCircle(startLocation, startPinName, startLocationIcon));
                Marker destination = mMap.addMarker(mapUtility.createMarker(endLocation, endPinName, endLocationIcon));
                drawAndCenter(startLocation, endLocation, locations);
                return destination;
            } else {
                return null;
            }
        } else {
            return null;
        }

    }

    public void updatePath(Context context, List<LatLng> locations, Marker marker) {
        if (locations != null) {
            if (locations.size() > 1) {
                startAnim(locations);
                Location location = new Location("");
                location.setLatitude(locations.get(locations.size() - 1).latitude);
                location.setLongitude(locations.get(locations.size() - 1).longitude);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animateMarkerNew(location, marker);
                    }
                }, 1000);
            } else if (locations.size() == 1) {
//                startAnim(locations);
                Location location = new Location("");
                location.setLatitude(locations.get(0).latitude);
                location.setLongitude(locations.get(0).longitude);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animateMarkerNew(location, marker);
                    }
                }, 1000);
            }
        }
    }


    public Marker updatePath(LatLng startLocation, float startIconBearing, String startPinName, List<LatLng> wayPoints, int startLocationIcon, LatLng endLocation, String endPinName, int endLocationIcon, LatLng lastLocation) {

        if (checkMapIsReady()) {
            mMap.clear();
            this.startLocation = startLocation;
            this.endLocation = endLocation;

            Marker startMaker = mMap.addMarker(mapUtility.createMarkerWithBearing(startLocation, startPinName, startLocationIcon, startIconBearing));
            //   mMap.addMarker(mapUtility.createMarker(endLocation, endPinName, endLocationIcon));

            mMap.addMarker(mapUtility.createMarker(lastLocation, endPinName, endLocationIcon));

            Location location = new Location("");
            location.setLatitude(endLocation.latitude);
            location.setLongitude(endLocation.longitude);

            animateMarkerNew(location, startMaker);

            String url = mapUtility.getDirectionsUrl(startLocation, endLocation, wayPoints, "driving");
            new FetchURL(this).execute(url, "walking");

            return startMaker;
        }

        return null;

    }

    public void updatePath(LatLng startLocation, LatLng endLocation, List<LatLng> wayPoints, Marker marker) {


        String url = mapUtility.getDirectionsUrl(startLocation, endLocation, wayPoints, "driving");
        new FetchURL(this).execute(url, "walking");

        Location location = new Location("");
        location.setLatitude(startLocation.latitude);
        location.setLongitude(startLocation.longitude);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//
//            }
//        }, 1000);

        animateMarkerNew(location, marker);

    }


    public void moveMarker(Context context, Location newLocation, Marker marker) {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animateMarkerWithoutCameraMove(newLocation, marker);
            }
        }, 1000);


    }


    public void moveMarkerWithCameraUpdate(Context context, Location newLocation, Marker marker) {
        LogUtility.debug(TAG, "drawPathForTracking moveMarkerWithCameraUpdate :" + new Gson().toJson(newLocation));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animateMarkerNew(newLocation, marker);
            }
        }, 1000);


    }

    public void moveMarkerWithCameraUpdate(Context context, LatLng newLocation, Marker marker) {
        LogUtility.debug(TAG, "drawPathForTracking moveMarkerWithCameraUpdate :" + new Gson().toJson(newLocation));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animateMarkerNew(newLocation, marker, true);
            }
        }, 100);


    }

    public void moveMarkerWithoutCameraUpdate(Context context, LatLng newLocation, Marker marker) {
        LogUtility.debug(TAG, "drawPathForTracking moveMarkerWithCameraUpdate :" + new Gson().toJson(newLocation));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animateMarkerNew(newLocation, marker, false);
            }
        }, 100);


    }

    //for tmdone
    public void updatePath(Context context, LatLng startLocation, LatLng endLocation, Marker marker) {

        Location location = new Location("");
        location.setLatitude(startLocation.latitude);
        location.setLongitude(startLocation.longitude);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animateMarkerNew(location, marker);
            }
        }, 1000);

    }

    @Override
    public void onTaskCompleted(Object... values) {

        drawAndCenter(startLocation, endLocation, (List<LatLng>) values[0]);
    }

    private void drawAndCenter(LatLng startPoint, LatLng endPoint, List<LatLng> points) {
        centerScreen(startPoint, endPoint, points);
        startAnim(points);

    }


    public void centerScreen(LatLng startPoint, LatLng endPoint, List<LatLng> waypoints) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(startPoint);
        builder.include(endPoint);


        if ((waypoints != null) && (waypoints.size() > 0)) {

            for (LatLng waypoint : waypoints) {
                builder.include(waypoint);
            }
        }

        LatLngBounds bounds = builder.build();

//        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, getBound(startPoint, endPoint));
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 0);


        lastKnownCameraUpdate = cu;

//        CameraPosition cameraPosition = new CameraPosition.Builder()
//                .target(startPoint)
//                .target(endPoint)
//                .build();
//
//        CameraUpdate update = CameraUpdateFactory.newCameraPosition(cameraPosition);
//        mMap.moveCamera(update);
//        mMap.animateCamera(update);
        mMap.moveCamera(cu);
        mMap.animateCamera(cu, 1000, null);


    }

    private int getBound(LatLng start, LatLng end) {
        double distance = MapUtility.getDistance(start, end) / 1000;


        if (haveDisplayMargins) {
            if (distance <= 20) {
                return 180;
            } else if (distance <= 50) {
                return 300;
            } else {
                return 200;
            }
        } else {
            if (distance <= 20) {
                return 80;
            } else if (distance <= 50) {
                return 300;
            } else {
                return 200;
            }
        }
    }

    private void startAnim(List<LatLng> points) {
        if (checkMapIsReady()) {
            if (points.size() <= 1) {
                return;
            }
            MapAnimator.getInstance().animateRoute(mMap, points);
        }
    }

//    public void resetAnimation(View view) {
//        startAnim();
//    }


    private void animateMarkerNew(final Location destination, final Marker marker) {

        if (marker != null) {

            final LatLng startPosition = marker.getPosition();
            final LatLng endPosition = new LatLng(destination.getLatitude(), destination.getLongitude());

            final float startRotation = marker.getRotation();
            final LatLngInterpolatorNew latLngInterpolator = new LatLngInterpolatorNew.LinearFixed();

            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(2000); // duration 3 second
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        float v = animation.getAnimatedFraction();
                        LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                        marker.setPosition(newPosition);
                        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                                .target(newPosition)
                                .zoom(defaultZoomLevelForMarkerUpdate)
                                .build()));

                        marker.setRotation(getBearing(startPosition, new LatLng(destination.getLatitude(), destination.getLongitude())));
                    } catch (Exception ex) {
                        LogUtility.debug(TAG, "MOVE ANIMATION ERROR :" + ex.getLocalizedMessage());
                        //I don't care atm..
                    }
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    // if (mMarker != null) {
                    // mMarker.remove();
                    // }
                    // mMarker = googleMap.addMarker(new MarkerOptions().position(endPosition).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car)));

                }
            });
            valueAnimator.start();
        }
    }

    private void animateMarkerNew(final LatLng destination, final Marker marker, boolean withCameraUpdate) {

        LogUtility.debug(TAG, "MOVING MARKER IS MARKER NULL : " + (marker == null));

        if (marker != null) {

            final LatLng startPosition = marker.getPosition();
            final LatLng endPosition = destination;

            LogUtility.debug(TAG, "MOVING MARKER Start : " + marker.getPosition().toString());
            LogUtility.debug(TAG, "MOVING MARKER End : " + endPosition.toString());

            final float startRotation = marker.getRotation();
            final LatLngInterpolatorNew latLngInterpolator = new LatLngInterpolatorNew.LinearFixed();

            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(2000); // duration 3 second
            valueAnimator.setInterpolator(new LinearInterpolator());
            LogUtility.debug(TAG, "MOVING MARKER");
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        float v = animation.getAnimatedFraction();
                        LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                        marker.setPosition(newPosition);
                        if (withCameraUpdate && (lastKnownCameraUpdate!=null)) {
//                            mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
//                                    .target(newPosition)
//                                    .zoom(defaultZoomLevelForMarkerUpdate)
//                                    .build()));

                            mMap.moveCamera(lastKnownCameraUpdate);
                            mMap.animateCamera(lastKnownCameraUpdate, 1000, null);
                        }

                        marker.setRotation(getBearing(startPosition, destination));
                    } catch (Exception ex) {

                        LogUtility.debug(TAG, "MOVING MARKER DONE");
                        LogUtility.debug(TAG, "MOVE ANIMATION ERROR :" + ex.getLocalizedMessage());
                        //I don't care atm..
                    }
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    // if (mMarker != null) {
                    // mMarker.remove();
                    // }
                    // mMarker = googleMap.addMarker(new MarkerOptions().position(endPosition).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car)));

                }
            });
            valueAnimator.start();
        }
    }

    private void animateMarkerWithoutCameraMove(final Location destination, final Marker marker) {

        if (marker != null) {

            final LatLng startPosition = marker.getPosition();
            final LatLng endPosition = new LatLng(destination.getLatitude(), destination.getLongitude());

            final float startRotation = marker.getRotation();
            final LatLngInterpolatorNew latLngInterpolator = new LatLngInterpolatorNew.LinearFixed();

            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(2000); // duration 3 second
            valueAnimator.setInterpolator(new LinearInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        float v = animation.getAnimatedFraction();
                        LatLng newPosition = latLngInterpolator.interpolate(v, startPosition, endPosition);
                        marker.setPosition(newPosition);
//                        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
//                                .target(newPosition)
//                                .zoom(defaultZoomLevelForMarkerUpdate)
//                                .build()));

                        marker.setRotation(getBearing(startPosition, new LatLng(destination.getLatitude(), destination.getLongitude())));
                    } catch (Exception ex) {
                        //I don't care atm..
                    }
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);

                    // if (mMarker != null) {
                    // mMarker.remove();
                    // }
                    // mMarker = googleMap.addMarker(new MarkerOptions().position(endPosition).icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_car)));

                }
            });
            valueAnimator.start();
        }
    }

    private interface LatLngInterpolatorNew {
        LatLng interpolate(float fraction, LatLng a, LatLng b);

        class LinearFixed implements LatLngInterpolatorNew {
            @Override
            public LatLng interpolate(float fraction, LatLng a, LatLng b) {
                double lat = (b.latitude - a.latitude) * fraction + a.latitude;
                double lngDelta = b.longitude - a.longitude;
                // Take the shortest path across the 180th meridian.
                if (Math.abs(lngDelta) > 180) {
                    lngDelta -= Math.signum(lngDelta) * 360;
                }
                double lng = lngDelta * fraction + a.longitude;
                return new LatLng(lat, lng);
            }
        }
    }


    //Method for finding bearing between two points
    private float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }
}