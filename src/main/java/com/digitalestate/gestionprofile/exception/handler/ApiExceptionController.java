package com.digitalestate.gestionprofile.exception.handler;
import com.digitalestate.gestionprofile.exception.ApiFileException;
import com.digitalestate.gestionprofile.exception.ApiNotFoundException;
import com.digitalestate.gestionprofile.exception.ResourceAlreadyExistsException;
import com.digitalestate.gestionprofile.exception.ResourceNotAllowedException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@Getter
@Setter
@ControllerAdvice
public class ApiExceptionController {


    @ExceptionHandler(value = ApiFileException.class)
    public ResponseEntity<Object> exception(ApiFileException exception) {
        return new ResponseEntity<>(exception.getMessageException(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = ResourceAlreadyExistsException.class)
    public ResponseEntity<Object> exception(ResourceAlreadyExistsException exception) {
        return new ResponseEntity<>(exception.getMessageException(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = ApiNotFoundException.class)
    public ResponseEntity<Object> exception(ApiNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessageException(), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(value = ResourceNotAllowedException.class)
    public ResponseEntity<Object> exception(ResourceNotAllowedException exception) {
        return new ResponseEntity<>(exception.getMessageException(), HttpStatus.FORBIDDEN);
    }
}
