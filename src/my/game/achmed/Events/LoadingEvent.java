package my.game.achmed.Events;

import my.game.achmed.Listeners.OnLoadingEventListener;

public class LoadingEvent {

    private OnLoadingEventListener onLoadingEvent;

    public void addOnEventListener(OnLoadingEventListener listener) {
	onLoadingEvent = listener;
    }

    public void doLoadingEvent(boolean loaded) {
         if (onLoadingEvent != null)
             onLoadingEvent.onLoadingEvent(loaded);
    }
    
}
