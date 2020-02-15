package com.laioffer.GasMaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.laioffer.GasMaster.GasFragment;
import com.laioffer.GasMaster.PromotionFragment;
import com.laioffer.GasMaster.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.laioffer.GasMaster.R;

public class MainActivity extends AppCompatActivity {
  BottomNavigationView bottomNavigation;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Intent intent = new Intent(this, LoginActivity.class);
    startActivity(intent);

    bottomNavigation = findViewById(R.id.bottom_navigation);
    bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    openFragment(RouteFragment.newInstance("", ""));
  }
  public void openFragment(Fragment fragment) {
    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    transaction.replace(R.id.container, fragment);
    transaction.addToBackStack(null);
    transaction.commit();
  }
  BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
          new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
              switch (item.getItemId()) {
                case R.id.navigation_gas:
                  openFragment(RouteFragment.newInstance("", ""));
                  return true;
                case R.id.navigation_promotion:
                  openFragment(PromotionFragment.newInstance("", ""));
                  return true;
                case R.id.navigation_user:
                  openFragment(UserFragment.newInstance("", ""));
                  return true;
              }
              return false;
            }
          };
}