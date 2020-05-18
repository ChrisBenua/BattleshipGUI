package battleship.network;

import battleship.network.dto.GreetingsDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class DtoReader implements IDtoReader {
    private List<IDtoReaderEventsHandler> subscribers = new ArrayList<>();

    @Override
    public void addEventsHandler(IDtoReaderEventsHandler handler) {
        subscribers.add(handler);
    }

    @Override
    public void read(String object) {
        try {
            var jsonNode = new ObjectMapper().readTree(object);

            String type = jsonNode.get("type").asText();

            if (type.equals(GreetingsDto.TYPE)) {
                GreetingsDto dto = new ObjectMapper().readValue(object, GreetingsDto.class);
                subscribers.forEach(el -> {
                    el.processGreetings(dto);
                });
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
