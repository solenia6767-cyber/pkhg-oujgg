package com.hamster.modmenu;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 1234);
        }

        Button btnOpen = findViewById(R.id.btnOpenCheat);
        Button btnClose = findViewById(R.id.btnCloseCheat);

        btnOpen.setOnClickListener(v -> {
            Intent serviceIntent = new Intent(MainActivity.this, ModMenuService.class);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent);
            } else {
                startService(serviceIntent);
            }
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            startActivity(homeIntent);
        });

        btnClose.setOnClickListener(v -> {
            stopService(new Intent(MainActivity.this, ModMenuService.class));
        });
    }
}
