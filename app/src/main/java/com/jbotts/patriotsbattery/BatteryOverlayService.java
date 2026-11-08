package com.jbotts.patriotsbattery;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.BatteryManager;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class BatteryOverlayService extends Service {
    private static final String CHANNEL = "battery_overlay";
    private WindowManager wm;
    private BatteryBadgeView badge;
    private WindowManager.LayoutParams params;
    private boolean locked;
    private boolean receiverRegistered;
    private final BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override public void onReceive(Context c, Intent i) {
            int level = i.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            int scale = i.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
            int state = i.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            if (badge != null) badge.setBattery(scale > 0 ? Math.round(level * 100f / scale) : 0,
                state == BatteryManager.BATTERY_STATUS_CHARGING || state == BatteryManager.BATTERY_STATUS_FULL);
        }
    };
    @Override public void onCreate() {
        super.onCreate();
        NotificationManager nm = getSystemService(NotificationManager.class);
        nm.createNotificationChannel(new NotificationChannel(CHANNEL, "Battery overlay", NotificationManager.IMPORTANCE_LOW));
        Intent open = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, open, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(this, CHANNEL)
            .setSmallIcon(android.R.drawable.ic_lock_idle_charging).setContentTitle("Patriots Battery active")
            .setContentText("Tap to manage the battery overlay").setOngoing(true).setContentIntent(pi);
        startForeground(1, builder.build());
        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        badge = new BatteryBadgeView(this);
        params = new WindowManager.LayoutParams(dp(128), dp(48), WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.RIGHT;
        params.x = getSharedPreferences("position", MODE_PRIVATE).getInt("x", dp(8));
        params.y = getSharedPreferences("position", MODE_PRIVATE).getInt("y", dp(34));
        locked = getSharedPreferences("position", MODE_PRIVATE).getBoolean("locked", false);
        badge.setLocked(locked);
        badge.setOnTouchListener(new View.OnTouchListener() {
            private float downX, downY;
            private int startX, startY;
            private boolean moved;
            @Override public boolean onTouch(View v, MotionEvent e) {
                switch (e.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = e.getRawX(); downY = e.getRawY();
                        startX = params.x; startY = params.y; moved = false; return true;
                    case MotionEvent.ACTION_MOVE:
                        if (!locked) {
                            int dx = Math.round(e.getRawX() - downX);
                            int dy = Math.round(e.getRawY() - downY);
                            if (Math.abs(dx) > dp(4) || Math.abs(dy) > dp(4)) moved = true;
                            // RIGHT gravity reverses the horizontal offset.
                            params.x = Math.max(0, startX - dx);
                            params.y = Math.max(0, startY + dy);
                            wm.updateViewLayout(badge, params);
                        }
                        return true;
                    case MotionEvent.ACTION_UP:
                        if (!moved) { locked = !locked; badge.setLocked(locked); }
                        getSharedPreferences("position", MODE_PRIVATE).edit()
                            .putInt("x", params.x).putInt("y", params.y).putBoolean("locked", locked).apply();
                        return true;
                    default: return true;
                }
            }
        });
        try { wm.addView(badge, params); }
        catch (Exception e) { stopSelf(); return; }
        registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        receiverRegistered = true;
    }
    @Override public int onStartCommand(Intent intent, int flags, int startId) { return START_STICKY; }
    @Override public void onDestroy() {
        if (receiverRegistered) unregisterReceiver(batteryReceiver);
        if (wm != null && badge != null) { try { wm.removeView(badge); } catch (Exception ignored) {} }
        super.onDestroy();
    }
    @Override public IBinder onBind(Intent intent) { return null; }
    private int dp(float n) { return (int)(n * getResources().getDisplayMetrics().density + .5f); }
}
