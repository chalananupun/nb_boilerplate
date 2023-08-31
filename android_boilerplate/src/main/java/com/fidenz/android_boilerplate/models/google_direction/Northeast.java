package com.fidenz.android_boilerplate.models.google_direction;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Northeast implements Parcelable {
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;

    protected Northeast(Parcel in) {
        if (in.readByte() == 0) {
            lat = null;
        } else {
            lat = in.readDouble();
        }
        if (in.readByte() == 0) {
            lng = null;
        } else {
            lng = in.readDouble();
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (lat == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(lat);
        }
        if (lng == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(lng);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Northeast> CREATOR = new Creator<Northeast>() {
        @Override
        public Northeast createFromParcel(Parcel in) {
            return new Northeast(in);
        }

        @Override
        public Northeast[] newArray(int size) {
            return new Northeast[size];
        }
    };

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
