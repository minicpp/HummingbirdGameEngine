package game.hummingbird.core;


public class HbeVertex implements Cloneable{
    public float x,y;//screen position
    public int col;//color
    public float tx,ty;//texture coordinates
    public Object clone()
    {
        HbeVertex o = null;
        try {
            o = (HbeVertex)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }
}