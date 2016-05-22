package game.hummingbird.helper;


import android.util.Log;

import game.hummingbird.HbeConfig;
import game.hummingbird.core.HbEngine;
import game.hummingbird.core.HbeColor;
import game.hummingbird.core.HbeUtility;

public class HbeParticleSystem implements Cloneable {

    public HbeParticleSystemInfo info;

    private void _loadParticleFile(String filename,HbeSprite sprite){
        byte[] buf = HbEngine.resourceLoad(filename);
        if (buf.length != 128)
            Log.v("HbeParticleSystem", "the file " + filename
                    + " don't have proper size");
        info.bReverse = false;
        info.setSprite(sprite);
        int pos = 4;
        byte[] bit4 = new byte[4];

        System.arraycopy(buf, pos, bit4, 0, 4);
        info.nEmission = HbeUtility.bytesToInt(bit4);
        pos += 4;

        System.arraycopy(buf, pos, bit4, 0, 4);
        info.fLifetime = HbeUtility.bytesToFloat(bit4);
        pos += 4;

        System.arraycopy(buf, pos, bit4, 0, 4);
        info.fParticleLifeMin = HbeUtility.bytesToFloat(bit4);
        pos += 4;

        System.arraycopy(buf, pos, bit4, 0, 4);
        info.fParticleLifeMax = HbeUtility.bytesToFloat(bit4);
        pos += 4;

        System.arraycopy(buf, pos, bit4, 0, 4);
        info.fDirection = HbeUtility.bytesToFloat(bit4);
        pos += 4;

        System.arraycopy(buf, pos, bit4, 0, 4);
        info.fSpread = HbeUtility.bytesToFloat(bit4);
        pos += 4;

        System.arraycopy(buf, pos, bit4, 0, 1);
        if (bit4[0] == 0)
            info.bRelative = false;
        else
            info.bRelative = true;
        pos += 4;// bool is also four for align

        // speed
        System.arraycopy(buf, pos, bit4, 0, 4);
        info.fSpeedMin = HbeUtility.bytesToFloat(bit4);
        pos += 4;
        System.arraycopy(buf, pos, bit4, 0, 4);
        info.fSpeedMax = HbeUtility.bytesToFloat(bit4);
        pos += 4;

        // gravity
        System.arraycopy(buf, pos, bit4, 0, 4);
        info.fGravityMin = HbeUtility.bytesToFloat(bit4);
        pos += 4;
        System.arraycopy(buf, pos, bit4, 0, 4);
        info.fGravityMax = HbeUtility.bytesToFloat(bit4);
        pos += 4;

        // radial accel
        System.arraycopy(buf, pos, bit4, 0, 4);
        info.fRadialAccelMin = HbeUtility.bytesToFloat(bit4);
        pos += 4;
        System.arraycopy(buf, pos, bit4, 0, 4);
        info.fRadialAccelMax = HbeUtility.bytesToFloat(bit4);
        pos += 4;

        // tangential accel
        System.arraycopy(buf, pos, bit4, 0, 4);
        info.fTangentialAccelMin = HbeUtility.bytesToFloat(bit4);
        pos += 4;
        System.arraycopy(buf, pos, bit4, 0, 4);
        info.fTangentialAccelMax = HbeUtility.bytesToFloat(bit4);
        pos += 4;

        // size
        System.arraycopy(buf, pos, bit4, 0, 4);
        info.fSizeStart = HbeUtility.bytesToFloat(bit4);
        pos += 4;
        System.arraycopy(buf, pos, bit4, 0, 4);
        info.fSizeEnd = HbeUtility.bytesToFloat(bit4);
        pos += 4;
        System.arraycopy(buf, pos, bit4, 0, 4);
        info.fSizeVar = HbeUtility.bytesToFloat(bit4);
        pos += 4;

        // spin
        System.arraycopy(buf, pos, bit4, 0, 4);
        info.fSpinStart = HbeUtility.bytesToFloat(bit4);
        pos += 4;
        System.arraycopy(buf, pos, bit4, 0, 4);
        info.fSpinEnd = HbeUtility.bytesToFloat(bit4);
        pos += 4;
        System.arraycopy(buf, pos, bit4, 0, 4);
        info.fSpinVar = HbeUtility.bytesToFloat(bit4);
        pos += 4;

        // color start
        System.arraycopy(buf, pos, bit4, 0, 4);
        info.colColorStart.r = HbeUtility.bytesToFloat(bit4);
        pos += 4;
        System.arraycopy(buf, pos, bit4, 0, 4);
        info.colColorStart.g = HbeUtility.bytesToFloat(bit4);
        pos += 4;
        System.arraycopy(buf, pos, bit4, 0, 4);
        info.colColorStart.b = HbeUtility.bytesToFloat(bit4);
        pos += 4;
        System.arraycopy(buf, pos, bit4, 0, 4);
        info.colColorStart.a = HbeUtility.bytesToFloat(bit4);
        pos += 4;

        // color end
        System.arraycopy(buf, pos, bit4, 0, 4);
        info.colColorEnd.r = HbeUtility.bytesToFloat(bit4);
        pos += 4;
        System.arraycopy(buf, pos, bit4, 0, 4);
        info.colColorEnd.g = HbeUtility.bytesToFloat(bit4);
        pos += 4;
        System.arraycopy(buf, pos, bit4, 0, 4);
        info.colColorEnd.b = HbeUtility.bytesToFloat(bit4);
        pos += 4;
        System.arraycopy(buf, pos, bit4, 0, 4);
        info.colColorEnd.a = HbeUtility.bytesToFloat(bit4);
        pos += 4;

        // color var
        System.arraycopy(buf, pos, bit4, 0, 4);
        info.fColorVar = HbeUtility.bytesToFloat(bit4);
        pos += 4;

        // alpha var
        System.arraycopy(buf, pos, bit4, 0, 4);
        info.fAlphaVar = HbeUtility.bytesToFloat(bit4);
        pos += 4;

    }

