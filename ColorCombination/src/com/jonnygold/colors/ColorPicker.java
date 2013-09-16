package com.jonnygold.colors;

import com.jonnygold.colors.R;

import android.os.Bundle;
import android.app.Dialog;
import android.content.Context;
import android.graphics.*;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

public class ColorPicker extends Dialog {

    public interface OnColorChangedListener {
        void colorChanged(int color);
    }

    private OnColorChangedListener mListener;
    private int mInitialColor;

    private static class ColorPickerView extends View {
        private Paint mPaint;
        private Paint mCenterPaint;
        //------------------------------------------------------------
        private Paint mMyPaint;
        //------------------------------------------------------------
       
        private final int[] mColors;
        private OnColorChangedListener mListener;

        ColorPickerView(Context c, OnColorChangedListener l, int color) {
            super(c);
            mListener = l;
            mCurColor = color;
            // ������ ������ ��� ��������� ������ (������ �������� ���)
            mColors = new int[] {
                0xFFFF0000, 0xFFFF00FF, 0xFF0000FF, 0xFF00FFFF, 0xFF00FF00,
                0xFFFFFF00, 0xFFFF0000
            };
            // �������� ��� ������
            Shader s = new SweepGradient(0, 0, mColors, null);

            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setShader(s);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(32);
           
            mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mCenterPaint.setColor(color);
            mCenterPaint.setStrokeWidth(5);
           
            //------------------------------------------------------------
            float r = CENTER_X - mPaint.getStrokeWidth()*0.5f;
            R = r;
            // ����� � �������� ���������� (������ - ������� ���� - �����
            // ��� ��������� ������������ � �������
            mMyPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            Shader s1 =new LinearGradient(-R-10,0, R+10, 0, new int [] {0xFF000000, mCurColor, 0xFFFFFFFF}, null, Shader.TileMode.CLAMP);
            mMyPaint.setShader(s1);
            mMyPaint.setStyle(Paint.Style.FILL);
          //------------------------------------------------------------
          
        }

        private boolean mTrackingCenter;
        private boolean mHighlightCenter;

        @Override
        protected void onDraw(Canvas canvas) {
            //float r = CENTER_X - mPaint.getStrokeWidth()*0.5f;
            canvas.translate(CENTER_X, CENTER_X);
            // ������� �������� ������
            canvas.drawOval(new RectF(-R, -R, R, R), mPaint);
            // ������� ������� ������
            canvas.drawCircle(0, 0, CENTER_RADIUS, mCenterPaint);
            // ������� �����
            canvas.drawRect(new RectF(-R-10,R+30, R+10, R+60), mMyPaint);
            Shader s1 =new LinearGradient(-R-10,0, R+10, 0, new int [] {0xFF000000, mCurColor, 0xFFFFFFFF}, null, Shader.TileMode.CLAMP);
            mMyPaint.setShader(s1);
           
            // ������ ����� ?
            if (mTrackingCenter) {
                int c = mCenterPaint.getColor();
                mCenterPaint.setStyle(Paint.Style.STROKE);

                if (mHighlightCenter) {
                    mCenterPaint.setAlpha(0xFF);
                } else {
                    mCenterPaint.setAlpha(0x80);
                }
                canvas.drawCircle(0, 0,
                                  CENTER_RADIUS + mCenterPaint.getStrokeWidth(),
                                  mCenterPaint);

                mCenterPaint.setStyle(Paint.Style.FILL);
                mCenterPaint.setColor(c);
            }
           
           
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(CENTER_X*2, CENTER_Y*2);
        }

        private static final int CENTER_X = 100;
        private static final int CENTER_Y = 130;
        private static final int CENTER_RADIUS = 32;
        //------------------------------------------------------------
        private float R;
        //private float mHue = 0;
        private int mCurColor;
        //------------------------------------------------------------

        private int ave(int s, int d, float p) {
            return s + java.lang.Math.round(p * (d - s));
        }

        private int interpColor(int colors[], float unit) {
             if (unit <= 0) {
                return colors[0];
            }
            if (unit >= 1) {
                return colors[colors.length - 1];
            }

            float p = unit * (colors.length - 1);
            int i = (int)p;
            p -= i;

            // now p is just the fractional part [0...1) and i is the index
            int c0 = colors[i];
            int c1 = colors[i+1];
            int a = ave(Color.alpha(c0), Color.alpha(c1), p);
            int r = ave(Color.red(c0), Color.red(c1), p);
            int g = ave(Color.green(c0), Color.green(c1), p);
            int b = ave(Color.blue(c0), Color.blue(c1), p);

            return Color.argb(a, r, g, b);
        }

