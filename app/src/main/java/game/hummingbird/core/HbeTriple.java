package game.hummingbird.core;

public class HbeTriple
{
    public HbeVertex		v[] = new HbeVertex[3];
    public HbeTexture		htex = null;
    public int		blend;
    public HbeTriple()
    {
        for(int i = 0; i< 3; ++i)
        {
            v[i] = new HbeVertex();
        }
    }
}