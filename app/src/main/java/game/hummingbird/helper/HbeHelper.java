package game.hummingbird.helper;

public class HbeHelper {
    static public void helperLibInit(){
        HbeParticle.initMemoryPool();
    }

    private static i_Point _pointI;
    private static f_Point _pointF;
    private static Calculate _calculate;
    public HbeHelper() {
        _pointI=new i_Point();
        _pointF=new f_Point();
        _calculate=new Calculate();
    };
    public static i_Point GetI_point(){
        return _pointI;
    }

    public static f_Point GetF_point(){
        return _pointF;
    }

    public static Calculate GetCalculate(){
        return _calculate;
    }

    public class i_Point {
        private int ix=0;
        private int iy=0;

        public i_Point() {
            ix = 0;
            iy = 0;
        }

        public i_Point(int x, int y) {
            ix = x;
            iy = y;
        }

        public int getX() {
            return ix;
        }

        public int getY() {
            return iy;
        }

        public void setX(int x) {
            ix = x;
        }

        public void setY(int y) {
            iy = y;
        }

        public void set(int x,int y) {
            ix = x;
            iy = y;
        }

        public void copy(i_Point point) {
            ix = point.getX();
            iy = point.getY();
        }
    };

    public class f_Point {
        private float fx;
        private float fy;

        public f_Point() {
            fx = 0;
            fy = 0;
        }

        public f_Point(float x, float y) {
            fx = x;
            fy = y;
        }

        public float getX() {
            return fx;
        }

        public float getY() {
            return fy;
        }

        public void setX(float x) {
            fx = x;
        }

        public void setY(float y) {
            fy = y;
        }

        public void set(float x,float y) {
            fx = x;
            fy = y;
        }

        public void copy(f_Point point) {
            fx = point.getX();
            fy = point.getY();
        }
    };


    public class AdRect {
        private float _x0;
        private float _x1;
        private float _x2;
        private float _x3;
        private float _y0;
        private float _y1;
        private float _y2;
        private float _y3;
        private float _width=0;
        private float _height=0;

        public AdRect(){
            _x0=0;
            _x1=0;
            _x2=0;
            _x3=0;
            _y0=0;
            _y1=0;
            _y2=0;
            _y3=0;
        }
        /**
         *       0_________1
         *       |         |
         *       |         |
         *       |         |
         *       |         |
         *       |_________|
         *       2         3 <br/>   with the clockwise direction is 0132.
         * */
        public AdRect(int left,int top,int width,int height){
            _x0=left;
            _x2=left;
            _x1=_x0+width;
            _x3=_x1;
            _y0=top;
            _y1=top;
            _y2=top+height;
            _y3=top+height;
            _width=width;
            _height=height;
        }

        public AdRect(float left,float top,float width,float height){
            _x0=left;
            _x2=left;
            _x1=_x0+width;
            _x3=_x1;
            _y0=top;
            _y1=top;
            _y2=top+height;
            _y3=top+height;
            _width=width;
            _height=height;
        }

        public final void Set(int left,int top,int width,int height){
            _x0=left;
            _x2=left;
            _x1=_x0+width;
            _x3=_x1;
            _y0=top;
            _y1=top;
            _y2=top+height;
            _y3=top+height;
            _width=width;
            _height=height;
        }

        public final void Set(float left,float top,float width,float height){
            _x0=left;
            _x2=left;
            _x1=_x0+width;
            _x3=_x1;
            _y0=top;
            _y1=top;
            _y2=top+height;
            _y3=top+height;
            _width=width;
            _height=height;
        }


        /**
         * Once you use these function, make sure the height and width never changed
         * Or else, when you want to get the width or height, a serious error may
         * take place.
         * */
        public final void Set(float x0, float y0, float x1, float y1, float x2,
                              float y2, float x3, float y3) {
            _x0 = x0;
            _x2 = x2;
            _x1 = x1;
            _x3 = x3;
            _y0 = y0;
            _y1 = y1;
            _y2 = y2;
            _y3 = y3;
        }

