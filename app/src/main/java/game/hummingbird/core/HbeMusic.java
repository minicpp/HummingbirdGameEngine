package game.hummingbird.core;
import android.media.MediaPlayer;

public class HbeMusic extends HbeAudio{
    private  MediaPlayer _mediaPlayer;
    private  float _volume = 1.0f;


    /**
     *play music, play only once by default, no loops.
     */
    public void play()//loop false
    {
        play(false);
    }

    /**
     * adjust if loops.
     * If the music is playing, then the play will not cause force playing from beginning.
     * It will control if the play will be looped at the end the music.
     */
    public void play(boolean isLoop) {
        if (isLoop != _mediaPlayer.isLooping()) {
            _mediaPlayer.setLooping(isLoop);
        }
        if (_mediaPlayer.isPlaying()) {
            return;
        }
        _mediaPlayer.seekTo(0);
        _mediaPlayer.start();
    }

    /**
     * get volume strength
     */
    public float getVolume()
    {
        return _volume;
    }

    /**set volume strength.
     * volume should be between 0 ~ 1 */
    public void setVolume(float v)
    {
        if(v< 0.0f)
            v = 0.0f;
        if(v > 1.0f)
            v = 1.0f;
        _volume = v;
        _mediaPlayer.setVolume(_volume, _volume);
    }

    /**stop play. To play again, call play or resume, It will begin from the beginning*/
    public void stop()
    {
        if (_mediaPlayer.isPlaying()) {
            _mediaPlayer.pause();

        }
    }
    /**pause play
     * call play will play from beginning,  call resume to continue the current play*/
    public void pause()
    {
        if(_mediaPlayer.isPlaying())
        {
            _mediaPlayer.pause();
        }
    }
    /**after pause, call it to resume play*/
    public void resume()
    {
        if (_mediaPlayer.isPlaying() == false) {
            _mediaPlayer.start();
        }
    }
    public boolean isPlaying()
    {
        return _mediaPlayer.isPlaying();
    }

    /**Seeks to specified time position.*/
    public void SeekToPos(int mSec){
        _mediaPlayer.seekTo(mSec);
    }

    /**Get current music position and return in million second*/
    public int GetCurPos(){
        return _mediaPlayer.getCurrentPosition();
    }

    ////////////////////////////////////////////////////

    /*
     * Internal using. User can ignore the construction.
     */
    HbeMusic(MediaPlayer player, float volume)
    {
        _mediaPlayer = player;
        _volume = volume;
        this._type = HbeAudio.MUSIC;
    }
    /*
     * internal using
     */
    MediaPlayer getMediaPlayer()
    {
        return _mediaPlayer;
    }

    void freeMusic()
    {
        if(_mediaPlayer == null)
            return;
        _mediaPlayer.release();
        _mediaPlayer = null;
    }
}
