package game.hummingbird.core;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.util.Log;
import android.view.WindowManager;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import game.hummingbird.HbeConfig;


public class HbEngine {
    // for system
    static int _sceneWidth;
    static int _sceneHeight;
    static GLSurfaceView _surfaceView;
    static GL10 _gl;
    static Random _rand;
    static HbeResourceList _resItem;

    private static HbeRender _render;
    private static String _iniFileName;
    private static String _packetFileName;
    //private static InputStream _packetFileInputStream;
    private static HashMap<String, HbePacketItem> _packetMap;
    private static ArrayList<HbeSectionProperty> _iniSectionList;
    // for graphic
    public final static int BLEND_ALPHABLEND = 4;
    public final static int BLEND_ALPHAADD = 8;

    private final static int PRIM_TRIPLES = 1;
    private final static int PRIM_QUADS = 2;
    private final static int PRIM_LINES = 3;
    private static int _coordArray[] = new int[16];
    private static int _rgba[] = new int[4];

    private final static int VERTEX_SIZE = HbeConfig.VERTEX_NUM * 2;// x,y two
    // element
    private final static int INDEX_SIZE = HbeConfig.VERTEX_NUM * 6 / 4;
    private final static int COLOR_SIZE = HbeConfig.VERTEX_NUM * 4;
    private final static int TEXTURE_SIZE = HbeConfig.VERTEX_NUM * 2;

    private static ShortBuffer _indexBuffer;
    private static short[] _indexArray;
    private static int _indexPos;

    private static IntBuffer _vertexBuffer;
    private static int[] _vertexArray;
    private static int _vertexPos;
    private static int _drawPos;

    private static int _colorPos;
    private static IntBuffer _colorBuffer;
    private static int[] _colorArray;

    private static IntBuffer _textureBuffer;
    private static int[] _textureArray;
    private static int _texturePos;

    private static int _curPrimType;
    private static int _curTexture;
    private static int _curBlendMode;
    // for time
    private static long _measureTime;
    private static long _measureElapse;

    //for audio
    private static LinkedList<HbeAudio> _audioList;
    private static ArrayList<Object> _audioPauseList;
    private static long _nextSoundId = 1;
    private static SoundPool _soundPool = null;
    // some function which can be used to set engine before initialize engine
    public final static void sysSetActivity(HbeActivity activity) {
        HbeRender._activity = activity;
    }

    public final static HbeActivity sysGetActivity() {
        return HbeRender._activity;
    }

    public final static void sysSetScreen(int width, int height) {
        _sceneWidth = width;
        _sceneHeight = height;
    }

    /*
     * graphic graphic method for engine
     */
    public final static int sysGetSceneWidth() {
        return _sceneWidth;
    }

    public final static int sysGetSceneHeight() {
        return _sceneHeight;
    }

    public final static void sysSetFPS(int fps) {
        HbeRender._nHBEFPS = fps;
        if (HbeRender._nHBEFPS > 0)
            HbeRender._nFixedDelta = (long) (1000.0f / fps);
        else
            HbeRender._nFixedDelta = 0;
    }


    public final static int sysGetFPS() {
        return (int) HbeRender._nHBEFPS;
    }



    public final static boolean sysSetIniFile(String filename){

        _iniFileName = filename;
        if (!_isFileExist(filename)) {
            try{//create file
                FileOutputStream os = HbeRender._activity.openFileOutput(_iniFileName,0);
                os.close();
            }
            catch(Exception e)
            {
                Log.v("sysSetIniFile","Create file failed");
            }
            _serializeIni();//write to file
        }
        else
        {
            _unSerializeIni();//create object from file
        }
        return true;
    }
    public final static String sysGetIniFile(){
        return _iniFileName;
    }
    private final static byte[] _decrypt(byte[] buf,int size){
        for(int i=0;i<size;++i)
        {
            buf[i] ^= 0x89;
        }
        return buf;
    }
    public final static void sysSetPacketFile(String filename)
    {
        _packetFileName = filename;

        byte[] buf = new byte[13];
        int size = 0;
        try {
            InputStream packetFileInputStream = HbeRender._activity.getAssets().open(_packetFileName);

            packetFileInputStream.read(buf,0,13);
            if(!HbeUtility.compareString(buf, "HDHummingBird", 13))
            {
                Log.v("sysSetPacketFile", "Magic error");
                return;
            }
            packetFileInputStream.read(buf,0,4);
            size = HbeUtility.bytesToInt(buf);
            if(size < 0)
            {
                Log.v("sysSetPacketFile", "Header size < 0");
                return;
            }
            //decrypt header
            buf = new byte[size];
            packetFileInputStream.read(buf,0,size);
            buf = _decrypt(buf, size);
            //analysis header
            int pos = 0;
            size = HbeUtility.bytesToInt(buf);
            pos += 4;
            int fileNameLength = 0;
            byte[] intByte = new byte[4];
            byte[] tempByte = null;
            for(int i=0;i<size;++i)
            {
                //file path size
                System.arraycopy(buf, pos, intByte, 0, 4);
                pos +=4;
                fileNameLength = HbeUtility.bytesToInt(intByte);
                //file path string
                tempByte = new byte[fileNameLength];
                System.arraycopy(buf, pos, tempByte, 0, fileNameLength);
                pos +=fileNameLength;
                String itemName = new String(tempByte,"UTF-8");

                //file item
                HbePacketItem item = new HbePacketItem();
                //file item length
                System.arraycopy(buf, pos, intByte, 0, 4);
                pos +=4;
                item.size = HbeUtility.bytesToInt(intByte);
                //file item offset
                System.arraycopy(buf, pos, intByte, 0, 4);
                pos +=4;
                item.offset = HbeUtility.bytesToInt(intByte);
                _packetMap.put(itemName, item);
            }
            packetFileInputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
        }



    }

