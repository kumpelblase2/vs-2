package de.hawhamburg.vs.restopoly;

import de.hawhamburg.vs.restopoly.data.model.Event;
import org.springframework.web.client.RestTemplate;

public class EventPublisher {
    private static final RestTemplate restTemplate = new RestTemplate();

    public static String sendEvent(String eventUri, Event event) {
        try {
            return restTemplate.postForObject(eventUri, event, String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
