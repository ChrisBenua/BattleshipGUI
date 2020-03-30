package battleship.inner;

public interface ISubscriber<T> {
    void accept(T newVal);
}
