package com.tencent.tmf.h5container.doc;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import androidx.annotation.Nullable;

public class LoadingProgress extends View {

    private static final int DEFAULT_TEXT_COLOR = Color.BLUE;
    private static final int DEFAULT_CIRCLE_COLOR = Color.GRAY;
    private static final int DEFAULT_LINE_COLOR = Color.GREEN;
    private static final int MIN_RADIUS = 20;
    private static final int DEFAULT_HITS_TEXT_SIZE = 15;//dp

    private int mTextColor = DEFAULT_TEXT_COLOR;
    private int mCircleColor = DEFAULT_CIRCLE_COLOR;
    private int mLineColor = DEFAULT_LINE_COLOR;
    private Paint mHitPaint;
    private Paint mLinePaint;
    private int mCircleRadius;
    private int mCircleWidth;
    private int mCenterX;
    private int mCenterY;

    private int mProgress;
    private String mHits;
    private Rect mTextBound;
    private float mStartAngle;
    private float mSweepAngle; //degree
    private Paint.FontMetrics fontMetrics;
    private Context mContext;

    public LoadingProgress(Context context) {
        super(context, null);
    }

    public LoadingProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        mHitPaint = new Paint();
        mHitPaint.setAntiAlias(true);
        mHitPaint.setColor(mTextColor);

        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeJoin(Paint.Join.ROUND);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(mCircleColor);

        mProgress = 0;
        mStartAngle = 270; //从顶部开始
        mSweepAngle = 0;
        mTextBound = new Rect();
        fontMetrics = new Paint.FontMetrics();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCircleRadius = Math.min(Math.min(w, h) / 2, dp2px(mContext, MIN_RADIUS));
        mCircleWidth = (int) (mCircleRadius * 0.2f);
        mCenterX = w / 2;
        mCenterY = h / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mLinePaint.setStrokeWidth(mCircleWidth);
        //绘制背景
        mLinePaint.setColor(mCircleColor);
        canvas.save();
        canvas.translate(mCenterX, mCenterY);
        canvas.drawCircle(0, 0, mCircleRadius, mLinePaint);
        //绘制进度线
        mLinePaint.setColor(mLineColor);
        canvas.drawArc(new RectF(-mCircleRadius, -mCircleRadius, mCircleRadius, mCircleRadius), mStartAngle,
                mSweepAngle, false, mLinePaint);
        //绘制文本
        mHitPaint.setTextSize(dp2px(mContext, DEFAULT_HITS_TEXT_SIZE));
        mHits = mProgress + "%";
        mHitPaint.getTextBounds(mHits, 0, mHits.length(), mTextBound);
        mHitPaint.getFontMetrics(fontMetrics);
        canvas.drawText(mProgress + "%", -mTextBound.width() / 2, -fontMetrics.bottom / 2 - fontMetrics.top / 2,
                mHitPaint);
        canvas.restore();
    }

    /**
     * 设置当前进度
     *
     * @param progress 取值1到100
     */
    public void setProgress(int progress) {
        if (progress < 0) {
            progress = 0;
        }
        if (progress > 100) {
            progress = 100;
        }
        mProgress = progress;
        mSweepAngle = (int) ((progress / 100.0f) * 360);
        invalidate();
    }


    private int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }
}
