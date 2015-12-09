package de.hawhamburg.vs.restopoly.data;

import de.hawhamburg.vs.restopoly.data.model.Service;
import de.hawhamburg.vs.restopoly.data.responses.ServicesResponse;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

public class ServiceRegistrator {
    private static final RestTemplate restTemplate = new RestTemplate();

    public static void registerService(String at, Service toRegister) {
        restTemplate.postForLocation("http://" + at + "/services", toRegister);
    }

    public static String lookupService(String at, String service) {
        ServicesResponse response = restTemplate.getForObject(at + "/services/of/name/" + service, ServicesResponse.class);
        String otherService = response.services[response.services.length - 1];
        return restTemplate.getForObject(at + otherService, Service.class).getUri();
    }
}
