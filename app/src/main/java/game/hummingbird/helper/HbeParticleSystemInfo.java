package game.hummingbird.helper;

public class HbeParticleSystemInfo  implements Cloneable{
    static final int SPRITE = 0;
    static final int ANIMATE = 1;
    int type;// 0 for sprite 1 for animation
    private HbeSprite sprite; // texture blend animation

    public boolean bReverse;
    public float fRadiusMin;
    public float fRadiusMax;

    public int nEmission; // particles per sec
    public float fLifetime;

    public float fParticleLifeMin;
    public float fParticleLifeMax;

    /*
     * 0 up
     *pi/4 right
     * from up to right clock wise
     */
    public float fDirection;
    /*
     * the spread from the left direction side to right direction side
     */
    public float fSpread;
    public boolean bRelative;

    public float fSpeedMin;
    public float fSpeedMax;

    public float fGravityMin;
    public float fGravityMax;

    public float fRadialAccelMin;
    public float fRadialAccelMax;

    public float fTangentialAccelMin;
    public float fTangentialAccelMax;

    public float fSizeStart;
    public float fSizeEnd;
    public float fSizeVar;

    //spin = spin_start ~ spin_end*t
    //the init spin is randStart to let the init spin is different with each other
    //the end spin is End*t
    //the immediate speed is s+2t((e-s)/T)
    //the accel is 2((E-S)/T)
    public float fSpinStart;
    public float fSpinEnd;
    public float fSpinVar;

    public HbeColorRGB colColorStart = new HbeColorRGB(); // + alpha
    public HbeColorRGB colColorEnd = new HbeColorRGB();
    public float fColorVar;
    public float fAlphaVar;

    public void setSprite(HbeSprite sprite)
    {
        this.sprite = sprite;
        if(sprite instanceof HbeAnimation)
        {
            this.type = ANIMATE;
        }
        else
            this.type = SPRITE;
    }
    public HbeSprite getSprite()
    {
        return this.sprite;
    }
    public Object clone() {
        HbeParticleSystemInfo info = null;
        try {
            info = (HbeParticleSystemInfo)super.clone();
            info.colColorStart = (HbeColorRGB) colColorStart.clone();
            info.colColorEnd = (HbeColorRGB) colColorEnd.clone();
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return info;
    }
}
