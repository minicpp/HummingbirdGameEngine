package game.hummingbird.helper;

public class HbeRect implements Cloneable {
    public float x1;
    public float y1;
    public float x2;
    public float y2;
    private boolean _bClean;

    HbeRect(float fx1, float fy1, float fx2, float fy2) {
        _bClean = false;
        x1 = fx1;
        y1 = fy1;
        x2 = fx2;
        y2 = fy2;
    }

    HbeRect() {
        _bClean = true;
    }

    void clear() {
        _bClean = true;
    }

    boolean isClean() {
        return _bClean;
    }

    void set(float fx1, float fy1, float fx2, float fy2) {
        _bClean = false;
        x1 = fx1;
        y1 = fy1;
        x2 = fx2;
        y2 = fy2;
    }

    void setRadius(float x, float y, float r) {
        x1 = x - r;
        x2 = x + r;
        y1 = y - r;
        y2 = y + r;
        _bClean = false;
    }

    void encapsulate(float x, float y) {
        if (_bClean) {
            x1 = x2 = x;
            y1 = y2 = y;
            _bClean = false;
        } else {
            if (x < x1)
                x1 = x;
            if (x > x2)
                x2 = x;
            if (y < y1)
                y1 = y;
            if (y > y2)
                y2 = y;
        }
    }

    boolean testPoint(float x, float y) {
        if (x >= x1 && x < x2 && y >= y1 && y < y2)
            return true;
        return false;
    }

    boolean intersect(HbeRect rect) {
        if (Math.abs(x1 + x2 - rect.x1 - rect.x2) < (x2 - x1 + rect.x2 - rect.x1))
            if (Math.abs(y1 + y2 - rect.y1 - rect.y2) < (y2 - y1 + rect.y2 - rect.y1))
                return true;
        return false;
    }

    public Object clone() {
        HbeRect r = null;
        try {
            return r = (HbeRect) super.clone();
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return r;
    }
}
