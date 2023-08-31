package com.fidenz.android_boilerplate.utility;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import androidx.core.content.ContextCompat;

import com.fidenz.android_boilerplate.R;
import com.fidenz.android_boilerplate.listeners.OnDirectionFetchedListener;
import com.fidenz.android_boilerplate.listeners.OnDistanceFetchedListener;
import com.fidenz.android_boilerplate.listeners.onGpsListener;
import com.fidenz.android_boilerplate.models.CustomLocation;
import com.fidenz.android_boilerplate.models.DistanceMatrixResult;
import com.fidenz.android_boilerplate.models.DriveMode;
import com.fidenz.android_boilerplate.models.IconSize;
import com.fidenz.android_boilerplate.models.google_direction.DistanceMatrixResponse;
import com.fidenz.android_boilerplate.models.google_direction.Element;
import com.fidenz.android_boilerplate.models.google_direction.GoogleDirectionResponse;
import com.fidenz.android_boilerplate.models.google_direction.Row;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;

public class MapUtility {


    private static String TAG = "MapUtility";

    private static final int MAP_TO_IMAGE_WIDTH = 400;
    private static final int MAP_TO_IMAGE_HEIGHT = 200;

    private Activity activity;

    public MapUtility(Activity activity) {
        this.activity = activity;
    }

    public Bitmap resizeMapIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(activity.getResources(), activity.getResources().getIdentifier(iconName, "drawable", activity.getPackageName()));

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }



    public static void GPSOnRequest(Context context) {
        new GpsUtility(context).turnGpsOn(new onGpsListener() {
            @Override
            public void gpsStatus(boolean isGPSEnable) {
                // turn on GPS
                LogUtility.error(TAG, "GPS Status = " + isGPSEnable);
            }
        });
    }

    public static LatLng locationToLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

//    public LatLng geoPointToLatLng(GeoPoint location) {
//        return new LatLng(location.getLatitude(), location.getLongitude());
//    }
//
//    public static Location geoPointToLocation(GeoPoint geoPoint) {
//        Location location = new Location("");
//        location.setLatitude(geoPoint.getLatitude());
//        location.setLongitude(geoPoint.getLongitude());
//
//        return location;
//    }

    public static Location pointTpLocation(double lat,double lng) {
        Location location = new Location("");
        location.setLatitude(lat);
        location.setLongitude(lng);

        return location;
    }

//    public static GeoPoint latlngToGeoPoint(LatLng latLng) {
//        return new GeoPoint(latLng.latitude, latLng.longitude);
//    }

    public static Location LatLngToLocation(LatLng latLng) {

        Location location = new Location("");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);

        return location;
    }

    public static LatLng ArrayToLatLng(List<Double> latLng, boolean shouldSwipe) {
        if (shouldSwipe) {
            return new LatLng(latLng.get(1), latLng.get(0));
        } else {
            return new LatLng(latLng.get(0), latLng.get(1));
        }
    }

    public static LatLng getCenterPoint(List<LatLng> points) {
        double latitude = 0;
        double longitude = 0;
        int n = points.size();

        for (LatLng point : points) {
            latitude += point.latitude;
            longitude += point.longitude;
        }

        return new LatLng(latitude / n, longitude / n);
    }

    public int colorConverter(String colorCode) {
        String opacityValue = "#33";
        if (StringUtility.isNotNull(colorCode)) {
            if (colorCode.contains("#")) {
                colorCode = colorCode.split("#")[1];
            }

            colorCode = opacityValue + colorCode;
        }
        int myColor = Color.parseColor(colorCode);
        return myColor;
    }

    public PolygonOptions markArea(List<LatLng> locationList, String hexColorCode) {
        int color = colorConverter(hexColorCode);
        return new PolygonOptions()
                .addAll(locationList)
                .strokeColor(color)
                .strokeWidth(5)
                .fillColor(color);
    }


    public static MapStyleOptions getMapStyle(Context context,int mapRes) {
        return MapStyleOptions.loadRawResourceStyle(context, mapRes);
    }

    public MarkerOptions createMarker(LatLng latLng, String pinName, int icon) {

        return new MarkerOptions()
                .position(latLng)
                .title(pinName)
                // .anchor(0,0)
                .icon(generateBitmapDescriptorFromResWithoutImageMargine(activity, icon));
    }


    public MarkerOptions createMarkerWithBearing(LatLng latLng, String pinName, int icon, float bearing) {

        return new MarkerOptions()
                .position(latLng)
                .title(pinName)
                .rotation(bearing)
                // .anchor(0,0)
                .icon(generateBitmapDescriptorFromResWithoutImageMargine(activity, icon));
    }

    public MarkerOptions createMarkerForCircle(LatLng latLng, String pinName, String icon) {

        return new MarkerOptions()
                .position(latLng)
                .title(pinName)
                .anchor(0.51f, 0.51f)
                .icon(BitmapDescriptorFactory
                        .fromBitmap(resizeMapIcons(icon, 40, 40)));
    }


    public MarkerOptions createMarkerForCircle(LatLng latLng, String pinName, String icon, int size) {

        return new MarkerOptions()
                .position(latLng)
                .title(pinName)
                .anchor(0.51f, 0.51f)
                .icon(BitmapDescriptorFactory
                        .fromBitmap(resizeMapIcons(icon, size, size)));
    }

    public MarkerOptions createMarkerForCircle(LatLng latLng, String pinName, String icon, int width, int height) {

        return new MarkerOptions()
                .position(latLng)
                .title(pinName)
                .anchor(0.51f, 0.51f)
                .icon(BitmapDescriptorFactory
                        .fromBitmap(resizeMapIcons(icon, width, height)));
    }

    public MarkerOptions createMarker(LatLng latLng, String pinName, String icon, int width, int height) {

        return new MarkerOptions()
                .position(latLng)
                .title(pinName)
                //   .anchor(0.8f,0.8f)
                .icon(BitmapDescriptorFactory
                        .fromBitmap(resizeMapIcons(icon, width, height)));
    }

    public MarkerOptions createMarker(LatLng latLng, String pinName, String icon, int width, int height, float rotation) {

        return new MarkerOptions()
                .position(latLng)
                .title(pinName)
                .rotation(rotation)
                //   .anchor(0.8f,0.8f)
                .icon(BitmapDescriptorFactory
                        .fromBitmap(resizeMapIcons(icon, width, height)));
    }

    public static Bitmap resizeMapIcons(String iconName, int width, int height, Context context) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(context.getResources(), context.getResources().getIdentifier(iconName, "drawable", context.getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }


    private BitmapDescriptor generateBitmapDescriptorFromRes(Context context, int resId) {
        Drawable drawable = ContextCompat.getDrawable(context, resId);
        drawable.setBounds(
                0,
                0,
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    private BitmapDescriptor generateBitmapDescriptorFromResWithoutImageMargine(Context context, int resId) {
        Drawable drawable = ContextCompat.getDrawable(context, resId);
        drawable.setBounds(
                0,
                0,
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicWidth());
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static void zoomToPolyline(GoogleMap map, Polyline p) {
        if (p == null || p.getPoints().isEmpty())
            return;

        LatLngBounds.Builder builder = LatLngBounds.builder();

        for (LatLng latLng : p.getPoints()) {
            builder.include(latLng);
        }
        final LatLngBounds bounds = builder.build();
        try {
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 250));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void startRippleAnimation(GoogleMap googleMap, LatLng latLng,int rippleSize) {

        int defaultRippleSize = rippleSize;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final GroundOverlay groundOverlay11 = googleMap.addGroundOverlay(new
                        GroundOverlayOptions()
                        .position(latLng, 10)
                        .transparency(0.5f)
                        .image(BitmapDescriptorFactory.fromResource(R.drawable.img_ripple_image)));
//                    .image(BitmapDescriptorFactory
//                            .fromBitmap(MapUtility.resizeMapIcons("one", 60, 60,TaxiHomeActivity.this))));
                OverLay(groundOverlay11, defaultRippleSize);
            }
        }, 0);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final GroundOverlay groundOverlay1 = googleMap.addGroundOverlay(new GroundOverlayOptions()
                        .position(latLng, 10)
                        .transparency(0.4f)
                        .image(BitmapDescriptorFactory.fromResource(R.drawable.img_ripple_image)));
//                        .image(BitmapDescriptorFactory
//                                .fromBitmap(MapUtility.resizeMapIcons("two", 60, 60,TaxiHomeActivity.this))));
                OverLay(groundOverlay1, defaultRippleSize);
            }
        }, 4000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final GroundOverlay groundOverlay2 = googleMap.addGroundOverlay(new GroundOverlayOptions()
                        .position(latLng, 10)
                        .transparency(0.3f)
                        .image(BitmapDescriptorFactory.fromResource(R.drawable.img_ripple_image)));
