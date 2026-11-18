package com.hamster.modmenu;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Switch;

public class ModMenuService extends Service {
    private WindowManager windowManager;
    private View floatingIconView;
    private View modMenuView;
    private WindowManager.LayoutParams iconParams, menuParams;

    static {
        System.loadLibrary("hamster_core");
    }

    public static native void toggleFeatureNative(int featureId, boolean enable);

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        floatingIconView = LayoutInflater.from(this).inflate(R.layout.float_icon, null);
        
        int LAYOUT_FLAG = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                : WindowManager.LayoutParams.TYPE_PHONE;

        iconParams = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            LAYOUT_FLAG,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        );
        iconParams.gravity = Gravity.TOP | Gravity.START;
        iconParams.x = 100;
        iconParams.y = 100;

        windowManager.addView(floatingIconView, iconParams);

        modMenuView = LayoutInflater.from(this).inflate(R.layout.mod_menu, null);
        menuParams = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            LAYOUT_FLAG,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        );
        menuParams.gravity = Gravity.CENTER;
        modMenuView.setVisibility(View.GONE);
        windowManager.addView(modMenuView, menuParams);

        floatingIconView.setOnClickListener(v -> {
            if (modMenuView.getVisibility() == View.GONE) {
                modMenuView.setVisibility(View.VISIBLE);
                floatingIconView.setVisibility(View.GONE);
            }
        });

        modMenuView.findViewById(R.id.btnCloseMenu).setOnClickListener(v -> {
            modMenuView.setVisibility(View.GONE);
            floatingIconView.setVisibility(View.VISIBLE);
        });

        Switch switchAimBot = modMenuView.findViewById(R.id.switchAimBot);
        switchAimBot.setOnCheckedChangeListener((buttonView, isChecked) -> {
            toggleFeatureNative(1, isChecked);
        });

        Switch switchEsp = modMenuView.findViewById(R.id.switchEsp);
        switchEsp.setOnCheckedChangeListener((buttonView, isChecked) -> {
            toggleFeatureNative(2, isChecked);
        });

        floatingIconView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX, initialY;
            private float initialTouchX, initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = iconParams.x;
                        initialY = iconParams.y;
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        iconParams.x = initialX + (int) (event.getRawX() - initialTouchX);
                        iconParams.y = initialY + (int) (event.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(floatingIconView, iconParams);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (floatingIconView != null) windowManager.removeView(floatingIconView);
        if (modMenuView != null) windowManager.removeView(modMenuView);
    }
}
