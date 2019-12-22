package com.vinay.stepview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.vinay.stepview.models.Step;

import java.util.ArrayList;
import java.util.List;

abstract class StepViewIndicator extends View {

    interface onUpdateIndicatorListener {
        void onIndicatorUpdated();
    }

    final int DEFAULT_STEP_INDICATOR_DIMENSION = (int) convertDpToPx(40);

    float mCompletedLineHeight; // completed line height
    float mCircleRadius; // Step circle radius

    Drawable mCompletedStepIcon; // Drawable/icon used for a completed step
    Drawable mCurrentStepIcon; // Drawable/icon used for the current step
    Drawable mNotCompletedStepIcon; // Drawable/icon used for a not completed (default) step

    Paint mNotCompletedLinePaint; // Style of line leading to a not-completed step
    Paint mCompletedLinePaint; // Style of line leading to a completed step

    int mNotCompletedLineColor = Color.WHITE; // Default color of a not-completed line
    int mCompletedLineColor = Color.WHITE; // Default color of a completed line
    float mLineLength; // Default spacing between circles of two steps

    List<Float> mCircleCenterPointPositionList; // List of center points of all circles
    List<Step> mStepList; // List of steps

    Path mPath; // Path of lines leading to not-completed steps
    final Rect mRect = new Rect(); // The bounding rectangle of the Step icon/drawables

    boolean mNotCompletedLineType = true;

    /**
     * Determines whether the StepView should be displayed in reverse order.
     * Defaults to true.
     * Note: Only applicable for {@link VerticalStepViewIndicator}
     */
    boolean mIsReverseDraw = true;

    onUpdateIndicatorListener mUpdateIndicatorListener;

    private DashPathEffect mEffects;

    public StepViewIndicator(Context context) {
        this(context, null);
    }

    public StepViewIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepViewIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Returns the drawable used for completed steps
     *
     * @return {@link Drawable}
     */
    @NonNull
    public Drawable getCompletedStepIcon() {
        return mCompletedStepIcon;
    }

    /**
     * Sets the drawable used for completed steps
     *
     * @param completedStepIcon {@link Drawable}
     */
    public void setCompletedStepIcon(@NonNull Drawable completedStepIcon) {
        mCompletedStepIcon = completedStepIcon;
    }

    /**
     * Returns the drawable used for steps that are not completed
     *
     * @return {@link Drawable}
     */
    @NonNull
    public Drawable getNotCompletedStepIcon() {
        return mNotCompletedStepIcon;
    }

    /**
     * Sets the drawable used for steps that are not completed
     *
     * @param notCompletedStepIcon {@link Drawable}
     */
    public void setNotCompletedStepIcon(@NonNull Drawable notCompletedStepIcon) {
        mNotCompletedStepIcon = notCompletedStepIcon;
    }

    /**
     * Returns the drawable used for the current step
     *
     * @return {@link Drawable}
     */
    @NonNull
    public Drawable getCurrentStepIcon() {
        return mCurrentStepIcon;
    }

    /**
     * Sets the drawable used for the current step
     *
     * @param currentStepIcon {@link Drawable}
     */
    public void setCurrentStepIcon(@NonNull Drawable currentStepIcon) {
        mCurrentStepIcon = currentStepIcon;
    }

    /**
     * Returns the color used for completed lines
     *
     * @return Completed line color
     */
    public int getCompletedLineColor() {
        return mCompletedLineColor;
    }

    /**
     * Sets the color used for completed lines
     *
     * @param completedLineColor (int) - Completed line color
     */
    public void setCompletedLineColor(int completedLineColor) {
        mCompletedLineColor = completedLineColor;
    }

    /**
     * Returns the radius, in pixels, of the circle in which the Step icon/drawable is displayed
     *
     * @return Circle radius, in pixels (float)
     */
    public float getCircleRadiusPx() {
        return mCircleRadius;
    }

    /**
     * Sets the radius of the circle in which the Step icon/drawable is displayed
     *
     * @param circleRadiusPx Circle radius, in pixels (float)
     */
    public void setCircleRadiusPx(float circleRadiusPx) {
        this.mCircleRadius = circleRadiusPx;
    }

    /**
     * Sets the radius of the circle in which the Step icon/drawable is displayed
     *
     * @param circleRadiusDp Circle radius, in density-independent pixels (dp) (float)
     */
    public void setCircleRadius(float circleRadiusDp) {
        Log.d("StepViewIndicator", "setCircleRadius called with " + circleRadiusDp);
        Log.d("StepViewIndicator", "convertDpToPx = " + convertDpToPx(circleRadiusDp));
        this.mCircleRadius = convertDpToPx(circleRadiusDp);
    }

    /**
     * Returns the length of the line that separates Step icons
     *
     * @return Line length, in pixels (float)
     */
    public float getLineLengthPx() {
        return mLineLength;
    }

    /**
     * Sets the length of the line that separates Step icons
     *
     * @param lineLengthPx Line length, in pixels (float)
     */
    public void setLineLengthPx(float lineLengthPx) {
        this.mLineLength = lineLengthPx;
    }

