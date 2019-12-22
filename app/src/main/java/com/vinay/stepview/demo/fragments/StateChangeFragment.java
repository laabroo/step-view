package com.vinay.stepview.demo.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.vinay.stepview.StepView;
import com.vinay.stepview.demo.R;
import com.vinay.stepview.models.Step;

import java.util.ArrayList;
import java.util.List;

public class StateChangeFragment extends Fragment {

  private StepView mHorizontalStepView;
  private List<Step> mStepList;
  private Button mPrevStepBtn, mNextStepBtn, mToggleStateBtn;

  private int mCurrentStepIndex;
  private Step.State mCurrentStepState = Step.State.NOT_COMPLETED;

  public StateChangeFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_state_change, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mHorizontalStepView = view.findViewById(R.id.horizontal_step_view);
    mPrevStepBtn = view.findViewById(R.id.prev_step_btn);
    mNextStepBtn = view.findViewById(R.id.next_step_btn);
    mToggleStateBtn = view.findViewById(R.id.toggle_state_btn);
  }

  @Override
  public void onStart() {
    super.onStart();

    mStepList = new ArrayList<>();
    mStepList.add(new Step("Lorem", Step.State.CURRENT));
    mStepList.add(new Step("Ipsum"));
    mStepList.add(new Step("Dolor"));
    mStepList.add(new Step("Sit"));
    mStepList.add(new Step("Amet"));

    mHorizontalStepView.setSteps(mStepList);
    updateDirectionButtonStates();
    updateToggleButtonText();

    mPrevStepBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mHorizontalStepView.setStepState(mCurrentStepState, mCurrentStepIndex); // Restore step state
        mCurrentStepIndex--; // Change step
        mCurrentStepState = mHorizontalStepView.getStep(mCurrentStepIndex).getState(); // Save new step state
        mHorizontalStepView.setStepState(Step.State.CURRENT, mCurrentStepIndex); // Mark new step as CURRENT

        updateDirectionButtonStates();
        updateToggleButtonText();
      }
    });

    mNextStepBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mHorizontalStepView.setStepState(mCurrentStepState, mCurrentStepIndex); // Restore step state
        mCurrentStepIndex++; // Change step
        mCurrentStepState = mHorizontalStepView.getStep(mCurrentStepIndex).getState(); // Save new step state
        mHorizontalStepView.setStepState(Step.State.CURRENT, mCurrentStepIndex); // Mark new step as CURRENT

        updateDirectionButtonStates();
        updateToggleButtonText();
      }
    });

    mToggleStateBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mCurrentStepState = (mCurrentStepState == Step.State.NOT_COMPLETED) ? Step.State.COMPLETED : Step.State.NOT_COMPLETED;
        updateToggleButtonText();
      }
    });
  }

  private void updateDirectionButtonStates() {
    mPrevStepBtn.setEnabled(mCurrentStepIndex != 0); // Disable prev button when user is at the first step
    mNextStepBtn.setEnabled(mCurrentStepIndex != mStepList.size() - 1); // Disable prev button when user is at the last step
  }

  private void updateToggleButtonText() {
    mToggleStateBtn.setText(mCurrentStepState == Step.State.NOT_COMPLETED ? R.string.state_change_mark_completed : R.string.state_change_mark_not_completed);
  }
}
