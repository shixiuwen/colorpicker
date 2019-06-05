package com.shixia.colorpickerview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

public class ColorPickerView extends LinearLayout {

    private int red = 255, green = 0, blue = 0;
    private int index = 0;
    private float widthPercent;
    private float heightPercent;
    private View vColorPreview;
    private View vLocation;
    private View vBgColor;

    public ColorPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.view_color_picker, this);
        vBgColor = view.findViewById(R.id.fl_color);
        vLocation = view.findViewById(R.id.view_location);
        SeekBar sbColor = view.findViewById(R.id.sb_color);
        vColorPreview = view.findViewById(R.id.view_color_preview);
        sbColor.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                red = 0;
                green = 0;
                blue = 0;
                index = (int) (progress / (100 / 6F));
                float v = progress % (100 / 6F) / (100 / 6F);
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
                Log.e("color", "progress:" + progress + " v:" + v + " red:" + red + " green:" + green + " blue:" + blue);
                vBgColor.setBackgroundColor(Color.rgb(red, green, blue));
                changeColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

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
                        Log.e("event", "X" + event.getX()
                                + " Y" + event.getY()
                                + " widthPercent:" + widthPercent
                                + " heightPercent:" + heightPercent);
                        changeColor();
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return true;
            }
        });
    }

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
        Log.e("color", " red:" + tempRed + " green:" + tempGreen + " blue:" + tempBlue);
        vColorPreview.setBackgroundColor(Color.rgb(tempRed, tempGreen, tempBlue));
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) vLocation.getLayoutParams();
        layoutParams.leftMargin = vBgColor.getWidth() - vLocation.getWidth();
        vLocation.setLayoutParams(layoutParams);
    }
}
