package game.hummingbird.helper;

import android.view.MotionEvent;

import game.hummingbird.HbeConfig;

public final class HbeTouchControl {
    public static final float MIN_TOUCH_MOVE_DISTANCE_SEC = HbeConfig.MIN_SOLOTOUCH_POS_MOVE_SEC;

    private static boolean _isMutiTouch = false;
    private static float _displayRateX = 1.0f, _displayRateY = 1.0f;
    private static int _curTouchCount = 0;
    private static HbeHelper.i_Point _curTouchPos, _preTouchPos,
            _curTouchPos_P2, _preTouchPos_P2, _curRealTouchPos;
    private static int _touchState, _touchState_P2;
    private static float _curPressure = 0.0f, _curPressure_P2 = 0.0f;
    private static long _curTouchTime = 0, _preTouchTime = 0,
            _curTouchTime_P2 = 0, _preTouchTime_P2 = 0;
    private static float _avgMovX = 0.0f, _avgMovY = 0.0f, _avgMovX_P2 = 0.0f,
            _avgMovY_P2 = 0.0f;
    private static HbeHelper.Calculate _compute;
    private static boolean _isMoving = false;
    private final static int TRUE = 1, FALSE = 2, NONE = 0;
    private static int _isPressed = NONE, _isTouchDown = NONE,
            _isTouchUp = NONE, _preTouchDown = NONE, _preTouchUp = NONE;
    private static boolean _isMoving_P2 = false;
    private static int _isPressed_P2 = NONE, _isTouchDown_P2 = NONE,
            _isTouchUp_P2 = NONE, _preTouchDown_P2 = NONE,
            _preTouchUp_P2 = NONE;

    public HbeTouchControl(boolean isMutiTouch) {
        _isMutiTouch = isMutiTouch;
        _displayRateX = (float) HbeConfig.GAME_WINDOW_WIDTH
                / HbeConfig.REAL_WINDOW_WIDTH;
        _displayRateY = (float) HbeConfig.GAME_WINDOW_HIGHT
                / HbeConfig.REAL_WINDOW_HEIGHT;
        HbeHelper cmath = new HbeHelper();
        if (!isMutiTouch) {
            _curTouchPos = cmath.new i_Point();
            _preTouchPos = cmath.new i_Point();
            _curRealTouchPos = cmath.new i_Point();
        } else {
            _curTouchPos = cmath.new i_Point();
            _preTouchPos = cmath.new i_Point();
            _curRealTouchPos = cmath.new i_Point();
            _curTouchPos_P2 = cmath.new i_Point();
            _preTouchPos_P2 = cmath.new i_Point();
            _compute = cmath.new Calculate();
        }
    }

    public static void InitTouch() {
        _displayRateX = (float) HbeConfig.GAME_WINDOW_WIDTH
                / HbeConfig.REAL_WINDOW_WIDTH;
        _displayRateY = (float) HbeConfig.GAME_WINDOW_HIGHT
                / HbeConfig.REAL_WINDOW_HEIGHT;
    }