        private static final float PI = 3.1415926f;

        @Override
        public boolean onTouchEvent(MotionEvent event) {
             // �������� ���������� �������
            float x = event.getX() - CENTER_X;
            float y = event.getY() - CENTER_Y+CENTER_RADIUS;
           
            // ���������� �� ������ �� ����� �������
            double hypotenuse = java.lang.Math.sqrt(x*x + y*y);
           
            // ������������ ��������������� ���� � ����������� �� ���� �������
            // � ������ (������� ������)
            boolean inCenter = hypotenuse<= CENTER_RADIUS;
            // �������� ������
            boolean inMainSelect = (hypotenuse > CENTER_RADIUS) && (hypotenuse<= R+20);
            // ������ �����
            boolean inAdvSelect = ((x >= -R-10) && (x <= R+10)) && ((y >= (R +30)) && (y <= (R+60)));
      
            // �������� �����������. ��� ����� ������ ����� ������-�� �� ���������������� ����� ������ ����� �� ������
            invalidate();
            // ��������� �������
            switch (event.getAction()) {
            // �������
                case MotionEvent.ACTION_DOWN:
                    mTrackingCenter = inCenter;
                    // ���� � ������ - ������������ �����
                    if (inCenter) {
                        mHighlightCenter = true;
                        invalidate();
                        break;
                    }
                // ��������
                case MotionEvent.ACTION_MOVE:
                    if (mTrackingCenter) {
                        if (mHighlightCenter != inCenter) {
                            mHighlightCenter = inCenter;
                        }
                    }
                    // � ������
                    else if (inMainSelect) {
                           // ��������� ����
                        float angle = (float)java.lang.Math.atan2(y, x);
                        // need to turn angle [-PI ... PI] into unit [0....1]
                        float unit = angle/(2*PI);
                        if (unit < 0) {
                            unit += 1;
                        }
                        // ���������� ��� � ������������� ��� ������������ �����
                        mCurColor = interpColor(mColors, unit);
                        mCenterPaint.setColor(mCurColor);
                    }
                    // �� �����
                    else if (inAdvSelect)
                    {
                           float val;
                           // ����������� ������� ���� ������ � HSV
                           float[] hsv = color2HSV(mCenterPaint.getColor());
                           // � ����� ����� ����� (����� ������ � ��������� ������)
                           if (x <= 0) {
                                  // ��������� � ������ �������� �������, ��� �� �������, ������������ - 1
                                  val = Math.abs(R+10 + x) / (R+10);
                                  // ��������������� �� HSV � RGB � ������������� ���������� ���� ��� ������
                           mCenterPaint.setColor(Color.HSVToColor(0xFF, new float[] { hsv[0], 1, val }));
                           }
                           // � ������ ����� ����� (����� ������ � �����)
                           else
                           {
                                  // ��������� � ������ �������� ������������, ��� �� �������, ������� - 1
                                  val = 1 - Math.abs(x) / (R+10);
                                  // ��������������� �� HSV � RGB � ������������� ���������� ���� ��� ������
                                  mCenterPaint.setColor(Color.HSVToColor(0xFF, new float[] { hsv[0], val, 1 }));
                           }
                    }
                    // ��������������
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    if (mTrackingCenter) {
                        if (inCenter) {
                            mListener.colorChanged(mCenterPaint.getColor());
                        }
                        mTrackingCenter = false;    // so we draw w/o halo
                        invalidate();
                    }
                    break;
            }
            return true;
        }
    }
   
    static float[] color2HSV(int color) {
             float[] hsv = new float[3];

             int red = Color.red(color);
             int green = Color.green(color);
             int blue = Color.blue(color);
             Color.RGBToHSV(red, green, blue, hsv);

             return hsv;
       }

    public ColorPicker(Context context,
                             OnColorChangedListener listener,
                             int initialColor) {
        super(context);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mListener = listener;
        mInitialColor = initialColor;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OnColorChangedListener l = new OnColorChangedListener() {
            @Override
			public void colorChanged(int color) {
                mListener.colorChanged(color);
                dismiss();
            }
        };
        setContentView(new ColorPickerView(getContext(), l, mInitialColor));
        setTitle(R.string.settings_bg_color_dialog);
    }
}

