package com.vinaykpro.whatsviewer;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ImFlexboxLayoutForMedia extends RelativeLayout {
    private TextView viewPartMain;
    private View viewPartSlave;
    private View viewPartImage;
    private View viewPartAudio;
    private View viewPartDocument;

    private TypedArray a;

    private RelativeLayout.LayoutParams viewPartMainLayoutParams;
    private int viewPartMainWidth;
    private int viewPartMainHeight;

    private RelativeLayout.LayoutParams viewPartSlaveLayoutParams;
    private int viewPartSlaveWidth;
    private int viewPartSlaveHeight;

    private RelativeLayout.LayoutParams viewPartImageLayoutParams;
    private int viewPartImageWidth;
    private int viewPartImageHeight;

    private RelativeLayout.LayoutParams viewPartAudioLayoutParams;
    private int viewPartAudioWidth;
    private int viewPartAudioHeight;

    public ImFlexboxLayoutForMedia(Context context) {
        super(context);
    }

    public ImFlexboxLayoutForMedia(Context context, AttributeSet attrs) {
        super(context, attrs);
        a = context.obtainStyledAttributes(attrs, R.styleable.ImFlexboxLayout, 0, 0);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        try {
            viewPartMain = (TextView) this.findViewById(a.getResourceId(R.styleable.ImFlexboxLayout_viewPartMain, -1));
            viewPartSlave = this.findViewById(a.getResourceId(R.styleable.ImFlexboxLayout_viewPartSlave, -1));
            viewPartImage = this.findViewById(a.getResourceId(R.styleable.ImFlexboxLayout_viewPartImage, -1));
            viewPartAudio = this.findViewById(a.getResourceId(R.styleable.ImFlexboxLayout_viewPartAudio,-1));
            viewPartDocument = this.findViewById(a.getResourceId(R.styleable.ImFlexboxLayout_viewPartDocument,-1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (viewPartMain == null || viewPartSlave == null || widthSize <= 0) {
            return;
        }

        int availableWidth = widthSize - getPaddingLeft() - getPaddingRight();
        int availableHeight = heightSize - getPaddingTop() - getPaddingBottom();

        viewPartImageLayoutParams = (LayoutParams) viewPartImage.getLayoutParams();
        viewPartImageWidth = viewPartImage.getMeasuredWidth() + viewPartImageLayoutParams.leftMargin + viewPartImageLayoutParams.rightMargin;
        viewPartImageHeight = viewPartImage.getMeasuredHeight() + viewPartImageLayoutParams.topMargin + viewPartImageLayoutParams.bottomMargin;

        viewPartMainLayoutParams = (LayoutParams) viewPartMain.getLayoutParams();
        viewPartMainWidth = viewPartMain.getMeasuredWidth() + viewPartMainLayoutParams.leftMargin + viewPartMainLayoutParams.rightMargin;
        viewPartMainHeight = viewPartMain.getMeasuredHeight() /*+ viewPartMainLayoutParams.topMargin + viewPartMainLayoutParams.bottomMargin*/;

        viewPartSlaveLayoutParams = (LayoutParams) viewPartSlave.getLayoutParams();
        viewPartSlaveWidth = viewPartSlave.getMeasuredWidth() + viewPartSlaveLayoutParams.leftMargin + viewPartSlaveLayoutParams.rightMargin;
        viewPartSlaveHeight = viewPartSlave.getMeasuredHeight() + viewPartSlaveLayoutParams.topMargin + viewPartSlaveLayoutParams.bottomMargin;
        if(viewPartAudio!=null) {
            viewPartAudioLayoutParams = (LayoutParams) viewPartAudio.getLayoutParams();
            viewPartAudioWidth = viewPartAudio.getMeasuredWidth() + viewPartAudioLayoutParams.leftMargin + viewPartAudioLayoutParams.rightMargin;
            viewPartAudioHeight = viewPartAudio.getMeasuredHeight() + viewPartAudioLayoutParams.topMargin + viewPartAudioLayoutParams.bottomMargin;
        }
        /*viewPartImage.setMinimumHeight(400);
        viewPartImage.setMinimumWidth(300);
        viewPartImage.setLayoutParams(viewPartImageLayoutParams);*/

        int viewPartMainLineCount = viewPartMain.getLineCount();
        float viewPartMainLastLineWitdh = viewPartMainLineCount > 0 ? viewPartMain.getLayout().getLineWidth(viewPartMainLineCount - 1) : 0;
        widthSize = getPaddingLeft() + getPaddingRight() + 2; //+2 normal
        heightSize = getPaddingTop() + getPaddingBottom();

        if(viewPartImageWidth>1 && viewPartImage != null) {
        if (viewPartMainLineCount > 1 && !(viewPartMainLastLineWitdh + viewPartSlaveWidth >= viewPartMain.getMeasuredWidth())) {
           widthSize += viewPartImageWidth;
            heightSize += viewPartMainHeight;
        } else if (viewPartMainLineCount > 1 && ((viewPartMainLastLineWitdh) + viewPartSlaveWidth >= (viewPartImageWidth-10))) {
            widthSize += viewPartImageWidth;
            heightSize += viewPartMainHeight + viewPartSlaveHeight;
        } else if (viewPartMainLineCount == 1 && (viewPartMainWidth + viewPartSlaveWidth >= (viewPartImageWidth-10))) {
           widthSize += viewPartImage.getMeasuredWidth();
            heightSize += viewPartMainHeight + viewPartSlaveHeight;
        } else {
            widthSize += viewPartImage.getMeasuredWidth();
            heightSize += viewPartMainHeight;
        }
            heightSize += viewPartImageHeight;
            //widthSize = viewPartImage.getMeasuredWidth() + getPaddingStart() + getPaddingEnd() + getPaddingTop()*2;
        } else {
            if (viewPartMainLineCount > 1 && !(viewPartMainLastLineWitdh + viewPartSlaveWidth >= viewPartMain.getMeasuredWidth())) {
                widthSize += viewPartMainWidth;
                heightSize += viewPartMainHeight;
            } else if (viewPartMainLineCount > 1 && (viewPartMainLastLineWitdh + viewPartSlaveWidth >= availableWidth)) {
                widthSize += viewPartMainWidth;
                heightSize += viewPartMainHeight + viewPartSlaveHeight;
            } else if (viewPartMainLineCount == 1 && (viewPartMainWidth + viewPartSlaveWidth >= availableWidth)) {
                widthSize += viewPartMain.getMeasuredWidth();
                heightSize += viewPartMainHeight + viewPartSlaveHeight;
            } else {
                widthSize += viewPartMainWidth + viewPartSlaveWidth;
                heightSize += viewPartMainHeight;
            }

        }
        if(viewPartAudio != null) {
            if (viewPartAudio.getVisibility() == View.VISIBLE) {
                heightSize = getPaddingTop() + getPaddingBottom() + viewPartAudio.getMeasuredHeight();
            } else if (viewPartDocument != null) {
                if (viewPartDocument.getVisibility() == View.VISIBLE) {
                 heightSize = getPaddingTop() + viewPartDocument.getMeasuredHeight();
            } }
        }

        this.setMeasuredDimension(widthSize, heightSize);
        super.onMeasure(MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec((heightSize), MeasureSpec.EXACTLY));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (viewPartMain == null || viewPartSlave == null /*|| viewPartImage == null*/) {
            return;
        }

        if(viewPartDocument != null)
        {
            viewPartDocument.layout(
                    getPaddingLeft(),
                    getPaddingTop(),
                    viewPartDocument.getWidth()/* - viewPartMain.getPaddingTop()*2*/,
                    viewPartDocument.getHeight() + getPaddingTop() + viewPartDocument.getBottom());
        }

        if(viewPartImage != null) {
            viewPartImage.layout(
                    getPaddingLeft() > 0 ? (getPaddingStart() + 3) + getPaddingTop() : getPaddingTop(),
                    getPaddingTop(),
                    viewPartImage.getWidth() - getPaddingTop(),
                    viewPartImage.getHeight() + getPaddingTop());
        }

        viewPartMain.layout(
                getPaddingLeft(),
                getPaddingTop()+viewPartImage.getHeight(),
                viewPartMain.getWidth() + getPaddingLeft(),
                viewPartMain.getHeight() + getPaddingTop()+viewPartImage.getHeight());

        viewPartSlave.layout(
                right - left - viewPartSlaveWidth - getPaddingRight(),
                bottom - top - getPaddingBottom() - viewPartSlaveHeight,
                right - left - getPaddingRight(),
                bottom - top - getPaddingBottom());
    }
}