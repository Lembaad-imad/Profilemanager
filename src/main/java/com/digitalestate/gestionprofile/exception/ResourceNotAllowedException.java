package com.digitalestate.gestionprofile.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResourceNotAllowedException extends RuntimeException{

    String messageException;
    public ResourceNotAllowedException() {
    }

    public ResourceNotAllowedException(String message) {
        super(message);
        this.messageException = message;
    }

    public ResourceNotAllowedException(String message, Throwable cause) {
        super(message, cause);
        this.messageException = message;
    }

}