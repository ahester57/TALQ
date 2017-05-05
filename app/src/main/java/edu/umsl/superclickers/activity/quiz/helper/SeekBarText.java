package edu.umsl.superclickers.activity.quiz.helper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;

import edu.umsl.superclickers.R;

/**
 * Created by Damon on 5/4/2017.
 */

public class SeekBarText extends AppCompatSeekBar {

    private int mThumbSize;
    private TextPaint mTextPaint;

    public SeekBarText(Context context) {
        this(context, null);
    }

    public SeekBarText(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.seekBarStyle);
    }

    public SeekBarText(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);

        TextView t = new TextView(context);
        t.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Widget_Button);
        mThumbSize = getResources().getDimensionPixelSize(R.dimen.thumb_size);
        mTextPaint = new TextPaint();
        mTextPaint.setTextSize(40);
        mTextPaint.setTypeface(t.getTypeface());
        mTextPaint.setTextAlign(Paint.Align.CENTER);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String progressText = String.valueOf(getProgress());
        Rect bounds = new Rect();
        mTextPaint.getTextBounds(progressText, 0, progressText.length(), bounds);
        int leftPadding = getPaddingLeft() - getThumbOffset();
        int rightPadding = getPaddingRight() - getThumbOffset();
        int width = getWidth() - leftPadding - rightPadding;
        float progressRatio = (float) getProgress() / getMax();
        float thumbOffset = mThumbSize * (.5f - progressRatio);
        float thumbX = progressRatio * width + leftPadding + thumbOffset;
        float thumbY = getHeight() / 2f + bounds.height() / 2f;
        canvas.drawText(progressText, thumbX, thumbY, mTextPaint);
    }
}