    /**
     * Sets the length of the line that separates Step icons
     *
     * @param lineLengthDp Line length, in density-independent pixels (dp) (float)
     */
    public void setLineLength(float lineLengthDp) {
        this.mLineLength = convertDpToPx(lineLengthDp);
    }

    /**
     * Returns the color used for not-completed lines
     *
     * @return Not-completed line color
     */
    public int getNotCompletedLineColor() {
        return mNotCompletedLineColor;
    }

    /**
     * Sets the color used for not-completed lines
     *
     * @param notCompletedLineColor (int) - Not completed line color
     */
    public void setNotCompletedLineColor(int notCompletedLineColor) {
        mNotCompletedLineColor = notCompletedLineColor;
    }


    /**
     * Sets the line type
     *
     * @param isDashType
     */
    public void setNotCompletedLineType(boolean isDashType) {
        mNotCompletedLineType = isDashType;

        updateNotCompleteLineType();
    }

    /**
     * Get
     *
     * @return notCompletedLineType (boolean) - Not complete line type
     * When return is True, it means the line type is Dash.
     * And when the return is false, it means the line type is full line
     */
    public boolean getNotCompletedLineType() {
        return mNotCompletedLineType;
    }

    /**
     * Sets a listener to be notified when the StepViewIndicator has redrawn itself
     *
     * @param updateIndicatorListener OnUpdateIndicatorListener that will be notified after StepViewIndicator redraw
     */
    public void setOnUpdateIndicatorListener(@NonNull onUpdateIndicatorListener updateIndicatorListener) {
        mUpdateIndicatorListener = updateIndicatorListener;
    }

    /**
     * Sets whether the StepViewIndicator and StepView should be displayed in reverse order.
     * This defaults to <code>true</code>
     *
     * @param isReverseDraw - Display the Steps in reverse order (boolean)
     */
    public void setReverse(boolean isReverseDraw) {
        this.mIsReverseDraw = isReverseDraw;
        invalidate();
    }

    /**
     * Sets the list of steps. This is invoked whenever the list of steps
     * is updated in the wrapper {@link StepView}
     *
     * @param stepList {@link List} of {@link Step}s, or null
     */
    public void setSteps(@Nullable List<Step> stepList) {
        int numSteps = getNumOfSteps();
        Log.d("StepView", "SVIndicator: setSteps called with " + (stepList == null ? "null" : stepList.size()) + " items");
        mStepList = stepList;

        invalidate();
        if (numSteps != mStepList.size()) {
            requestLayout();
        }
    }

    /**
     * Returns the list of coordinates of the center of the Step icons
     *
     * @return {@link List} of center point coordinates
     */
    @NonNull
    List<Float> getCircleCenterPointPositionList() {
        return mCircleCenterPointPositionList;
    }

    /**
     * Helper method that returns the number of steps (if any), or zero.
     *
     * @return Number of steps, or zero
     */
    int getNumOfSteps() {
        return mStepList == null ? 0 : mStepList.size();
    }

    /**
     * Helper method to convert density-independent pixels to pixels
     *
     * @param numDp Value, in dp
     * @return Value in px
     */
    private float convertDpToPx(float numDp) {
        Log.d("SVI", "convertDpToPx called with " + numDp);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        Log.d("SVI", String.format("SVI displayMetrics: h: %d, w: %d. Density: %f", displayMetrics.heightPixels, displayMetrics.widthPixels, displayMetrics.density));
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, numDp, getResources().getDisplayMetrics());
    }

    private void init() {
        mPath = new Path();
        mEffects = new DashPathEffect(new float[]{8, 8, 8, 8}, 1);
        mCircleCenterPointPositionList = new ArrayList<>();//初始化

        mNotCompletedLinePaint = new Paint();
        mCompletedLinePaint = new Paint();
        mNotCompletedLinePaint.setAntiAlias(true);
        mNotCompletedLinePaint.setColor(mNotCompletedLineColor);
        mNotCompletedLinePaint.setStyle(Paint.Style.STROKE);
        mNotCompletedLinePaint.setStrokeWidth(2);

        mCompletedLinePaint.setAntiAlias(true);
        mCompletedLinePaint.setColor(mCompletedLineColor);
        mCompletedLinePaint.setStyle(Paint.Style.STROKE);
        mCompletedLinePaint.setStrokeWidth(2);

        updateNotCompleteLineType();

        mCompletedLinePaint.setStyle(Paint.Style.FILL);

        mCompletedLineHeight = 0.05f * DEFAULT_STEP_INDICATOR_DIMENSION;
        mCircleRadius = 0.28f * DEFAULT_STEP_INDICATOR_DIMENSION;
        mLineLength = 0.85f * DEFAULT_STEP_INDICATOR_DIMENSION;

        mCompletedStepIcon = AppCompatResources.getDrawable(getContext(), R.drawable.ic_completed);
        mCurrentStepIcon = AppCompatResources.getDrawable(getContext(), R.drawable.ic_current);
        mNotCompletedStepIcon = AppCompatResources.getDrawable(getContext(), R.drawable.ic_not_completed);
    }

    private void updateNotCompleteLineType() {

        if (mNotCompletedLineType) {
            mNotCompletedLinePaint.setPathEffect(mEffects);
        }else {
            mNotCompletedLinePaint.setStyle(Paint.Style.FILL);
        }
    }
}