        public final float getLeftTopXf()
        {
            return _x0;
        }
        public final float getLeftTopYf()
        {
            return _y0;
        }
        public final float getRightTopXf()
        {
            return _x1;
        }
        public final float getRightTopYf()
        {
            return _y1;
        }
        public final float getLeftBottomXf()
        {
            return _x2;
        }
        public final float getLeftBottomYf()
        {
            return _y2;
        }
        public final float getRightBottomXf()
        {
            return _x3;
        }
        public final float getRightBottomYf()
        {
            return _y3;
        }
        public final float getLeftf(){
            return _x0;
        }
        public final float getTopf(){
            return _y0;
        }
        public final float getWidthf()
        {
            return _width;
        }
        public final float getHeightf()
        {
            return _height;
        }
        public final int getLeftTopXi()
        {
            return (int)_x0;
        }
        public final int getLeftTopYi()
        {
            return (int)_y0;
        }
        public final int getRightTopXi()
        {
            return (int)_x1;
        }
        public final int getRightTopYi()
        {
            return (int)_y1;
        }
        public final int getLeftBottomXi()
        {
            return (int)_x2;
        }
        public final int getLeftBottomYi()
        {
            return (int)_y2;
        }
        public final int getRightBottomXi()
        {
            return (int)_x3;
        }
        public final int getRightBottomYi()
        {
            return (int)_y3;
        }
        public final int getLefti(){
            return (int)_x0;
        }
        public final int getTopi(){
            return (int)_y0;
        }
        public final int getWidthi()
        {
            return (int)_width;
        }
        public final int getHeighti()
        {
            return (int)_height;
        }
        public final void setLeft(float x)
        {
            _x0 = x;
            _x2 = x;
        }
        public final void setWidth(float w)
        {
            _width = w;
            _x1 = _x0 + w;
            _x3 = _x2 + w;
        }
        public final void setTop(float y)
        {
            _y0 = y;
            _y1 = y;
        }
        public final void setHeight(float h)
        {
            _height = h;
            _y2 = _y0+h;
            _y3 = _y1+h;
        }

        public final void setLeft(int x)
        {
            _x0 = x;
            _x2 = x;
        }
        public final void setWidth(int w)
        {
            _width = w;
            _x1 = _x0 + w;
            _x3 = _x2 + w;
        }
        public final void setTop(int y)
        {
            _y0 = y;
            _y1 = y;
        }
        public final void setHeight(int h)
        {
            _height = h;
            _y2 = _y0+h;
            _y3 = _y1+h;
        }
    }


    public class Calculate {

        private f_Point temPoint;
        private AdRect  _temRect;

        public Calculate(){
            temPoint=new f_Point();
            _temRect=new AdRect();
        };

        /** calculate distance from p1 to p2,return an integer */
        public int distance2Di(i_Point p1, i_Point p2) {
            int temp = 0;
            temp = (int) Math.sqrt((p2.getX() - p1.getX())
                    * (p2.getX() - p1.getX()) + (p2.getY() - p1.getY())
                    * (p2.getY() - p1.getY()));
            return temp;
        };

        /** calculate distance from p1 to p2,return an float */
        public float distance2Df(i_Point p1, i_Point p2) {
            float temp = 0;
            temp = (float) Math.sqrt((p2.getX() - p1.getX())
                    * (p2.getX() - p1.getX()) + (p2.getY() - p1.getY())
                    * (p2.getY() - p1.getY()));
            return temp;
        };

        /** calculate distance from p1 to p2,return an integer */
        public float distance2Df(f_Point p1, f_Point p2) {
            float temp = 0;
            temp = (int) Math.sqrt((p2.getX() - p1.getX())
                    * (p2.getX() - p1.getX()) + (p2.getY() - p1.getY())
                    * (p2.getY() - p1.getY()));
            return temp;
        };

        /** calculate distance from p1 to p2, return an double */
        public double distance2Dd(i_Point p1, i_Point p2) {
            double temp = 0;
            temp = Math.sqrt((p2.getX() - p1.getX()) * (p2.getX() - p1.getX())
                    + (p2.getY() - p1.getY()) * (p2.getY() - p1.getY()));
            return temp;
        };