    public final static String sysGetPacketFile()
    {
        return _packetFileName;
    }

    /*
     * must be called first time
     */
    final static void sysInitStaticInstance() {
        // initial variable
        HbeRender._fTime = 0.0f;
        HbeRender._t0 = HbeRender._t0fps = System.currentTimeMillis();
        HbeRender._dt = HbeRender._cfps = 0;
        HbeRender._nFPS = 0;
        HbeRender._binit = false;
        _resItem = null;
        _rand = new Random();
        _iniSectionList = new ArrayList<HbeSectionProperty>();
        _packetMap =new HashMap<String, HbePacketItem>();
    }

    /*
     * Initializes hardware and software needed to run engine
     */
    public final static void sysInit() {
        _surfaceView = new GLSurfaceView(HbeRender._activity);
        _surfaceView.setEGLConfigChooser(HbeConfig.EGL_BIT, HbeConfig.EGL_BIT,
                HbeConfig.EGL_BIT, HbeConfig.EGL_BIT, HbeConfig.EGL_DEPT, 0);
        _render = new HbeRender();
        _surfaceView.setRenderer(_render);
        _surfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        HbeRender._activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//stop sleeping
        HbeRender._activity.setContentView(_surfaceView);
        _initGraphic();
        _initAudio();
    }

