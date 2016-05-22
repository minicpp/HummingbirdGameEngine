package game.hummingbird.helper;

import game.hummingbird.HbeConfig;
import game.hummingbird.core.HbEngine;
import game.hummingbird.core.HbeColor;
import game.hummingbird.core.HbeTexture;

public class HbeFont {
    public static final int LEFT = 0;
    public static final int RIGHT = 1;//slow
    public static final int CENTER = 2;//slow
    private static final int MASK = 0x03;

    public HbeFont(String filename) {
        _filename = filename;
        _tex = HbEngine.textureLoad(_filename);
        _tex.setLock(true);
        _fWidth = _tex.width / HbeConfig.COUNT_FONT;
        _fHeight = _tex.height / HbeConfig.COUNT_FONT;
        // ? x=64 y=288
        for (int len = letters.length, i = 0; i < len; ++i) {
            letters[i] = new HbeSprite(_tex, 2 * _fWidth, 9 * _fHeight,
                    _fWidth, _fHeight);
        }
        // load all texture
        for (int ilen = HbeConfig.FONT_TABLE.length, i = 0; i < ilen; ++i) {
            for (int jlen = HbeConfig.FONT_TABLE[i].length, j = 0; j < jlen; ++j) {
                letters[HbeConfig.FONT_TABLE[i][j]].setTextureRect(j * _fWidth,
                        i * _fHeight, _fWidth, _fHeight);
            }
        }
        _fScale = 1.0f;//scale of total
        _fProportion = 1.0f;//scale of width
        _fRot = 0.0f;
        _fTracking = 0.0f;//space between characters
        _fSpacing = 1.0f;//space between lines

    }

    public HbeFont(HbeTexture charArray) {
        _tex = charArray;
        _tex.setLock(true);
        _fWidth = _tex.width / HbeConfig.COUNT_FONT;
        _fHeight = _tex.height / HbeConfig.COUNT_FONT;
        // ? x=64 y=288
        for (int len = letters.length, i = 0; i < len; ++i) {
            letters[i] = new HbeSprite(_tex, 2 * _fWidth, 9 * _fHeight,
                    _fWidth, _fHeight);
        }
        // load all texture
        for (int ilen = HbeConfig.FONT_TABLE.length, i = 0; i < ilen; ++i) {
            for (int jlen = HbeConfig.FONT_TABLE[i].length, j = 0; j < jlen; ++j) {
                letters[HbeConfig.FONT_TABLE[i][j]].setTextureRect(j * _fWidth,
                        i * _fHeight, _fWidth, _fHeight);
            }
        }

        _fScale = 1.0f;//scale of total
        _fProportion = 1.0f;//scale of width
        _fRot = 0.0f;
        _fTracking = 0.0f;//space between characters
        _fSpacing = 1.0f;//space between lines

    }

    public void releaseFont() {
        HbEngine.textureFree(_tex);
    }


    /**
     * use StringBuilder for number generation
     */
    public void render(float x,float y,StringBuilder str){
        render(x,y,LEFT,str);
    }

    /**
     * left align rendering, the most efficient choice
     */
    public void render(float x, float y, String str)
    {
        render(x,y,LEFT,str);
    }

    public void render(float x,float y, int align,StringBuilder str){
        int i;
        float fx = x;

        align &= MASK;
        if (align == RIGHT)
            fx -= getStringWidth(str, false);
        if (align == CENTER)
            fx -= (int) (getStringWidth(str, false) / 2.0f);

        int pos = 0;
        int len = str.length();
        while (pos < len) {
            if (str.charAt(pos) == '\n') {
                y += (int) (_fHeight * _fScale * _fSpacing);
                fx = x;
                if (align == RIGHT)
                    fx -= getStringWidth(str.substring(pos + 1), false);
                if (align == CENTER)
                    fx -= (int) (getStringWidth(str.substring(pos + 1), false) / 2.0f);
            } else {
                i = str.charAt(pos);
                if (i >= letters.length)
                    i = '?';
                fx += _fScale * _fProportion;
                letters[i].renderEx(fx, y, _fRot, _fScale * _fProportion,
                        _fScale);
                fx += (_fWidth + _fTracking) * _fScale * _fProportion;
            }
            ++pos;
        }
    }

