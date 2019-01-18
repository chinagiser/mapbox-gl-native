package com.mapbox.mapboxsdk.location;

import android.animation.ValueAnimator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.mapbox.mapboxsdk.location.modes.PulseMode;

/**
 * Manages the logic of the interpolated animation which is applied to the LocationComponent's pulsing circle
 */
public class PulsingLocationCircleAnimator extends MapboxFloatAnimator {

  /**
   *
   * @param updateListener
   * @param maxAnimationFps
   * @param locationComponentOptions the stying options of the LocationComponent pulsing circle
   */
  public PulsingLocationCircleAnimator(AnimationsValueChangeListener updateListener,
                                       int maxAnimationFps, LocationComponentOptions locationComponentOptions) {
    super(0f, LocationComponentOptions.CIRCLE_PULSING_FINAL_RADIUS_DEFAULT, updateListener, maxAnimationFps);
    setDuration((long) locationComponentOptions.pulseSingleDuration());
    setRepeatMode(ValueAnimator.RESTART);
    setRepeatCount(ValueAnimator.INFINITE);
    setInterpolator(retrievePulseInterpolator(locationComponentOptions.pulseInterpolator()));
  }

  public Interpolator retrievePulseInterpolator(String desiredInterpolatorFromOptions) {
    switch(desiredInterpolatorFromOptions){
      case PulseMode.LINEAR:
        return new LinearInterpolator();
      case PulseMode.ACCELERATE:
        return new AccelerateInterpolator();
      case PulseMode.DECELERATE:
        return new DecelerateInterpolator();
      case PulseMode.BOUNCE:
        return new BounceInterpolator();
      default:
        return new DecelerateInterpolator();
    }
  }
}