//                        .image(BitmapDescriptorFactory
//                                .fromBitmap(MapUtility.resizeMapIcons("three", 60, 60,TaxiHomeActivity.this))));
                OverLay(groundOverlay2, defaultRippleSize);
            }
        }, 8000);
    }


    public static int getRippleSize(GoogleMap map) {
        CameraPosition cameraPosition = map.getCameraPosition();
        double zoomLevel = cameraPosition.zoom;
        LogUtility.debug(TAG, "Zoom level = " + zoomLevel);
        if (zoomLevel >= 18) {
            return 100;
        } else if (zoomLevel >= 15) {
            return 300;
        } else if (zoomLevel >= 12) {
            return 1500;
        } else if (zoomLevel >= 11) {
            return 4200;
        } else if (zoomLevel >= 9) {
            return 12000;
        } else if (zoomLevel >= 7) {
            return 20000;
        } else if (zoomLevel >= 5) {
            return 30000;
        } else if (zoomLevel >= 3) {
            return 40000;
        } else {
            return 50000;
        }
    }


    public static double getDistance(LatLng start, LatLng end) {
        Location locationA = new Location("point A");

        locationA.setLatitude(start.latitude);
        locationA.setLongitude(start.longitude);

        Location locationB = new Location("point B");

        locationB.setLatitude(end.latitude);
        locationB.setLongitude(end.longitude);

        double distance = locationA.distanceTo(locationB);
        return distance;
    }


    static GroundOverlay groundOverlay11, groundOverlay2, groundOverlay3;

    public static void startRippleAnimation(GoogleMap googleMap, LatLng latLng, int rippleSize, boolean clearMap) {

        if (clearMap) {
            googleMap.clear();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (groundOverlay11 == null) {
                    groundOverlay11 = googleMap.addGroundOverlay(new
                            GroundOverlayOptions()
                            .position(latLng, 10)
                            .transparency(0.5f)
                            .image(BitmapDescriptorFactory.fromResource(R.drawable.img_ripple_image)));
                }
//                    .image(BitmapDescriptorFactory
//                            .fromBitmap(MapUtility.resizeMapIcons("one", 60, 60,TaxiHomeActivity.this))));
                OverLay(groundOverlay11, rippleSize);
            }
        }, 0);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (groundOverlay2 == null) {
                    groundOverlay2 = googleMap.addGroundOverlay(new GroundOverlayOptions()
                            .position(latLng, 10)
                            .transparency(0.3f)
                            .image(BitmapDescriptorFactory.fromResource(R.drawable.img_ripple_image)));
                }
