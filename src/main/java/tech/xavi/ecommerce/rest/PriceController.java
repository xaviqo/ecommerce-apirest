package tech.xavi.ecommerce.rest;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.xavi.ecommerce.cfg.RestPaths;
import tech.xavi.ecommerce.dto.PriceResponse;
import tech.xavi.ecommerce.exception.price.PriceNotFoundException;
import tech.xavi.ecommerce.service.PriceService;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(RestPaths.PRICE)
public class PriceController {

    private final PriceService priceService;

    @GetMapping
    public ResponseEntity<PriceResponse> getPriceForDate(
            @RequestParam @Positive long brandId,
            @RequestParam @Positive long productId,
            @RequestParam @Positive long targetDate
    ) {
        return priceService
                .getPriceForDate(brandId, productId, targetDate)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new PriceNotFoundException(brandId, productId, targetDate));
    }
}