    public final static void sysExit() {
        HbEngine.sysAudioSpeaker();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static final void randomSeed(int seed) {
        if (seed == 0)
            _rand.setSeed(System.currentTimeMillis());
        else
            _rand.setSeed(seed);
    }

    public static final void sysAudioEarpiece()
    {
        AudioManager amAudioManager;
        amAudioManager = (AudioManager)HbeRender._activity.
                getSystemService(Context.AUDIO_SERVICE);
        amAudioManager.setSpeakerphoneOn(false);
        amAudioManager.setRouting(AudioManager.MODE_NORMAL,
                AudioManager.ROUTE_EARPIECE, AudioManager.ROUTE_ALL);
        HbeRender._activity.setVolumeControlStream(AudioManager.STREAM_VOICE_CALL
                |AudioManager.STREAM_MUSIC);
        amAudioManager.setMode(AudioManager.MODE_IN_CALL);
    }

    public static final void sysAudioSpeaker()
    {
        AudioManager amAudioManager;
        amAudioManager = (AudioManager)HbeRender._activity.
                getSystemService(Context.AUDIO_SERVICE);
        amAudioManager.setMode(AudioManager.MODE_NORMAL);
    }

    /*
     * include min and max
     */
    public static final int randomInt(int min, int max) {
        return (min + _rand.nextInt(max - min + 1));
    }

    public static final float randomFloat(float min, float max) {
        return (min + _rand.nextFloat() * (max - min));
    }

    public final static void graphicClear(int color) {
        float[] cv = HbeColor.convertColor(color);
        _gl.glClearColor(cv[0], cv[1], cv[2], cv[3]);
        _gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
    }

    public final static void graphicRenderQuad(HbeQuad quad) {
        // 4 condition will change state
        int tex = 0;
        if (quad.tex != null) {
            tex = quad.tex.tex[0];
        }
        // the primitive to draw changed. vertexPos full. texture change. blend
        // change
        if (_curPrimType != PRIM_QUADS || _vertexPos + 8 > VERTEX_SIZE
                || _curTexture != tex || _curBlendMode != quad.blend) {
            _renderBatch();
            // change current state to new state
            _curPrimType = PRIM_QUADS;
            if (_curTexture != tex) {
                if (_curTexture == 0) {// <=0
                    _gl.glEnable(GL10.GL_TEXTURE_2D);
                    _gl.glBindTexture(GL10.GL_TEXTURE_2D, tex);
                } else if (tex == 0) {
                    _gl.glDisable(GL10.GL_TEXTURE_2D);
                } else
                    _gl.glBindTexture(GL10.GL_TEXTURE_2D, tex);
                _curTexture = tex;

            }
            if (_curBlendMode != quad.blend) {
                _curBlendMode = quad.blend;
                if (_curBlendMode == BLEND_ALPHABLEND)
                    _gl.glBlendFunc(GL10.GL_SRC_ALPHA,
                            GL10.GL_ONE_MINUS_SRC_ALPHA);
                else
                    _gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
            }
        }
        _indexArray[_indexPos++] = (short) _drawPos;
        _indexArray[_indexPos++] = (short) (_drawPos + 1);
        _indexArray[_indexPos++] = (short) (_drawPos + 2);
        _indexArray[_indexPos++] = (short) _drawPos;
        _indexArray[_indexPos++] = (short) (_drawPos + 2);
        _indexArray[_indexPos++] = (short) (_drawPos + 3);
        _drawPos += 4;

        HbeVertex[] v = quad.v;
        int[] vrect = _getFixVertexCoord(v);
        _vertexArray[_vertexPos++] = vrect[0];
        _vertexArray[_vertexPos++] = vrect[1];
        _vertexArray[_vertexPos++] = vrect[2];
        _vertexArray[_vertexPos++] = vrect[3];
        _vertexArray[_vertexPos++] = vrect[4];
        _vertexArray[_vertexPos++] = vrect[5];
        _vertexArray[_vertexPos++] = vrect[6];
        _vertexArray[_vertexPos++] = vrect[7];

        vrect = _getFixColor(v);
        _colorArray[_colorPos++] = vrect[0];
        _colorArray[_colorPos++] = vrect[1];
        _colorArray[_colorPos++] = vrect[2];
        _colorArray[_colorPos++] = vrect[3];
        _colorArray[_colorPos++] = vrect[4];
        _colorArray[_colorPos++] = vrect[5];
        _colorArray[_colorPos++] = vrect[6];
        _colorArray[_colorPos++] = vrect[7];
        _colorArray[_colorPos++] = vrect[8];
        _colorArray[_colorPos++] = vrect[9];
        _colorArray[_colorPos++] = vrect[10];
        _colorArray[_colorPos++] = vrect[11];
        _colorArray[_colorPos++] = vrect[12];
        _colorArray[_colorPos++] = vrect[13];
        _colorArray[_colorPos++] = vrect[14];
        _colorArray[_colorPos++] = vrect[15];

        if (_curTexture != 0) {// >0
            vrect = _getFixTextureCoord(v);
            _textureArray[_texturePos++] = vrect[0];
            _textureArray[_texturePos++] = vrect[1];

            _textureArray[_texturePos++] = vrect[2];
            _textureArray[_texturePos++] = vrect[3];

            _textureArray[_texturePos++] = vrect[4];
            _textureArray[_texturePos++] = vrect[5];

            _textureArray[_texturePos++] = vrect[6];
            _textureArray[_texturePos++] = vrect[7];
        }
    }

    public final static void graphicRenderTriple(HbeTriple triple) {
        // 4 condition will change state
        // the primitive to draw changed. vertexPos full. texture change. blend
        // change
        int tex = 0;
        if (triple.htex != null) {
            tex = triple.htex.tex[0];
        }
        if (_curPrimType != PRIM_TRIPLES || _vertexPos + 6 > VERTEX_SIZE
                || _curTexture != tex || _curBlendMode != triple.blend) {
            _renderBatch();
            // change current state to new state
            _curPrimType = PRIM_TRIPLES;
            if (_curTexture != tex) {
                if (_curTexture == 0) {
                    _gl.glEnable(GL10.GL_TEXTURE_2D);
                    _gl.glBindTexture(GL10.GL_TEXTURE_2D, tex);
                } else if (tex == 0) {
                    _gl.glDisable(GL10.GL_TEXTURE_2D);
                } else
                    _gl.glBindTexture(GL10.GL_TEXTURE_2D, tex);
                _curTexture = tex;

            }
            if (_curBlendMode != triple.blend) {
                _curBlendMode = triple.blend;
                if (_curBlendMode == BLEND_ALPHABLEND)
                    _gl.glBlendFunc(GL10.GL_SRC_ALPHA,
                            GL10.GL_ONE_MINUS_SRC_ALPHA);
                else
                    _gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
            }
        }
        HbeVertex[] v = triple.v;
        int[] vrect = _getFixVertexCoord(v);

        _vertexArray[_vertexPos++] = vrect[0];
        _vertexArray[_vertexPos++] = vrect[1];
        _vertexArray[_vertexPos++] = vrect[2];
        _vertexArray[_vertexPos++] = vrect[3];
        _vertexArray[_vertexPos++] = vrect[4];
        _vertexArray[_vertexPos++] = vrect[5];

        vrect = _getFixColor(v);
        _colorArray[_colorPos++] = vrect[0];
        _colorArray[_colorPos++] = vrect[1];
        _colorArray[_colorPos++] = vrect[2];
        _colorArray[_colorPos++] = vrect[3];
        _colorArray[_colorPos++] = vrect[4];
        _colorArray[_colorPos++] = vrect[5];
        _colorArray[_colorPos++] = vrect[6];
        _colorArray[_colorPos++] = vrect[7];
        _colorArray[_colorPos++] = vrect[8];
        _colorArray[_colorPos++] = vrect[9];
        _colorArray[_colorPos++] = vrect[10];
        _colorArray[_colorPos++] = vrect[11];

        if (_curTexture != 0) {// >0
            vrect = _getFixTextureCoord(v);
            _textureArray[_texturePos++] = vrect[0];
            _textureArray[_texturePos++] = vrect[1];

            _textureArray[_texturePos++] = vrect[2];
            _textureArray[_texturePos++] = vrect[3];

            _textureArray[_texturePos++] = vrect[4];
            _textureArray[_texturePos++] = vrect[5];
        }
    }

    public final static void graphicRenderLine(float x1, float y1, float x2,
                                               float y2, int color) {
        if (_curPrimType != PRIM_LINES || _vertexPos + 4 > VERTEX_SIZE
                || _curTexture != 0 || _curBlendMode != BLEND_ALPHABLEND) {// >0
            _renderBatch();
            // change current state to new state
            _curPrimType = PRIM_LINES;
            if (_curTexture != 0) {// >0
                _gl.glDisable(GL10.GL_TEXTURE_2D);
                _curTexture = 0;
            }
            if (_curBlendMode != BLEND_ALPHABLEND) {
                _curBlendMode = BLEND_ALPHABLEND;
                if (_curBlendMode == BLEND_ALPHABLEND)
                    _gl.glBlendFunc(GL10.GL_SRC_ALPHA,
                            GL10.GL_ONE_MINUS_SRC_ALPHA);
                else
                    _gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE);
            }
        }

        _vertexArray[_vertexPos++] = (int) (x1 * 65535);
        _vertexArray[_vertexPos++] = (int) (y1 * 65535);
        _vertexArray[_vertexPos++] = (int) (x2 * 65535);
        _vertexArray[_vertexPos++] = (int) (y2 * 65535);

        int[] vrect = _convertColorToFixInt(color);
        _colorArray[_colorPos++] = vrect[0];
        _colorArray[_colorPos++] = vrect[1];
        _colorArray[_colorPos++] = vrect[2];
        _colorArray[_colorPos++] = vrect[3];
    }
    /*
     * Load music.
     * For the music, when it is playing, you call play again, will have no other effect.
     * For the sound, you call play multiple times, these sound will be mixed together.
     */
    public final static HbeMusic musicLoad(String filename){
        try {
            AssetFileDescriptor afd = HbeRender._activity.getAssets().openFd(filename);
            MediaPlayer mp = new MediaPlayer();
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.prepare();
            mp.setVolume(HbeConfig.VOLUMN_MUSIC_DEFAULT, HbeConfig.VOLUMN_MUSIC_DEFAULT);
            HbeMusic mus = new HbeMusic(mp,HbeConfig.VOLUMN_MUSIC_DEFAULT);
            _audioList.add(mus);
            return mus;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.v("musicLoad", e.toString());
        }
        return null;
    }

