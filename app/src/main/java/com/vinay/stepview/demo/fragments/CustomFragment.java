package com.vinay.stepview.demo.fragments;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.content.res.AppCompatResources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vinay.stepview.StepView;
import com.vinay.stepview.demo.R;
import com.vinay.stepview.models.Step;

import java.util.ArrayList;
import java.util.List;

public class CustomFragment extends Fragment {

  private StepView mHorizontalStepView;

  public CustomFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_custom, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mHorizontalStepView = view.findViewById(R.id.horizontal_step_view);

    mHorizontalStepView
            // Drawables
            .setCompletedStepIcon(AppCompatResources.getDrawable(getContext(), R.drawable.ic_custom_completed))
            .setNotCompletedStepIcon(AppCompatResources.getDrawable(getContext(), R.drawable.ic_custom_not_completed))
            .setCurrentStepIcon(AppCompatResources.getDrawable(getContext(), R.drawable.ic_custom_current))
            // Text colors
            .setCompletedStepTextColor(Color.DKGRAY)                          // Default: Color.WHITE
            .setNotCompletedStepTextColor(Color.DKGRAY)                       // Default: Color.WHITE
            .setCurrentStepTextColor(Color.BLACK)                             // Default: Color.WHITE
            // Line colors
            .setCompletedLineColor(Color.parseColor("#ea655c"))     // Default: Color.WHITE
            .setNotCompletedLineColor(Color.parseColor("#eaac5c"))  // Default: Color.WHITE
            // Text size (in sp)
            .setTextSize(15)                                                  // Default: 14sp
            // Drawable radius (in dp)
            .setCircleRadius(15)                                              // Default: 11.2dp
            // Length of lines separating steps (in dp)
            .setLineLength(50);                                                // Default: 34dp

  }

  @Override
  public void onStart() {
    super.onStart();

    List<Step> stepList = new ArrayList<>();
    stepList.add(new Step("Lorem", Step.State.COMPLETED));
    stepList.add(new Step("Ipsum", Step.State.COMPLETED));
    stepList.add(new Step("Dolor", Step.State.CURRENT));
    stepList.add(new Step("Sit"));
    stepList.add(new Step("Amet"));

    mHorizontalStepView.setSteps(stepList);
  }
}
