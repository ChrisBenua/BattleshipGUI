package battleship.network;

import battleship.network.dto.*;

public interface IDtoEventsHandler {
    public default void processGreetings(GreetingsDto greetingsDto) {}

    public default void processShotInfo(ShotInfoDto shotInfoDto) {}

    public default void processShotResult(ShotResultDto shotResultDto) {}

    public default void processGameStartingEvent(GameStartingEventDto gameStartingEventDto) {}

    public default void processCancelGameEvent(CancelGameDto cancelGameDto) {}
}
