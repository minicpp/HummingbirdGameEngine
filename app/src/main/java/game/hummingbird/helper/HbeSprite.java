package game.hummingbird.helper;

import game.hummingbird.core.HbEngine;
import game.hummingbird.core.HbeQuad;
import game.hummingbird.core.HbeTexture;

public class HbeSprite implements Cloneable {
    public HbeSprite(HbeTexture tex, float texx, float texy, float w, float h) {
        float texx1, texy1, texx2, texy2;
        _tx = texx;
        _ty = texy;
        _width = w;
        _height = h;

        if (tex != null) {
            _texWidth = (float) tex.powerWidth;
            _texHeight = (float) tex.powerHeight;
        } else {
            _texWidth = _texHeight = 1.0f;
        }

        _hotX = 0;
        _hotY = 0;
        _bXFlip = false;
        _bYFlip = false;
        _bHSFlip = false;

        texx1 = texx / _texWidth;
        texy1 = texy / _texHeight;
        texx2 = (texx + w) / _texWidth;
        texy2 = (texy + h) / _texHeight;
        _quad = new HbeQuad();
        _quad.tex = tex;

        _quad.v[0].tx = texx1;
        _quad.v[0].ty = texy1;
        _quad.v[1].tx = texx2;
        _quad.v[1].ty = texy1;
        _quad.v[2].tx = texx2;
        _quad.v[2].ty = texy2;
        _quad.v[3].tx = texx1;
        _quad.v[3].ty = texy2;
        _quad.v[0].col = _quad.v[1].col = _quad.v[2].col = _quad.v[3].col = 0xFFFFFFFF;
        _quad.blend = HbEngine.BLEND_ALPHABLEND;
    }

    /*
     * render a sprite. the hotX and hotY at x,y
     */
    public void render(float x, float y) {
        float tempx1, tempy1, tempx2, tempy2;
        tempx1 = x - _hotX;
        tempy1 = y - _hotY;
        tempx2 = x + _width - _hotX;
        tempy2 = y + _height - _hotY;
        _quad.v[0].x = tempx1;
        _quad.v[0].y = tempy1;
        _quad.v[1].x = tempx2;
        _quad.v[1].y = tempy1;
        _quad.v[2].x = tempx2;
        _quad.v[2].y = tempy2;
        _quad.v[3].x = tempx1;
        _quad.v[3].y = tempy2;
        // Log.v("HbEngine","HbeSprite x:"+x);
        // Log.v("HbEngine","HbeSprite v[0]="+_quad.v[0].x);
        HbEngine.graphicRenderQuad(_quad);
    }

    /*
     * render the sprite, set two different corner one at left top one at right
     * bottom
     */
    public void renderStretch(float x1, float y1, float x2, float y2) {
        _quad.v[0].x = x1;
        _quad.v[0].y = y1;
        _quad.v[1].x = x2;
        _quad.v[1].y = y1;
        _quad.v[2].x = x2;
        _quad.v[2].y = y2;
        _quad.v[3].x = x1;
        _quad.v[3].y = y2;
        HbEngine.graphicRenderQuad(_quad);
    }

    /*
     * render a 4 point quadrangle It can be any shape
     */
    public void render4V(float x0, float y0, float x1, float y1, float x2,
                         float y2, float x3, float y3) {
        _quad.v[0].x = x0;
        _quad.v[0].y = y0;
        _quad.v[1].x = x1;
        _quad.v[1].y = y1;
        _quad.v[2].x = x2;
        _quad.v[2].y = y2;
        _quad.v[3].x = x3;
        _quad.v[3].y = y3;
        HbEngine.graphicRenderQuad(_quad);
    }

    /*
     * render a sprite with rotation
     */
    public void renderEx(float x, float y, float rot) {
        renderEx(x, y, rot, 1.0f, 1.0f);
    }

