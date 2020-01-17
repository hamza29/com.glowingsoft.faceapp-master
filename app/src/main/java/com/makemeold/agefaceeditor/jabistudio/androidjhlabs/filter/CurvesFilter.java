package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

public class CurvesFilter extends TransferFilter {
    private Curve[] curves;

    public CurvesFilter() {
        this.curves = new Curve[1];
        this.curves = new Curve[3];
        this.curves[0] = new Curve();
        this.curves[1] = new Curve();
        this.curves[2] = new Curve();
    }

    protected void initialize() {
        this.initialized = true;
        if (this.curves.length == 1) {
            int[] makeTable = this.curves[0].makeTable();
            this.bTable = makeTable;
            this.gTable = makeTable;
            this.rTable = makeTable;
            return;
        }
        this.rTable = this.curves[0].makeTable();
        this.gTable = this.curves[1].makeTable();
        this.bTable = this.curves[2].makeTable();
    }

    public void setCurve(Curve curve) {
        this.curves = new Curve[]{curve};
        this.initialized = false;
    }

    public void setCurves(Curve[] curves) {
        if (curves == null || !(curves.length == 1 || curves.length == 3)) {
            throw new IllegalArgumentException("Curves must be length 1 or 3");
        }
        this.curves = curves;
        this.initialized = false;
    }

    public Curve[] getCurves() {
        return this.curves;
    }

    public String toString() {
        return "Colors/Curves...";
    }
}
