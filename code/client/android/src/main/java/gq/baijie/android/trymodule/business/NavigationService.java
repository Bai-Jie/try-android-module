package gq.baijie.android.trymodule.business;

import android.support.annotation.NonNull;


import flow.Dispatcher;
import flow.Traversal;
import flow.TraversalCallback;
import gq.baijie.android.trymodule.business.NavigationService.TraversalEvent;
import gq.baijie.android.trymodule.util.Event;
import gq.baijie.android.trymodule.util.EventBusHelper;
import gq.baijie.android.trymodule.util.EventSource;
import rx.Observable;

public class NavigationService implements EventSource<TraversalEvent>, Dispatcher {

  private EventBusHelper<TraversalEvent> eventBusHelper = EventBusHelper.create();

  @Override
  public Observable<TraversalEvent> getEventBus() {
    return eventBusHelper.getEventBus();
  }

  @Override
  public void dispatch(@NonNull Traversal traversal, @NonNull TraversalCallback callback) {
    eventBusHelper.nextEvent(new TraversalEvent(traversal));
    callback.onTraversalCompleted();
  }

  public static class TraversalEvent implements Event {
    public final Traversal traversal;

    public TraversalEvent(Traversal traversal) {
      this.traversal = traversal;
    }
  }

}
