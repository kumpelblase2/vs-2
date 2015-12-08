package de.hawhamburg.vs.restopoly.data;

import de.hawhamburg.vs.restopoly.data.model.Service;
import org.springframework.web.client.RestTemplate;

public class ServiceRegistrator {
    private static final RestTemplate restTemplate = new RestTemplate();

    public static void registerService(String at, Service toRegister) {
        restTemplate.postForLocation("http://" + at + "/services", toRegister);
    }

    public static String lookupService(String at, String service) {
        return restTemplate.getForObject(at + "/services/of/type/" + service, Service.class).getUri();
    }
}
