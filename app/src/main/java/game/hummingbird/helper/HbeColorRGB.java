package game.hummingbird.helper;

public class HbeColorRGB  implements Cloneable{
    public float r, g, b, a;

    public HbeColorRGB(float _r, float _g, float _b, float _a) {
        r = _r;
        g = _g;
        b = _b;
        a = _a;
    }

    public HbeColorRGB(int col) {
        setHWColor(col);
    }

    public HbeColorRGB() {
    }

    static final public float colorClamp(float x) {
        if (x < 0.0f)
            x = 0.0f;
        if (x > 1.0f)
            x = 1.0f;
        return x;
    }

    public void clamp() {
        r = colorClamp(r);
        g = colorClamp(g);
        b = colorClamp(b);
        a = colorClamp(a);
    }

    public void setHWColor(int col) {
        r = (col >>> 24) / 255.0f;
        g = ((col >>> 16) & 0xFF) / 255.0f;
        b = ((col >>> 8) & 0xFF) / 255.0f;
        a = (col & 0xFF) / 255.0f;
    }

    public int getHWColor() {
        return ((int) (r * 255.0f) << 24) + ((int) (g * 255.0f) << 16)
                + ((int) (b * 255.0f) << 8) + (int) (a * 255.0f);
    }

    HbeColorRGB sub(HbeColorRGB c, HbeColorRGB res) {
        res.r = r - c.r;
        res.g = g - c.g;
        res.b = b - c.b;
        res.a = a - c.a;
        return res;
    }

    HbeColorRGB sub(HbeColorRGB c) {
        r -= c.r;
        g -= c.g;
        b -= c.b;
        a -= c.a;
        return this;
    }

    HbeColorRGB add(HbeColorRGB c, HbeColorRGB res) {
        res.r = r + c.r;
        res.g = g + c.g;
        res.b = b + c.b;
        res.a = a + c.a;
        return res;
    }

    HbeColorRGB add(HbeColorRGB c) {
        r += c.r;
        g += c.g;
        b += c.b;
        a += c.a;
        return this;
    }

    HbeColorRGB mul(HbeColorRGB c, HbeColorRGB res) {
        res.r = r * c.r;
        res.g = g * c.g;
        res.b = b * c.b;
        res.a = a * c.a;
        return res;
    }

    HbeColorRGB mul(HbeColorRGB c) {
        r *= c.r;
        g *= c.g;
        b *= c.b;
        a *= c.a;
        return this;
    }

    HbeColorRGB mul(float scalar, HbeColorRGB res) {
        res.r = r * scalar;
        res.g = g * scalar;
        res.b = g * scalar;
        res.a = a * scalar;
        return res;
    }

    HbeColorRGB mul(float scalar) {
        r *= scalar;
        g *= scalar;
        b *= scalar;
        a *= scalar;
        return this;
    }

    HbeColorRGB div(float scalar, HbeColorRGB res) {
        res.r = r / scalar;
        res.g = g / scalar;
        res.b = g / scalar;
        res.a = a / scalar;
        return res;
    }

    HbeColorRGB div(float scalar) {
        r /= scalar;
        g /= scalar;
        b /= scalar;
        a /= scalar;
        return this;
    }

    public boolean equals(Object o) {
        // TODO Auto-generated method stub
        HbeColorRGB c = (HbeColorRGB) o;
        return (c.r == r && c.g == g && c.b == b && c.a == a);
    }

    public boolean unequals(HbeColorRGB c) {
        return (c.r != r || c.g != g || c.b != b || c.a != a);
    }

    public Object clone() {
        HbeColorRGB c = null;
        try {
            c = (HbeColorRGB)super.clone();
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return c;
    }
}