        /** calculate distance from p1 to p2, return an double */
        public double distance2Dd(f_Point p1, f_Point p2) {
            double temp = 0;
            temp = Math.sqrt((p2.getX() - p1.getX()) * (p2.getX() - p1.getX())
                    + (p2.getY() - p1.getY()) * (p2.getY() - p1.getY()));
            return temp;
        };

        /** calculate distance from p1 to p2, return an integer */
        public int distance2Di(int x1, int x2, int y1, int y2) {
            int temp = 0;
            temp = (int) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1)
                    * (y2 - y1));
            return temp;
        };

        /** calculate distance from p1 to p2, return an float */
        public float distance2Df(int x1, int x2, int y1, int y2) {
            float temp = 0;
            temp = (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1)
                    * (y2 - y1));
            return temp;
        };

        /** calculate distance from p1 to p2, return an float */
        public float distance2Df(float x1, float x2, float y1, float y2) {
            float temp = 0;
            temp = (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1)
                    * (y2 - y1));
            return temp;
        };

        /** calculate distance from p1 to p2, return an double */
        public double distance2Dd(double x1, double x2, double y1, double y2) {
            double temp = 0;
            temp = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
            return temp;
        };

        /** calculate distance from p1 to p2, return an double */
        public double distance2Dd(float x1, float x2, float y1, float y2) {
            double temp = 0;
            temp = Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
            return temp;
        };

        /** get the result of a input point rotate with its absolute pivot clockwise*/
        public f_Point RotatePoint(f_Point p1,float PivotX,float PivotY,float _angle){
            float tempX=0,tempY=0;
            _angle=((_angle*1000)%360000)/1000;
            temPoint.set(0, 0);
            tempX = p1.getX() - PivotX;
            tempY = p1.getY() - PivotY;
            temPoint.setX((float)(tempX*Math.cos(_angle)-tempY*Math.sin(_angle)+PivotX));
            temPoint.setY((float)(tempX*Math.sin(_angle)+tempY*Math.cos(_angle)+PivotY));
            return temPoint;
        };

        /** get the result of a input point rotate with its absolute pivot clockwise*/
        public f_Point RotatePoint(f_Point p1,f_Point Pivot,float _angle){
            float tempX=0,tempY=0;
            _angle=((_angle*1000)%360000)/1000;
            temPoint.set(0, 0);
            tempX = p1.getX() - Pivot.getX();
            tempY = p1.getY() - Pivot.getY();
            temPoint.setX((float)(tempX*Math.cos(_angle)-tempY*Math.sin(_angle)+Pivot.getX()));
            temPoint.setY((float)(tempX*Math.sin(_angle)+tempY*Math.cos(_angle)+Pivot.getY()));
            return temPoint;
        };

        /** get the result of a input point rotate with its absolute pivot clockwise*/
        public float RotatePoint_GetX(float p1X,float p1Y,float PivotX,float PivotY,float _angle){
            float tempX=0,tempY=0;
            _angle=((_angle*1000)%360000)/1000;
            temPoint.set(0, 0);
            tempX = p1X - PivotX;
            tempY = p1Y - PivotY;
            temPoint.setX((float)(tempX*Math.cos(_angle)-tempY*Math.sin(_angle)+PivotX));
            return temPoint.getX();
        };

        /** get the result of a input point rotate with its absolute pivot clockwise*/
        public float RotatePoint_GetY(float p1X,float p1Y,float PivotX,float PivotY,float _angle){
            float tempX=0,tempY=0;
            _angle=((_angle*1000)%360000)/1000;
            temPoint.set(0, 0);
            tempX = p1X - PivotX;
            tempY = p1Y - PivotY;
            temPoint.setY((float)(tempX*Math.sin(_angle)+tempY*Math.cos(_angle)+PivotY));
            return temPoint.getY();
        };

        /** get the result of a input point rotate with its absolute pivot clockwise*/
        public f_Point RotatePoint(float p1X,float p1Y,float PivotX,float PivotY,float _angle){
            float tempX=0,tempY=0;
            _angle=((_angle*1000)%360000)/1000;
            temPoint.set(0, 0);
            tempX = p1X - PivotX;
            tempY = p1Y - PivotY;
            temPoint.setX((float)(tempX*Math.cos(_angle)-tempY*Math.sin(_angle)+PivotX));
            temPoint.setY((float)(tempX*Math.sin(_angle)+tempY*Math.cos(_angle)+PivotY));
            return temPoint;
        };

        /**scale the input point with the input absolute pivot, get f_Point type. */
        public f_Point ScalePoint(f_Point p1,f_Point Pivot,float scaleX,float scaleY){
            float tempX=0,tempY=0;
            temPoint.set(0, 0);
            tempX = p1.getX() - Pivot.getX();
            tempY = p1.getY() - Pivot.getY();
            temPoint.setX(scaleX*tempX+Pivot.getX());
            temPoint.setY(scaleY*tempY+Pivot.getY());
            return temPoint;
        }

        /**scale the input point with the input absolute pivot, get f_Point type. */
        public f_Point ScalePoint(f_Point p1,float PivotX,float PivotY,float scaleX,float scaleY){
            float tempX=0,tempY=0;
            temPoint.set(0, 0);
            tempX = p1.getX() - PivotX;
            tempY = p1.getY() - PivotY;
            temPoint.setX(scaleX*tempX+PivotX);
            temPoint.setY(scaleY*tempY+PivotY);
            return temPoint;
        }

        /** scale the input point with the input absolute pivot, get float type. */
        public float ScalePoint(float pointXorY, float PivotXorY, float scaleXorY) {
            float temp = 0;
            temp = pointXorY - PivotXorY;
            temp = scaleXorY * temp + PivotXorY;
            return temp;
        }

        /** scale and rotate the point*/
        public f_Point RotateScalePoint(f_Point p1,float PivotX,float PivotY,float _angle,float scaleX,float scaleY){
            float tempX=0,tempY=0;
            _angle=((_angle*1000)%360000)/1000;
            temPoint.set(0, 0);
            tempX = p1.getX() - PivotX;
            tempY = p1.getY() - PivotY;
            temPoint.setX((float)(scaleX*(tempX*Math.cos(_angle)-tempY*Math.sin(_angle))+PivotX));
            temPoint.setY((float)(scaleY*(tempX*Math.sin(_angle)+tempY*Math.cos(_angle))+PivotY));
            return temPoint;
        };

        /**RectPivotX and RectPivotY are absolute points in the game display*/
        public AdRect ScaleRect(AdRect targetRect,float RectPivotX,float RectPivotY,float scaleX,float scaleY){

            float n_x0 = 0, n_x1 = 0, n_x2 = 0, n_x3 = 0, n_y0 = 0, n_y1 = 0, n_y2 = 0, n_y3 = 0;

            n_x0 = ScalePoint(targetRect.getLeftTopXf(),RectPivotX, scaleX);
            n_y0 = ScalePoint(targetRect.getLeftTopYf(),RectPivotY, scaleY);

            n_x1 = ScalePoint(targetRect.getRightTopXf(),RectPivotX, scaleX);
            n_y1 = ScalePoint(targetRect.getRightTopYf(),RectPivotY, scaleY);

            n_x2 = ScalePoint(targetRect.getLeftBottomXf(),RectPivotX, scaleX);
            n_y2 = ScalePoint(targetRect.getLeftBottomYf(),RectPivotY, scaleY);

            n_x3 = ScalePoint(targetRect.getRightBottomXf(),RectPivotX, scaleX);
            n_y3 = ScalePoint(targetRect.getRightBottomYf(),RectPivotY, scaleY);

            _temRect.Set(n_x0, n_y0, n_x1, n_y1, n_x2, n_y2,n_x3, n_y3);
            return _temRect;
        };

        public boolean IsAtLineRight(float checkPointX, float checkPointY,
                                     float LineStartX, float LineStartY, float LineEndX,
                                     float LineEndY) {
            float k = (LineEndY - LineStartY) / (LineEndX - LineStartX);
            float b = LineStartY - k * LineStartX;

            if (checkPointY - k * checkPointX - b >= 0) {
                return true;
            } else {
                return false;
            }
        };
    }
}
