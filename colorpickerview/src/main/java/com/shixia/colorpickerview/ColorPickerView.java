package com.shixia.colorpickerview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ColorPickerView extends LinearLayout {

    private final View llColorProgress;
    private final View vColorBar;
    private final View rlTransBar;
    private final View vTransBar;
    private final RelativeLayout.LayoutParams transBarLayoutParams;
    private int red = 255, green = 0, blue = 0;
    private int index = 0;
    private float widthPercent;
    private float heightPercent;
    private ColorPreviewView cpvColorPreview;
    private View vLocation;
    private View vBgColor;
    private final RelativeLayout.LayoutParams colorBarLayoutParams;

    private int transValue = 255;    //透明度
    private final ImageView vTransPreview;

    public ColorPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.view_color_picker, this);
        vBgColor = view.findViewById(R.id.fl_color);
        vLocation = view.findViewById(R.id.view_location);
        llColorProgress = findViewById(R.id.ll_color_progress);
        cpvColorPreview = view.findViewById(R.id.cpv_color_preview);
        vColorBar = view.findViewById(R.id.view_color_bar);
        colorBarLayoutParams = (RelativeLayout.LayoutParams) vColorBar.getLayoutParams();

        rlTransBar = view.findViewById(R.id.rl_trans_bar);
        vTransBar = view.findViewById(R.id.view_trans_bar);
        transBarLayoutParams = (RelativeLayout.LayoutParams) vTransBar.getLayoutParams();

        vTransPreview = view.findViewById(R.id.view_trans_preview);

        /*调整颜色*/
        llColorProgress.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                int width = llColorProgress.getWidth();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                float leftMargin = event.getX();
                float x = 0;
                if (leftMargin < vColorBar.getWidth() / 2) {
                    colorBarLayoutParams.leftMargin = 0;
                } else if (leftMargin > width - vColorBar.getWidth() / 2) {
                    x = 100;
                    colorBarLayoutParams.leftMargin = width - vColorBar.getWidth();
                } else {
                    x = event.getX() / width * 100;
                    colorBarLayoutParams.leftMargin = (int) (leftMargin - vColorBar.getWidth() / 2);
                }
                vColorBar.setLayoutParams(colorBarLayoutParams);
                onProgressChanged((int) x);
                return true;
            }
        });

        /*调整透明度*/
        rlTransBar.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                int width = rlTransBar.getWidth();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                float leftMargin = event.getX();
                float x = 0;
                if (leftMargin < vTransBar.getWidth() / 2) {
                    transBarLayoutParams.leftMargin = 0;
                } else if (leftMargin > width - vTransBar.getWidth() / 2) {
                    x = 100;
                    transBarLayoutParams.leftMargin = width - vTransBar.getWidth();
                } else {
                    x = event.getX() / width * 100;
                    transBarLayoutParams.leftMargin = (int) (leftMargin - vTransBar.getWidth() / 2);
                }
                vTransBar.setLayoutParams(transBarLayoutParams);
                changeTransparency((int) x);
                return true;
            }
        });

        /*调整颜色明暗*/
        vBgColor.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int width = vBgColor.getWidth();
                int height = vBgColor.getHeight();
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        widthPercent = 1 - event.getX() / width;
                        heightPercent = event.getY() / height;
                        if (widthPercent <= 0 || widthPercent >= 1 || heightPercent <= 0 || heightPercent >= 1) {
                            return true;
                        }
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) vLocation.getLayoutParams();
                        layoutParams.leftMargin = (int) event.getX();
                        layoutParams.topMargin = (int) event.getY();
                        //防止越界处理
                        if (layoutParams.leftMargin > width - vLocation.getWidth()
                                || layoutParams.topMargin > height - vLocation.getHeight()) {
                            return true;
                        }
                        vLocation.setLayoutParams(layoutParams);
                        changeColor();
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return true;
            }
        });
    }


    /**
     * 颜色值调整
     *
     * @param progressColor
     */
    private void onProgressChanged(int progressColor) {
        red = 0;
        green = 0;
        blue = 0;
        index = (int) (progressColor / (100 / 6F));
        float v = progressColor % (100 / 6F) / (100 / 6F);
        switch (index) {
            case 0: //红<-->中--绿
                red = 255;
                green = (int) (255 * v);
                break;
            case 1://红--中<-->绿
                red = (int) (255 * (1 - v));
                green = 255;
                break;
            case 2: //绿<-->中--蓝
                green = 255;
                blue = (int) (255 * v);
                break;
            case 3://绿--中<-->蓝
                green = (int) (255 * (1 - v));
                blue = 255;
                break;
            case 4: //蓝<-->中--红
                blue = 255;
                red = (int) (255 * v);
                break;
            case 5://蓝--中<-->红
                blue = (int) (255 * (1 - v));
                red = 255;
                break;
            default:
                red = 255;
                break;
        }
        vBgColor.setBackgroundColor(Color.rgb(red, green, blue));
        changeColor();
    }

    /**
     * 颜色明暗度调整
     */
    private void changeColor() {
        int tempRed = red;
        int tempGreen = green;
        int tempBlue = blue;
        switch (index) {
            case 0:
                tempGreen = (int) (green + widthPercent * (255 - green));
                tempBlue = (int) (blue + widthPercent * (255 - blue));
                break;
            case 1:
                tempRed = (int) (red + widthPercent * (255 - red));
                tempBlue = (int) (blue + widthPercent * (255 - blue));
                break;
            case 2:
                tempRed = (int) (red + widthPercent * (255 - red));
                tempBlue = (int) (blue + widthPercent * (255 - blue));
                break;
            case 3:
                tempRed = (int) (red + widthPercent * (255 - red));
                tempGreen = (int) (green + widthPercent * (255 - green));
                break;
            case 4:
                tempRed = (int) (red + widthPercent * (255 - red));
                tempGreen = (int) (green + widthPercent * (255 - green));
                break;
            case 5:
            case 6:
                tempGreen = (int) (green + widthPercent * (255 - green));
                tempBlue = (int) (blue + widthPercent * (255 - blue));
                break;
        }
        tempRed = (int) (tempRed - tempRed * heightPercent);
        tempGreen = (int) (tempGreen - tempGreen * heightPercent);
        tempBlue = (int) (tempBlue - tempBlue * heightPercent);
        cpvColorPreview.setColor(Color.argb(transValue, tempRed, tempGreen, tempBlue));
        int[] color = {Color.argb(0, 0, 0, 0), Color.rgb(tempRed, tempGreen, tempBlue)};
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, color);
        vTransPreview.setBackground(drawable);
    }

    /**
     * 改变透明度
     *
     * @param progress
     */
    private void changeTransparency(int progress) {
        transValue = (int) (progress / 100F * 255);
        cpvColorPreview.setColor(Color.argb(transValue, red, green, blue));
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) vLocation.getLayoutParams();
        layoutParams.leftMargin = vBgColor.getWidth() - vLocation.getWidth();
        vLocation.setLayoutParams(layoutParams);

        colorBarLayoutParams.leftMargin = llColorProgress.getWidth() - vColorBar.getWidth();
        vColorBar.setLayoutParams(colorBarLayoutParams);

        transBarLayoutParams.leftMargin = rlTransBar.getWidth() - vTransBar.getWidth();
        vTransBar.setLayoutParams(transBarLayoutParams);

        int[] color = {Color.argb(0, 0, 0, 0), Color.rgb(255, 0, 0)};
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, color);
        vTransPreview.setBackground(drawable);
    }
}