    /*
     * release music. Such as at the end of a level in game.
     * You can release the music of the present level.
     * Then enter a new level.
     */
    public final static void musicFree(HbeMusic hmusic){
        MediaPlayer mp = null;
        for(HbeAudio au:_audioList){
            if(au._type == HbeMusic.MUSIC){
                mp = ((HbeMusic)au).getMediaPlayer();
                if(mp == hmusic.getMediaPlayer())
                {
                    mp.release();
                    _audioList.remove(au);
                    break;
                }
            }
        }
    }


    public final static void musicFreeAll(){
        HbeMusic mu = null;
        HbeAudio au = null;
        ListIterator<HbeAudio> listIter = _audioList.listIterator();
        while(listIter.hasNext()){
            au = listIter.next();
            if(au._type == HbeMusic.MUSIC)
            {
                mu = (HbeMusic) au;
                if(mu.isLock() == false)
                {
                    mu.stop();
                    mu.freeMusic();
                    listIter.remove();
                }
            }
        }
    }
    public final static HbeSound soundLoad(String filename){
        AssetManager am = HbeRender._activity.getAssets();
        int soundID = -1;
        try{
            AssetFileDescriptor fd = am.openFd(filename);
            soundID = _soundPool.load(fd, 1);
        }
        catch(Exception ex)
        {
            return null;
        }
        HbeSound wave=new HbeSound();
        wave.sid = soundID;
        _audioList.add(wave);
        return wave;
    }

/*	public final static HbeSound soundLoad(String filename,int maxTracks){
		byte[] byteArray = resourceLoad(filename);
		byte[] bit4 = new byte[4];
		int pos = 0;
		int basepos =0;
		int chunkSize = 0;
		HbeSound wave = new HbeSound();
		System.arraycopy(byteArray, pos, bit4, 0, 4);
		if(!HbeUtility.compareString(bit4,"RIFF",4))
			return null;
		pos += 4;
		pos +=4;//long size
		System.arraycopy(byteArray, pos, bit4, 0, 4);
		if(!HbeUtility.compareString(bit4,"WAVE",4))
			return null;
		pos += 4;
		System.arraycopy(byteArray, pos, bit4, 0, 4);
		if(!HbeUtility.compareString(bit4,"fmt ",4))
			return null;
		pos +=4;
		//long chunkSize
		System.arraycopy(byteArray, pos, bit4, 0, 4);
		chunkSize =  HbeUtility.bytesToInt(bit4);
		pos +=4;
		basepos = pos;
		//short wFormatTag
		pos +=2;
		//channel
		wave.channel = byteArray[pos];
		if(wave.channel > 2)
			return null;
		pos +=1;
		if(byteArray[pos] != 0)
			return null;
		pos +=1;
		//unsigned long dwSamplesPerSec
		System.arraycopy(byteArray, pos, bit4, 0, 4);
		wave.samplePerSec = HbeUtility.bytesToInt(bit4);
		pos +=4;
		//unsigned long dwAvgBytesPerSec
		pos +=4;
		//unsigned short wBlockAlign
		pos +=2;
		//unsigned short wBitsPerSample
		System.arraycopy(byteArray, pos, bit4, 0, 2);
		bit4[2]=bit4[3]=0;
		wave.bitsPerSample = HbeUtility.bytesToInt(bit4);
		if(wave.bitsPerSample!=8 && wave.bitsPerSample !=16)
			return null;
		pos +=2;
		pos = basepos + chunkSize;
		//
		System.arraycopy(byteArray, pos, bit4, 0, 4);
		if(!HbeUtility.compareString(bit4,"data",4))
			return null;
		pos +=4;
		//long chunkSize
		System.arraycopy(byteArray, pos, bit4, 0, 4);
		int byteSize = HbeUtility.bytesToInt(bit4);
		pos +=4;
		wave.buf = new byte[byteSize];
		System.arraycopy(byteArray, pos,wave.buf , 0, byteSize);
		wave.frames = (byteSize/(wave.channel*(wave.bitsPerSample/8)));
		_audioList.add(wave);
		wave.initSoundTrack(maxTracks);
		return wave;
	}
	*/
    public final static void soundFree(HbeSound hsound){
        _soundPool.unload(hsound.sid);
    }
    //Free all sounds except lockeed
    public final static void soundFreeAll(){
        HbeSound sd = null;
        HbeAudio au = null;
        ListIterator<HbeAudio> listIter = _audioList.listIterator();
        while(listIter.hasNext()){
            au = listIter.next();
            if(au._type == HbeMusic.SOUND)
            {
                sd = (HbeSound) au;
                if(sd.isLock()==false)
                {
                    _soundPool.unload(sd.sid);
                    listIter.remove();
                }
            }
        }
    }

    public final static int soundPlay(HbeSound s)
    {
        return soundPlay(s,false,1.0f);
    }

