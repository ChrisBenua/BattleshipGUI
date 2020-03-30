package battleship.gui;

/**
 * Interface for handling refreshing
 * @param <T>
 */
public interface IRefreshable<T> {
    public void refresh(T key);
}
