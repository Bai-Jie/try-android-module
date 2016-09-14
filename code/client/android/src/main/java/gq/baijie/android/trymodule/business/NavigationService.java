package gq.baijie.android.trymodule.business;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Map;

import flow.Direction;
import flow.Dispatcher;
import flow.State;
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

  //  @Override
  public void changeKey(@Nullable State outgoingState, @NonNull State incomingState,
                        @NonNull Direction direction,
                        @NonNull Map<Object, Context> incomingContexts,
                        @NonNull TraversalCallback callback) {
/*    TraversalEvent event = new TraversalEvent(
        outgoingState != null ? outgoingState.getKey() : null,
        incomingState.getKey());*/
//    eventBusHelper.nextEvent(event);
    callback.onTraversalCompleted();
  }

  public static class TraversalEvent implements Event {
    public final Traversal traversal;

    public TraversalEvent(Traversal traversal) {
      this.traversal = traversal;
    }
  }

}
