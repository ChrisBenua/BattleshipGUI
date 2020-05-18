package battleship.network;

import battleship.network.dto.GreetingsDto;

public interface IDtoReaderEventsHandler {
    public void processGreetings(GreetingsDto greetingsDto);
}
