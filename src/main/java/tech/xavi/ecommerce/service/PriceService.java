package tech.xavi.ecommerce.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import tech.xavi.ecommerce.dto.PriceResponse;
import tech.xavi.ecommerce.mapper.PriceMapper;
import tech.xavi.ecommerce.repository.PriceRepository;

import java.time.Instant;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PriceService {

    private final PriceRepository priceRepository;
    private final PriceMapper priceMapper;

    public Optional<PriceResponse> getPriceForDate(
            long brandId,
            long productId,
            long epochSeconds
    ) {
        return priceRepository
                .findPriceForDate(
                        brandId,
                        productId,
                        Instant.ofEpochSecond(epochSeconds),
                        PageRequest.of(0,1)
                )
                .stream()
                .findFirst()
                .map(priceMapper::toDto);
    }

}