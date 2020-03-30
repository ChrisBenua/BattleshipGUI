package battleship.inner;

public interface IObservable<T> {
    void subscribe(ISubscriber<T> subscriber);
}