    public void renderEx(float x, float y, float rot, float hscale) {
        renderEx(x,y,rot,hscale,0);
    }
    public void renderEx(float x, float y, float rot, float hscale, float vscale) {
        float tx1, ty1, tx2, ty2;
        float sint, cost;

        if (vscale == 0)
            vscale = hscale;

        tx1 = -_hotX * hscale;
        ty1 = -_hotY * vscale;
        tx2 = (_width - _hotX) * hscale;
        ty2 = (_height - _hotY) * vscale;

        if (rot != 0.0f) {
            cost = (float) Math.cos(rot);
            sint = (float) Math.sin(rot);

            _quad.v[0].x = tx1 * cost - ty1 * sint + x;
            _quad.v[0].y = tx1 * sint + ty1 * cost + y;

            _quad.v[1].x = tx2 * cost - ty1 * sint + x;
            _quad.v[1].y = tx2 * sint + ty1 * cost + y;

            _quad.v[2].x = tx2 * cost - ty2 * sint + x;
            _quad.v[2].y = tx2 * sint + ty2 * cost + y;

            _quad.v[3].x = tx1 * cost - ty2 * sint + x;
            _quad.v[3].y = tx1 * sint + ty2 * cost + y;
        } else {
            _quad.v[0].x = tx1 + x;
            _quad.v[0].y = ty1 + y;
            _quad.v[1].x = tx2 + x;
            _quad.v[1].y = ty1 + y;
            _quad.v[2].x = tx2 + x;
            _quad.v[2].y = ty2 + y;
            _quad.v[3].x = tx1 + x;
            _quad.v[3].y = ty2 + y;
        }

        HbEngine.graphicRenderQuad(_quad);
    }

    public void setFlip(boolean bX, boolean bY, boolean bHotSpot) {
        float tx, ty;

        if (_bHSFlip && _bXFlip)
            _hotX = _width - _hotX;
        if (_bHSFlip && _bYFlip)
            _hotY = _height - _hotY;

        _bHSFlip = bHotSpot;

        if (_bHSFlip && _bXFlip)
            _hotX = _width - _hotX;
        if (_bHSFlip && _bYFlip)
            _hotY = _height - _hotY;

        if (bX != _bXFlip) {
            tx = _quad.v[0].tx;
            _quad.v[0].tx = _quad.v[1].tx;
            _quad.v[1].tx = tx;
            ty = _quad.v[0].ty;
            _quad.v[0].ty = _quad.v[1].ty;
            _quad.v[1].ty = ty;
            tx = _quad.v[3].tx;
            _quad.v[3].tx = _quad.v[2].tx;
            _quad.v[2].tx = tx;
            ty = _quad.v[3].ty;
            _quad.v[3].ty = _quad.v[2].ty;
            _quad.v[2].ty = ty;

            _bXFlip = !_bXFlip;
        }

        if (bY != _bYFlip) {
            tx = _quad.v[0].tx;
            _quad.v[0].tx = _quad.v[3].tx;
            _quad.v[3].tx = tx;
            ty = _quad.v[0].ty;
            _quad.v[0].ty = _quad.v[3].ty;
            _quad.v[3].ty = ty;
            tx = _quad.v[1].tx;
            _quad.v[1].tx = _quad.v[2].tx;
            _quad.v[2].tx = tx;
            ty = _quad.v[1].ty;
            _quad.v[1].ty = _quad.v[2].ty;
            _quad.v[2].ty = ty;

            _bYFlip = !_bYFlip;
        }
    }

    public void setTexture(HbeTexture tex) {
        float tx1, ty1, tx2, ty2;
        float tw, th;

        _quad.tex = tex;

        if (tex != null) {
            tw = (float) tex.powerWidth;
            th = (float) tex.powerHeight;
        } else {
            tw = 1.0f;
            th = 1.0f;
        }

        if (tw != _texWidth || th != _texHeight) {
            tx1 = _quad.v[0].tx * _texWidth;
            ty1 = _quad.v[0].ty * _texHeight;
            tx2 = _quad.v[2].tx * _texWidth;
            ty2 = _quad.v[2].ty * _texHeight;

            _texWidth = tw;
            _texHeight = th;

            tx1 /= tw;
            ty1 /= th;
            tx2 /= tw;
            ty2 /= th;

            _quad.v[0].tx = tx1;
            _quad.v[0].ty = ty1;
            _quad.v[1].tx = tx2;
            _quad.v[1].ty = ty1;
            _quad.v[2].tx = tx2;
            _quad.v[2].ty = ty2;
            _quad.v[3].tx = tx1;
            _quad.v[3].ty = ty2;
        }
    }

