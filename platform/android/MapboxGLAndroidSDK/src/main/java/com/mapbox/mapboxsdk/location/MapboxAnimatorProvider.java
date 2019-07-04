package com.mapbox.mapboxsdk.location;

import android.animation.ValueAnimator;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

final class MapboxAnimatorProvider {

  private static MapboxAnimatorProvider INSTANCE;

  private MapboxAnimatorProvider() {
    // private constructor
  }

  public static MapboxAnimatorProvider getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new MapboxAnimatorProvider();
    }
    return INSTANCE;
  }

  MapboxLatLngAnimator latLngAnimator(LatLng previous, LatLng target,
                                      MapboxAnimator.AnimationsValueChangeListener updateListener,
                                      int maxAnimationFps) {
    return new MapboxLatLngAnimator(previous, target, updateListener, maxAnimationFps);
  }

  MapboxFloatAnimator floatAnimator(Float previous, Float target,
                                    MapboxAnimator.AnimationsValueChangeListener updateListener,
                                    int maxAnimationFps) {
    return new MapboxFloatAnimator(previous, target, updateListener, maxAnimationFps);
  }

  MapboxCameraAnimatorAdapter cameraAnimator(Float previous, Float target,
                                             MapboxAnimator.AnimationsValueChangeListener updateListener,
                                             @Nullable MapboxMap.CancelableCallback cancelableCallback) {
    return new MapboxCameraAnimatorAdapter(previous, target, updateListener, cancelableCallback);
  }

  PulsingLocationCircleAnimator pulsingCircleAnimator(MapboxAnimator.AnimationsValueChangeListener updateListener,
                                                      int maxAnimationFps,
                                                      float pulseSingleDuration,
                                                      float pulseMaxRadius,
                                                      String desiredInterpolatorFromOptions) {
    PulsingLocationCircleAnimator pulsingLocationCircleAnimator =
        new PulsingLocationCircleAnimator(updateListener, maxAnimationFps, pulseMaxRadius);
    Log.d("Mbgl-MapboxAnimatorProvider", "pulsingCircleAnimator: pulseSingleDuration = " + pulseSingleDuration);
    pulsingLocationCircleAnimator.setDuration((long) pulseSingleDuration);
    pulsingLocationCircleAnimator.setRepeatMode(ValueAnimator.RESTART);
    pulsingLocationCircleAnimator.setRepeatCount(ValueAnimator.INFINITE);
    pulsingLocationCircleAnimator.retrievePulseInterpolator(desiredInterpolatorFromOptions);
    pulsingLocationCircleAnimator.setInterpolator(
        pulsingLocationCircleAnimator.retrievePulseInterpolator(desiredInterpolatorFromOptions));
    return pulsingLocationCircleAnimator;
  }
}
