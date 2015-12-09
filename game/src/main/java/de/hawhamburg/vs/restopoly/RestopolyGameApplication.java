package de.hawhamburg.vs.restopoly;

import de.hawhamburg.vs.restopoly.data.SSLUtil;
import de.hawhamburg.vs.restopoly.data.ServiceRegistrator;
import de.hawhamburg.vs.restopoly.data.model.Service;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
public class RestopolyGameApplication {

    public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException {
        SSLUtil.turnOffSslChecking();
        SpringApplication.run(RestopolyGameApplication.class, args);
    }

    @EventListener
    public void onCreate(ContextRefreshedEvent event) throws Exception {
        ServiceRegistrator.registerService("vs-docker.informatik.haw-hamburg.de:8053", new Service("Nyuu~Games", "Pooperbutts", "games", "https://vs-docker.informatik.haw-hamburg.de/ports/12642/games"));
    }
}
