package game.hummingbird.core;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import game.hummingbird.HbeConfig;
import game.hummingbird.helper.HbeKeyControl;
import game.hummingbird.helper.HbeTouchControl;

public abstract class HbeActivity extends Activity {
    public abstract boolean gameMain();
    public abstract boolean gameUpdate();
    public abstract void gameDraw();
    public abstract void gameExit();
    public abstract boolean gameInit();


    private HbeTouchControl _touchControl;
    private HbeKeyControl _keyEvent;
    public HbeActivity(){
        _touchControl=new HbeTouchControl(HbeConfig.IS_USE_MUTI_TOUCH);
        _keyEvent=new HbeKeyControl();
    }

    public void onCreate(Bundle savedInstanceState) {
        Log.v("HbEngine:","HbeActivity onCreate");
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        HbEngine.sysInitStaticInstance();
        if(!gameMain())
            HbEngine.sysExit();
    }
    protected void onPause() {
        Log.v("HbEngine:","HbeActivity onPause");
        super.onPause();
        HbEngine._pauseAudio();
        HbEngine._surfaceView.onPause();
    }

    protected void onResume() {
        Log.v("HbEngine:","HbeActivity onResume");
        super.onResume();
        HbEngine._surfaceView.onResume();
        HbEngine._resumeAudio();
    }

    protected void onDestory(){
        Log.v("HbEngine:","HbeActivity onDestroy");
        gameExit();
        //HbEngine.channelStopAll();
        super.onDestroy();
    }

    public void onConfigurationChanged(Configuration newConfig){
        Log.v("HbEngine:","HbeActivity onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
    }

    public boolean onTouchEvent(MotionEvent everyTouchEvent){
        _touchControl.OnTouchEvent(everyTouchEvent);
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent msg) {
        _keyEvent.DoKeyDown(keyCode, msg);
        return true;
    }

    public boolean onKeyUp(int keyCode, KeyEvent msg) {
        _keyEvent.DoKeyUp(keyCode, msg);
        return true;
    }
}
