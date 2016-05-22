package game.hummingbird.helper;

import android.util.Log;

import game.hummingbird.HbeConfig;

public class HbeParticle  implements Cloneable{
    public HbeVector vecLocation = new HbeVector();
    public HbeVector vecVelocity = new HbeVector();

    public float fGravity;
    public float fRadialAccel;
    public float fTangentialAccel;

    public float fSpin;
    public float fSpinDelta;

    public float fSize;
    public float fSizeDelta;

    public HbeColorRGB colColor = new HbeColorRGB(); // + alpha
    public HbeColorRGB colColorDelta = new HbeColorRGB();

    public float fAge;
    public float fTerminalAge;

    public HbeParticle prev = null;
    public HbeParticle next = null;

    public Object clone() {
        HbeParticle s = null;
        try {
            s = (HbeParticle)super.clone();
            s.vecLocation = (HbeVector) vecLocation.clone();
            s.vecVelocity = (HbeVector) vecVelocity.clone();
            s.colColor = (HbeColorRGB)colColor.clone();
            s.colColorDelta = (HbeColorRGB) colColorDelta.clone();
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return s;
    }

    public void setSame(HbeParticle p){
        p.colColor.r = this.colColor.r;
        p.colColor.g = this.colColor.g;
        p.colColor.b = this.colColor.b;
        p.colColor.a = this.colColor.a;
        p.colColorDelta.r = this.colColorDelta.r;
        p.colColorDelta.g = this.colColorDelta.g;
        p.colColorDelta.b = this.colColorDelta.b;
        p.colColorDelta.a = this.colColorDelta.a;
        p.vecLocation.x = this.vecLocation.x;
        p.vecLocation.y = this.vecLocation.y;
        p.vecVelocity.x = this.vecVelocity.x;
        p.vecVelocity.y = this.vecVelocity.y;
        p.fAge = this.fAge;
        p.fGravity = this.fGravity;
        p.fRadialAccel = this.fRadialAccel;
        p.fSize = this.fSize;
        p.fSizeDelta = this.fSizeDelta;
        p.fSpin = this.fSpin;
        p.fSpinDelta = this.fSpinDelta;
        p.fTangentialAccel = this.fTangentialAccel;
        p.fTerminalAge = this.fTerminalAge;
    }

    private static HbeParticle _freeHead = null;
    private static int _freeSize;
    public static int getFreeSize()
    {
        HbeParticle p = _freeHead;
        int size = 0;
        while(p != null)
        {
            ++size;
            p = p.next;
        }
        if(size == _freeSize)
        {
            Log.v("HbeParticle", "size = "+_freeSize+" is correct");
        }
        else
        {
            Log.v("HbeParticle", "size = "+size+" freeSize="+_freeSize+" is correct");
        }
        return _freeSize;
    }
    public static HbeParticle malloc()
    {
        HbeParticle p = null;
        if(_freeHead==null)
        {
            //allocat more object
            for(int i = 0; i< HbeConfig.PARTICLES_POOL_SIZE_DELTA; ++i)
            {
                p = new HbeParticle();
                p.next = _freeHead;
                if(_freeHead!=null)
                {
                    _freeHead.prev = p;
                }
                _freeHead = p;
            }
        }
        p = _freeHead;
        _freeHead = _freeHead.next;
        if(_freeHead!=null)
            _freeHead.prev = null;
        p.next = null;
        --_freeSize;
        return p;
    }
    public static void free(HbeParticle p)
    {
        p.next = _freeHead;
        p.prev = null;
        if(_freeHead !=null)
            _freeHead.prev = p;
        _freeHead = p;
        ++_freeSize;
    }
    static void initMemoryPool()
    {
        _freeSize = 0;
        _freeHead = null;
        HbeParticle p = null;
        for(int i = 0; i< HbeConfig.INIT_PARTICLES_POOL_SIZE; ++i)
        {
            ++_freeSize;
            p = new HbeParticle();
            p.next = _freeHead;
            if(_freeHead!=null)
                _freeHead.prev = p;
            _freeHead = p;
        }
    }
    private HbeParticle(){}
}