    public void OnTouchEvent(MotionEvent event) {

        _curTouchCount = event.getPointerCount();

        if (_isMutiTouch && (_curTouchCount > 1)) {

            _curPressure = event.getPressure(0);
            _preTouchPos.copy(_curTouchPos);
            _preTouchTime = _curTouchTime;
            _touchState = event.getAction();
            _curTouchTime = System.currentTimeMillis();
            _curTouchPos.setX((int) event.getX(0));
            _curTouchPos.setY((int) event.getY(0));

            if (_touchState == MotionEvent.ACTION_MOVE) {
                float timegap = 1000.0f;
                timegap = (_curTouchTime - _preTouchTime) / 100.0f;
                _avgMovX = Math.abs((_curTouchPos.getX() - _preTouchPos.getX())
                        / timegap);
                _avgMovY = Math.abs((_curTouchPos.getY() - _preTouchPos.getY())
                        / timegap);
                if (_avgMovX > MIN_TOUCH_MOVE_DISTANCE_SEC
                        || _avgMovY > MIN_TOUCH_MOVE_DISTANCE_SEC) {
                    _isMoving = true;
                } else
                    _isMoving = false;
            } else
                _isMoving = false;

            _preTouchDown = _isTouchDown;
            _preTouchUp = _isTouchUp;
            if (_touchState == MotionEvent.ACTION_POINTER_1_UP) {
                _isTouchDown = FALSE;
                _isTouchUp = TRUE;
                _isPressed = TRUE;
            } else {
                _isTouchDown = TRUE;
                _isTouchUp = FALSE;
                _isPressed = FALSE;
            }

            _curPressure_P2 = event.getPressure(1);
            _preTouchPos_P2.copy(_curTouchPos_P2);
            _preTouchTime_P2 = _curTouchTime_P2;
            _touchState_P2 = event.getAction();
            _curTouchTime_P2 = System.currentTimeMillis();
            _curTouchPos_P2.setX((int) event.getX(1));
            _curTouchPos_P2.setY((int) event.getY(1));

            if (_touchState_P2 == MotionEvent.ACTION_MOVE) {
                float timegap = 1000.0f;
                timegap = (_curTouchTime_P2 - _preTouchTime_P2) / 100.0f;
                _avgMovX_P2 = Math
                        .abs((_curTouchPos_P2.getX() - _preTouchPos_P2.getX())
                                / timegap);
                _avgMovY_P2 = Math
                        .abs((_curTouchPos_P2.getY() - _preTouchPos_P2.getY())
                                / timegap);
                if (_avgMovX_P2 > MIN_TOUCH_MOVE_DISTANCE_SEC
                        || _avgMovY_P2 > MIN_TOUCH_MOVE_DISTANCE_SEC) {
                    _isMoving_P2 = true;
                } else
                    _isMoving_P2 = false;
            } else
                _isMoving_P2 = false;

            _preTouchDown_P2 = _isTouchDown_P2;
            _preTouchUp_P2 = _isTouchUp_P2;
            if (_touchState_P2 == MotionEvent.ACTION_POINTER_2_UP) {
                _isTouchDown_P2 = FALSE;
                _isTouchUp_P2 = TRUE;
                _isPressed_P2 = TRUE;
            } else {
                _isTouchDown_P2 = TRUE;
                _isTouchUp_P2 = FALSE;
                _isPressed_P2 = FALSE;
            }

        } else {
            _curPressure = event.getPressure(0);
            _preTouchPos.copy(_curTouchPos);
            _preTouchTime = _curTouchTime;
            _touchState = event.getAction();
            _curTouchTime = System.currentTimeMillis();
            _curTouchPos.setX((int) event.getX(0));
            _curTouchPos.setY((int) event.getY(0));
            if (_touchState == MotionEvent.ACTION_MOVE) {
                float timegap = 1000.0f;
                timegap = (_curTouchTime - _preTouchTime) / 100.0f;
                _avgMovX = Math.abs((_curTouchPos.getX() - _preTouchPos.getX())
                        / timegap);
                _avgMovY = Math.abs((_curTouchPos.getY() - _preTouchPos.getY())
                        / timegap);
                if (_avgMovX > MIN_TOUCH_MOVE_DISTANCE_SEC
                        || _avgMovY > MIN_TOUCH_MOVE_DISTANCE_SEC) {
                    _isMoving = true;
                } else
                    _isMoving = false;
            } else
                _isMoving = false;

            _preTouchDown = _isTouchDown;
            _preTouchUp = _isTouchUp;
            if (_touchState == MotionEvent.ACTION_UP) {
                _isTouchDown = FALSE;
                _isTouchUp = TRUE;
                _isPressed = TRUE;
                _isTouchDown_P2 = FALSE;
                _isTouchUp_P2 = TRUE;
            } else {
                _isTouchDown = TRUE;
                _isTouchUp = FALSE;
                _isPressed = FALSE;
            }
        }
    };

    public static int GetCurTouchCount() {
        return _curTouchCount;
    };

