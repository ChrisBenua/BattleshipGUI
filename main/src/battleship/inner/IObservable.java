package battleship.inner;

/**
 * Interface for defining publisher-like behaviour
 * @param <T> type of emitted values
 */
public interface IObservable<T> {
    /**
     * Adds a subscriber, subscriber will receive new  values emitted since subscription
     * @param subscriber new subscriber
     */
    void subscribe(ISubscriber<T> subscriber);
}
