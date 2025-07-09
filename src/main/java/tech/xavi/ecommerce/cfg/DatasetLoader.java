package tech.xavi.ecommerce.cfg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import tech.xavi.ecommerce.entity.Price;
import tech.xavi.ecommerce.repository.PriceRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.NoSuchFileException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Configuration
public class DatasetLoader {

    @Bean
    public CommandLineRunner loadPricesCSV(
            @Value("${tech.xavi.cfg.load-dataset}") boolean shouldLoad,
            PriceRepository priceRepository
    ) {
        final String csv = "dataset/prices.csv";

        return args -> {
            if (shouldLoad)
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                new ClassPathResource(csv).getInputStream(),
                                StandardCharsets.UTF_8)
                        )
                ) {
                    final List<Price> prices = reader
                            .lines()
                            .skip(1)
                            .map(line -> line.split(","))
                            .map(fields -> Price.builder()
                                    .brandId(Long.parseLong(fields[0]))
                                    .startDate(LocalDateTime.parse(fields[1]))
                                    .endDate(LocalDateTime.parse(fields[2]))
                                    .priceList(Long.parseLong(fields[3]))
                                    .productId(Long.parseLong(fields[4]))
                                    .priority(Long.parseLong(fields[5]))
                                    .price(new BigDecimal(fields[6]))
                                    .curr(fields[7])
                                    .build())
                            .toList();

                    log.info("Loaded {} prices from dataset",
                            priceRepository.saveAll(prices).size()
                    );
                } catch (NoSuchFileException e) {
                    log.error("CSV {} not found in classpath", csv, e);
                } catch (IOException e) {
                    log.error("I/O error reading {} file", csv, e);
                }
            else
                log.info("Price dataset loading is disabled - Skipping CSV import");
        };
    }

}