    public static HbeHelper.i_Point GetCurTouchPos() {
        _curRealTouchPos.set((int) (_curTouchPos.getX() * _displayRateX),
                (int) (_curTouchPos.getY() * _displayRateY));
        return _curRealTouchPos;
    };

    public static int GetCurTouchPosX() {
        return (int) (_curTouchPos.getX() * _displayRateX);
    };

    public static int GetCurTouchPosY() {
        return (int) (_curTouchPos.getY() * _displayRateY);
    };

    public static boolean isTouchDown() {
        if (_isTouchDown == TRUE) {
            return true;
        } else {
            return false;
        }
    };

    public static boolean isTouchUp() {
        if (_isTouchUp == TRUE) {
            return true;
        } else {
            return false;
        }
    };

    public static boolean isPreTouchDown() {
        if (_preTouchDown == TRUE) {
            return true;
        } else {
            return false;
        }
    };

    public static boolean isPreTouchUp() {
        if (_preTouchUp == TRUE) {
            return true;
        } else {
            return false;
        }
    };

    public static boolean isJustPressed() {
        if (_isPressed == TRUE) {
            return true;
        } else {
            return false;
        }
    };

    public static void Reset() {
        _isPressed = NONE;
        _isTouchDown = NONE;
        _isTouchUp = NONE;
        _isPressed_P2 = NONE;
        _isTouchDown_P2 = NONE;
        _isTouchUp_P2 = NONE;
    };

    public static void ResetJustPressed() {
        _isPressed = NONE;
    };

    public static boolean isMoving() {
        return _isMoving;
    };

    public static float GetTouchPressure() {
        return _curPressure;
    };

    /**
     * return the angle of the moving path of touch point, the angle will from
     * left -180 to right +180, and straight from bottom to top is 180.
     * */
    public static int GetMovingAngle() {
        double angle = 0;
        double tx = _curTouchPos.getX() - _preTouchPos.getX();
        double ty = _curTouchPos.getY() - _preTouchPos.getY();
        angle = Math.atan2(tx, ty);
        int temp = 0;
        temp = (int) (angle * 180 / Math.PI);
        return temp;
    };

    public static double GetMovingAngleRadian() {
        double angle = 0;
        double tx = _curTouchPos.getX() - _preTouchPos.getX();
        double ty = _curTouchPos.getY() - _preTouchPos.getY();
        angle = Math.atan2(tx, ty);
        return angle;
    };

    public static int GetMoveDistance_i() {
        return _compute.distance2Di(_preTouchPos, _curTouchPos);
    };

    public static float GetMoveDistance_f() {
        return _compute.distance2Df(_preTouchPos, _curTouchPos);
    };

    // =============================== SecondPoint ==================================
    public static HbeHelper.i_Point GetCurTouchPos_P2() {
        _curRealTouchPos.set((int) (_curTouchPos_P2.getX() * _displayRateX),
                (int) (_curTouchPos_P2.getY() * _displayRateY));
        return _curRealTouchPos;
    };

    public static int GetCurTouchPosX_P2() {
        return (int) (_curTouchPos_P2.getX() * _displayRateX);
    };

    public static int GetCurTouchPosY_P2() {
        return (int) (_curTouchPos_P2.getY() * _displayRateY);
    };

    public static boolean isTouchDown_P2() {
        if (_isTouchDown_P2 == TRUE) {
            return true;
        } else {
            return false;
        }
    };

    public static boolean isTouchUp_P2() {
        if (_isTouchUp_P2 == TRUE) {
            return true;
        } else {
            return false;
        }
    };

    public static boolean isPreTouchDown_P2() {
        if (_preTouchDown_P2 == TRUE) {
            return true;
        } else {
            return false;
        }
    };

    public static boolean isPreTouchUp_P2() {
        if (_preTouchUp_P2 == TRUE) {
            return true;
        } else {
            return false;
        }
    };

    public static boolean isJustPressed_P2() {
        if (_isPressed_P2 == TRUE) {
            return true;
        } else {
            return false;
        }
    };

