package tech.xavi.ecommerce.exception.price;

import org.springframework.http.HttpStatus;
import tech.xavi.ecommerce.exception.EcomBaseException;

import java.time.Instant;

public class PriceNotFoundException extends EcomBaseException {

    private static final String ERROR_MSG_TEMPLATE = "No applicable price for product %d, brand %d on %s";

    public PriceNotFoundException(
            long brandId,
            long productId,
            long epochSeconds
    ) {
        super(
                String.format(
                        ERROR_MSG_TEMPLATE,
                        productId,
                        brandId,
                        Instant.ofEpochSecond(epochSeconds)
                ),
                HttpStatus.NOT_FOUND
        );
    }
}