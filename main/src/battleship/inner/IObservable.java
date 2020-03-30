package battleship.inner;

/**
 * Interface for defining publisher-like behaviour
 * @param <T> type of emitted values
 */
public interface IObservable<T> {
    void subscribe(ISubscriber<T> subscriber);
}
