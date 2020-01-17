package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

import com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter.math.ImageMath;

public class PointillizeFilter extends CellularFilter {
    private int edgeColor = -16777216;
    private float edgeThickness = 0.4f;
    private boolean fadeEdges = false;
    private float fuzziness = 0.1f;

    public PointillizeFilter() {
        setScale(16.0f);
        setRandomness(0.0f);
    }

    public void setEdgeThickness(float edgeThickness) {
        this.edgeThickness = edgeThickness;
    }

    public float getEdgeThickness() {
        return this.edgeThickness;
    }

    public void setFadeEdges(boolean fadeEdges) {
        this.fadeEdges = fadeEdges;
    }

    public boolean getFadeEdges() {
        return this.fadeEdges;
    }

    public void setEdgeColor(int edgeColor) {
        this.edgeColor = edgeColor;
    }

    public int getEdgeColor() {
        return this.edgeColor;
    }

    public void setFuzziness(float fuzziness) {
        this.fuzziness = fuzziness;
    }

    public float getFuzziness() {
        return this.fuzziness;
    }

    public int getPixel(int x, int y, int[] inPixels, int width, int height) {
        float nx = ((this.m00 * ((float) x)) + (this.m01 * ((float) y))) / this.scale;
        float f = evaluate(nx + 1000.0f, (((this.m10 * ((float) x)) + (this.m11 * ((float) y))) / (this.scale * this.stretch)) + 1000.0f);
        float f1 = this.results[0].distance;
        int v = inPixels[(ImageMath.clamp((int) ((this.results[0].f447y - 1000.0f) * this.scale), 0, height - 1) * width) + ImageMath.clamp((int) ((this.results[0].f446x - 1000.0f) * this.scale), 0, width - 1)];
        if (!this.fadeEdges) {
            return ImageMath.mixColors(1.0f - ImageMath.smoothStep(this.edgeThickness, this.edgeThickness + this.fuzziness, f1), this.edgeColor, v);
        }
        float f2 = this.results[1].distance;
        return ImageMath.mixColors((0.5f * f1) / f2, v, inPixels[(ImageMath.clamp((int) ((this.results[1].f447y - 1000.0f) * this.scale), 0, height - 1) * width) + ImageMath.clamp((int) ((this.results[1].f446x - 1000.0f) * this.scale), 0, width - 1)]);
    }

    public String toString() {
        return "Pixellate/Pointillize...";
    }
}
