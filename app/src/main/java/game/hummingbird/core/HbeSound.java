package game.hummingbird.core;

public class HbeSound extends HbeAudio{
    int sid;
    HbeSound()
    {
        this._type = HbeAudio.SOUND;
    }

}

class HbeSoundQueueNode
{
    public long id;
    public HbeSound sound;
    public boolean loop;
    public float volume;
    static HbeSoundQueueNode queue[]=null;
    static int queueSize = 0;
}
