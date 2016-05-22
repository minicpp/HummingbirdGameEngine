package game.hummingbird.helper;

import android.view.KeyEvent;

import game.hummingbird.HbeConfig;

public final class HbeKeyControl {

    public static final int KEY_ACTION_UP=2;
    public static final int KEY_ACTION_DOWN=1;
    public static final int KEY_ACTION_NONE=0;
    public class KeyElement{
        /**KeyAction: 0-noKey, 1-keyDown, 2-keyUp*/
        public KeyElement(int _keyCode, KeyEvent _KeyValue, int _KeyAction) {
            this.keyCode = _keyCode;
            this.KeyValue = _KeyValue;
            this.KeyAction = _KeyAction;
        }

        public KeyElement(KeyEvent _KeyValue) {
            KeyValue = _KeyValue;
            keyCode = 0;
            KeyAction = 0;
        }
        public void CopyKey(KeyElement keyCopy){
            this.keyCode = keyCopy.keyCode;
            this.KeyValue = keyCopy.KeyValue;
            this.KeyAction = keyCopy.KeyAction;
        }

        public int keyCode;
        public KeyEvent KeyValue;
        public int KeyAction;
    }// It can be a structure when is writing in C++.

    private static KeyElement[] keyArray;
    private static KeyElement tempKey;
    private static KeyElement nullKey;
    private static int curKeyNum=0;
    private static int MaxAcceptInput=HbeConfig.Max_INPUT_ACCEPT;

    public HbeKeyControl(){
        keyArray= new KeyElement[MaxAcceptInput];
        KeyEvent key=new KeyEvent(1,0);
        tempKey=new KeyElement(key);
        nullKey=new KeyElement(0,key,KEY_ACTION_NONE);
        for(int i=0;i<MaxAcceptInput;i++){
            keyArray[i]=new KeyElement(0,key,KEY_ACTION_NONE);
        }
    }

    private void AddKey(int KeyCode, KeyEvent keyValue, int _KeyAction){
        tempKey.KeyAction=_KeyAction;
        tempKey.keyCode=KeyCode;
        tempKey.KeyValue=keyValue;
        if(curKeyNum<MaxAcceptInput){
            for(int i=0;i<curKeyNum;i++){
                keyArray[curKeyNum-i].CopyKey(keyArray[curKeyNum-i-1]);
            }
            keyArray[0].CopyKey(tempKey);
            curKeyNum++;
        }else{
            for(int i=0;i<MaxAcceptInput-1;i++){
                keyArray[MaxAcceptInput-i-1].CopyKey(keyArray[MaxAcceptInput-i-2]);
            }
            keyArray[0].CopyKey(tempKey);
        }
    }

    public static void CleanKeyArray(){
        for(int i=0;i<curKeyNum;i++){
            keyArray[i]=nullKey;
        }
        curKeyNum=0;
    }

    /**remove the earliest key in the array*/
    public static void remove(){
        for(int i=0;i<curKeyNum-1;i++){
            keyArray[i].CopyKey(keyArray[i+1]);
        }
        for(int j=curKeyNum-1;j<MaxAcceptInput;j++){
            keyArray[j]=nullKey;
        }
        curKeyNum--;
    }

    /**remove the relative key in the array*/
    public static void remove(int index){
        for(int i=index;i<curKeyNum-1;i++){
            keyArray[i].CopyKey(keyArray[i+1]);
        }
        for(int j=curKeyNum-1;j<MaxAcceptInput;j++){
            keyArray[j]=nullKey;
        }
        curKeyNum--;
    }

    public static int GetCurButtonNum(){
        return curKeyNum;
    }

    public static KeyElement GetKeyArray(int index){
        return keyArray[index];
    }

    public static KeyElement[] GetKeyArray(){
        return keyArray;
    }

    public void DoKeyDown(int KeyCode, KeyEvent keyValue) {
        AddKey(KeyCode, keyValue, KEY_ACTION_DOWN);
    }

    public void DoKeyUp(int KeyCode, KeyEvent keyValue) {
        AddKey(KeyCode, keyValue, KEY_ACTION_UP);
    }
}
