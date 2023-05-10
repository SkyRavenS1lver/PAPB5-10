package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

public class MyCanvasView extends View {

    private Paint paint;
    private Path path;
    private int drawColor;
    private int bgColor;
    private Canvas extraCanvas;
    private Bitmap extraBitmap;
    private Rect frame;
    private float lastX,lastY;
    private static final float TOUCH_TOLERANCE = 4;




    public MyCanvasView(Context context) {
        this(context, null);
    }

    public MyCanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        bgColor = ResourcesCompat.getColor(getResources(), R.color.opaque_orange, null);
        drawColor = ResourcesCompat.getColor(getResources(), R.color.opaque_yellow, null);
    path = new Path();
    paint = new Paint();
    paint.setColor(drawColor);
    paint.setAntiAlias(true);
    // make the image smooth
    paint.setDither(true);
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeJoin(Paint.Join.ROUND);
    paint.setStrokeCap(Paint.Cap.ROUND);
    paint.setStrokeWidth(12);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        extraBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);
        extraCanvas = new Canvas(extraBitmap);
        extraCanvas.drawColor(bgColor);
        int inset = 40;
        frame = new Rect(inset, inset, w-inset, h-inset);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(extraBitmap, 0,0 , null);
        canvas.drawRect(frame, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchStart(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(x,y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                touchUp();
                break;
        }
        return true;
    }

    private void touchUp() {
        path.reset();
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x-lastX);
        float dy = Math.abs(y-lastY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE){
            path.quadTo(lastX, lastY, (x+lastX)/2, (y+lastY)/2);
            lastX = x;
            lastY = y;
            extraCanvas.drawPath(path, paint);
        }
    }

    private void touchStart(float x, float y) {
        path.moveTo(x,y);
        lastX = x; lastY = y;
    }
}
