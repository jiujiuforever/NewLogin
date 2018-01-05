package com.lesso.debug.utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.lesso.debug.R;

import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

/**
 * 悬浮窗显示工具类
 */
public class FloatUtils {

    private WindowManager windowManager;
    private Context context;
    private FrameLayout wholeView;

    private View.OnClickListener onClickListener;
    private float initialTouchX;
    private float initialTouchY;
    private int initialX;
    private int initialY;

    private boolean isShowing = false;

    public FloatUtils(Context application) {
        context = application;
        windowManager = (WindowManager) application.getSystemService(Context.WINDOW_SERVICE);
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void show() {
        if (isShowing || !SharedPreferenceUtils.getFloatWindowFlag(context)) {
            return;
        }
        isShowing = true;

        final FrameLayout view = (FrameLayout) View.inflate(context, R.layout.im_debug_float_window, null);
        if (onClickListener != null) {
            view.setOnClickListener(onClickListener);
        }

        wholeView = view;
        int w = WindowManager.LayoutParams.WRAP_CONTENT;
        int h = WindowManager.LayoutParams.WRAP_CONTENT;

        int flags = FLAG_NOT_TOUCH_MODAL | FLAG_NOT_FOCUSABLE;
        int type = WindowManager.LayoutParams.TYPE_PHONE;

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(w, h, type, flags, PixelFormat.TRANSLUCENT);
        params.x = 0;
        params.y = 500;
        params.gravity = Gravity.LEFT | Gravity.TOP;

        windowManager.addView(wholeView, params);

        wholeView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    initialX = params.x;
                    initialY = params.y;
                    initialTouchX = event.getRawX();
                    initialTouchY = event.getRawY();
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    params.x = initialX + (int) (event.getRawX() - initialTouchX);
                    params.y = initialY + (int) (event.getRawY() - initialTouchY);
                    windowManager.updateViewLayout(wholeView, params);
                    return true;
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (Math.abs(event.getRawY() - initialTouchY) < 30 || Math.abs(event.getRawY() - initialTouchY) < 30) {
                        onClickListener.onClick(v);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void remoteView() {
        if (windowManager != null && wholeView != null && isShowing) {
            windowManager.removeView(wholeView);
            isShowing = false;
        }
    }

    public void hide() {
        remoteView();
    }


    public void showFloatWindow(boolean show) {
        if (show) {
            show();
        } else {
            hide();
        }
    }
}