    public static void ResetJustPressed_P2() {
        _isPressed_P2 = NONE;
    };

    public static boolean isMoving_P2() {
        return _isMoving_P2;
    };

    public static float GetTouchPressure_P2() {
        return _curPressure_P2;
    };

    /**
     * return the angle of the moving path of Point2, the angle will from left
     * -180 to right +180, and straight from bottom to top is 180.
     * */
    public static int GetMovingAngle_P2() {
        double angle = 0;
        double tx = _curTouchPos_P2.getX() - _preTouchPos_P2.getX();
        double ty = _curTouchPos_P2.getY() - _preTouchPos_P2.getY();
        angle = Math.atan2(tx, ty);
        int temp = 0;
        temp = (int) (angle * 180 / Math.PI);
        return temp;
    };

    public static double GetMovingAngleRadian_P2() {
        double angle = 0;
        double tx = _curTouchPos_P2.getX() - _preTouchPos_P2.getX();
        double ty = _curTouchPos_P2.getY() - _preTouchPos_P2.getY();
        angle = Math.atan2(tx, ty);
        return angle;
    };

    /**
     * get the integer distance between touch point1 and point2, but if not set
     * MutiTouch, this will return -1
     */
    public static int GetDistance_i() {
        if (!_isMutiTouch
                || ((_curTouchPos_P2.getX() == 0) && (_curTouchPos_P2.getY() == 0))) {
            return -1;
        } else {
            return _compute.distance2Di(_curTouchPos, _curTouchPos_P2);
        }
    };

    /**
     * get the float distance between touch point1 and point2, but if not set
     * MutiTouch, it will return -1
     */
    public static float GetDistance_f() {
        if (!_isMutiTouch
                || ((_curTouchPos_P2.getX() == 0) && (_curTouchPos_P2.getY() == 0))) {
            return -1;
        } else {
            return _compute.distance2Df(_curTouchPos, _curTouchPos_P2);
        }
    };

    /**
     * return the angle between 2 touch point from p1 to p2, the angle will from
     * left -180 to right +180, and straight from bottom to top is 180, but if
     * not set MutiTouch, it will return -1
     * */
    public static int Get2TouchAngle() {
        if (!_isMutiTouch
                || ((_curTouchPos_P2.getX() == 0) && (_curTouchPos_P2.getY() == 0))) {
            return -1;
        } else {
            double angle = 0;
            double tx = _curTouchPos_P2.getX() - _curTouchPos.getX();
            double ty = _curTouchPos_P2.getY() - _curTouchPos.getY();
            angle = Math.atan2(tx, ty);
            int temp = 0;
            temp = (int) (angle * 180 / Math.PI);
            return temp;
        }
    }

    /**
     * return the angle between 2 touch point from p1 to p2 but if not set
     * MutiTouch, it will return -1
     * */
    public static double Get2TouchAngleRadian() {
        if (!_isMutiTouch
                || ((_curTouchPos_P2.getX() == 0) && (_curTouchPos_P2.getY() == 0))) {
            return -1;
        } else {
            double angle = 0;
            double tx = _curTouchPos_P2.getX() - _curTouchPos.getX();
            double ty = _curTouchPos_P2.getY() - _curTouchPos.getY();
            angle = Math.atan2(tx, ty);
            return angle;
        }
    }

    public static int GetMoveDistance_P2_i() {
        if (!_isMutiTouch
                || ((_curTouchPos_P2.getX() == 0) && (_curTouchPos_P2.getY() == 0))) {
            return 0;
        } else {
            return _compute.distance2Di(_preTouchPos_P2, _curTouchPos_P2);
        }
    };

    public static float GetMoveDistance_P2_f() {
        if (!_isMutiTouch
                || ((_curTouchPos_P2.getX() == 0) && (_curTouchPos_P2.getY() == 0))) {
            return 0;
        } else {
            return _compute.distance2Df(_preTouchPos_P2, _curTouchPos_P2);
        }
    };
}
