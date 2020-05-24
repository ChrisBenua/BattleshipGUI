package battleship.network;

import battleship.network.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DtoReader implements IDtoReader {
    private List<IDtoEventsHandler> subscribers = new ArrayList<>();

    @Override
    public void addEventsHandler(IDtoEventsHandler handler) {
        subscribers.add(handler);
    }

    @Override
    public void removeEventsHandler(IDtoEventsHandler handler) {
        subscribers.remove(handler);
    }

    @Override
    public void read(String object) {
        try {
            var jsonNode = new ObjectMapper().readTree(object);

            String type = jsonNode.get("type").asText();

            switch (type) {
                case GreetingsDto.TYPE: {
                    GreetingsDto dto = new ObjectMapper().readValue(object, GreetingsDto.class);
                    subscribers.forEach(el -> {
                        el.processGreetings(dto);
                    });
                    break;
                }
                case ShotInfoDto.TYPE: {
                    var dto = new ObjectMapper().readValue(object, ShotInfoDto.class);

                    subscribers.forEach(el -> {
                        el.processShotInfo(dto);
                    });
                    break;
                }
                case ShotResultDto.TYPE: {
                    var dto = new ObjectMapper().readValue(object, ShotResultDto.class);

                    subscribers.forEach(el -> el.processShotResult(dto));
                    break;
                }
                case GameStartingEventDto.TYPE: {
                    var dto = new ObjectMapper().readValue(object, GameStartingEventDto.class);
                    subscribers.forEach(el -> el.processGameStartingEvent(dto));
                    break;
                }
                case CancelGameDto.TYPE: {
                    var dto = new ObjectMapper().readValue(object, CancelGameDto.class);
                    subscribers.forEach(el -> el.processCancelGameEvent(dto));
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
