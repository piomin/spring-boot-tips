package pl.piomin.samples.springboot.tips.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class TipNotFoundHandler {

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ExceptionHandler(NoSuchElementException.class)
    public void handleNotFound() {

    }
}
