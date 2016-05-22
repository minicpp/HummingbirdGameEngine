package game.hummingbird.core;

public class HbeColor {
    /*
     * int 31....24 23....16 15.....8 7......0 RRRRRRRR GGGGGGGG BBBBBBBB
     * AAAAAAAA
     */
    private static float _fcolArray[] = new float[4];

    // from integer color vertex to long

    public static final int setR(int col, int r) {
        return (col & 0xFFFFFF) | ((r & 0xFF) << 24);
    }

    public static final int setG(int col, int g) {
        return (col & 0xFF00FFFF) | ((g & 0xFF) << 16);
    }

    public static final int setB(int col, int b) {
        return (col & 0xFFFF00FF) | ((b & 0xFF) << 8);
    }

    public static final int setA(int col, int a) {
        return (col & 0xFFFFFF00) | (a & 0xFF);
    }

    public static final int convertColor(int r, int g, int b, int a) {
        int col = 0x0;
        col = r;
        col <<= 8;
        col |= g;
        col <<= 8;
        col |= b;
        col <<= 8;
        col |= a;
        return col;
    };

    public static final int getR(int col) {
        return (col >>> 24);
    }

    public static final int getG(int col) {
        return ((col & 0x00FF0000) >>> 16);
    }

    public static final int getB(int col) {
        return ((col & 0x0000FF00) >>> 8);
    }

    public static final int getA(int col) {
        return (col & 0x000000FF);
    }

    // from long color to float between 0~1.0f
    public static final float[] convertColor(int col) {
        _fcolArray[0] = ((col >>> 24)) / 255.0f;
        _fcolArray[1] = ((col >>> 16) & 0xFF) / 255.0f;
        _fcolArray[2] = ((col >>> 8) & 0xFF) / 255.0f;
        _fcolArray[3] = (col & 0xFF) / 255.0f;
        return _fcolArray;
    }
}