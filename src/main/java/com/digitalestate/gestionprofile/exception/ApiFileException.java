package com.digitalestate.gestionprofile.exception;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiFileException extends RuntimeException{

    String messageException;
    public ApiFileException() {
    }

    public ApiFileException(String message) {
        super(message);
        this.messageException = message;
    }

    public ApiFileException(String message, Throwable cause) {
        super(message, cause);
        this.messageException = message;
    }

}