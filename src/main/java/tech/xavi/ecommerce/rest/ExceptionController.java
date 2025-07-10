package tech.xavi.ecommerce.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import tech.xavi.ecommerce.exception.EcomBaseException;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(EcomBaseException.class)
    public ResponseEntity<Map<String, ?>> handleApiException(
            EcomBaseException baseExc,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .status(baseExc.getStatusCode())
                .body(Map.of(
                    "traceId", baseExc.getErrorId(),
                    "message", baseExc.getMessage(),
                    "timestamp", Instant.now(),
                    "path", request.getRequestURI(),
                    "method", request.getMethod()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, ?>> handleDtoValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .badRequest()
                .body(Map.of(
                    "traceId", UUID.randomUUID(),
                    "message", "The request contains invalid data",
                    "details", ex.getBindingResult()
                                .getFieldErrors(),
                    "timestamp", Instant.now(),
                    "path", request.getRequestURI(),
                    "method", request.getMethod()
                ));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, ?>> handleParamValidation(
            ConstraintViolationException ex,
            HttpServletRequest request
    ) {
        return ResponseEntity
                .badRequest()
                .body(Map.of(
                    "traceId", UUID.randomUUID(),
                    "message", "Some request parameters are not valid",
                    "details", ex.getConstraintViolations()
                                .stream()
                                .collect(Collectors.toMap(
                                        ConstraintViolation::getPropertyPath,
                                        ConstraintViolation::getMessage
                                )),
                    "timestamp", Instant.now(),
                    "path", request.getRequestURI(),
                    "method", request.getMethod()
                ));
    }

}
