package de.hawhamburg.vs.restopoly;

import de.hawhamburg.vs.restopoly.data.model.Service;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class RestopolyDeckApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestopolyDeckApplication.class, args);
    }

    @EventListener
    public void onCreate(ContextRefreshedEvent event) {
        ServiceRegistrator.registerService("vs-docker.informatik.haw-hamburg.de:8053", new Service("Nyuu~Decks", "Pooperbutts", "decks", "localhost"));
    }
}
