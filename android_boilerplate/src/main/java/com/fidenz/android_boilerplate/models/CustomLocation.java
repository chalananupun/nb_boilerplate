package com.fidenz.android_boilerplate.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class CustomLocation implements Parcelable {
    private String locationAddress;
    private Double latitude;
    private Double longitude;
    private Double bearing;

    public CustomLocation(String locationAddress, Double latitude, Double longitude, Double bearing) {
        this.locationAddress = locationAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.bearing = bearing;
    }

    public static final Creator<CustomLocation> CREATOR = new Creator<CustomLocation>() {
        @Override
        public CustomLocation createFromParcel(Parcel in) {
            return new CustomLocation(in);
        }

        @Override
        public CustomLocation[] newArray(int size) {
            return new CustomLocation[size];
        }
    };

    public CustomLocation(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    protected CustomLocation(Parcel in) {
        locationAddress = in.readString();
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            bearing = null;
        } else {
            bearing = in.readDouble();
        }
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }


    public LatLng getLatLng() {
        return new LatLng(latitude, longitude);
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getBearing() {
        return bearing;
    }

    public void setBearing(Double bearing) {
        this.bearing = bearing;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(locationAddress);
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
        if (bearing == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(bearing);
        }
    }
}
