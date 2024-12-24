package com.digitalestate.gestionprofile.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResourceAlreadyExistsException extends RuntimeException{

    String messageException;
    public ResourceAlreadyExistsException() {
    }

    public ResourceAlreadyExistsException(String message) {
        super(message);
        this.messageException = message;
    }

    public ResourceAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
        this.messageException = message;
    }

}