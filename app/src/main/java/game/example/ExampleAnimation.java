package game.example;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;

import game.hummingbird.HbeConfig;
import game.hummingbird.core.HbEngine;
import game.hummingbird.core.HbeActivity;
import game.hummingbird.core.HbeColor;
import game.hummingbird.core.HbeTexture;
import game.hummingbird.helper.HbeAnimation;
import game.hummingbird.helper.HbeHelper;
import game.hummingbird.helper.HbeKeyControl;
import game.hummingbird.helper.HbeTouchControl;

/**
 * Created by wynter on 5/22/2016.
 */
public class ExampleAnimation extends HbeActivity {

    private HbeTexture texture;
    private HbeAnimation ani;
    private DisplayMetrics metrics;
    int flyDirection = 0;
    int posX = 0;
    int posY = 0;
    int speed = 10;

    @Override
    public boolean gameMain() {

        Log.v("HbEngine:","gameMain()");
        HbEngine.sysSetActivity(this);


        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        HbEngine.sysSetScreen(metrics.widthPixels, metrics.heightPixels);
        Log.v("Window Size:", "width="+metrics.widthPixels+", height="+metrics.heightPixels);

        HbEngine.sysSetFPS(60);
        HbEngine.randomSeed(0);
        HbEngine.sysInit();
        HbeHelper.helperLibInit();

        HbeConfig.REAL_WINDOW_WIDTH=metrics.widthPixels;
        HbeConfig.REAL_WINDOW_HEIGHT=metrics.heightPixels;
        HbeTouchControl.InitTouch();


        return true;
    }

    @Override
    public boolean gameUpdate() {
        ani.update(1.0f/60.0f);


        for (int i = 0; i < HbeKeyControl.GetCurButtonNum(); i++) {
            if (HbeKeyControl.GetKeyArray(i).keyCode == KeyEvent.KEYCODE_BACK) {
                if (HbeKeyControl.GetKeyArray(i).KeyAction == HbeKeyControl.KEY_ACTION_UP) {
                    HbEngine.sysExit();
                }
            }
            //HbeKeyControl.remove();
        }

        return true;
    }

    @Override
    public void gameDraw() {
        HbEngine.graphicClear(HbeColor.convertColor(posY/speed % 256, posY/speed % 256, posY/speed % 256, 255));
        ani.renderEx(posX, posY, 0, 5, 5);
        if(posY > metrics.heightPixels){
            posY = 0;
        }
        else
            posY += speed;
    }

    @Override
    public void gameExit() {

    }

    @Override
    public boolean gameInit() {
        texture = HbEngine.textureLoad("bat.png");
        ani = new HbeAnimation(texture, 4, 8, 0, 0, 64, 64);
        ani.setHotSpot(32, 32);
        ani.play();
        posX = metrics.widthPixels/2;
        return true;
    }
}