    /*
     * adjsize = true
     */
    public void setTextureRect(float x, float y, float w, float h) {
        setTextureRect(x, y, w, h, true);
    }

    public void setTextureRect(float x, float y, float w, float h,
                               boolean adjSize) {
        float tx1, ty1, tx2, ty2;
        boolean bX, bY, bHS;

        _tx = x;
        _ty = y;

        if (adjSize) {
            _width = w;
            _height = h;
        }

        tx1 = _tx / _texWidth;
        ty1 = _ty / _texHeight;
        tx2 = (_tx + w) / _texWidth;
        ty2 = (_ty + h) / _texHeight;

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

    public void setHotSpot(float x, float y) {
        _hotX = x;
        _hotY = y;
    }

    public void setColor(int col, int i) {
        _quad.v[i].col = col;
    }

    public void setColor(int col) {
        _quad.v[0].col = _quad.v[1].col = _quad.v[2].col = _quad.v[3].col = col;
    }

    public void setBlendMode(int blend) {
        _quad.blend = blend;
    }

    HbeTexture getTexture() {
        return _quad.tex;
    }

    int getColor() {
        return _quad.v[0].col;
    }

    int getColor(int i) {
        return _quad.v[i].col;
    }

    int getBlendMode() {
        return _quad.blend;
    }

    float getTextureRectX() {
        return _tx;
    }

    float getTextureRectY() {
        return _ty;
    }

    float getTextureRectW() {
        return _width;
    }

    float getTextureRectH() {
        return _height;
    }

    float getHotSpotX() {
        return _hotX;
    }

    float getHotSpotY() {
        return _hotY;
    }

    boolean getFlipX() {
        return _bXFlip;
    }

    boolean getFilpY() {
        return _bYFlip;
    }

    HbeRect getBoundingBox(float x, float y, HbeRect rect) {
        rect.set(x - _hotX, y - _hotY, x - _hotX + _width, y - _hotY + _height);
        return rect;
    }

    HbeRect getBoundingBoxEx(float x, float y, float rot, float hscale,
                             float vscale, HbeRect rect) {
        float tx1, ty1, tx2, ty2;
        float sint, cost;

        rect.clear();

        tx1 = -_hotX * hscale;
        ty1 = -_hotY * vscale;
        tx2 = (_width - _hotX) * hscale;
        ty2 = (_height - _hotY) * vscale;

        if (rot != 0.0f) {
            cost = (float) Math.cos(rot);
            sint = (float) Math.sin(rot);
            rect.encapsulate(tx1 * cost - ty1 * sint + x, tx1 * sint + ty1
                    * cost + y);
            rect.encapsulate(tx2 * cost - ty1 * sint + x, tx2 * sint + ty1
                    * cost + y);
            rect.encapsulate(tx2 * cost - ty2 * sint + x, tx2 * sint + ty2
                    * cost + y);
            rect.encapsulate(tx1 * cost - ty2 * sint + x, tx1 * sint + ty2
                    * cost + y);
        } else {
            rect.encapsulate(tx1 + x, ty1 + y);
            rect.encapsulate(tx2 + x, ty1 + y);
            rect.encapsulate(tx2 + x, ty2 + y);
            rect.encapsulate(tx1 + x, ty2 + y);
        }
        return rect;
    }

    public Object clone() {
        HbeSprite s = null;
        try {
            s = (HbeSprite) super.clone();
            s._quad = (HbeQuad) this._quad.clone();
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return s;
    }

    protected HbeSprite() {
    }

    protected HbeQuad _quad;
    protected float _tx, _ty, _width, _height;
    protected float _texWidth, _texHeight;
    protected float _hotX, _hotY;
    protected boolean _bXFlip, _bYFlip, _bHSFlip;
}
