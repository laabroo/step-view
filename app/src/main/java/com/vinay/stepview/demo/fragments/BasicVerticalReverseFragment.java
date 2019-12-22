package com.vinay.stepview.demo.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vinay.stepview.VerticalStepView;
import com.vinay.stepview.demo.R;
import com.vinay.stepview.models.Step;

import java.util.ArrayList;
import java.util.List;

public class BasicVerticalReverseFragment extends Fragment {

  private VerticalStepView mVerticalStepView;

  public BasicVerticalReverseFragment() {
    // Required empty public constructor
  }

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_basic_vertical_reverse, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mVerticalStepView = view.findViewById(R.id.vertical_step_view);
  }

  @Override
  public void onStart() {
    super.onStart();

    List<Step> stepList = new ArrayList<>();
    stepList.add(new Step("1. Add items to cart", Step.State.COMPLETED));
    stepList.add(new Step("2. Proceed to checkout", Step.State.COMPLETED));
    stepList.add(new Step("3. Confirm checkout", Step.State.COMPLETED));
    stepList.add(new Step("4. Enter shipping address", Step.State.COMPLETED));
    stepList.add(new Step("5. Choose payment option", Step.State.COMPLETED));
    stepList.add(new Step("6. Enter payment information", Step.State.CURRENT));
    stepList.add(new Step("7. Finish payment", Step.State.NOT_COMPLETED));
    stepList.add(new Step("8. Receive order confirmation email", Step.State.NOT_COMPLETED));
    stepList.add(new Step("9. Wait for delivery", Step.State.NOT_COMPLETED));

    mVerticalStepView.setSteps(stepList);
  }
}
