package battleship.inner;

/**
 * Defines interface for handling new emitted values
 * @param <T> type of handled emitted values
 */
public interface ISubscriber<T> {
    /**
     * handles new emitted value
     * @param newVal new value
     */
    void accept(T newVal);
}
