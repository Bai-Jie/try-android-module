package gq.baijie.android.trymodule.util;

import rx.Observable;

public interface EventSource<T extends Event> {

  Observable<T> getEventBus();

}
