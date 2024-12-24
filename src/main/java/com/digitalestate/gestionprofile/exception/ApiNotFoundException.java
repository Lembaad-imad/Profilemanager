package com.digitalestate.gestionprofile.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiNotFoundException extends RuntimeException{

    String messageException;
    public ApiNotFoundException() {
    }

    public ApiNotFoundException(String message) {
        super(message);
        this.messageException = message;
    }

    public ApiNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.messageException = message;
    }

}