package de.hawhamburg.vs.restopoly;

import de.hawhamburg.vs.restopoly.data.model.Service;
import de.hawhamburg.vs.restopoly.data.responses.ServicesResponse;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

public class ServiceRegistrator {
    private static final RestTemplate restTemplate = new RestTemplate();
    private static final String FALLBACK_URL = "http://localhost:8080";

    public static void registerService(String at, Service toRegister) {
        try {
            restTemplate.postForLocation("http://" + at + "/services", toRegister);
        } catch(Exception e) {
            System.out.println("Couldn't register service: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String lookupService(String at, String service) {
        try {
            ServicesResponse response = restTemplate.getForObject(at + "/services/of/name/" + service, ServicesResponse.class);
            String otherService = response.services[response.services.length - 1];
            return restTemplate.getForObject(at + otherService, Service.class).getUri();
        } catch(Exception e) {
            System.err.println("Couldn't lookup service: " + e.getMessage());
            e.printStackTrace();
            return FALLBACK_URL;
        }
    }
}