    public final static int soundPlay(HbeSound s,
                                      boolean bloop,float volume)
    {

        if(HbeSoundQueueNode.queueSize >= HbeConfig.MAX_QUEUE_SOUND)
            return -1;
        HbeSoundQueueNode node = null;
        //test if the sound is duplicated in this update call
        for(int j=0;j<HbeSoundQueueNode.queueSize;++j)
        {
            node = HbeSoundQueueNode.queue[j];
            if(node.sound == s)//duplicate object
            {
                if(node.volume <1.0f)
                    node.volume += 0.5*volume;
                if(node.volume >1.0f)
                    node.volume = 1.0f;
                return -1;
            }
        }
        //find a slot for it
        if(HbeSoundQueueNode.queueSize>=HbeConfig.MAX_QUEUE_SOUND)
        {
            return -1;//no free slot for it
        }
        node = HbeSoundQueueNode.queue[HbeSoundQueueNode.queueSize++];
        node.loop = bloop;
        node.volume = volume;
        node.sound = s;
        return s.sid;
    }

    public final static void _soundPlay(HbeSound s,
                                        boolean bloop,float volume,long id){
        int loop = 0;
        if(bloop)
            loop = -1;
        _soundPool.play(s.sid, volume, volume, 1, loop, 1);
    }

    public final static void channelStop(int id){
        _soundPool.stop(id);
    }

    public final static void channelPause(int id){
        _soundPool.pause(id);
    }

    public final static void channelResume(int id){
        _soundPool.resume(id);
    }

    public final static void channelSetVolume(int id,float v){
        _soundPool.setVolume(id, v, v);
    }


    public final static void iniSetInt(String section,String name,int value)
    {
        iniSetString(section,name,value+"");
    }

    public final static int iniGetInt(String section,String name,int defValue)
    {
        return Integer.parseInt(iniGetString(section,name,defValue+""));
    }

    public final static void iniSetFloat(String section,String name,float value)
    {
        iniSetString(section,name,value+"");
    }

    public final static float iniGetFloat(String section,String name,float defValue)
    {
        return Float.parseFloat(iniGetString(section,name,defValue+""));
    }

    public final static void iniSetString(String section,String name,String value)
    {
        //Log.v("set string","set string:"+section+"; name:"+name+"; value:" + value);
        HbeSectionProperty s = _findSectionProperty(section);
        if(s!=null){
            HbeEntryProperty ep = _findEntryProperty(s, name);
            if(ep!=null)
                ep.value = value;
            else
            {
                s.entryList.add(new HbeEntryProperty(name,value));
            }
        }
        else
        {
            _iniSectionList.add(new HbeSectionProperty(section,name,value));
        }
        _serializeIni();
    }

    public final static String iniGetString(String section,String name,String defValue)
    {
        HbeSectionProperty s = _findSectionProperty(section);
        if(s!=null){
            HbeEntryProperty ep = _findEntryProperty(s, name);
            if(ep!=null)
            {
                return ep.value;
            }
        }
        return defValue;
    }

    public final static HbeSectionProperty _findSectionProperty(String section)
    {
        for(HbeSectionProperty sp:_iniSectionList)
        {
            if(sp.key.compareTo(section)==0)
            {
                return sp;
            }
        }
        return null;
    }
    public final static HbeEntryProperty _findEntryProperty(HbeSectionProperty section,
                                                            String key)
    {
        for(HbeEntryProperty ep:section.entryList){
            if(ep.key.compareTo(key) ==0)
            {
                return ep;
            }
        }
        return null;
    }

    public final static void timeMeasureStart() {
        _measureTime = System.nanoTime();
    }

    public final static long timeMeasureEnd() {
        _measureElapse = System.nanoTime() - _measureTime;
        return _measureElapse;
    }

    public final static long timeGetTimeElapse() {
        return _measureElapse;
    }

    public final static float timerGetTime() {
        return HbeRender._fTime;
    }

    public final static float timerGetDelta() {
        return HbeRender._fDeltaTime;
    }

    public final static int timerGetFPS() {
        return (int) HbeRender._nFPS;
    }

    private final static byte[] _resourceLoadFromPath(String path)
    {
        InputStream in = null;
        byte[] buf = null;
        try {
            in = HbeRender._activity.getAssets().open(path);
            int size = in.available();
            buf = new byte[size];
            in.read(buf, 0, size);
            in.close();
            return buf;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.v("exception",e.toString());
            return null;
        }
    }

    private final static byte[] _resourceLoadFromPacket(String path)
    {
        HbePacketItem item = _packetMap.get(path);
        if(item == null)
            return null;
        InputStream in = null;
        byte[] buf = new byte[item.size];
        try {
            in = HbeRender._activity.getAssets().open(_packetFileName);
            in.skip(item.offset);
            in.read(buf, 0,item.size);
            _decrypt(buf, item.size);
            in.close();
            return buf;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return null;
        }
    }

    public final static byte[] resourceLoad(String path) {
        byte[] res = _resourceLoadFromPath(path);
        if(res==null)
            res = _resourceLoadFromPacket(path);
        return res;
    }

    public final static HbeTexture textureLoad(String path) {
        try {
            Bitmap bitmap = null;
            byte[] byteArray = resourceLoad(path);
            ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
            HbeTexture t = new HbeTexture(path);
            if (in != null) {
                // from file asset
                bitmap = BitmapFactory.decodeStream(in);

                t = _textureLoad(t, bitmap, 0, 0, bitmap.getWidth(),
                        bitmap.getHeight());
                // bitmap.recycle();
                HbeTexture.add(t);
            }
            //System.gc();
            return t;
        } catch (Exception e) {
            Log.v("HbEngine::textureLoad", "Can not get texture on:" + path);
            return null;
        }
    }

