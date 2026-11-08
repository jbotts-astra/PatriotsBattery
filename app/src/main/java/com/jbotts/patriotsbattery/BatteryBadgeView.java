package com.jbotts.patriotsbattery;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

public class BatteryBadgeView extends View {
    private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int level = 100;
    private boolean charging = false;
    private boolean locked = false;
    public BatteryBadgeView(Context context) { super(context); }
    public void setBattery(int percent, boolean isCharging) { level = Math.max(0, Math.min(100, percent)); charging = isCharging; invalidate(); }
    public void setLocked(boolean value) { locked = value; invalidate(); }
    private int dp(float v) { return (int)(v * getResources().getDisplayMetrics().density + .5f); }
    @Override protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float sx = getWidth() / 128f, sy = getHeight() / 48f;
        canvas.save(); canvas.scale(sx, sy);
        p.setStyle(Paint.Style.FILL); p.setColor(0xee07182c);
        canvas.drawRoundRect(new RectF(1, 1, 127, 47), 15, 15, p);
        p.setStyle(Paint.Style.STROKE); p.setStrokeWidth(2.5f); p.setColor(locked ? 0xffb0bac4 : 0xffe8eef7);
        canvas.drawRoundRect(new RectF(2, 2, 126, 46), 14, 14, p);
        p.setStyle(Paint.Style.FILL);
        // Custom red-white-blue flying pennant: original artwork, not an NFL team logo.
        Path pennant = new Path(); pennant.moveTo(9, 12); pennant.lineTo(44, 8); pennant.lineTo(32, 20); pennant.lineTo(48, 24); pennant.lineTo(30, 26); pennant.lineTo(40, 40); pennant.lineTo(10, 33); pennant.close();
        p.setColor(0xffd52035); canvas.drawPath(pennant, p);
        Path stripe = new Path(); stripe.moveTo(10, 22); stripe.lineTo(37, 17); stripe.lineTo(30, 22); stripe.lineTo(42, 24); stripe.lineTo(10, 27); stripe.close();
        p.setColor(Color.WHITE); canvas.drawPath(stripe, p);
        p.setColor(0xff214d87); canvas.drawCircle(18, 17, 5, p);
        p.setColor(Color.WHITE); p.setTextSize(6); p.setFakeBoldText(true); canvas.drawText("★", 15.5f, 19, p);
        p.setColor(0xffdce6f2); p.setTextSize(21); p.setFakeBoldText(true);
        canvas.drawText(level + "%", 53, 30, p);
        p.setTextSize(10); p.setColor(charging ? 0xff60e6a4 : 0xffd52035);
        canvas.drawText(charging ? "⚡" : "•", 111, 17, p);
        canvas.restore();
    }
}
