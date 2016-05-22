package game.hummingbird.core;

public abstract class HbeAudio {
    int _type = 0;
    static final int MUSIC = 0;
    static final int SOUND = 1;
    private boolean _lock = false;
    public boolean isLock() {
        return _lock;
    }
    public void setLock(boolean lock) {
        _lock = lock;
    }
}