    /*
     * This is to load a scale of bitmap from file to the texture
     */
    public final static HbeTexture textureLoad(String path, int left, int top,
                                               int width, int height) {
        try {
            Bitmap bitmap = null;
            byte[] byteArray = resourceLoad(path);
            ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
            HbeTexture t = new HbeTexture(path);
            if (in != null) {
                bitmap = BitmapFactory.decodeStream(in);
                t = _textureLoad(t, bitmap, left, top, width, height);
                bitmap.recycle();
                HbeTexture.add(t);
            }
            //System.gc();
            return t;
        } catch (Exception e) {
            Log.v("HbEngine::textureLoad", "Can not get texture on:" + path);
            return null;
        }
    }

    public final static void textureFree(HbeTexture tex) {
        _gl.glDeleteTextures(1, tex.tex, 0);
        HbeTexture.free(tex);
    }

    public static final void textureFreeAll( )
    {
        HbeTexture t = HbeTexture.head;
        HbeTexture dt = null;
        while(t!=null)
        {
            if(t.getLock() == false)
            {
                _gl.glDeleteTextures(1, t.tex, 0);
                dt = t;
                t = t.next;
                HbeTexture.free(dt);
            }
            else
                t = t.next;
        }
        //HbeTexture.head = null;
        //reset all graphic device variable
        _indexPos = 0;
        _vertexPos = 0;
        _drawPos = 0;
        _colorPos = 0;
        _texturePos = 0;
        _curTexture=0;
        _gl.glDisable(GL10.GL_TEXTURE_2D);
    }