    public void resetParticleFile(String filename){
        _loadParticleFile(filename, info.getSprite());
    }

    public HbeParticleSystem(String filename, HbeSprite sprite) {
        info = new HbeParticleSystemInfo();
        _loadParticleFile(filename, sprite);
        ////////////////////////////////////
        _vecLocation.x=_vecPrevLocation.x=0.0f;
        _vecLocation.y=_vecPrevLocation.y=0.0f;
        _fTx=_fTy=0;
        _fScale = 1.0f;

        _fEmissionResidue=0.0f;
        _nParticlesAlive=0;
        _fAge=-2.0f;

        _rectBoundingBox.clear();
        _bUpdateBoundingBox=false;
    }

    public HbeParticleSystem(HbeParticleSystemInfo hsi) {

        info = hsi;
        _vecLocation.x=_vecPrevLocation.x=0.0f;
        _vecLocation.y=_vecPrevLocation.y=0.0f;
        _fTx=_fTy=0;
        _fScale = 1.0f;

        _fEmissionResidue=0.0f;
        _nParticlesAlive=0;
        _fAge=-2.0f;

        _rectBoundingBox.clear();
        _bUpdateBoundingBox=false;
    }

    public Object clone() {
        HbeParticleSystem s = null;
        try {
            s = (HbeParticleSystem) super.clone();
            s.info = (HbeParticleSystemInfo) info.clone();
            s._vecLocation = (HbeVector) _vecLocation.clone();
            s._vecPrevLocation = (HbeVector) _vecPrevLocation.clone();
            s.particleHead = null;
            HbeParticle p = particleHead;
            HbeParticle temp = null;
            HbeParticle pos = s.particleHead;
            while(p!=null)
            {
                temp = HbeParticle.malloc();
                p.setSame(temp);
                p = p.next;
                if(pos==null)
                {
                    s.particleHead = temp;
                    temp.prev = null;
                    temp.next = null;
                }
                else
                {
                    pos.next = temp;
                    temp.prev = pos;
                    temp.next = null;
                }
                pos = temp;
            }
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return s;
    }

    public void render() {
        int col;
        HbeParticle par = particleHead;
        HbeSprite sprite = info.getSprite();
        col = sprite.getColor();
        while(par!=null)
        {
            if(info.colColorStart.r<0)
                sprite.setColor(HbeColor.setA(col,(int)par.colColor.a*255));
            else
                sprite.setColor(par.colColor.getHWColor());
            sprite.renderEx(par.vecLocation.x*_fScale+_fTx,
                    par.vecLocation.y*_fScale+_fTy,
                    par.fSpin*par.fAge,par.fSize*_fScale);
            par = par.next;
        }
        sprite.setColor(col);
    }

    public void fireAt(float x, float y) {
        stop();
        moveTo(x,y);
        fire();
    }

    public void fire() {
        if(info.fLifetime==-1.0f) _fAge=-1.0f;
        else _fAge=0.0f;
    }

    public void stop(boolean bKillParticles) {
        _fAge=-2.0f;
        if(bKillParticles)
        {
            _nParticlesAlive = 0;
            _rectBoundingBox.clear();
            HbeParticle p = null;
            while(particleHead!=null)
            {
                p = particleHead;
                HbeParticle.free(p);
                particleHead = particleHead.next;
            }
        }
    }

    public void stop() {
        stop(false);
    }

    private static HbeVector _vecAccel= new HbeVector();
    private static HbeVector _vecAccel2= new HbeVector();
    private static HbeVector _vectorTemp= new HbeVector();
    public void update(float fDeltaTime) {
        int i;
        float ang;
        HbeParticle par;


        if(_fAge >= 0)
        {
            _fAge += fDeltaTime;
            if(_fAge >= info.fLifetime) _fAge = -2.0f;
        }

        // update living particles

        if(_bUpdateBoundingBox) _rectBoundingBox.clear();
        par=particleHead;
        HbeParticle delP = null;
        while(par!=null)
        {
            par.fAge += fDeltaTime;
            if(par.fAge >= par.fTerminalAge)
            {
                _nParticlesAlive--;
                delP = par;
                par = par.next;
                _deleteParticle(delP);
                continue;
            }

            if (!this.info.bReverse) {
                par.vecLocation.sub(_vecLocation, _vecAccel);
                _vecAccel.normalize();
                if (Float.compare(_vecAccel.x, Float.NaN) == 0)
                    _vecAccel.x = 0;
                if (Float.compare(_vecAccel.y, Float.NaN) == 0)
                    _vecAccel.y = 0;
                _vecAccel2.x = _vecAccel.x;
                _vecAccel2.y = _vecAccel.y;
                _vecAccel.mul(par.fRadialAccel);

                // vecAccel2.Rotate(M_PI_2);
                // the following is faster
                ang = _vecAccel2.x;
                _vecAccel2.x = -_vecAccel2.y;
                _vecAccel2.y = ang;
                _vecAccel2.mul(par.fTangentialAccel);
                par.vecVelocity.add(_vecAccel.add(_vecAccel2).mul(fDeltaTime));
                // par.vecVelocity.x +=_vecAccel.x*fDeltaTime;
                // par.vecVelocity.y += _vecAccel.y*fDeltaTime;
                par.vecVelocity.y += par.fGravity * fDeltaTime;

                par.vecLocation.x += par.vecVelocity.x * fDeltaTime;
                par.vecLocation.y += par.vecVelocity.y * fDeltaTime;
            }
            else
            {
                _vecLocation.sub(par.vecLocation,_vectorTemp);
                _vectorTemp.normalize();//get direction
                par.vecVelocity.x += par.fRadialAccel*fDeltaTime;
                _vectorTemp.mul(par.vecVelocity.x);//get speed
                //update pos
                par.vecLocation.x += _vectorTemp.x * fDeltaTime;
                par.vecLocation.y += _vectorTemp.y * fDeltaTime;

            }

            par.fSpin += par.fSpinDelta*fDeltaTime;
            par.fSize += par.fSizeDelta*fDeltaTime;

            par.colColor.r += par.colColorDelta.r * fDeltaTime;
            par.colColor.g += par.colColorDelta.g * fDeltaTime;
            par.colColor.b += par.colColorDelta.b * fDeltaTime;
            par.colColor.a += par.colColorDelta.a * fDeltaTime;

            if(_bUpdateBoundingBox)
                _rectBoundingBox.encapsulate(par.vecLocation.x, par.vecLocation.y);
            par = par.next;
        }

        // generate new particles

        if(_fAge != -2.0f)
        {
            float fParticlesNeeded = info.nEmission*fDeltaTime + _fEmissionResidue;
            int nParticlesCreated = (int) fParticlesNeeded;
            _fEmissionResidue=fParticlesNeeded-(float)nParticlesCreated;
            float randomFloat;

            for(i=0; i<nParticlesCreated; i++)
            {
                if(_nParticlesAlive>= HbeConfig.MAX_PARTICLES) break;

                par= HbeParticle.malloc();

                par.next = particleHead;
                if(particleHead!=null)
                    particleHead.prev = par;
                particleHead = par;

                if(!info.bReverse)//normal particle
                {
                    par.fAge = 0.0f;
                    par.fTerminalAge = HbEngine.randomFloat(
                            info.fParticleLifeMin, info.fParticleLifeMax);

                    randomFloat = HbEngine.randomFloat(0.0f, 1.0f);
                    par.vecLocation.x = _vecPrevLocation.x
                            + (_vecLocation.x - _vecPrevLocation.x)
                            * randomFloat;
                    par.vecLocation.y = _vecPrevLocation.y
                            + (_vecLocation.y - _vecPrevLocation.y)
                            * randomFloat;
                    par.vecLocation.x += HbEngine.randomFloat(-2.0f, 2.0f);
                    par.vecLocation.y += HbEngine.randomFloat(-2.0f, 2.0f);

                    ang = info.fDirection - HbeConfig.M_PI_2
                            + HbEngine.randomFloat(0, info.fSpread)
                            - info.fSpread / 2.0f;

                    _vecPrevLocation.sub(_vecLocation, _vectorTemp);

                    if (info.bRelative)
                        ang += _vectorTemp.angle() + HbeConfig.M_PI_2;
                    randomFloat = HbEngine.randomFloat(info.fSpeedMin,
                            info.fSpeedMax);
                    par.vecVelocity.x = (float) Math.cos(ang) * randomFloat;
                    par.vecVelocity.y = (float) Math.sin(ang) * randomFloat;

                    par.fGravity = HbEngine.randomFloat(info.fGravityMin,
                            info.fGravityMax);
                    par.fRadialAccel = HbEngine.randomFloat(
                            info.fRadialAccelMin, info.fRadialAccelMax);
                    par.fTangentialAccel = HbEngine.randomFloat(
                            info.fTangentialAccelMin, info.fTangentialAccelMax);
                }
                else//reverse particle
                {
                    //we need to set
                    //fAge\fTerminalAge\vecLocation\vecVelocity\fGravity\fRadialAccel\fTangentialAccel
                    //the vecVelocity we will just use the x axis, because we just need one speed value
                    //the speed speed is direct to point of the particle system
                    par.fAge = 0.0f;
                    //first random a degree
                    ang = info.fDirection - HbeConfig.M_PI_2
                            + HbEngine.randomFloat(0, info.fSpread)
                            - info.fSpread / 2.0f;
                    //random a radius
                    randomFloat = HbEngine.randomFloat(info.fRadiusMin,info.fRadiusMax);
                    //calc a position
                    par.vecLocation.x = _vecLocation.x+(float) (Math.cos(ang) * randomFloat);
                    par.vecLocation.y = _vecLocation.y+(float) (Math.sin(ang) * randomFloat);
                    //speed as usual
                    randomFloat = HbEngine.randomFloat(info.fSpeedMin,
                            info.fSpeedMax);
                    par.vecVelocity.x = Math.abs(HbEngine.randomFloat(info.fSpeedMin,info.fSpeedMax));
                    par.vecVelocity.y = 0;
                    //accel
                    //par.fRadialAccel = Math.abs(HbEngine.randomFloat(info.fRadialAccelMin, info.fRadialAccelMax));
                    par.fRadialAccel = HbEngine.randomFloat(info.fRadialAccelMin, info.fRadialAccelMax);
                    float length = _vecLocation.sub(par.vecLocation, _vectorTemp).length();

                    par.fTerminalAge = 0.0f;
                    par.fGravity = 0.0f;
                    par.fTangentialAccel = 0.0f;

                    float delta = par.vecVelocity.x * par.vecVelocity.x+ 2.0f * par.fRadialAccel * length;
                    if (par.fRadialAccel != 0.0f && delta > 0.0f)
                    {
                        par.fTerminalAge = (-par.vecVelocity.x + (float) (Math
                                .sqrt(delta)))
                                / par.fRadialAccel;
                        par.fTerminalAge = Math.abs(par.fTerminalAge);
                        if(par.fTerminalAge > 65535.0f)
                        {
                            par.fTerminalAge = 0.0f;
                        }
                    }


                }
                par.fSize = HbEngine.randomFloat(info.fSizeStart,
                        info.fSizeStart+(info.fSizeEnd-info.fSizeStart)*info.fSizeVar);
                par.fSizeDelta = (info.fSizeEnd - par.fSize)/par.fTerminalAge;

                par.fSpin = HbEngine.randomFloat(info.fSpinStart,
                        info.fSpinStart+(info.fSpinEnd-info.fSpinStart)*info.fSpinVar);
                par.fSpinDelta = (info.fSpinEnd - par.fSpin)/par.fTerminalAge;

                par.colColor.r = HbEngine.randomFloat(info.colColorStart.r,
                        info.colColorStart.r + (info.colColorEnd.r-info.colColorStart.r)*info.fColorVar);
                par.colColor.g = HbEngine.randomFloat(info.colColorStart.g,
                        info.colColorStart.g + (info.colColorEnd.g-info.colColorStart.g)*info.fColorVar);
                par.colColor.b = HbEngine.randomFloat(info.colColorStart.b,
                        info.colColorStart.b + (info.colColorEnd.b-info.colColorStart.b)*info.fColorVar);
                par.colColor.a = HbEngine.randomFloat(info.colColorStart.a,
                        info.colColorStart.a + (info.colColorEnd.a-info.colColorStart.a)*info.fAlphaVar);

                par.colColorDelta.r = (info.colColorEnd.r - par.colColor.r)/par.fTerminalAge;
                par.colColorDelta.g = (info.colColorEnd.g - par.colColor.g)/par.fTerminalAge;
                par.colColorDelta.b = (info.colColorEnd.b - par.colColor.b)/par.fTerminalAge;
                par.colColorDelta.a = (info.colColorEnd.a - par.colColor.a)/par.fTerminalAge;
                if(_bUpdateBoundingBox)_rectBoundingBox.encapsulate(par.vecLocation.x, par.vecLocation.y);
                _nParticlesAlive++;
            }
        }

        _vecPrevLocation.x=_vecLocation.x;
        _vecPrevLocation.y=_vecLocation.y;
    }

    public void moveTo(float x, float y) {
        moveTo(x, y, false);
    }

    public void moveTo(float x, float y, boolean bMoveParticles) {
        float dx,dy;

        if(bMoveParticles)
        {
            dx=x-_vecLocation.x;
            dy=y-_vecLocation.y;

            HbeParticle p = particleHead;
            while(p!=null)
            {
                p.vecLocation.x += dx;
                p.vecLocation.y += dy;
                p = p.next;
            }

            _vecPrevLocation.x=_vecPrevLocation.x + dx;
            _vecPrevLocation.y=_vecPrevLocation.y + dy;
        }
        else
        {
            if(_fAge==-2.0) { _vecPrevLocation.x=x; _vecPrevLocation.y=y; }
            else { _vecPrevLocation.x=_vecLocation.x;	_vecPrevLocation.y=_vecLocation.y; }
        }

        _vecLocation.x=x;
        _vecLocation.y=y;
    }

    public void transpose(float x, float y) {
        _fTx = x;
        _fTy = y;
    }

    public void setScale(float scale) {
        _fScale = scale;
    }

    public void trackBoundingBox(boolean bTrack) {
        _bUpdateBoundingBox = bTrack;
    }

    public int getParticlesAlive() {
        int i=0;
        HbeParticle p = particleHead;
        while(p!=null){
            p = p.next;
            ++i;
        }
        if(_nParticlesAlive == i)
        {
            Log.v("alive:", "correct");
        }
        else
            Log.v("alive:","failed");
        return _nParticlesAlive;
    }

    public float getAge() {
        return _fAge;
    }

    public float getPositionX() {
        return _vecLocation.x;
    }

    public float getPositionY() {
        return _vecLocation.y;
    }

    public HbeVector getPosition(HbeVector v){
        v.x=_vecLocation.x;
        v.y=_vecLocation.y;
        return v;
    }

    public float getTranspositionX() {
        return _fTx;
    }

    public float getTranspositionY() {
        return _fTy;
    }
    public HbeVector getTransposition(HbeVector v)
    {
        v.x = _fTx;
        v.y = _fTy;
        return v;
    }

    public float getScale() {
        return _fScale;
    }

    public HbeRect getBoundingBox(HbeRect rect) {
        rect.x1 = _rectBoundingBox.x1 * _fScale;
        rect.y1 = _rectBoundingBox.y1 * _fScale;
        rect.x2 = _rectBoundingBox.x2 * _fScale;
        rect.y2 = _rectBoundingBox.y2 * _fScale;
        return rect;
    }

    private HbeParticleSystem() {
        //not be used now.
    }

    private void _deleteParticle(HbeParticle p){
        if(p.prev !=null )
            p.prev.next = p.next;
        else
            particleHead = p.next;
        if(p.next !=null )
            p.next.prev = p.prev;
        HbeParticle.free(p);
    }


    private float _fAge;
    private float _fEmissionResidue;

    private HbeVector _vecPrevLocation = new HbeVector();
    private HbeVector _vecLocation = new HbeVector();
    private float _fTx, _fTy;
    private float _fScale;

    private int _nParticlesAlive;
    private HbeRect _rectBoundingBox = new HbeRect();
    private boolean _bUpdateBoundingBox;

    private HbeParticle particleHead;

    public HbeParticleSystem prev = null;
    public HbeParticleSystem next = null;
    private static HbeParticleSystem _freeHead = null;
    private static int _freeSize;
    public static int getFreeSize()
    {
        HbeParticleSystem p = _freeHead;
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
    public static HbeParticleSystem malloc()
    {
        HbeParticleSystem p = null;
        if(_freeHead==null)
        {
            //allocat more object
            for(int i = 0; i< HbeConfig.PSYP_POOL_DELTA; ++i)
            {
                p = new HbeParticleSystem();
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
    public static void free(HbeParticleSystem p)
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
        HbeParticleSystem p = null;
        for(int i = 0; i< HbeConfig.INIT_PSYS_POOL_SIZE; ++i)
        {
            ++_freeSize;
            p = new HbeParticleSystem();
            p.next = _freeHead;
            if(_freeHead!=null)
                _freeHead.prev = p;
            _freeHead = p;
        }
    }
}
