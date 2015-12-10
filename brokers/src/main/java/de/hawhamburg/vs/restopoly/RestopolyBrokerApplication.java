package de.hawhamburg.vs.restopoly;

import de.hawhamburg.vs.restopoly.data.model.Service;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class RestopolyBrokerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestopolyBrokerApplication.class, args);
    }

    @EventListener
    public void onCreate(ContextRefreshedEvent event) {
        ServiceRegistrator.registerService("vs-docker.informatik.haw-hamburg.de:8053", new Service("Nyuu~Broker", "Pooperbutts", "brokers", "localhost"));
    }
}
