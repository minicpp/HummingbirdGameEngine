package game.hummingbird.core;

public class HbeUtility {

    public static final int bytesToInt(byte[] b) {
        int s = 0;
        for (int i = 0; i < 3; i++) {
            if (b[3 - i] >= 0) {
                s = s + b[3 - i];
            } else {
                s = s + 256 + b[3 - i];
            }
            s = s * 256;
        }
        if (b[0] >= 0) {
            s = s + b[0];
        } else {
            s = s + 256 + b[0];
        }
        return s;
        //return b[0]<<24 | (b[1]&0xff)<<16 | (b[2]&0xff)<<8 | (b[3]&0xff);
    }

    public final static float bytesToFloat(byte[] b) {
        int i = 0;
        i = ((((b[3] & 0xff) << 8 | (b[2] & 0xff)) << 8) | (b[1] & 0xff)) << 8
                | (b[0] & 0xff);
        return Float.intBitsToFloat(i);
    }

    public static boolean compareString(byte[] barray,String str,int size)
    {
        for(int i=0;i<size;++i)
        {
            int m = barray[i];
            int n = str.charAt(i);
            if(m!=n)
                return false;
        }
        return true;
    }
}
