package com.laioffer.GasMaster;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.laioffer.GasMaster.GasFragment;
import com.laioffer.GasMaster.PromotionFragment;
import com.laioffer.GasMaster.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.laioffer.GasMaster.R;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SearchView.OnFocusChangeListener {
  BottomNavigationView bottomNavigation;
  SearchView searchView;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    // start from login activity
//    Intent intent = new Intent(this, LoginActivity.class);
//    startActivity(intent);

    bottomNavigation = findViewById(R.id.bottom_navigation);
    bottomNavigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    bottomNavigation.setFocusableInTouchMode(true);
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

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.action_bar_menu, menu);
    searchView = (SearchView) menu.findItem(R.id.search).getActionView();
    searchView.setIconifiedByDefault(true);
    searchView.setOnFocusChangeListener(this);
    searchView.setOnQueryTextListener(this);
    return super.onCreateOptionsMenu(menu);
  }

  /**
   * Called when the user submits the query. This could be due to a key press on the
   * keyboard or due to pressing a submit button.
   * The listener can override the standard behavior by returning true
   * to indicate that it has handled the submit request. Otherwise return false to
   * let the SearchView handle the submission by launching any associated intent.
   *
   * @param query the query text that is to be submitted
   * @return true if the query has been handled by the listener, false to let the
   * SearchView perform the default action.
   */
  @Override
  public boolean onQueryTextSubmit(String query) {
    Log.e("Search View", "Called");
    return false;
  }

  /**
   * Called when the query text is changed by the user.
   *
   * @param newText the new content of the query text field.
   * @return false if the SearchView should perform the default action of showing any
   * suggestions if available, true if the action was handled by the listener.
   */
  @Override
  public boolean onQueryTextChange(String newText) {
    return false;
  }

  @Override
  public void onFocusChange(View view, boolean hasFocus) {
    if (!hasFocus) {
      InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
  }

}