//                        .image(BitmapDescriptorFactory
//                                .fromBitmap(MapUtility.resizeMapIcons("two", 60, 60,TaxiHomeActivity.this))));
                OverLay(groundOverlay2, rippleSize);
            }
        }, 4000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (groundOverlay3 == null) {
                    groundOverlay3 = googleMap.addGroundOverlay(new GroundOverlayOptions()
                            .position(latLng, 10)
                            .transparency(0.15f)
                            .image(BitmapDescriptorFactory.fromResource(R.drawable.img_ripple_image)));
                }
//                        .image(BitmapDescriptorFactory
//                                .fromBitmap(MapUtility.resizeMapIcons("three", 60, 60,TaxiHomeActivity.this))));
                OverLay(groundOverlay3, rippleSize);
            }
        }, 8000);
    }


    private static void OverLay(final GroundOverlay groundOverlay, int rippleSize) {
        ValueAnimator vAnimator = ValueAnimator.ofInt(0, rippleSize);
        int r = 99999;
        vAnimator.setRepeatCount(r);
        //vAnimator.setIntValues(0, 500);
        vAnimator.setDuration(12000);
        vAnimator.setEvaluator(new IntEvaluator());
        vAnimator.setInterpolator(new LinearInterpolator());
        vAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedFraction = valueAnimator.getAnimatedFraction();
                Integer i = (Integer) valueAnimator.getAnimatedValue();
                groundOverlay.setDimensions(i);
            }
        });
        vAnimator.start();


    }


    public String getDistanceUrl(LatLng origin, LatLng dest, List<LatLng> wayPoints, String directionMode) {
        String str_origin = "origins=" + origin.latitude + "," + origin.longitude;
        // Destination of route

//
//        String urlX = "https://maps.googleapis.com/maps/api/distancematrix/" +
//                "json?" + str_origin + "&" + end_origin + "&mode=driving&" +
//                "language=en-EN&sensor=false&key=AIzaSyBxCACesQOMrfqkHNlmnWpzOumyBVGeDEo";

        LogUtility.debug(TAG, "origin = " + origin);

        String wayPointJoint = "%7C";

        String str_dest = "destinations=" + dest.latitude + "," + dest.longitude;
        LogUtility.debug(TAG, "dest = " + origin);
        String wayPoint = wayPointJoint;
        if (wayPoints != null) {
            if (wayPoints.size() == 1) {
                wayPoint += (wayPoints.get(0).latitude + "," + wayPoints.get(0).longitude);
            } else {
                for (int i = 0; i < wayPoints.size(); i++) {
                    LogUtility.debug("waypoint", wayPoints.get(i).latitude + "");
                    wayPoint += (wayPoints.get(i).latitude + "," + wayPoints.get(i).longitude);
                    if (wayPoints.size() - 1 > i) {
                        wayPoint += wayPointJoint;
                    }
                }
            }
        }
        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        String key = "key=" + activity.getResources().getString(R.string.google_maps_key);
        // Building the parameters to the web service
        String parameters = "";
        if (StringUtility.isNotNull(wayPoint)) {
//            String str_waypoint = "waypoints=" + wayPoint;
            String str_waypoint = wayPoint;
//            parameters = str_origin + "&" + str_dest + "&" + str_waypoint + "&" + sensor + "&" + mode + "&" + key;
            parameters = str_origin + "&" + str_dest + "" + str_waypoint + "&" + sensor + "&" + mode + "&" + key;
        } else {
            parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&" + key;
        }
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/distancematrix/" + output + "?" + parameters;
        LogUtility.debug(TAG, "url = " + origin);
        return url;
    }


    public String getDistanceUrl(List<LatLng> wayPoints, String directionMode) {
        String str_origin = "origins=" + wayPoints.get(0).latitude + "," + wayPoints.get(0).longitude;
        // Destination of route
//
//        String urlX = "https://maps.googleapis.com/maps/api/distancematrix/" +
//                "json?" + str_origin + "&" + end_origin + "&mode=driving&" +
//                "language=en-EN&sensor=false&key=AIzaSyBxCACesQOMrfqkHNlmnWpzOumyBVGeDEo";
        LatLng dest = wayPoints.get((wayPoints.size() - 1));

        String str_dest = "destinations=" + dest.latitude + "," + dest.longitude;
        String wayPoint = "";
        if (wayPoints != null) {
            if (wayPoints.size() == 3) {
                wayPoint += (wayPoints.get(3).latitude + "," + wayPoints.get(3).longitude);
            } else if (wayPoints.size() > 3) {
                for (int i = 1; i < wayPoints.size(); i++) {
                    LogUtility.debug("waypoint", wayPoints.get(i).latitude + "");
                    wayPoint += (wayPoints.get(i).latitude + "," + wayPoints.get(i).longitude);
                    if (wayPoints.size() - 1 > i) {
                        wayPoint += "|";
                    }
                }
            }
        }

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        String key = "key=" + activity.getResources().getString(R.string.google_maps_key);
        // Building the parameters to the web service
        String parameters = "";
        if (StringUtility.isNotNull(wayPoint)) {
            String str_waypoint = "waypoints=" + wayPoint;
            parameters = str_origin + "&" + str_dest + "&" + str_waypoint + "&" + sensor + "&" + mode + "&" + key;
        } else {
            parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&" + key;
        }
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/distancematrix/" + output + "?" + parameters;
        LogUtility.debug(TAG, "Direction URL = " + url);
        return url;
    }


    public String getDirectionsUrl(LatLng origin, LatLng dest, List<LatLng> wayPoints, String directionMode) {
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        LogUtility.debug(TAG, "origin = " + origin);
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        LogUtility.debug(TAG, "dest = " + origin);
        String wayPoint = "";
        if (wayPoints != null) {
            if (wayPoints.size() == 1) {
                wayPoint += (wayPoints.get(0).latitude + "," + wayPoints.get(0).longitude);
            } else {
                for (int i = 0; i < wayPoints.size(); i++) {
                    LogUtility.debug("waypoint", wayPoints.get(i).latitude + "");
                    wayPoint += (wayPoints.get(i).latitude + "," + wayPoints.get(i).longitude);
                    if (wayPoints.size() - 1 > i) {
                        wayPoint += "|";
                    }
                }
            }
        }

        // Sensor enabled
        String sensor = "sensor=false";
        String mode = "mode=driving";
        String key = "key=" + activity.getResources().getString(R.string.google_maps_key);
        // Building the parameters to the web service
        String parameters = "";
        if (StringUtility.isNotNull(wayPoint)) {
            String str_waypoint = "waypoints=" + wayPoint;
            parameters = str_origin + "&" + str_dest + "&" + str_waypoint + "&" + sensor + "&" + mode + "&" + key;
        } else {
            parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode + "&" + key;
        }

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        LogUtility.debug(TAG, "Direction URL = " + url);
        return url;
    }

    public String getDirectionsUrl(String origin, String dest, List<CustomLocation> wayPoints, String directionMode) {

        StringBuilder wayPointsBuilder = new StringBuilder();
        String wayPoint = "";
        String str_origin = "origin=" + origin;
        // Destination of route
        String originString, destString;
        LogUtility.debug(TAG, "origin = " + origin);
        String str_dest = "destination=" + dest;
        for (int i = 0; i < wayPoints.size(); i++) {
            if (i == 0) {
                wayPointsBuilder = new StringBuilder("optimize:true");
            }
            wayPointsBuilder.append("|").append(wayPoints.get(i).getLocationAddress());
        }

        wayPoint = wayPointsBuilder.toString();
        // Sensor enabled
        try {
            wayPoint = URLEncoder.encode(wayPoint, "utf-8");
            originString = URLEncoder.encode(str_origin, "utf-8");
            destString = URLEncoder.encode(str_dest, "utf-8");

            String plus = "\\u002B";

            origin = origin.replaceAll(plus, " ");
            dest = dest.replaceAll(plus, " ");

            origin = origin.replaceAll(" ", "%20");
            dest = dest.replaceAll(" ", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + origin + "&destination=" + dest + "&waypoints=" + wayPoint + "&key=" + activity.getResources().getString(R.string.google_maps_key);
        LogUtility.debug(TAG, "url = " + origin);
        return url;
    }


    public void showMapLoadingToast() {
        CommonUtility.showToast(CommonUtility.ToastTypes.WARNING, activity, activity.getString(R.string.map_loading_message));
    }

    public void showLocationListErrorToast() {
        CommonUtility.showToast(CommonUtility.ToastTypes.WARNING, activity, activity.getString(R.string.map_loading_message));
    }


//    //for more details https://github.com/renaudcerrato/static-maps-api
//    public static StaticMap getMapToImageWithLocationPath(String apiKey, int width, int height, List<LatLng> locations, int color, StaticMap.GeoPoint wayPoints[]) {
//        StaticMap map = getMapUrl(width, height, apiKey);
//        map.path(new StaticMap.Path(StaticMap.Path.Style.builder().color(color).build(), wayPoints));
//        return map;
//    }
//
//
//    public static StaticMap getMapToImageWithLocationPath(String apiKey, StaticMap.GeoPoint wayPoints[], int color) {
//        StaticMap map = getMapUrl(MAP_TO_IMAGE_WIDTH, MAP_TO_IMAGE_HEIGHT, apiKey);
//        map.path(new StaticMap.Path(StaticMap.Path.Style.builder().color(color).build(), wayPoints));
//        map.marker(StaticMap.Marker.Style.builder().icon("https://i.ibb.co/rQCv4L2/Path-1473.png").build(), new StaticMap.GeoPoint(wayPoints[0].latitude(), wayPoints[0].longitude()));
//        map.marker(StaticMap.Marker.Style.builder().icon("https://i.ibb.co/CVBR37J/Path-1474.png").build(), new StaticMap.GeoPoint(wayPoints[wayPoints.length - 1].latitude(), wayPoints[wayPoints.length - 1].longitude()));
//        //  map.type(StaticMap.Type.SATELLITE);
//        return map;
//    }

    public MarkerOptions createMarker(LatLng latLng, String pinName, int icon, IconSize iconSize) {

        return new MarkerOptions()
                .position(latLng)
                .title(pinName)
                // .anchor(0,0)
                .icon(generateBitmapDescriptorFromResWithoutImageMargine(activity, icon, iconSize));
    }

//    public static StaticMap getMapImageWithMapType(String apiKey, int width, int height, StaticMap.Type mapType) {
//        StaticMap map = getMapUrl(width, height, apiKey);
//        map.center("NYC").type(mapType);
//        return map;
//    }

//    public static StaticMap getMapImageWithMarkers(String apiKey, int width, int height, StaticMap.Marker markerList[]) {
//        StaticMap map = getMapUrl(width, height, apiKey);
//        map.marker(markerList);
//        return map;
//    }




//    public static StaticMap getMapImageWithMarker(String apiKey, int width, int height, StaticMap.Marker marker) {
//        StaticMap map = getMapUrl(width, height, apiKey);
//        map.marker(marker);
//        return map;
//    }


    String distanceAsValue, distanceAsText, distanceUrl, timeAsText, timeAsValue;
    int totalDistance, totalTime;

    @SuppressLint("StaticFieldLeak")
    public void getRoadDistance(LatLng startLocation, LatLng endLocation, List<LatLng> wayPoints, OnDistanceFetchedListener onDistanceFetchedListener) {


        new AsyncTask<Void, Void, Void>() {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

//                distanceUrl = getDistanceUrl(startLocation, endLocation, wayPoints, DriveMode.DRIVING);

                totalTime = 0;
                totalDistance = 0;
                distanceUrl = getDistanceUrl(startLocation, endLocation, wayPoints, DriveMode.DRIVING);


                LogUtility.debug(TAG, "Distance URL: " + distanceUrl);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

//                int distance = StringUtility.isNotNull(distanceAsValue) ? (Integer.parseInt(distanceAsValue) / 1000) : 0;
//                int time = StringUtility.isNotNull(timeAsValue) ? (Integer.parseInt(timeAsValue) / 60) : 0;
//                int timeSecond = StringUtility.isNotNull(timeAsValue) ? (Integer.parseInt(timeAsValue)) : 0;


                double distance = (totalDistance / 1000.0);
                int timeAsMins = (totalTime / 60);
//                int timeAsMins = StringUtility.isNotNull(timeAsValue) ? (Integer.parseInt(timeAsValue)) : 0;

                String timeAsString = DateTimeUtility.splitMinutesToHorusAndMins(timeAsMins, activity);
                String distanceAsString = metersToKm(totalDistance, activity);

                onDistanceFetchedListener.onResult(new DistanceMatrixResult(distanceAsString, timeAsString, distance, timeAsMins, totalTime, totalDistance));
            }

            @Override
            protected Void doInBackground(Void... voids) {


                try {
                    String data = "";
                    InputStream iStream = null;
                    HttpURLConnection urlConnection = null;
                    try {
                        URL url = new URL(distanceUrl);
                        urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.connect();
                        iStream = urlConnection.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
                        StringBuffer sb = new StringBuffer();
                        String line = "";
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        data = sb.toString();
                        Log.d(TAG, "Downloaded URL: " + data.toString());

                        DistanceMatrixResponse distanceMatrixResponse = new Gson().fromJson(data, DistanceMatrixResponse.class);
                        totalTime = 0;
                        totalDistance = 0;
                        if (distanceMatrixResponse != null) {

                            for (Row row : distanceMatrixResponse.getRows()) {
                                for (Element element : row.getElements()) {
                                    totalDistance += element.getDistance().getValue();
                                    totalTime += element.getDuration().getValue();
                                }
                            }

                        } else {
//                            distanceAsValue = new JSONObject(data).getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("distance").getString("value");
//                            distanceAsText = new JSONObject(data).getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("distance").getString("text");
//                            timeAsText = new JSONObject(data).getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("duration").getString("text");
//                            timeAsValue = new JSONObject(data).getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0).getJSONObject("duration").getString("value");
                        }


                        br.close();
                    } catch (Exception e) {
                        LogUtility.debug(TAG, "Exception downloading URL: " + e.toString());
                        e.printStackTrace();
                    } finally {
                        iStream.close();
                        urlConnection.disconnect();
                    }

                } catch (Exception e) {
                    LogUtility.debug(TAG, e.getLocalizedMessage());
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();


    }

    String directionUrl, directionResponse;

    @SuppressLint("StaticFieldLeak")
    public void getDirection(LatLng origin, LatLng dest, List<LatLng> wayPoints, OnDirectionFetchedListener onDirectionFetchedListener) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                directionUrl = getDirectionsUrlCarpool(origin, dest, wayPoints, DriveMode.DRIVING);
                LogUtility.debug(TAG, "DirectionsUrlCarpool URL: " + directionUrl);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                GoogleDirectionResponse googleDirectionResponse = new Gson().fromJson(directionResponse, GoogleDirectionResponse.class);
                onDirectionFetchedListener.onResult(googleDirectionResponse);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    String data = "";
                    InputStream iStream = null;
                    HttpURLConnection urlConnection = null;
                    try {
                        URL url = new URL(directionUrl);
                        urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.connect();
                        iStream = urlConnection.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
                        StringBuffer sb = new StringBuffer();
                        String line = "";
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        data = sb.toString();
                        Log.d(TAG, "Downloaded URL: " + data.toString());
                        directionResponse = data;
                        br.close();
                    } catch (Exception e) {
                        LogUtility.debug(TAG, "Exception downloading URL: " + e.toString());
                        e.printStackTrace();
                    } finally {
                        iStream.close();
                        urlConnection.disconnect();
                    }

                } catch (Exception e) {
                    LogUtility.debug(TAG, e.getLocalizedMessage());
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }


    public String getDirectionsUrlCarpool(LatLng origin, LatLng dest, List<LatLng> wayPoints, String directionMode) {

        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route


        LogUtility.debug(TAG, "origin = " + origin);

        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        LogUtility.debug(TAG, "dest = " + origin);
        String wayPoint = "";
        if (wayPoints != null) {
            if (wayPoints.size() == 1) {
                wayPoint += (wayPoints.get(0).latitude + "," + wayPoints.get(0).longitude);
            } else {
                for (int i = 0; i < wayPoints.size(); i++) {
                    LogUtility.debug("waypoint", wayPoints.get(i).latitude + "");
                    wayPoint += (wayPoints.get(i).latitude + "," + wayPoints.get(i).longitude);
                    if (wayPoints.size() - 1 > i) {
                        wayPoint += "|";
                    }
                }
            }
        }

        // Sensor enabled
//        String sensor = "sensor=false";
//        String mode = "mode=driving";
        String key = "key=" + activity.getResources().getString(R.string.google_maps_key);
        // Building the parameters to the web service
        String parameters = "";
        if (StringUtility.isNotNull(wayPoint)) {
            String str_waypoint = "waypoints=" + wayPoint;
            parameters = str_origin + "&" + str_dest + "&" + str_waypoint + "&" + key;
        } else {
            parameters = str_origin + "&" + str_dest + "&" + key;
        }

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        LogUtility.debug(TAG, "url = " + origin);
        return url;
    }

    public Bitmap addBorderToBitmap(Bitmap bmp, int borderSize, int color) {
        Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth() + borderSize * 2, bmp.getHeight() + borderSize * 2, bmp.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawColor(color);
        canvas.drawBitmap(bmp, borderSize, borderSize, null);
        return bmpWithBorder;
    }

    public Bitmap getCircullerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff4242DB;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());


        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(1);

        final RectF rectF = new RectF(rect);
        final float roundPx = bitmap.getWidth() / 2;

        paint.setAntiAlias(true);
        // canvas.drawARGB(0, 0, 0, 0);
        //    paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }


    public MarkerOptions createWebUrlMarker(LatLng location, String pinName,String imageUrl,float bearing ,int borderColor) {
        try {
            //load image
            URL url = null;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            url = new URL(imageUrl);


//                //to bitmap
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                //resize bitmap
            Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, 80, 80, true);
//                //add circuler shape
            Bitmap circular = getCircullerBitmap(scaledBmp);
//                //add border
//               // Bitmap bitmapWithBorder  = mapUtility.addBorderToBitmap(circular,1,R.color.colorPrimary);


//                Bitmap circular = mapUtility.getCircularBitmap(bmp,80);
            Bitmap circular1 = addBorderToCircularBitmap(circular, 2, borderColor);
//               Bitmap circular2 = mapUtility.addShadowToCircularBitmap(circular1,3,R.color.colorBlack);


            MarkerOptions markerOptions = new MarkerOptions()
                    .position(location)
                    .title(pinName)
                    .rotation(bearing)
                    // .anchor(0,0)
                    .icon(BitmapDescriptorFactory.fromBitmap(circular1));

            return markerOptions;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public MarkerOptions createWebUrlMarker(LatLng location, String pinName,String imageUrl ,int borderColor) {
        try {
            //load image
            URL url = null;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            url = new URL(imageUrl);


//                //to bitmap
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                //resize bitmap
            Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, 80, 80, true);
//                //add circuler shape
            Bitmap circular = getCircullerBitmap(scaledBmp);
//                //add border
//               // Bitmap bitmapWithBorder  = mapUtility.addBorderToBitmap(circular,1,R.color.colorPrimary);


//                Bitmap circular = mapUtility.getCircularBitmap(bmp,80);
            Bitmap circular1 = addBorderToCircularBitmap(circular, 2, borderColor);
//               Bitmap circular2 = mapUtility.addShadowToCircularBitmap(circular1,3,R.color.colorBlack);


            MarkerOptions markerOptions = new MarkerOptions()
                    .position(location)
                    .title(pinName)
                    // .anchor(0,0)
                    .icon(BitmapDescriptorFactory.fromBitmap(circular1));

            return markerOptions;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public MarkerOptions createWebUrlMarker(LatLng location, String pinName,String imageUrl,IconSize iconSize ,int borderColor) {
        try {
            //load image
            URL url = null;
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            url = new URL(imageUrl);


//                //to bitmap
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                //resize bitmap
            Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, iconSize.getWidth(), iconSize.getHeight(), true);
//                //add circuler shape
            Bitmap circular = getCircullerBitmap(scaledBmp);
//                //add border
//               // Bitmap bitmapWithBorder  = mapUtility.addBorderToBitmap(circular,1,R.color.colorPrimary);


//                Bitmap circular = mapUtility.getCircularBitmap(bmp,80);
            Bitmap circular1 = addBorderToCircularBitmap(circular, 2, borderColor);
//               Bitmap circular2 = mapUtility.addShadowToCircularBitmap(circular1,3,R.color.colorBlack);


            MarkerOptions markerOptions = new MarkerOptions()
                    .position(location)
                    .title(pinName)
                    // .anchor(0,0)
                    .icon(BitmapDescriptorFactory.fromBitmap(circular1));

            return markerOptions;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public MarkerOptions createLocalCircularMarker(LatLng location, String pinName,int icon,IconSize iconSize,int borderColor ) {
        try {
//                //to bitmap
            Bitmap bmp = BitmapFactory.decodeResource(activity.getResources(),icon);
//                //resize bitmap
            Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, iconSize.getWidth(), iconSize.getHeight(), true);
//                //add circuler shape
            Bitmap circular = getCircullerBitmap(scaledBmp);
//                //add border
//               // Bitmap bitmapWithBorder  = mapUtility.addBorderToBitmap(circular,1,R.color.colorPrimary);

//                Bitmap circular = mapUtility.getCircularBitmap(bmp,80);
            Bitmap circular1 = addBorderToCircularBitmap(circular, 2,borderColor);
//               Bitmap circular2 = mapUtility.addShadowToCircularBitmap(circular1,3,R.color.colorBlack);


            MarkerOptions markerOptions = new MarkerOptions()
                    .position(location)
                    .title(pinName)
                    // .anchor(0,0)
                    .icon(BitmapDescriptorFactory.fromBitmap(circular1));

            return markerOptions;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public Bitmap getCircularBitmap(Bitmap srcBitmap, int size) {
        // Calculate the circular bitmap width with border
        int squareBitmapWidth = Math.min(srcBitmap.getWidth(), srcBitmap.getHeight());

        // Initialize a new instance of Bitmap
        Bitmap dstBitmap = Bitmap.createBitmap(
                size, // Width
                size, // Height
                Bitmap.Config.ARGB_8888 // Config
        );

        /*
            Canvas
                The Canvas class holds the "draw" calls. To draw something, you need 4 basic
                components: A Bitmap to hold the pixels, a Canvas to host the draw calls (writing
                into the bitmap), a drawing primitive (e.g. Rect, Path, text, Bitmap), and a paint
                (to describe the colors and styles for the drawing).
        */
        // Initialize a new Canvas to draw circular bitmap
        Canvas canvas = new Canvas(dstBitmap);

        // Initialize a new Paint instance
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        /*
            Rect
                Rect holds four integer coordinates for a rectangle. The rectangle is represented by
                the coordinates of its 4 edges (left, top, right bottom). These fields can be accessed
                directly. Use width() and height() to retrieve the rectangle's width and height.
                Note: most methods do not check to see that the coordinates are sorted correctly
                (i.e. left <= right and top <= bottom).
        */
        /*
            Rect(int left, int top, int right, int bottom)
                Create a new rectangle with the specified coordinates.
        */
        // Initialize a new Rect instance
        Rect rect = new Rect(0, 0, squareBitmapWidth, squareBitmapWidth);

        /*
            RectF
                RectF holds four float coordinates for a rectangle. The rectangle is represented by
                the coordinates of its 4 edges (left, top, right bottom). These fields can be
                accessed directly. Use width() and height() to retrieve the rectangle's width and
                height. Note: most methods do not check to see that the coordinates are sorted
                correctly (i.e. left <= right and top <= bottom).
        */
        // Initialize a new RectF instance
        RectF rectF = new RectF(rect);

        /*
            public void drawOval (RectF oval, Paint paint)
                Draw the specified oval using the specified paint. The oval will be filled or
                framed based on the Style in the paint.

            Parameters
                oval : The rectangle bounds of the oval to be drawn

        */
        // Draw an oval shape on Canvas
        canvas.drawOval(rectF, paint);

        /*
            public Xfermode setXfermode (Xfermode xfermode)
                Set or clear the xfermode object.
                Pass null to clear any previous xfermode. As a convenience, the parameter passed
                is also returned.

            Parameters
                xfermode : May be null. The xfermode to be installed in the paint
            Returns
                xfermode
        */
        /*
            public PorterDuffXfermode (PorterDuff.Mode mode)
                Create an xfermode that uses the specified porter-duff mode.

            Parameters
                mode : The porter-duff mode that is applied

        */
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        // Calculate the left and top of copied bitmap
        float left = (squareBitmapWidth - srcBitmap.getWidth()) / 2;
        float top = (squareBitmapWidth - srcBitmap.getHeight()) / 2;

        /*
            public void drawBitmap (Bitmap bitmap, float left, float top, Paint paint)
                Draw the specified bitmap, with its top/left corner at (x,y), using the specified
                paint, transformed by the current matrix.

                Note: if the paint contains a maskfilter that generates a mask which extends beyond
                the bitmap's original width/height (e.g. BlurMaskFilter), then the bitmap will be
                drawn as if it were in a Shader with CLAMP mode. Thus the color outside of the

                original width/height will be the edge color replicated.

                If the bitmap and canvas have different densities, this function will take care of
                automatically scaling the bitmap to draw at the same density as the canvas.

            Parameters
                bitmap : The bitmap to be drawn
                left : The position of the left side of the bitmap being drawn
                top : The position of the top side of the bitmap being drawn
                paint : The paint used to draw the bitmap (may be null)
        */
        // Make a rounded image by copying at the exact center position of source image
        canvas.drawBitmap(srcBitmap, left, top, paint);

        // Free the native object associated with this bitmap.
        srcBitmap.recycle();

        // Return the circular bitmap
        return dstBitmap;
    }

    // Custom method to add a border around circular bitmap
    public Bitmap addBorderToCircularBitmap(Bitmap srcBitmap, int borderWidth, int borderColor) {
        // Calculate the circular bitmap width with border
        int dstBitmapWidth = srcBitmap.getWidth() + borderWidth * 2;

        // Initialize a new Bitmap to make it bordered circular bitmap
        Bitmap dstBitmap = Bitmap.createBitmap(dstBitmapWidth, dstBitmapWidth, Bitmap.Config.ARGB_8888);

        // Initialize a new Canvas instance
        Canvas canvas = new Canvas(dstBitmap);
        // Draw source bitmap to canvas
        canvas.drawBitmap(srcBitmap, borderWidth, borderWidth, null);

        // Initialize a new Paint instance to draw border
        Paint paint = new Paint();
        paint.setColor(borderColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
        paint.setAntiAlias(true);

        /*
            public void drawCircle (float cx, float cy, float radius, Paint paint)
                Draw the specified circle using the specified paint. If radius is <= 0, then nothing
                will be drawn. The circle will be filled or framed based on the Style in the paint.

            Parameters
                cx : The x-coordinate of the center of the cirle to be drawn
                cy : The y-coordinate of the center of the cirle to be drawn
                radius : The radius of the cirle to be drawn
                paint : The paint used to draw the circle
        */
        // Draw the circular border around circular bitmap
        canvas.drawCircle(
                canvas.getWidth() / 2, // cx
                canvas.getWidth() / 2, // cy
                canvas.getWidth() / 2 - borderWidth / 2, // Radius
                paint // Paint
        );

        // Free the native object associated with this bitmap.
        srcBitmap.recycle();

        // Return the bordered circular bitmap
        return dstBitmap;
    }

    // Custom method to add a shadow around circular bitmap
    public Bitmap addShadowToCircularBitmap(Bitmap srcBitmap, int shadowWidth, int shadowColor) {
        // Calculate the circular bitmap width with shadow
        int dstBitmapWidth = srcBitmap.getWidth() + shadowWidth * 2;
        Bitmap dstBitmap = Bitmap.createBitmap(dstBitmapWidth, dstBitmapWidth, Bitmap.Config.ARGB_8888);

        // Initialize a new Canvas instance
        Canvas canvas = new Canvas(dstBitmap);
        canvas.drawBitmap(srcBitmap, shadowWidth, shadowWidth, null);

        // Paint to draw circular bitmap shadow
        Paint paint = new Paint();
        paint.setColor(shadowColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(shadowWidth);
        paint.setAntiAlias(true);

        // Draw the shadow around circular bitmap
        canvas.drawCircle(
                dstBitmapWidth / 2, // cx
                dstBitmapWidth / 2, // cy
                dstBitmapWidth / 2 - shadowWidth / 2, // Radius
                paint // Paint
        );

        /*
            public void recycle ()
                Free the native object associated with this bitmap, and clear the reference to the
                pixel data. This will not free the pixel data synchronously; it simply allows it to
                be garbage collected if there are no other references. The bitmap is marked as
                "dead", meaning it will throw an exception if getPixels() or setPixels() is called,
                and will draw nothing. This operation cannot be reversed, so it should only be
                called if you are sure there are no further uses for the bitmap. This is an advanced
                call, and normally need not be called, since the normal GC process will free up this
                memory when there are no more references to this bitmap.
        */
        srcBitmap.recycle();

        // Return the circular bitmap with shadow
        return dstBitmap;
    }


    private BitmapDescriptor generateBitmapDescriptorFromResWithoutImageMargine(Context context, int resId, IconSize iconSize) {
        Drawable drawable = ContextCompat.getDrawable(context, resId);
        drawable.setBounds(
                0,
                0,
                iconSize.getWidth(),
                iconSize.getHeight());
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

//    public static StaticMap getMapImageWithMarkers(String apiKey, StaticMap.Marker markerList[]) {
//        StaticMap map = getMapUrl(MAP_TO_IMAGE_WIDTH, MAP_TO_IMAGE_HEIGHT, apiKey);
//        map.marker(markerList);
//        return map;
//    }
//
//    private static StaticMap getMapUrl(int width, int height, String apiKey) {
//        StaticMap map = new StaticMap()
//                .size(width, height)
//                .key(apiKey);
//
//        return map;
//    }


    public static CameraUpdate createCameraUpdate(LatLng location, int zoomLevel) {
        return CameraUpdateFactory.newLatLngZoom(location, zoomLevel);
    }

    public static CameraUpdate createCameraUpdate(Location location, int zoomLevel) {
        return CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), zoomLevel);
    }

    public String getCompleteAddressString(Context context, double LATITUDE, double LONGITUDE, Locale locale) {
        String strAdd = "";
        String postalCode = "";
        Geocoder geocoder = new Geocoder(context, locale);
//        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(",");
                    String street = returnedAddress.getAddressLine(0);
                    String city = returnedAddress.getAddressLine(1);
                    if (city != null) {
                        String[] names = city.split(" ");
                        for (int j = 0; j < names.length; j++) {
                            if (j == 0) {
                                city = names[j];
                            }
                            if (j == 1) {
                                postalCode = names[j];
                            }
                        }
                    }
                    if (postalCode == null) {
                        postalCode = "";
                    }
                }
                strAdd = strReturnedAddress.toString();
                LogUtility.debug("Current location", "" + strReturnedAddress.toString());
            } else {
                LogUtility.debug("Current location", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtility.debug("Current location", "Cannot get Address!");
        }
        return strAdd;
    }


    private static String convertMapJSONtoUrl(JSONArray array) throws JSONException {
        String result = "&";

        for (int i = 0; i < array.length(); i++) {

            JSONObject object = array.getJSONObject(i);
            if (object != null) {
                result += (object.has("featureType") ? "feature:" + object.getString("featureType") : "feature:all") + "%7C";
                result += (object.has("elementType") ? "element:" + object.getString("elementType") : "element:all") + "%7C";

                JSONArray styles = object.getJSONArray("stylers");

                for (int x = 0; x < styles.length(); x++) {
                    JSONObject style = (JSONObject) styles.get(x);
                    String styleName = style.keys().next();
                    String styleValue = style.get(styleName).toString();

                    if (styleValue.startsWith("#")) {
                        styleValue = styleValue.replace("#", "0x");
                    }

                    result += styleName + ":" + styleValue + "%7C";
                }

            }
            if (array.length() - 1 != i) {
                result += "&";
            }

        }
        return result;
    }


    public static String metersToKm(int meter, Activity activity) {
        int kmI = (meter / 1000);
//        int metersI = (meter % 1000);

//        String km = (kmI + " " + activity.getString(R.string.km));
//        String meters = (metersI + " " + activity.getString(R.string.min));
//
//
//        if (kmI > 0 && metersI > 0) {
//            return km + " " + meters;
//        } else if (kmI > 0) {
//            return km;
//        } else if ((metersI > 0)) {
//            return meters;
//        } else {
//            return null;
//        }

        return kmI + " " + activity.getString(R.string.km);
    }
}