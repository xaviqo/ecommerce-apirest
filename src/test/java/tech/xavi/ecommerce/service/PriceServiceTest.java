package tech.xavi.ecommerce.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tech.xavi.ecommerce.dto.PriceResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class PriceServiceTest {

    @Autowired
    private PriceService priceService;

    private static Stream<Arguments> expectedPriceArgs() {
        return Stream.of(
                Arguments.of("2020-06-14T10:00:00", 1, new BigDecimal("35.50")),
                Arguments.of("2020-06-14T16:00:00", 2, new BigDecimal("25.45")),
                Arguments.of("2020-06-14T21:00:00", 1, new BigDecimal("35.50")),
                Arguments.of("2020-06-15T10:00:00", 3, new BigDecimal("30.50")),
                Arguments.of("2020-06-16T21:00:00", 4, new BigDecimal("38.95"))
        );
    }

    private static Stream<Arguments> boundaryArgs() {
        return Stream.of(
                Arguments.of("2020-06-14T00:00:00", 1, new BigDecimal("35.50")),
                Arguments.of("2020-06-14T15:00:00", 2, new BigDecimal("25.45")),
                Arguments.of("2020-06-14T18:30:00", 2, new BigDecimal("25.45")),
                Arguments.of("2020-06-15T00:00:00", 3, new BigDecimal("30.50")),
                Arguments.of("2020-06-15T11:00:00", 3, new BigDecimal("30.50")),
                Arguments.of("2020-06-15T16:00:00", 4, new BigDecimal("38.95")),
                Arguments.of("2020-12-31T23:59:59", 4, new BigDecimal("38.95"))
        );
    }

    private static Stream<String> datesWithoutPriceArgs() {
        return Stream.of("2020-06-13T23:59:59", "2021-01-01T00:00:00");
    }

    @ParameterizedTest(name = "test: {index} - date: {0} - list: {1}, price: {2}")
    @MethodSource("expectedPriceArgs")
    void shouldReturnExpectedPriceAndListForTargetDate(
            String targetDate,
            long expectedPriceList,
            BigDecimal expectedPrice
    ) {
        final long epochSeconds = LocalDateTime.parse(targetDate)
                .atOffset(ZoneOffset.UTC)
                .toEpochSecond();

        final PriceResponse priceRes = priceService
                .getPriceForDate(1, 35455, epochSeconds)
                .orElseThrow();

        assertThat(priceRes.priceList()).isEqualTo(expectedPriceList);
        assertThat(priceRes.price()).isEqualByComparingTo(expectedPrice);
    }

    @ParameterizedTest(name = "test: {index} - boundary: {0}")
    @MethodSource("boundaryArgs")
    void shouldReturnCorrectPriceOnIntervalBoundaries(
            String targetDate,
            long expectedList,
            BigDecimal expectedPrice
    ) {
        final long epochSeconds = LocalDateTime.parse(targetDate)
                .atOffset(ZoneOffset.UTC)
                .toEpochSecond();

        final PriceResponse priceRes = priceService
                .getPriceForDate(1, 35455, epochSeconds)
                .orElseThrow();

        assertThat(priceRes.priceList()).isEqualTo(expectedList);
        assertThat(priceRes.price()).isEqualByComparingTo(expectedPrice);
    }

    @ParameterizedTest(name = "test: {index} - no price for {0}")
    @MethodSource("datesWithoutPriceArgs")
    void shouldReturnEmptyWhenNoPriceIsApplicable(String targetDate) {
        final long epochSeconds = LocalDateTime.parse(targetDate)
                .atOffset(ZoneOffset.UTC)
                .toEpochSecond();

        final Optional<PriceResponse> priceRes = priceService
                .getPriceForDate(1, 35455, epochSeconds);

        assertThat(priceRes).isEmpty();
    }

}