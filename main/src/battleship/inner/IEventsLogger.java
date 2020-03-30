package battleship.inner;

/**
 * Interface for events producer
 */
public interface IEventsLogger extends IObservable<String> {
    /**
     * Adds new log
     * @param newEventLog new log
     */
    void add(String newEventLog);
}
