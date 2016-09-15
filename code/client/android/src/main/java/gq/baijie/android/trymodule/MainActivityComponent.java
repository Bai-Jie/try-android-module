package gq.baijie.android.trymodule;

import dagger.Component;
import gq.baijie.android.trymodule.business.NavigationService;

@MainActivityScope
@Component
public interface MainActivityComponent {

  NavigationService getNavigationService();

}
