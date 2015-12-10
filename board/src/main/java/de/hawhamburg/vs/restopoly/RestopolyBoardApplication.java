package de.hawhamburg.vs.restopoly;

import de.hawhamburg.vs.restopoly.data.model.Service;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@SpringBootApplication
@EnableSwagger2
public class RestopolyBoardApplication {

    public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException {
        SSLUtil.turnOffSslChecking();
        SpringApplication.run(RestopolyBoardApplication.class, args);
    }

    @EventListener
    public void onCreate(ContextRefreshedEvent event) {
        ServiceRegistrator.registerService("vs-docker.informatik.haw-hamburg.de:8053", new Service("Nyuu~Board", "Pooperbutts", "boards", "https://vs-docker.informatik.haw-hamburg.de/ports/12641/boards"));
    }
}
