package com.fidenz.android_boilerplate.models.google_direction;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GoogleDirectionResponse implements Parcelable {
    @SerializedName("routes")
    @Expose
    private List<Route> routes = null;

    protected GoogleDirectionResponse(Parcel in) {
        routes = in.createTypedArrayList(Route.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(routes);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<GoogleDirectionResponse> CREATOR = new Creator<GoogleDirectionResponse>() {
        @Override
        public GoogleDirectionResponse createFromParcel(Parcel in) {
            return new GoogleDirectionResponse(in);
        }

        @Override
        public GoogleDirectionResponse[] newArray(int size) {
            return new GoogleDirectionResponse[size];
        }
    };

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }
}
