package tech.xavi.ecommerce.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import tech.xavi.ecommerce.cfg.RestPaths;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class PriceControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void getPriceForDate_shouldReturnBaseTariff() throws Exception {
        mvc.perform(get(RestPaths.PRICE)
                        .param("brandId", "1")
                        .param("productId", "35455")
                        .param("targetDate", String.valueOf(LocalDateTime.parse("2020-06-14T10:00:00")
                                .atOffset(ZoneOffset.UTC)
                                .toEpochSecond())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList").value(1))
                .andExpect(jsonPath("$.price").value(35.50));
    }

    @Test
    void getPriceForDate_shouldReturnPriorityTariff() throws Exception {
        mvc.perform(get(RestPaths.PRICE)
                        .param("brandId", "1")
                        .param("productId", "35455")
                        .param("targetDate", String.valueOf(LocalDateTime.parse("2020-06-14T16:00:00")
                                .atOffset(ZoneOffset.UTC)
                                .toEpochSecond())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.priceList").value(2))
                .andExpect(jsonPath("$.price").value(25.45));
    }

    @Test
    void getPriceForDate_shouldReturn404() throws Exception {
        mvc.perform(get(RestPaths.PRICE)
                        .param("brandId", "1")
                        .param("productId", "35455")
                .param("targetDate", String.valueOf(LocalDateTime.parse("2020-06-13T10:00:00")
                        .atOffset(ZoneOffset.UTC)
                        .toEpochSecond())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", containsString("No applicable price")));
    }

}