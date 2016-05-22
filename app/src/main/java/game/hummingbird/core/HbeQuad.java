package game.hummingbird.core;

public class HbeQuad implements Cloneable
{
    /*
     *    v0--------v1
     *    |          |
     *    |          |
     *    |          |
     *    |          |
     *    |          |
     *    v3--------v2
     */
    public HbeVertex	v[]= new HbeVertex[4];
    public HbeTexture	tex=null;
    public int		blend;
    public HbeQuad()
    {
        for(int i = 0; i< 4; ++i)
        {
            v[i] = new HbeVertex();
        }
    }
    public Object clone() {
        HbeQuad o = null;
        try {
            o = (HbeQuad) super.clone();
            o.v = new HbeVertex[4];
            for(int i = 0;i < 4; ++i)
            {
                o.v[i] = (HbeVertex) v[i].clone();
            }
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return o;
    }
}