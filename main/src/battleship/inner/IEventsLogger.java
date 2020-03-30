package battleship.inner;

public interface IEventsLogger extends IObservable<String> {
    void add(String newEventLog);
}
