package gq.baijie.android.trymodule;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import flow.Flow;
import flow.State;
import gq.baijie.android.trymodule.business.NavigationService;
import gq.baijie.android.trymodule.business.NavigationStates;
import gq.baijie.android.trymodule.view.MainLayoutView;

public class MainActivity extends AppCompatActivity {

  private final NavigationService navigationService = new NavigationService();

  private MainLayoutView mainLayoutView;
  private View contentView;

  public NavigationService getNavigationService() {
    return navigationService;
  }

  @Override
  protected void attachBaseContext(Context newBase) {
    newBase = Flow.configure(newBase, this)
        .dispatcher(navigationService)
        .defaultKey(NavigationStates.PAGE1)
        .install();
    super.attachBaseContext(newBase);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mainLayoutView = new MainLayoutView(this);
    setContentView(mainLayoutView);
    // init toolbar
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    // init drawer
    mainLayoutView.bindActivity(this, toolbar);
    // init navigation
    setupNavigation();
  }

  @Override
  public void onBackPressed() {
    if (!Flow.get(this).goBack()) {
      super.onBackPressed();
    }
  }

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
