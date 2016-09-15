package gq.baijie.android.trymodule;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import flow.Flow;
import flow.State;
import gq.baijie.android.trymodule.business.DaggerService;
import gq.baijie.android.trymodule.business.NavigationService;
import gq.baijie.android.trymodule.business.NavigationStates;
import gq.baijie.android.trymodule.view.MainLayoutView;
import mortar.MortarScope;

import static gq.baijie.android.trymodule.business.DaggerService.createComponent;
import static mortar.MortarScope.buildChild;

public class MainActivity extends AppCompatActivity {

  private static final String SCOPE_NAME = MainActivity.class.getName();

  private NavigationService navigationService;

  private MainLayoutView mainLayoutView;
  private View contentView;

  @Override
  protected void attachBaseContext(Context newBase) {
    newBase = Flow.configure(newBase, this)
        .dispatcher((traversal, callback) -> navigationService.dispatch(traversal, callback))
        .defaultKey(NavigationStates.PAGE1)
        .install();
    super.attachBaseContext(newBase);
  }

  @Override
  public Object getSystemService(@NonNull String name) {
    MortarScope activityScope = MortarScope.findChild(getApplicationContext(), SCOPE_NAME);

    if (activityScope == null) {
      activityScope = buildChild(getApplicationContext()) //
          .withService(DaggerService.SERVICE_NAME, createComponent(MainActivityComponent.class))
          .build(SCOPE_NAME);
    }

    return activityScope.hasService(name) ? activityScope.getService(name)
                                          : super.getSystemService(name);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mainLayoutView = new MainLayoutView(this);
    setContentView(mainLayoutView);
    // init toolbar
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    // init navigation
    final MainActivityComponent component = DaggerService.getDaggerComponent(this);
    navigationService = component.getNavigationService();
    setupNavigation();
  }

  @Override
  protected void onDestroy() {
    if (isFinishing()) {
      MortarScope activityScope = MortarScope.findChild(getApplicationContext(), SCOPE_NAME);
      if (activityScope != null) activityScope.destroy();
    }

    super.onDestroy();
  }

  @Override
  public void onBackPressed() {
    if (!Flow.get(this).goBack()) {
      super.onBackPressed();
    }
  }

  //TODO improve this bind method
  private void setupNavigation() {
    // * save origin's state
    navigationService.getEventBus().filter(it->it.traversal.origin != null).subscribe(event->{
      event.traversal.getState(event.traversal.origin.top()).save(contentView);
    });
    // * show new view
    navigationService.getEventBus().subscribe(event->{
      final Object incomingKey = event.traversal.destination.top();
      final Context incomingContext = event.traversal.createContext(incomingKey, this);
      final State incomingState = event.traversal.getState(incomingKey);

      if (NavigationStates.PAGE1.equals(incomingKey)) {
        contentView = LayoutInflater.from(incomingContext)
            .inflate(R.layout.page1, mainLayoutView, false);
      } else if (NavigationStates.PAGE2.equals(incomingKey)) {
        contentView = LayoutInflater.from(incomingContext)
            .inflate(R.layout.page2, mainLayoutView, false);
      } else {
        final TextView textView = new TextView(incomingContext);
        textView.setGravity(Gravity.CENTER);
        textView.setText(incomingKey.toString());
        contentView = textView;
      }

      incomingState.restore(contentView);
      mainLayoutView.setContentView(contentView);
    });
  }

}
