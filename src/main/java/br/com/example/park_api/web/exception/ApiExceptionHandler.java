package br.com.example.park_api.web.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // Listener for exceptions
@Slf4j // Lombok annotation to print message on console
public class ApiExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class) // Annotation that records which exception the class should catch
    public ResponseEntity<ErrorMessage> MethodArgumentNotValidException
            (MethodArgumentNotValidException e, HttpServletRequest request, BindingResult result) {

        log.error("Api Error - ", e); // Slf4j - To view exception trace

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, "Invalid field(s)", result));
    }
}
