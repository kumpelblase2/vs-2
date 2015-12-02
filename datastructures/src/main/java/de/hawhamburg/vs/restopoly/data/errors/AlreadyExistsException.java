package de.hawhamburg.vs.restopoly.data.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException() {
        super("The resource already exists and cannot be re-created.");
    }
}
