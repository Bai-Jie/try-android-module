package gq.baijie.android.trymodule;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.Map;

import flow.Direction;
import flow.Flow;
import flow.KeyChanger;
import flow.KeyDispatcher;
import flow.State;
import flow.TraversalCallback;
import gq.baijie.android.trymodule.business.NavigationStates;
import gq.baijie.android.trymodule.view.MainLayoutView;

public class MainActivity extends AppCompatActivity implements KeyChanger {

  private MainLayoutView mainLayoutView;
  private View contentView;

  @Override
  protected void attachBaseContext(Context newBase) {
    newBase = Flow.configure(newBase, this)
        .dispatcher(KeyDispatcher.configure(this, this).build())
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
  }

  @Override
  public void onBackPressed() {
    if (!Flow.get(this).goBack()) {
      super.onBackPressed();
    }
  }

  @Override
  public void changeKey(@Nullable State outgoingState, @NonNull State incomingState,
                        @NonNull Direction direction,
                        @NonNull Map<Object, Context> incomingContexts,
                        @NonNull TraversalCallback callback) {
    final Object key = incomingState.getKey();
    // * clean up showing view
    if (contentView != null && outgoingState != null) {
      // ** save state
      outgoingState.save(contentView);
      contentView = null;
    }
    // * show new view
    if (NavigationStates.PAGE1.equals(key)) {
      contentView = LayoutInflater.from(incomingContexts.get(key))
          .inflate(R.layout.page1, mainLayoutView, false);
    } else if (NavigationStates.PAGE2.equals(key)) {
      contentView = LayoutInflater.from(incomingContexts.get(key))
          .inflate(R.layout.page2, mainLayoutView, false);
    } else {
      final TextView textView = new TextView(incomingContexts.get(key));
      textView.setGravity(Gravity.CENTER);
      textView.setText(key.toString());
      contentView = textView;
    }
    // ** restore state
    incomingState.restore(contentView);
    mainLayoutView.updateNavigationStates(key);
    mainLayoutView.setContentView(contentView);
    callback.onTraversalCompleted();
  }

}
