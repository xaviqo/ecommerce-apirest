package tech.xavi.ecommerce.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record PriceResponse(
        long productId,
        long brandId,
        long priceList,
        Instant startDate,
        Instant endDate,
        BigDecimal price,
        String currency
) {
}