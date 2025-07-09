package tech.xavi.ecommerce.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceResponse(
        long productId,
        long brandId,
        long priceList,
        LocalDateTime startDate,
        LocalDateTime endDate,
        BigDecimal price,
        String currency
) {
}