package gq.baijie.android.trymodule;

import android.app.Application;

import mortar.MortarScope;

public class MainApplication extends Application {

  private MortarScope rootScope;

  @Override
  public Object getSystemService(String name) {
    if (rootScope == null) {
      rootScope = MortarScope.buildRootScope().build(getScopeName());
    }
    return rootScope.hasService(name) ? rootScope.getService(name) : super.getSystemService(name);
  }

  private String getScopeName() {
    return getClass().getName();
  }

}