    private final static HbeTexture _textureLoad(HbeTexture t,Bitmap bitmap, int left,
                                                 int top, int width, int height) {

        t.left = left;
        t.top = top;
        t.width = width;
        t.height = height;
        t.powerWidth = _getPowerValueGreaterThanGivenValue(t.width);
        t.powerHeight = _getPowerValueGreaterThanGivenValue(t.height);
        Bitmap tempBitmap = null;
        if(t.width == t.powerWidth && t.height == t.powerHeight&&left ==0 &&top ==0
                &&t.width == bitmap.getWidth() &&t.height == bitmap.getHeight()){
            tempBitmap = bitmap;
        }
        else if(t.width == t.powerWidth && t.height == t.powerHeight)
        {
            tempBitmap = Bitmap.createBitmap(bitmap, left, top, width, height);
        }
        else
        {
            tempBitmap = Bitmap.createBitmap(t.powerWidth, t.powerHeight, Bitmap.Config.ARGB_8888);
            Canvas _tempc = new Canvas(tempBitmap);
            _tempc.drawBitmap(bitmap, new Rect(left,top,left+width,top+height),
                    new Rect(0,0,t.width,t.height), null);
        }
        _gl.glGenTextures(1, t.tex, 0);
        int tempTexture = _curTexture;
        _gl.glBindTexture(GL10.GL_TEXTURE_2D, t.tex[0]);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, tempBitmap, 0);
        if(tempBitmap!=bitmap)
        {
            tempBitmap.recycle();
        }
        _gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
                GL10.GL_LINEAR);
        _gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
                GL10.GL_LINEAR);
        // to avoid the effect as sky box have line at corner
        _gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
                GL10.GL_CLAMP_TO_EDGE);
        _gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
                GL10.GL_CLAMP_TO_EDGE);
        _gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE,
                GL10.GL_MODULATE);
        if (tempTexture > 0)
            _gl.glBindTexture(GL10.GL_TEXTURE_2D, tempTexture);
        return t;

    }

    // A private method to allocate memory for vertex
    private final static void _initGraphic() {
        // size * 4 means one vertex size,the other 4 means 4 vertex compose 1
        // rectangle
        ByteBuffer bb;

        bb = ByteBuffer.allocateDirect(VERTEX_SIZE * 4);
        bb.order(ByteOrder.nativeOrder());
        _vertexBuffer = bb.asIntBuffer();
        _vertexArray = new int[VERTEX_SIZE];

        bb = ByteBuffer.allocateDirect(INDEX_SIZE * 2);
        bb.order(ByteOrder.nativeOrder());
        _indexBuffer = bb.asShortBuffer();
        _indexArray = new short[INDEX_SIZE];

        bb = ByteBuffer.allocateDirect(COLOR_SIZE * 4);
        bb.order(ByteOrder.nativeOrder());
        _colorBuffer = bb.asIntBuffer();
        _colorArray = new int[COLOR_SIZE];

        bb = ByteBuffer.allocateDirect(TEXTURE_SIZE * 4);
        bb.order(ByteOrder.nativeOrder());
        _textureBuffer = bb.asIntBuffer();
        _textureArray = new int[TEXTURE_SIZE];

        _vertexPos = 0;
        _drawPos = 0;
        _indexPos = 0;
        _colorPos = 0;
        _texturePos = 0;

        // init state
        _curBlendMode = BLEND_ALPHABLEND;
        _curPrimType = PRIM_QUADS;
        _curTexture = 0;// -1;

        //init texture
        HbeTexture t = HbeTexture.head;
        HbeTexture temp = null;
        while(t!=null)
        {
            temp = t.next;
            HbeTexture.free(t);
            t = temp;
        }
    }

    final static void _setVertexBuffer() {
        _gl.glVertexPointer(2, GL10.GL_FIXED, 0, _vertexBuffer);
        _gl.glColorPointer(4, GL10.GL_FIXED, 0, _colorBuffer);
        _gl.glTexCoordPointer(2, GL10.GL_FIXED, 0, _textureBuffer);
    }


    final static void _renderBatch() {
        if (_vertexPos == 0)
            return;

        _vertexBuffer.put(_vertexArray, 0, _vertexPos);
        _vertexBuffer.position(0);
        _colorBuffer.put(_colorArray, 0, _colorPos);
        _colorBuffer.position(0);

        if (_curTexture != 0)// copy texture buffer >0
        {
            _textureBuffer.put(_textureArray, 0, _texturePos);
            _textureBuffer.position(0);
            _texturePos = 0;
        }

        switch (_curPrimType) {
            case PRIM_QUADS:
                _indexBuffer.put(_indexArray, 0, _indexPos);
                _indexBuffer.position(0);
                _gl.glDrawElements(GL10.GL_TRIANGLES, _indexPos,
                        GL10.GL_UNSIGNED_SHORT, _indexBuffer);
                _indexPos = 0;
                break;
            case PRIM_TRIPLES:
                _gl.glDrawArrays(GL10.GL_TRIANGLES, 0, _vertexPos >> 1);
                break;
            case PRIM_LINES:
                _gl.glDrawArrays(GL10.GL_LINES, 0, _vertexPos >> 1);
        }

        _vertexPos = 0;
        _drawPos = 0;
        _colorPos = 0;

    }

    private final static int[] _getFixVertexCoord(HbeVertex[] vertex) {
        int i = 0;
        for (HbeVertex v : vertex) {
            _coordArray[i++] = (int) (v.x * 65536);
            _coordArray[i++] = (int) (v.y * 65536);
        }
        return _coordArray;
    }

    private final static int[] _getFixTextureCoord(HbeVertex[] vertex) {
        int i = 0;
        for (HbeVertex v : vertex) {
            _coordArray[i++] = (int) (v.tx * 65536);
            _coordArray[i++] = (int) (v.ty * 65536);
        }
        return _coordArray;
    }

    private final static int[] _getFixColor(HbeVertex[] vertex) {
        int[] col;
        int i = 0;
        for (HbeVertex v : vertex) {
            col = _convertColorToFixInt(v.col);
            _coordArray[i++] = col[0];
            _coordArray[i++] = col[1];
            _coordArray[i++] = col[2];
            _coordArray[i++] = col[3];
        }
        return _coordArray;
    }

    // long to float then to fix integer
    private static final int[] _convertColorToFixInt(int col) {
        float[] _fcol = HbeColor.convertColor(col);
        int i = 0;
        for (float f : _fcol) {
            _rgba[i++] = (int) (f * 65536);
        }
        return _rgba;
    }

    /*
     * just valid for positive number to get a power value which greater than
     * the given value
     */
    private static final int _getPowerValueGreaterThanGivenValue(int v) {
        if (v <= 0 || (v & (v - 1)) == 0)
            return v;
        int i = 0;
        while (v != 0) {
            v >>= 1;
            ++i;
        }
        return (0x1 << i);
    }
    private static final void _initAudio()
    {
        if(_audioList != null)
        {

            for(HbeAudio a : _audioList){
                if(a._type == HbeAudio.MUSIC)
                {
                    HbeMusic mus = (HbeMusic)a;
                    MediaPlayer mp = mus.getMediaPlayer();
                    mp.release();
                }
                else if(a._type == HbeAudio.SOUND)
                {
                    HbeSound sou = (HbeSound)a;
                    if(_soundPool!=null)
                        _soundPool.unload(sou.sid);
                }
            }
            _audioList.clear();
        }
        if(_soundPool!=null)
        {
            _soundPool.release();
        }
        _soundPool = new SoundPool(HbeConfig.MAX_TRACK_BUCKET, AudioManager.STREAM_MUSIC, 0);

        _audioList = new LinkedList<HbeAudio>();

        //init queue
        HbeSoundQueueNode.queueSize = 0;
        HbeSoundQueueNode.queue = new HbeSoundQueueNode[HbeConfig.MAX_QUEUE_SOUND];
        for(int i=0;i<HbeConfig.MAX_QUEUE_SOUND;++i)
        {
            HbeSoundQueueNode.queue[i] = new HbeSoundQueueNode();
        }
    }



    static final void _pauseAudio()
    {

        _audioPauseList = new ArrayList<Object>();

        if (_audioList != null) {
            for (HbeAudio au : _audioList) {
                if (au instanceof HbeMusic) {
                    HbeMusic mau = (HbeMusic) au;
                    if (mau.isPlaying()) {
                        mau.pause();
                        _audioPauseList.add(mau);
                    }
                }
            }
        }
		/*
		HbeSound sd = null;
		for(HbeAudio au:_audioList){
			if(au._type == HbeMusic.SOUND){
				sd = (HbeSound) au;
				for(int i=0;i<sd.track.length;++i){
					if(sd.track[i]._state == HbeSoundTrack.PLAYING)
						_audioPauseList.add(sd.track[i]);
				}
			}
		}*/
    }
    static final void _resumeAudio()
    {
        if(_audioPauseList !=null)
        {
            for(Object obj:_audioPauseList)
            {
                if(obj instanceof HbeMusic)
                {
                    HbeMusic mu = (HbeMusic)obj;
                    mu.resume();
                }
			/*	else
				{
					HbeSoundTrack tr = (HbeSoundTrack) obj;
					if(tr._track!=null)
						tr.resume();
				}*/
            }
            _audioPauseList = null;
        }
    }

    static final HbeTexture _textureReload(HbeTexture t) {
        try {
            Bitmap bitmap = null;
            byte[] byteArray = resourceLoad(t.path);
            ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
            if (in != null) {
                bitmap = BitmapFactory.decodeStream(in);
                t = _textureLoad(t, bitmap, t.left, t.top, t.width, t.height);
                bitmap.recycle();
            }
            return t;
        } catch (Exception e) {
            Log.v("HbEngine::textureLoad", "Can not get texture on:" + t.path);
            return null;
        }
    }

    static final void _resumeTexture( )
    {
        HbeTexture t = HbeTexture.head;
        while(t!=null)
        {
            _textureReload(t);
            t = t.next;
        }
    }

    static final void _updateSoundTrackList()
    {
        //Log.v("sound update","update track list");
/*		HbeSoundTrack st;
		HbeSound sd = null;
		for(HbeAudio au:_audioList){
			if(au._type == HbeMusic.SOUND){
				sd = (HbeSound) au;
				for(int i=0;i<sd.track.length;++i){
					st = sd.track[i];
					if(st._state == HbeSoundTrack.PLAYING)
					{
						if(st._bloop == false)
						{
							if (st._track.getPlaybackHeadPosition() >= sd.frames){
								// Log.v("sound free detect",st._track.getPlaybackHeadPosition()+"");
								st.reload();
							}
						}
					}
					else if(st._state == HbeSoundTrack.RELOADING)
					{
						if (st._track.getPlaybackHeadPosition() ==0)
						{
							st._state = HbeSoundTrack.STOPED;
						}
					}
				}
			}
		}
		*/
        HbeSoundQueueNode node;
        for(int i=0;i<HbeSoundQueueNode.queueSize;++i)
        {
            node = HbeSoundQueueNode.queue[i];
            _soundPlay(node.sound, node.loop, node.volume,node.id);
            node.sound=null;
            node.id=0;
            node.volume=0;
            node.loop=false;
        }
        HbeSoundQueueNode.queueSize = 0;
    }


    private final static void _serializeIni(){
        try{
            FileOutputStream os = HbeRender._activity.openFileOutput(_iniFileName,0);
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(_iniSectionList);
            os.close();
        }
        catch(Exception e){
            Log.v("_serializeIni","failed");
        }
    }

    @SuppressWarnings("unchecked")
    private final static void _unSerializeIni(){
        try
        {
            FileInputStream is = HbeRender._activity.openFileInput(_iniFileName);
            ObjectInputStream ois = new ObjectInputStream(is);
            _iniSectionList = (ArrayList<HbeSectionProperty>)ois.readObject();
            is.close();
        }
        catch(Exception e)
        {
            Log.v("_unSerializeIni","failed");
        }

    }

    private final static boolean _isFileExist(String filename){
        try {
            FileInputStream os = HbeRender._activity.openFileInput(filename);
            os.close();
            return true;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.v("_isFileExist",e.toString());
        }
        return true;

    }
}


