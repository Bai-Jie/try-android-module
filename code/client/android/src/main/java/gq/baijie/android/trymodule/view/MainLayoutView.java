package gq.baijie.android.trymodule.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import flow.Flow;
import gq.baijie.android.trymodule.R;
import gq.baijie.android.trymodule.business.NavigationStates;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class MainLayoutView extends DrawerLayout
    implements NavigationView.OnNavigationItemSelectedListener {

  private final NavigationView navigationView;

  private final FrameLayout mainContainer;

  public MainLayoutView(Context context) {
    super(context);
  }

  public MainLayoutView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public MainLayoutView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  // init
  {
    setId(R.id.drawer_layout);
    setFitsSystemWindows(true);

    LayoutInflater inflater = LayoutInflater.from(getContext());
    inflater.inflate(R.layout.app_bar_main, this);
    inflater.inflate(R.layout.nav_main, this);

    mainContainer = (FrameLayout) findViewById(R.id.main_container);
    navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
  }

  public void bindActivity(Activity activity, @Nullable Toolbar toolbar) {
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        activity, this, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    addDrawerListener(toggle);
    toggle.syncState();
  }

  public void updateNavigationStates(Object key) {
    if (NavigationStates.PAGE1.equals(key)) {
      navigationView.setCheckedItem(R.id.nav_camera);
    }
  }

  public void setContentView(View view) {
    mainContainer.removeAllViews();
    mainContainer.addView(view, MATCH_PARENT, MATCH_PARENT);
  }

  // ########## Input:onBackPressed ##########

  {
    // set Focusable to receive KeyEvent
    setFocusable(true);
    setFocusableInTouchMode(true);
  }

  //TODO recheck this method
  //reference: http://android-developers.blogspot.in/2009/12/back-and-other-hard-keys-three-stories.html
  @Override
  public boolean dispatchKeyEvent(KeyEvent event) {
    if (event.getKeyCode() != KeyEvent.KEYCODE_BACK) {
      return super.dispatchKeyEvent(event);
    }

    if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0
        ) {

      // Tell the framework to start tracking this event.
      getKeyDispatcherState().startTracking(event, this);
      return true;

    } else if (event.getAction() == KeyEvent.ACTION_UP) {
      getKeyDispatcherState().handleUpEvent(event);
      if (event.isTracking() && !event.isCanceled()) {

        // DO BACK ACTION HERE
        return onBackPressed();

      }
    }
    return super.dispatchKeyEvent(event);
  }

  private boolean onBackPressed() {
    return closeDrawer();
  }

  private boolean closeDrawer() {
    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
      return true;
    } else {
      return false;
    }
  }

  // ########## Input:onBackPressed End ##########


  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(MenuItem item) {
    // Handle navigation view item clicks here.
    int id = item.getItemId();

    if (id == R.id.nav_camera) {
      // Handle the camera action
      Flow.get(this).set(NavigationStates.PAGE1);
    } else if (id == R.id.nav_gallery) {
      Flow.get(this).set(NavigationStates.PAGE2);
    } else if (id == R.id.nav_slideshow) {
      Flow.get(this).set(NavigationStates.PAGE3);
    } else if (id == R.id.nav_manage) {
      Flow.get(this).set(NavigationStates.PAGE4);
    } else if (id == R.id.nav_share) {

    } else if (id == R.id.nav_send) {

    }

    closeDrawer(GravityCompat.START);
    return true;
  }


}
