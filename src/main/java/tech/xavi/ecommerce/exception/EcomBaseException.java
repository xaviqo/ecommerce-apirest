package tech.xavi.ecommerce.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public abstract class EcomBaseException extends RuntimeException {
    private static final String DEFAULT_ERROR_MSG = "An internal error has occurred";

    @Getter
    private final UUID errorId;
    @Getter
    private final HttpStatus statusCode;

    public EcomBaseException(String msg, HttpStatus statusCode) {
        super(msg != null ? msg : DEFAULT_ERROR_MSG);
        this.statusCode = statusCode != null ? statusCode : HttpStatus.INTERNAL_SERVER_ERROR;
        this.errorId = UUID.randomUUID();
    }
}