class HbeRender implements GLSurfaceView.Renderer
{
    static HbeActivity _activity;
    static float _fTime;
    static float _fDeltaTime;
    static long _nFixedDelta;
    static long _nFPS;
    static long _t0fps;
    static long _t0;
    static long _dt;
    static long _cfps;
    static long _nHBEFPS = 0;// to set fps
    static boolean _binit;

    public void onDrawFrame(GL10 gl) {
        if (!_binit) {
            _binit = true;
            if (!_activity.gameInit())
                HbEngine.sysExit();
            System.gc();
        }

        if (!_activity.gameUpdate())
            HbEngine.sysExit();
        //some system update every loop here
        HbEngine._updateSoundTrackList();
        _activity.gameDraw();
        HbEngine._renderBatch();

        _dt = System.currentTimeMillis() - _t0;
        if (_dt == 0)
            _dt = 1;
        _fDeltaTime = _dt / 1000.0f;
        if (_fDeltaTime > 0.2f) {
            _fDeltaTime = _nFixedDelta > 0 ? _nFixedDelta / 1000.0f : 0.01f;
        }
        _fTime += _fDeltaTime;
        try {
            if (_dt + 3 < _nFixedDelta)
                Thread.sleep(_nFixedDelta - _dt);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        _t0 = System.currentTimeMillis();
        if (_t0 - _t0fps <= 1000)
            ++_cfps;
        else {
            _nFPS = _cfps;
            _cfps = 0;
            _t0fps = _t0;
        }
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.v("HbEngine:", "HbeRender onSurfaceChanged");
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(0, HbEngine._sceneWidth, HbEngine._sceneHeight, 0,
                -HbeConfig.Z_SCOPE, HbeConfig.Z_SCOPE);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();

    }

    //will be called first time or resume
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.v("HbEngine:", "HbeRender onSurfaceCreated");
        HbEngine._gl = gl;
        gl.glDisable(GL10.GL_DITHER);
        gl.glShadeModel(GL10.GL_FLAT);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
        // enable cull
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glFrontFace(GL10.GL_CW);
        gl.glCullFace(GL10.GL_BACK);
        // enable depth
        // gl.glEnAdle(GL10.GL_DEPTH_TEST);
        // gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glDisable(GL10.GL_DEPTH_TEST);
        // enable alpha test
        gl.glDisable(GL10.GL_ALPHA_TEST);//we no need this ~handong
        //gl.glAlphaFunc(GL10.GL_GREATER, 0);
        // enable alpha blend
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        // using vertex buffer
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        // using color buffer
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        // using texture buffer
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        // using 2d texture
        gl.glEnable(GL10.GL_TEXTURE_2D);
        HbEngine._setVertexBuffer();
        HbEngine._resumeTexture();
        // gl.glClearColor(0,0,0,1);
    }

}

class HbeEntryProperty implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public String key;
    public String value;
    public HbeEntryProperty(){

    }
    public HbeEntryProperty(String key,String value)
    {
        this.key = key;
        this.value = value;
    }
}

class HbeSectionProperty implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public String key;
    public ArrayList<HbeEntryProperty> entryList =
            new ArrayList<HbeEntryProperty>();
    public HbeSectionProperty(){}
    public HbeSectionProperty(String key, String name, String value){
        this();
        this.key = key;
        entryList.add(new HbeEntryProperty(name, value));
    }
}

class HbePacketItem
{
    public int offset;
    public int size;
}