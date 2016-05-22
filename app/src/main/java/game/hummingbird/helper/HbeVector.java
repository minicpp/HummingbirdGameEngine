package game.hummingbird.helper;

public class HbeVector implements Cloneable {
    public float x, y;
    private static HbeVector s = new HbeVector();
    private static HbeVector t = new HbeVector();

    public HbeVector(float fx, float fy) {
        x = fx;
        y = fy;
    }

    public HbeVector() {
        x = y = 0;
    }

    public HbeVector unaryMin() {
        x = -x;
        y = -y;
        return this;
    }

    public HbeVector add(HbeVector v, HbeVector res) {
        res.x = x + v.x;
        res.y = y + v.y;
        return res;
    }

    public HbeVector add(HbeVector v) {
        x += v.x;
        y += v.y;
        return this;
    }

    public HbeVector sub(HbeVector v, HbeVector res) {
        res.x = x - v.x;
        res.y = y - v.y;
        return res;
    }

    public HbeVector sub(HbeVector v) {
        x -= v.x;
        y -= v.y;
        return this;
    }

    public HbeVector div(float scalar) {
        x /= scalar;
        y /= scalar;
        return this;
    }

    public HbeVector div(float scalar, HbeVector res) {
        res.x = x / scalar;
        res.y = y / scalar;
        return res;
    }

    public HbeVector mul(float scalar) {
        x *= scalar;
        y *= scalar;
        return this;
    }

    public HbeVector mul(float scalar, HbeVector res) {
        res.x = x * scalar;
        res.y = y * scalar;
        return res;
    }

    public float dot(HbeVector v) {
        return (x * v.x + y * v.y);
    }

    public float length() {
        return (float) Math.sqrt(dot(this));
    }

    public float angle() {
        return (float) Math.atan2(y, x);
    }

    public float angle(HbeVector v) {
        if (v != null) {
            s.x = x;
            s.y = y;
            t.x = x;
            t.y = y;
            s.normalize();
            t.normalize();
            return (float) Math.acos(s.dot(t));
        }
        return (float) Math.atan2(y, x);
    }

    /*
     * limit the length of the vector
     */
    public void clamp(float max) {
        if (length() > max) {
            normalize();
            x *= max;
            y *= max;
        }
    }

    public HbeVector normalize() {
        float rc = (float) (1.0f / Math.sqrt(dot(this)));
        x *= rc;
        y *= rc;
        return this;
    }

    public HbeVector rotate(float a) {
        s.x = (float) (x * Math.cos(a) - y * Math.sin(a));
        s.y = (float) (y * Math.sin(a) + y * Math.cos(a));
        x = s.x;
        y = s.y;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        // TODO Auto-generated method stub
        HbeVector v = (HbeVector) o;
        return (v.x == x && v.y == y);
    }

    public boolean unequals(HbeVector v) {
        return (v.x != x || v.y != y);
    }

    public Object clone() {
        HbeVector v = null;
        try {
            return v = (HbeVector) super.clone();
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return v;
    }
}
