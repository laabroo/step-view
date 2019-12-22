package com.vinay.stepview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vinay.stepview.models.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class that displays an array of steps ({@link Drawable} and {@link String}).
 * The drawable and the styling of the text are modified based on the {@link com.vinay.stepview.models.Step.State State} of the {@link Step}
 * <p>
 * This class is abstract and needs to be extended before it can be used.
 * The derived class must define how the Step text is displayed using the {@link #updateView()} method
 */
public abstract class StepView extends LinearLayout implements StepViewIndicator.onUpdateIndicatorListener {

    int mNotCompletedStepTextColor = Color.WHITE;
    int mCompletedStepTextColor = Color.WHITE;
    int mCurrentStepTextColor = Color.WHITE;
    int mTextSize = 14;

    StepViewIndicator mStepViewIndicator;
    RelativeLayout mTextContainer;

    List<Step> mStepList;
    final List<TextView> mTextViewList = new ArrayList<>();

    public StepView(Context context) {
        this(context, null);
    }

    public StepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Returns the list of steps
     *
     * @return {@link List} - List of steps, or null
     */
    @Nullable
    public List<Step> getSteps() {
        return mStepList;
    }

    /**
     * Sets the list of steps.
     *
     * @param stepList {@link List} of {@link Step} objects, or null
     * @return The current {@link StepView} instance for chaining
     */
    public StepView setSteps(@Nullable List<Step> stepList) {
        mStepList = stepList;
        mStepViewIndicator.setSteps(mStepList);
        ensureTextViewCount();

        return this;
    }

    /**
     * Sets the state of the step at the given position.
     *
     * @param state        - The {@link com.vinay.stepview.models.Step.State State} of the {@link Step}
     * @param stepPosition - The integer position of the step whose state is to be modified
     * @return The current {@link StepView} instance for chaining
     * @throws NullPointerException      if the list of steps is null
     * @throws IndexOutOfBoundsException if the position exceeds the size of the list
     */
    public StepView setStepState(@NonNull Step.State state, int stepPosition) {
        if (mStepList == null) {
            throw new NullPointerException(String.format("Invalid attempt to change step state at position %d. List of steps is null. Did you forget to call setSteps()?", stepPosition));
        }
        if (mStepList.size() <= stepPosition) {
            throw new IndexOutOfBoundsException(String.format("Invalid step position %d. The list of steps has %d items", stepPosition, mStepList.size()));
        }

        mStepList.get(stepPosition).setState(state);
        ensureTextViewCount();
        mStepViewIndicator.setSteps(mStepList);

        return this;
    }

    /**
     * Returns the step at the given position
     *
     * @param stepPosition - The integer position of the step to be retrieved
     * @return The current {@link StepView} instance for chaining
     * @throws NullPointerException      if the list of steps is null
     * @throws IndexOutOfBoundsException if the position exceeds the size of the list
     */
    @NonNull
    public Step getStep(int stepPosition) {
        if (mStepList == null) {
            throw new NullPointerException(String.format("Invalid attempt to get step at position %d when list of steps is null. Did you forget to call setSteps()", stepPosition));
        }
        if (mStepList.size() <= stepPosition) {
            throw new IndexOutOfBoundsException(String.format("Invalid step position %d. The list of steps has %d items", stepPosition, mStepList.size()));
        }

        return mStepList.get(stepPosition);
    }

    /**
     * Sets the step at the given position
     *
     * @param step         - The {@link Step} object to be set at the given position
     * @param stepPosition - The integer position in the list of steps
     * @return The current {@link StepView} instance for chaining
     * @throws NullPointerException      if the list of steps is null
     * @throws IndexOutOfBoundsException if the position exceeds the size of the list
     */
    public StepView setStep(@NonNull Step step, int stepPosition) {
        if (mStepList == null) {
            throw new NullPointerException(String.format("Invalid attempt to change step at position %d. List of steps is null. Did you forget to call setSteps()?", stepPosition));
        }
        if (mStepList.size() <= stepPosition) {
            throw new IndexOutOfBoundsException(String.format("Invalid step position %d. The list of steps has %d items", stepPosition, mStepList.size()));
        }

        mStepList.set(stepPosition, step);
        mStepViewIndicator.setSteps(mStepList);
        ensureTextViewCount();

        return this;
    }

    /**
     * Returns the font size (in scaled pixels) of the text
     * Default value is 14sp
     *
     * @return Font size, in scaled pixels
     */
    public int getTextSize() {
        return mTextSize;
    }

    /**
     * Sets the font size of the text.
     * <p>The default value is 14sp</p>
     *
     * @param textSizeSp - The integer font size, in scaled pixels
     * @return The current {@link StepView} instance for chaining
     * @throws Error when the given font size is less than or equal to zero.
     */
    public StepView setTextSize(int textSizeSp) {
        if (textSizeSp <= 0) {
            throw new Error(String.format("Invalid text size %d. Must be greater than zero", textSizeSp));
        }
        mTextSize = textSizeSp;

        return this;
    }

    /**
     * Returns the text color used for not-completed steps
     * <p>The default not-completed step text color is {@link Color#WHITE}</p>
     *
     * @return The text color integer used for not-completed steps
     */
    public int getNotCompletedStepTextColor() {
        return mNotCompletedStepTextColor;
    }

    /**
     * Sets the text color used for not-completed steps
     * <p>The default not-completed step text color is {@link Color#WHITE}</p>
     *
     * @param notCompletedStepTextColor - Not completed step text color (integer)
     * @return The current {@link StepView} instance for chaining
     */
    public StepView setNotCompletedStepTextColor(int notCompletedStepTextColor) {
        mNotCompletedStepTextColor = notCompletedStepTextColor;

        return this;
    }

    /**
     * Returns the text color used for completed steps
     * <p>The default completed step text color is {@link Color#WHITE}</p>
     *
     * @return The text color integer used for completed steps
     */
    public int getCompletedStepTextColor() {
        return mCompletedStepTextColor;
    }

    /**
     * Sets the text color used for completed steps
     * <p>The default completed step text color is {@link Color#WHITE}</p>
     *
     * @param completedStepTextColor - completed step text color (integer)
     * @return The current {@link StepView} instance for chaining
     */
    public StepView setCompletedStepTextColor(int completedStepTextColor) {
        this.mCompletedStepTextColor = completedStepTextColor;

        return this;
    }

    /**
     * Returns the text color used for the current (active) step
     * <p>The default current step text color is {@link Color#WHITE}</p>
     *
     * @return The text color integer used for the current (active) step
     */
    public int getCurrentStepTextColor() {
        return mCurrentStepTextColor;
    }

    /**
     * Sets the text color used for the current step
     * <p>The default current step text color is {@link Color#WHITE}</p>
     *
     * @param currentStepTextColor - current step text color (integer)
     * @return The current {@link StepView} instance for chaining
     */
    public StepView setCurrentStepTextColor(int currentStepTextColor) {
        this.mCurrentStepTextColor = currentStepTextColor;

        return this;
    }

    /**
     * Returns the color of lines leading to a not-completed step
     * <p>The default not-completed line color is {@link Color#WHITE}</p>
     *
     * @return The line color integer
     */
    public int getNotCompletedLineColor() {
        return mStepViewIndicator.getNotCompletedLineColor();
    }

    /**
     * Sets the color of the line leading to a not-completed step
     * <p>The default not-completed line color is {@link Color#WHITE}</p>
     *
     * @param notCompletedLineColor - the integer color of the line leading to a not-completed step
     * @return The current {@link StepView} instance for chaining
     */
    public StepView setNotCompletedLineColor(int notCompletedLineColor) {
        mStepViewIndicator.setNotCompletedLineColor(notCompletedLineColor);

        return this;
    }

    /**
     * Returns line type of Step view
     * @return line type is dash or full line
     */
    public boolean getNotCompleteLineType() {

        return mStepViewIndicator.getNotCompletedLineType();
    }

    /**
     * Set not complete line type
     * @param isDashType
     * @return
     */
    public StepView setNotCompleteLineType(boolean isDashType) {

        mStepViewIndicator.setNotCompletedLineType(isDashType);

        return this;
    }

    /**
     * Returns the color of lines leading to a completed step
     * <p>The default completed line color is {@link Color#WHITE}</p>
     *
     * @return The line color integer
     */
    public int getCompletedLineColor() {
        return mStepViewIndicator.getCompletedLineColor();
    }

    /**
     * Sets the color of the line leading to a completed step
     * <p>The default completed line color is {@link Color#WHITE}</p>
     *
     * @param completedLineColor - the integer color of the line leading to a completed step
     * @return The current {@link StepView} instance for chaining
     */
    public StepView setCompletedLineColor(int completedLineColor) {
        mStepViewIndicator.setCompletedLineColor(completedLineColor);
        return this;
    }

    /**
     * Returns the Drawable used for not-completed steps.
     *
     * @return {@link Drawable} Not-completed step icon
     */
    public Drawable getNotCompletedStepIcon() {
        return mStepViewIndicator.getNotCompletedStepIcon();
    }

    /**
     * Sets the drawable used for not-completed steps.
     *
     * @param notCompletedStepIcon - {@link Drawable} to be used for not-completed steps
     * @return The current {@link StepView} instance for chaining
     */
    public StepView setNotCompletedStepIcon(@NonNull Drawable notCompletedStepIcon) {
        mStepViewIndicator.setNotCompletedStepIcon(notCompletedStepIcon);
        return this;
    }

    /**
     * Returns the Drawable used for completed steps.
     *
     * @return {@link Drawable} completed step icon
     */
    public Drawable getCompletedStepIcon() {
        return mStepViewIndicator.getCompletedStepIcon();
    }

    /**
     * Sets the drawable used for completed steps.
     *
     * @param completedStepIcon - {@link Drawable} to be used for completed steps
     * @return The current {@link StepView} instance for chaining
     */
    public StepView setCompletedStepIcon(@NonNull Drawable completedStepIcon) {
        mStepViewIndicator.setCompletedStepIcon(completedStepIcon);
        return this;
    }

    /**
     * Returns the Drawable used for the current step.
     *
     * @return {@link Drawable} current step icon
     */
    public Drawable getCurrentStepIcon() {
        return mStepViewIndicator.getCurrentStepIcon();
    }

    /**
     * Sets the drawable used for the current step.
     *
     * @param currentStepIcon - {@link Drawable} to be used for the current (active) step
     * @return The current {@link StepView} instance for chaining
     */
    public StepView setCurrentStepIcon(@NonNull Drawable currentStepIcon) {
        mStepViewIndicator.setCurrentStepIcon(currentStepIcon);

        return this;
    }

    /**
     * Returns the length of the line between two steps in pixels
     * <p>
     * The default value is the pixel equivalent of 34dp (0.85 * 40dp)
     *
     * @return - Length of line between two steps in pixels
     */
    public float getLineLengthPx() {
        return mStepViewIndicator.getLineLengthPx();
    }

    /**
     * Sets the length, in pixels, of the line between two steps
     * <p>
     * The default value is the pixel equivalent of 34dp (0.85 * 40dp)
     * </p>
     *
     * @param lineLengthPx - The length, in pixels, of the line between two steps
     * @return The current {@link StepView} instance for chaining
     */
    public StepView setLineLengthPx(float lineLengthPx) {
        mStepViewIndicator.setLineLengthPx(lineLengthPx);

        return this;
    }

    /**
     * Sets the length, in dp, of the line between two steps
     * <p>The default value is the pixel equivalent of 34dp (0.85 * 40dp)</p>
     *
     * @param lineLengthDp - The length, in dp, of the line between two steps
     * @return The current {@link StepView} instance, for chaining
     */
    public StepView setLineLength(float lineLengthDp) {
        mStepViewIndicator.setLineLength(lineLengthDp);

        return this;
    }

    /**
     * Returns the radius of the step's drawable circle in pixels.
     * <p>The default circle radius is approximately the pixel equivalent of 11.2 dp (0.28 * 40dp)</p>
     *
     * @return - Radius of the step's drawable circle, in pixels
     */
    public float getCircleRadiusPx() {
        return mStepViewIndicator.getCircleRadiusPx();
    }

    /**
     * Sets the radius, in pixels, of the drawable circle
     *
     * @param circleRadiusPx - The radius, in pixels, of the drawable circle
     * @return The current {@link StepView} instance, for chaining
     */
    public StepView setCircleRadiusPx(float circleRadiusPx) {
        mStepViewIndicator.setCircleRadiusPx(circleRadiusPx);

        return this;
    }

    /**
     * Sets the radius, in dp, of the drawable circle
     *
     * @param circleRadiusDp - The radius, in dp, of the drawable circle
     * @return The current {@link StepView} instance, for chaining
     */
    public StepView setCircleRadius(float circleRadiusDp) {
        mStepViewIndicator.setCircleRadius(circleRadiusDp);

        return this;
    }

    /**
     * Sets whether the steps should be rendered in reverse order.
     * This defaults to <code>true</code> with {@link VerticalStepView}
     * <p>
     * This method does not affect the UI of {@link HorizontalStepView}
     *
     * @param isReverse - Boolean that indicates whether the steps should be rendered in reverse order
     * @return The current {@link StepView} instance, for chaining
     */
    public StepView setReverse(boolean isReverse) {
        mStepViewIndicator.setReverse(isReverse);

        return this;
    }

    @Override
    public void onIndicatorUpdated() {
        updateView();
    }

    /**
     * Updates the {@link TextView}s that display the {@link Step} text.
     * <p>
     * This must be implemented by derived classes to control the behaviour and direction of the
     * TextViews.
     * <p>
     * This method is invoked when the {@link StepViewIndicator} has redrawn itself as a result of
     * a change in the number of Steps, the size of the View, or manual invalidation
     */
    abstract protected void updateView();


    /**
     * Ensures that the number of cached {@link TextView}s is equal to the number of steps
     * <ol>
     * <li>When there are no steps, it clears all cached TextViews</li>
     * <li>When the number of steps is greater, it creates additional TextViews</li>
     * <li>When the number of TextViews is greater, it removes the extraneous TextViews</li>
     * </ol>
     */
    private void ensureTextViewCount() {
        int stepCount = mStepList == null ? 0 : mStepList.size();
        int textViewCount = mTextViewList.size();
        int delta = textViewCount - stepCount;
        if (stepCount == 0) { // All steps have been removed, or mStepsList is null
            mTextContainer.removeAllViews();
            mTextViewList.clear();
        } else if (delta < 0) { // More TextViews are needed
            for (int i = textViewCount; i < stepCount; i++) {
                TextView textView = new TextView(getContext());
                mTextViewList.add(textView);
                mTextContainer.addView(textView);
            }
        } else if (delta > 0) { // Some TextViews are unnecessary
            mTextContainer.removeViews(stepCount, delta);
            mTextViewList.removeAll(mTextViewList.subList(stepCount, textViewCount));
        }
    }
}
