package edu.umsl.superclickers.activity.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

/**
 * Created by Austin on 5/1/2017.
 */

public class HorDottedProgress extends View {

    private final static String TAG = HorDottedProgress.class.getSimpleName();
    private Canvas canvas;
    private int dotRadius = 6;
    private int bounceDotRadius = 12;
    private int dotPosition;
    private int dotAmount = 4;

    public HorDottedProgress(Context context) {
        super(context);
    }

    public HorDottedProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorDottedProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDotAmount(int dotAmount) {
        this.dotAmount = dotAmount;
        Log.d(TAG, "Set dot amount == " + dotAmount);
    }

    public void setProgress(int progress) {
        this.dotPosition = progress;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#fd584f"));
        createDot(canvas, paint);
    }

    private void createDot(Canvas canvas, Paint paint) {
        // create dots
        for (int i = 0; i < dotAmount; i++) {
            if (i == dotPosition) {
                canvas.drawCircle(10+(i*20), bounceDotRadius, bounceDotRadius, paint);
            } else {
                canvas.drawCircle(10+(i*20), bounceDotRadius, dotRadius, paint);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width, height;
        int calcWidth = (20*(dotAmount+1)*(bounceDotRadius+1));

        width = calcWidth;
        height = bounceDotRadius * 2;

        setMeasuredDimension(width, height);
    }

    public void nextDot() {
        dotPosition++;
        if (dotPosition == dotAmount) {
            dotPosition = 0;
        }
    }

    public void previousDot() {
        dotPosition--;
        if (dotPosition == -1) {
            dotPosition = dotAmount - 1;
        }
    }

    private void startAnimation() {
        BounceAnimation bAnimation = new BounceAnimation();
        bAnimation.setDuration(0);
        bAnimation.setRepeatCount(Animation.INFINITE);
        bAnimation.setInterpolator(new LinearInterpolator());
        bAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        startAnimation(bAnimation);
    }

    private class BounceAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            invalidate();
        }
    }
}
