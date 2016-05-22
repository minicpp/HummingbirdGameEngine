package game.hummingbird.core;



public class HbeTexture {
    public int tex[] = new int[1];
    public int width;
    public int height;
    public int powerWidth;
    public int powerHeight;
    public int left;
    public int top;
    String path;
    private boolean _lockedTexture = false;//false means release the texture, after running the free command; true means not free at any conditions.
    HbeTexture(String path)
    {
        this.path = path;
    }
    public void setLock(boolean lock)
    {
        _lockedTexture = lock;
    }
    public boolean getLock()
    {
        return _lockedTexture;
    }
    //can only be accessed by HbEngine
    HbeTexture next;
    HbeTexture prev;
    static HbeTexture head = null;
    static int size = 0;
    static void add(HbeTexture item)
    {
        if(head == null)
        {
            head = item;
            item.next = null;
            item.prev = null;
        }
        else
        {
            item.next = head;
            item.prev = null;
            head.prev = item;
            head = item;
        }
        ++size;
    }
    static HbeTexture free(HbeTexture item)
    {
        if(item.prev == null)//first one
        {
            head = item.next;
            if(head != null) //not the last one
                head.prev = null;
        }
        else
        {
            item.prev.next = item.next;
            if(item.next != null)
            {
                item.next.prev = item.prev;
            }
        }
        --size;
        item.next = null;
        item.prev = null;
        return item.next;
    }
}