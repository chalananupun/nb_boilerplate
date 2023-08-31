package com.fidenz.android_boilerplate.listeners;


import com.fidenz.android_boilerplate.models.google_direction.GoogleDirectionResponse;

public interface OnDirectionFetchedListener {
    void onResult(GoogleDirectionResponse result);
}
