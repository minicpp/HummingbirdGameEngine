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
import game.hummingbird.helper.HbeParticleSystem;
import game.hummingbird.helper.HbeParticleSystemInfo;
import game.hummingbird.helper.HbeSprite;
import game.hummingbird.helper.HbeTouchControl;

/**
 * Created by wynter on 5/22/2016.
 */
public class ExampleParticle extends HbeActivity {

    private HbeTexture texture;
    private HbeSprite sprite;
    private HbeParticleSystem par;

    private DisplayMetrics metrics;


    @Override
    public boolean gameMain() {

        Log.v("HbEngine:","gameMain()");
        HbEngine.sysSetActivity(this);


        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        HbEngine.sysSetScreen(metrics.widthPixels/4, metrics.heightPixels/4);
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

        par.update(1.0f/60.0f);

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

        HbEngine.graphicClear(HbeColor.convertColor(0, 0, 0, 255));
        par.render();
    }

    @Override
    public void gameExit() {

    }

    @Override
    public boolean gameInit() {
        texture = HbEngine.textureLoad("particles.png");
        sprite = new HbeSprite(texture, 0, 0, 32, 32);
        sprite.setBlendMode(HbEngine.BLEND_ALPHAADD);
        sprite.setHotSpot(16, 16);
        par =new HbeParticleSystem("particle3.psi", sprite);

        par.info.fParticleLifeMin = 1;
        par.info.fParticleLifeMax = 10;
        par.info.fSpeedMin = 20;
        par.info.fSpeedMax = 60;



        par.fireAt(metrics.widthPixels/8, metrics.heightPixels*1.5f/8);

        return true;
    }
}