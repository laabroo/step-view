package com.vinay.stepview.demo;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.vinay.stepview.demo.fragments.BasicHorizontalFragment;
import com.vinay.stepview.demo.fragments.BasicVerticalForwardFragment;
import com.vinay.stepview.demo.fragments.BasicVerticalReverseFragment;
import com.vinay.stepview.demo.fragments.CustomFragment;
import com.vinay.stepview.demo.fragments.StateChangeFragment;

public class MainActivity extends AppCompatActivity {

  private DrawerLayout mDrawerLayout;
  private FragmentManager mFragmentManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    ActionBar actionbar = getSupportActionBar();
    if (actionbar != null) {
      actionbar.setDisplayHomeAsUpEnabled(true);
      actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
    }

    mFragmentManager = getSupportFragmentManager();
    mDrawerLayout = findViewById(R.id.drawer_layout);
    NavigationView navigationView = findViewById(R.id.nav_view);

    showFragment(new BasicHorizontalFragment());

    navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

      @Override
      public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
          case R.id.action_horizontal_basic:
            fragment = new BasicHorizontalFragment();
            break;
          case R.id.action_vertical_basic_reverse:
            fragment = new BasicVerticalReverseFragment();
            break;
          case R.id.action_vertical_basic_forward:
            fragment = new BasicVerticalForwardFragment();
            break;
          case R.id.action_state_changes:
            fragment = new StateChangeFragment();
            break;
          case R.id.action_custom:
            fragment = new CustomFragment();
            break;
        }

        if (fragment != null) {
          showFragment(fragment);
        }

        setTitle(item.getTitle());
        item.setChecked(true);
        mDrawerLayout.closeDrawers();
        return true;
      }
    });
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        mDrawerLayout.openDrawer(GravityCompat.START);
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private void showFragment(@NonNull Fragment fragment) {
    mFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
  }
}