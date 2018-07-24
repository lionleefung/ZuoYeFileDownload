package com.example.lilingyun.zuoyefiledownload.myprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.example.lilingyun.zuoyefiledownload.R;
import com.example.lilingyun.zuoyefiledownload.utils.DensityUtil;

public class MyProgressbar extends View {
    private Paint mCirclePaint;
    private Paint mTextPaint;
    private Paint mArcPaint;
    private float mPercentProgress;
    private int mInnerColor;
    private int mOutColor;
    private int mInnerCircleWidth;
    private int mOutCircleWidth;
    private String text = "点我";

    public MyProgressbar(Context context){
        this(context,null);
    }
    public MyProgressbar(Context context, AttributeSet attrs){
        this(context,attrs,0);
    }
    public MyProgressbar(Context context,AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MyProgressbar);
        mInnerColor = typedArray.getColor(R.styleable.MyProgressbar_innerCircleColor, ContextCompat.getColor(getContext(),R.color.colorPrimary));
        mOutColor = typedArray.getColor(R.styleable.MyProgressbar_outCircleColor,ContextCompat.getColor(getContext(),R.color.colorAccent));
        mInnerCircleWidth = (int)typedArray.getDimension(R.styleable.MyProgressbar_innerCircleWidth, DensityUtil.dip2px(getContext(),2));
        mOutCircleWidth = (int)typedArray.getDimension(R.styleable.MyProgressbar_outCircleWidth,DensityUtil.dip2px(getContext(),2));
        typedArray.recycle();
        initPaint();
    }

    private  void initPaint(){
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);  //抗锯齿
        mCirclePaint.setColor(mInnerColor);
        mCirclePaint.setStrokeWidth(mInnerCircleWidth);
        mCirclePaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(DensityUtil.dip2px(getContext(),12));

        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setColor(mOutColor);
        mArcPaint.setStrokeWidth(mOutCircleWidth);
        mArcPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width > height ? height:width,width > height ? height : width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /*画圆*/
        float cx = getWidth() / 2;
        float cy = getHeight() /2;
        canvas.drawCircle(cx,cy,getWidth() / 2 - mOutCircleWidth / 2,mCirclePaint);
        /*画显示的字符*/
        Rect textBounds = new Rect();
        mTextPaint.getTextBounds(text,0,text.length(),textBounds);
        int dx = getWidth() / 2 - textBounds.width() / 2;
        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        int baseline = getHeight() / 2 + dy;
        canvas.drawText(text,dx,baseline,mTextPaint);
        /*画下载进度*/
        RectF rectF = new RectF(mOutCircleWidth / 2,mOutCircleWidth / 2, getWidth() - mOutCircleWidth / 2,getHeight() - mOutCircleWidth / 2);
        float sweepAngle = mPercentProgress * 360;
        canvas.drawArc(rectF,0,sweepAngle,false,mArcPaint);
    }
    public synchronized  void setCurrentProgress(float currentProgress){
        this.mPercentProgress = currentProgress;
        if(mPercentProgress > 0){
            text = (int)(mPercentProgress * 100) + "%";
        }
        invalidate();
    }
    public synchronized  void setText(String text){
        this.text = text;
        invalidate();
    }
}