    /**
     * the top left, is x, y. Align is method of alignment.
     * HbeFont.LEFT is left align
     * HbeFont.RIGHT
     * HbeFont.CENTER
     * if there is '\n' in a string, the str will break in to a new line, the alignment is
     * not changed.
     * If there is unknown character, the '?' is applied.
     * The left align is more efficient than right or center.
     */
    public void render(float x, float y, int align, String str) {

        int i;
        float fx = x;

        align &= MASK;
        if (align == RIGHT)
            fx -= getStringWidth(str, false);
        if (align == CENTER)
            fx -= (int) (getStringWidth(str, false) / 2.0f);

        int pos = 0;
        int len = str.length();
        while (pos < len) {
            if (str.charAt(pos) == '\n') {
                y += (int) (_fHeight * _fScale * _fSpacing);
                fx = x;
                if (align == RIGHT)
                    fx -= getStringWidth(str.substring(pos + 1), false);
                if (align == CENTER)
                    fx -= (int) (getStringWidth(str.substring(pos + 1), false) / 2.0f);
            } else {
                i = str.charAt(pos);
                if (i >= letters.length)
                    i = '?';
                fx += _fScale * _fProportion;
                letters[i].renderEx(fx, y, _fRot, _fScale * _fProportion,
                        _fScale);
                fx += (_fWidth + _fTracking) * _fScale * _fProportion;
            }
            ++pos;
        }
    }

    /**
     * Set font color
     */
    public void setColor(int col) {
        _col = col;
        for (HbeSprite sp : letters) {
            sp.setColor(_col);
        }
    }

    /**
     * set font color as RGBA 0~255
     */
    public void setColor(int R, int G, int B, int A) {
        _col = HbeColor.convertColor(R, G, B, A);
        setColor(_col);
    }

    /**
     * set opacity  of font 0~255
     */
    public void setTransparent(int Optical) {
        _col = HbeColor.convertColor(255, 255, 255, Optical);
        setColor(_col);
    }


    /**
     * Set type of color blender.
     * Now it supports two types.
     * HbEngine.BLEND_ALPHABLEND: ordinary blender
     * HbEngine.BLEND_ALPHAADD: highlighted blender, like fire particles.
     */
    public void setBlendMode(int blend) {
        for (HbeSprite sp : letters) {
            sp.setBlendMode(blend);
        }
    }

    /**
     * set scale of font
     */
    public void setScale(float scale) {
        _fScale = scale;
    }

    /**
     * set width
     */
    public void setProportion(float prop) {
        _fProportion = prop;
    }

    /**
     * rotate each character
     */
    public void setRotation(float rot) {
        _fRot = rot;
    }

    /**
     * set width between characters, can be negative value
     */
    public void setTracking(float tracking) {
        _fTracking = tracking;
    }

    /**
     * set width between lines.
     */
    public void setSpacing(float spacing) {
        _fSpacing = spacing;
    }

    public int getColor() {
        return _col;
    }

    public int getBlendMode() {
        return _nBlend;
    }

    public float getScale() {
        return _fScale;
    }

    public float getProportion() {
        return _fProportion;
    }

    public float getRotation() {
        return _fRot;
    }

    public float getTracking() {
        return _fTracking;
    }

    public float getSpacing() {
        return _fSpacing;
    }

    //get total width for all texts
    // if bMultiline is false, just calculate width of the first line
    // otherwise, calculate the maximal width of lines.
    public float getStringWidth(String str, boolean bMultiline) {
        float linew, w = 0;

        int pos = 0;
        int len = str.length();
        while (pos < len) {
            linew = 0;
            while (pos < len && str.charAt(pos) != '\n') {
                linew += _fWidth + _fTracking;
                ++pos;
            }
            if (!bMultiline)
                return linew * _fScale * _fProportion;
            if (linew > w)
                w = linew;
            while (str.charAt(pos) == '\n' || str.charAt(pos) == '\r')
                ++pos;
        }
        return w * _fScale * _fProportion;
    }

    public float getStringWidth(StringBuilder str, boolean bMultiline) {
        float linew, w = 0;

        int pos = 0;
        int len = str.length();
        while (pos < len) {
            linew = 0;
            while (pos < len && str.charAt(pos) != '\n') {
                linew += _fWidth + _fTracking;
                ++pos;
            }
            if (!bMultiline)
                return linew * _fScale * _fProportion;
            if (linew > w)
                w = linew;
            while (str.charAt(pos) == '\n' || str.charAt(pos) == '\r')
                ++pos;
        }
        return w * _fScale * _fProportion;
    }

    public float getWidth() {
        return _fWidth;
    }

    public float getHeight() {
        return _fHeight;
    }

    public HbeSprite getSprite(char chr) {
        return letters[(int) chr];
    }

    private float _fScale = 1.0f;//total scale
    private float _fProportion = 1.0f;//width scale
    private float _fRot = 0.0f;
    private float _fTracking = 1.0f;//scale between characters
    private float _fSpacing = 1.0f;//line space scale
    private int _col;
    private int _nBlend;

    private HbeTexture _tex;
    private HbeSprite letters[] = new HbeSprite[128];

    private float _fHeight;
    private float _fWidth;
    private String _filename;
};
