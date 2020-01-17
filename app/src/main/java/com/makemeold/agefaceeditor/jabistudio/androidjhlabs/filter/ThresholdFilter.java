package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

//import android.support.v4.media.TransportMediator;
import android.support.v4.view.ViewCompat;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.math.ImageMath;
import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.util.PixelUtils;

public class ThresholdFilter extends PointFilter {
    private int black;
    private int lowerThreshold;
    private int upperThreshold;
    private int white;

    /*public ThresholdFilter() {
        this(TransportMediator.KEYCODE_MEDIA_PAUSE);
    }*/

    public ThresholdFilter(int t) {
        this.white = ViewCompat.MEASURED_SIZE_MASK;
        this.black = 0;
        setLowerThreshold(t);
        setUpperThreshold(t);
    }

    public ThresholdFilter() {

    }

    public void setLowerThreshold(int lowerThreshold) {
        this.lowerThreshold = lowerThreshold;
    }

    public int getLowerThreshold() {
        return this.lowerThreshold;
    }

    public void setUpperThreshold(int upperThreshold) {
        this.upperThreshold = upperThreshold;
    }

    public int getUpperThreshold() {
        return this.upperThreshold;
    }

    public void setWhite(int white) {
        this.white = white;
    }

    public int getWhite() {
        return this.white;
    }

    public void setBlack(int black) {
        this.black = black;
    }

    public int getBlack() {
        return this.black;
    }

    public int filterRGB(int x, int y, int rgb) {
        return (-16777216 & rgb) | (ImageMath.mixColors(ImageMath.smoothStep((float) this.lowerThreshold, (float) this.upperThreshold, (float) PixelUtils.brightness(rgb)), this.black, this.white) & ViewCompat.MEASURED_SIZE_MASK);
    }

    public String toString() {
        return "Stylize/Threshold...";
    }
}
