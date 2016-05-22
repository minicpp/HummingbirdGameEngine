package game.hummingbird;
/*
 * Some core configuration for game engine
 * such as buffer size, graphic parameters and so on.
 */
public class HbeConfig {

    // --------------- Control -----------------
    public final static boolean IS_USE_MUTI_TOUCH = true;
    public final static int MIN_SOLOTOUCH_POS_MOVE_SEC = 10;
    public final static int Max_INPUT_ACCEPT = 2;
    // -----------------------------------------

    // --------------- Display -----------------
    public final static int GAME_WINDOW_WIDTH = 240; //used by touch input
    public final static int GAME_WINDOW_HIGHT = 400; //used by touch input
    public static int REAL_WINDOW_WIDTH = 480;  //used by touch input
    public static int REAL_WINDOW_HEIGHT = 854; //used by touch input
    public final static int GAME_AVERGAE_FPS = 30;
    public final static float GAME_PERFRAME_TIME=1.0f/GAME_AVERGAE_FPS;
    public final static boolean IS_SHOW_FPS = true;
    public final static String CHAR_STANDARD="Char.png";
    // -----------------------------------------

    // -------------- math static --------------
    public static final float  M_PI	= 3.14159265358979323846f;
    public static final float  M_PI_2 =	1.57079632679489661923f;
    public static final float  M_PI_4= 0.785398163397448309616f;
    public static final float  M_1_PI =	0.318309886183790671538f;
    public static final float  M_2_PI= 0.636619772367581343076f;
    // -----------------------------------------

    // ---------------- graphic ----------------
    public static final int VERTEX_NUM = 2048;//the buffer size of the vertices
    public static final int EGL_BIT = 8;
    public static final int EGL_DEPT = 0;
    public static final float Z_SCOPE = 1.0f;
    // -----------------------------------------

    //---------------- particle ----------------
    public static final int MAX_PARTICLES = 500;
    public static final int INIT_PARTICLES_POOL_SIZE = 1024;
    public static final int PARTICLES_POOL_SIZE_DELTA = 128;
    //particle system
    public static final int MAX_PSYSTEMS = 100;
    public static final int INIT_PSYS_POOL_SIZE = 8;
    public static final int PSYP_POOL_DELTA = 8;
    // -----------------------------------------

    //---------------- font -------------- by hd
    public static final int COUNT_FONT = 10;
    public static final int FONT_TABLE[][] = {
            {'0','1','2','3','4','5','6','7','8','9'},
            {'a','b','c','d','e','f','g','h','i','j'},
            {'k','l','m','n','o','p','q','r','s','t'},
            {'u','v','w','x','y','z','A','B','C','D'},
            {'E','F','G','H','I','J','K','L','M','N'},
            {'O','P','Q','R','S','T','U','V','W','X'},
            {'Y','Z','`','!','@','#','$','%','^','&'},
            {'(',')','_','+','-','*','/','=','\\','|'},
            {'[',']','{','}',':',';','"','\'','<','>'},
            {',','.','?','~',' '}
    };

    //---------------- Audio ------------------ by hd
    public static final float VOLUMN_MUSIC_DEFAULT = 1.0f;
    public static final float VOLUMN_SOUND_DEFAULT = 1.0f;

    //public static final int MAX_SOUND_TRACK= 8;
    public static final int MAX_QUEUE_SOUND=8;
    public static final int MAX_TRACK_BUCKET =24;

}
