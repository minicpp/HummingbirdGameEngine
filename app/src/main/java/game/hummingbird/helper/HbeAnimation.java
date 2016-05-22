package game.hummingbird.helper;

import game.hummingbird.core.HbeTexture;

public class HbeAnimation extends HbeSprite {
    public final static int ANIM_FWD = 0;// forward direction
    public final static int ANIM_REV = 1;// reverse direction
    public final static int ANIM_PINGPONG = 2;
    public final static int ANIM_NOPINGPONG = 0;
    public final static int ANIM_LOOP = 4;
    public final static int ANIM_NOLOOP = 0;

    /*
     * tex is a handle of texture nframes is frame number per second frames sum
     * number x is the first frame left top hand y is the first frame left top
     * hand w is the first frame width h is the first frame height
     */
    public HbeAnimation(HbeTexture tex, int nframes, float FPS, float x,
                        float y, float w, float h) {
        super(tex, x, y, w, h);
        _origWidth = tex.width;

        _fSinceLastFrame = -1.0f;
        _fSpeed = 1.0f / FPS;
        _bPlaying = false;
        _nFrames = nframes;

        _mode = ANIM_FWD | ANIM_LOOP;
        _nDelta = 1;
        setFrame(0);
    }

    public void play() {
        _bPlaying = true;
        _fSinceLastFrame = -1.0f;
        if ((_mode & ANIM_REV) != 0) {
            _nDelta = -1;
            setFrame(_nFrames - 1);
        } else {
            _nDelta = 1;
            setFrame(0);
        }
    }

    public void stop() {
        _bPlaying = false;
    }

    public void resume() {
        _bPlaying = true;
    }

    public void update(float fDeltaTime) {
        if (!_bPlaying)
            return;

        if (_fSinceLastFrame == -1.0f)
            _fSinceLastFrame = 0.0f;
        else
            _fSinceLastFrame += fDeltaTime;

        while (_fSinceLastFrame >= _fSpeed) {
            _fSinceLastFrame -= _fSpeed;

            if (_nCurFrame + _nDelta == _nFrames) {
                switch (_mode) {
                    case ANIM_FWD:
                    case ANIM_REV | ANIM_PINGPONG:
                        _bPlaying = false;
                        break;

                    case ANIM_FWD | ANIM_PINGPONG:
                    case ANIM_FWD | ANIM_PINGPONG | ANIM_LOOP:
                    case ANIM_REV | ANIM_PINGPONG | ANIM_LOOP:
                        _nDelta = -_nDelta;
                        break;
                }
            } else if (_nCurFrame + _nDelta < 0) {
                switch (_mode) {
                    case ANIM_REV:
                    case ANIM_FWD | ANIM_PINGPONG:
                        _bPlaying = false;
                        break;

                    case ANIM_REV | ANIM_PINGPONG:
                    case ANIM_REV | ANIM_PINGPONG | ANIM_LOOP:
                    case ANIM_FWD | ANIM_PINGPONG | ANIM_LOOP:
                        _nDelta = -_nDelta;
                        break;
                }
            }

            if (_bPlaying)
                setFrame(_nCurFrame + _nDelta);
        }
    }

    public boolean isPlaying() {
        return _bPlaying;
    }

    public void setTexture(HbeTexture htex) {
        super.setTexture(htex);
        _origWidth = htex.width;
    }

    public void setTextureRect(float x1, float y1, float x2, float y2) {
        super.setTextureRect(x1, y1, x2, y2);
        setFrame(_nCurFrame);
    }

    public void setMode(int mode) {
        _mode = mode;

        if ((_mode & ANIM_REV) != 0) {
            _nDelta = -1;
            setFrame(_nFrames - 1);
        } else {
            _nDelta = 1;
            setFrame(0);
        }
    }

    public void setSpeed(float FPS) {
        _fSpeed = 1.0f / FPS;
    }

    public void setFrame(int n) {
        float tx1, ty1, tx2, ty2;
        boolean bX, bY, bHS;
        int ncols = (int) (_origWidth) / (int) (_width);

        n = n % _nFrames;
        if (n < 0)
            n = _nFrames + n;
        _nCurFrame = n;

        // calc the coords of the frame n
        ty1 = _ty;
        tx1 = _tx + n * _width;

        if (tx1 > _origWidth - _width) {
            n -= (int) (_origWidth - _tx) / (int) (_width);
            tx1 = _width * (n % ncols);
            ty1 += _height * (1 + n / ncols);
        }

        tx2 = tx1 + _width;
        ty2 = ty1 + _height;

        tx1 /= _texWidth;
        ty1 /= _texHeight;
        tx2 /= _texWidth;
        ty2 /= _texHeight;

        _quad.v[0].tx = tx1;
        _quad.v[0].ty = ty1;
        _quad.v[1].tx = tx2;
        _quad.v[1].ty = ty1;
        _quad.v[2].tx = tx2;
        _quad.v[2].ty = ty2;
        _quad.v[3].tx = tx1;
        _quad.v[3].ty = ty2;

        bX = _bXFlip;
        bY = _bYFlip;
        bHS = _bHSFlip;
        _bXFlip = false;
        _bYFlip = false;
        setFlip(bX, bY, bHS);
    }

    public void setFrames(int n) {
        _nFrames = n;
    }

    public int getMode() {
        return _mode;
    }

    public float getSpeed() {
        return 1.0f / _fSpeed;
    }

    public int getFrame() {
        return _nCurFrame;
    }

    public int getFrames() {
        return _nFrames;
    }

    @SuppressWarnings("unused")
    private HbeAnimation() {
        ;
    }

    private int _origWidth;

    private boolean _bPlaying;

    private float _fSpeed;
    private float _fSinceLastFrame;

    private int _mode;
    private int _nDelta;
    private int _nFrames;
    private int _nCurFrame;
};