package com.jbotts.patriotsbattery;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    private TextView status;
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(dp(24), dp(36), dp(24), dp(24));
        layout.setBackgroundColor(Color.rgb(8, 24, 45));
        TextView title = new TextView(this);
        title.setText("PATRIOTS BATTERY");
        title.setTextSize(26); title.setTextColor(Color.WHITE); title.setGravity(Gravity.CENTER);
        layout.addView(title);
        TextView subtitle = new TextView(this);
        subtitle.setText("A live, draggable New England-inspired battery badge. Tap the badge to lock or unlock its position.");
        subtitle.setTextSize(16); subtitle.setTextColor(0xffb7c8da);
        subtitle.setPadding(0, dp(18), 0, dp(18));
        layout.addView(subtitle);
        status = new TextView(this); status.setTextColor(Color.WHITE);
        layout.addView(status);
        addButton(layout, "Grant overlay permission", v -> {
            Intent i = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            startActivity(i);
        });
        addButton(layout, "Start battery overlay", v -> {
            if (!Settings.canDrawOverlays(this)) { status.setText("Grant overlay permission first."); return; }
            try {
                startForegroundService(new Intent(this, BatteryOverlayService.class));
                status.setText("Overlay started. Check the upper-right of your screen.");
            } catch (Exception e) { status.setText("Unable to start: " + e.getMessage()); }
        });
        addButton(layout, "Stop overlay", v -> {
            stopService(new Intent(this, BatteryOverlayService.class));
            status.setText("Overlay stopped.");
        });
        setContentView(layout);
    }
    @Override protected void onResume() {
        super.onResume();
        if (status != null) status.setText(Settings.canDrawOverlays(this) ? "Overlay permission granted." : "Overlay permission required.");
    }
    private void addButton(LinearLayout parent, String label, View.OnClickListener listener) {
        Button b = new Button(this); b.setText(label); b.setOnClickListener(listener); parent.addView(b);
    }
    private int dp(float n) { return (int)(n * getResources().getDisplayMetrics().density + .5f); }
}
