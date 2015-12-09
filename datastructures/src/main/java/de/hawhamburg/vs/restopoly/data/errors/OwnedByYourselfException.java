package de.hawhamburg.vs.restopoly.data.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class OwnedByYourselfException extends RuntimeException {
    public OwnedByYourselfException() {
        super("You already own this place.");
    }
}
