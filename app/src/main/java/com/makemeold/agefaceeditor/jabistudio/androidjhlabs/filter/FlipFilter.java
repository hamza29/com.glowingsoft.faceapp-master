package com.makemeold.agefaceeditor.jabistudio.androidjhlabs.filter;

public class FlipFilter {
    public static final int FLIP_180 = 6;
    public static final int FLIP_90CCW = 5;
    public static final int FLIP_90CW = 4;
    public static final int FLIP_H = 1;
    public static final int FLIP_HV = 3;
    public static final int FLIP_V = 2;
    private int operation;

    public FlipFilter() {
        this(3);
    }

    public FlipFilter(int operation) {
        this.operation = operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public int getOperation() {
        return this.operation;
    }

    public int[] filter(int[] src, int W, int H) {
        int width = W;
        int height = H;
        int[] inPixels = src;
        int w = width;
        int h = height;
        int newW = w;
        int newH = h;
        int newX;
        int newY;
        switch (this.operation) {
            case 1:
                newX = width - (0 + w);
                break;
            case 2:
                newY = height - (0 + h);
                break;
            case 3:
                newW = h;
                newH = w;
                newX = 0;
                newY = 0;
                break;
            case 4:
                newW = h;
                newH = w;
                newX = height - (0 + h);
                newY = 0;
                break;
            case 5:
                newW = h;
                newH = w;
                newX = 0;
                newY = width - (0 + w);
                break;
            case 6:
                newX = width - (0 + w);
                newY = height - (0 + h);
                break;
        }
        int[] newPixels = new int[(newW * newH)];
        for (int row = 0; row < h; row++) {
            for (int col = 0; col < w; col++) {
                int index = (row * width) + col;
                int newRow = row;
                int newCol = col;
                switch (this.operation) {
                    case 1:
                        newCol = (w - col) - 1;
                        break;
                    case 2:
                        newRow = (h - row) - 1;
                        break;
                    case 3:
                        newRow = col;
                        newCol = row;
                        break;
                    case 4:
                        newRow = col;
                        newCol = (h - row) - 1;
                        break;
                    case 5:
                        newRow = (w - col) - 1;
                        newCol = row;
                        break;
                    case 6:
                        newRow = (h - row) - 1;
                        newCol = (w - col) - 1;
                        break;
                    default:
                        break;
                }
                newPixels[(newRow * newW) + newCol] = inPixels[index];
            }
        }
        return newPixels;
    }

    public String toString() {
        switch (this.operation) {
            case 1:
                return "Flip Horizontal";
            case 2:
                return "Flip Vertical";
            case 3:
                return "Flip Diagonal";
            case 4:
                return "Rotate 90";
            case 5:
                return "Rotate -90";
            case 6:
                return "Rotate 180";
            default:
                return "Flip";
        }
    }
}
