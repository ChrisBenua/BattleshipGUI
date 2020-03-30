package battleship.inner;

import java.util.ArrayList;
import java.util.List;

/**
 * Events logger
 */
public class EventsLogger implements IEventsLogger {
    private List<ISubscriber<String>> subscribers = new ArrayList<>();

    /**
     * adds new log and emits to all subscribers
     * @param newEventLog new log
     */
    @Override
    public void add(String newEventLog) {
        subscribers.forEach(el -> el.accept(newEventLog));
    }

    /**
     * adds subscriber
     * @param subscriber new subscriber
     */
    @Override
    public void subscribe(ISubscriber<String> subscriber) {
        subscribers.add(subscriber);
    }
}
