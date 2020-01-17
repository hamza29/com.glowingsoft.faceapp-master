package com.makemeold.agefaceeditor.common;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

public class ShakeEventListener implements SensorEventListener {
    private static final int MAX_PAUSE_BETHWEEN_DIRECTION_CHANGE = 200;
    private static final int MAX_TOTAL_DURATION_OF_SHAKE = 400;
    private static final int MIN_DIRECTION_CHANGE = 3;
    private static final int MIN_FORCE = 10;
    private float lastX = 0.0f;
    private float lastY = 0.0f;
    private float lastZ = 0.0f;
    private int mDirectionChangeCount = 0;
    private long mFirstDirectionChangeTime = 0;
    private long mLastDirectionChangeTime;
    private OnShakeListener mShakeListener;

    public interface OnShakeListener {
        void onShake();
    }

    public void setOnShakeListener(OnShakeListener listener) {
        this.mShakeListener = listener;
    }

    public void onSensorChanged(SensorEvent se) {
        float x = se.values[0];
        float y = se.values[1];
        float z = se.values[2];
        if (Math.abs(((((x + y) + z) - this.lastX) - this.lastY) - this.lastZ) > 10.0f) {
            long now = System.currentTimeMillis();
            if (this.mFirstDirectionChangeTime == 0) {
                this.mFirstDirectionChangeTime = now;
                this.mLastDirectionChangeTime = now;
            }
            if (now - this.mLastDirectionChangeTime < 200) {
                this.mLastDirectionChangeTime = now;
                this.mDirectionChangeCount++;
                this.lastX = x;
                this.lastY = y;
                this.lastZ = z;
                if (this.mDirectionChangeCount >= 3 && now - this.mFirstDirectionChangeTime < 400) {
                    this.mShakeListener.onShake();
                    resetShakeParameters();
                    return;
                }
                return;
            }
            resetShakeParameters();
        }
    }

    private void resetShakeParameters() {
        this.mFirstDirectionChangeTime = 0;
        this.mDirectionChangeCount = 0;
        this.mLastDirectionChangeTime = 0;
        this.lastX = 0.0f;
        this.lastY = 0.0f;
        this.lastZ = 0.0f;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
