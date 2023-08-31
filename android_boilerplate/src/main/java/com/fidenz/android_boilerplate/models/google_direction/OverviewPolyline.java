package com.fidenz.android_boilerplate.models.google_direction;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OverviewPolyline implements Parcelable {
    @SerializedName("points")
    @Expose
    private String points;

    protected OverviewPolyline(Parcel in) {
        points = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(points);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OverviewPolyline> CREATOR = new Creator<OverviewPolyline>() {
        @Override
        public OverviewPolyline createFromParcel(Parcel in) {
            return new OverviewPolyline(in);
        }

        @Override
        public OverviewPolyline[] newArray(int size) {
            return new OverviewPolyline[size];
        }
    };

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}
