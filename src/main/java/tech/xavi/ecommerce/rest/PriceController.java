package tech.xavi.ecommerce.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Get price for a specific date",
            description = "Returns the price list that applies to a given product/brand for the supplied date (UTC), " +
                    "expressed as **epoch seconds**."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Price found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PriceResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No price found for the given input",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                            {
                              "method": "GET",
                              "timestamp": "2025-07-10T18:11:46.129460279Z",
                              "message": "No applicable price for product 35455, brand 3 on 2020-06-14T10:00:00Z",
                              "traceId": "41c68a35-5b4f-44a9-a641-eacb98bf76ce",
                              "path": "/api/v1/prices"
                            }"""
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<PriceResponse> getPriceForDate(
            @Parameter(
                    description = "Brand identifier",
                    example = "1"
            )
            @RequestParam @Positive long brandId,

            @Parameter(
                    description = "Product identifier",
                    example = "35455"
            )
            @RequestParam @Positive long productId,

            @Parameter(
                    description = "Target date (UTC) in epoch seconds",
                    example = "1592128800"
            )
            @RequestParam @Positive long targetDate
    ) {
        return priceService
                .getPriceForDate(brandId, productId, targetDate)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new PriceNotFoundException(brandId, productId, targetDate));
    }
}