package tech.xavi.ecommerce.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import tech.xavi.ecommerce.dto.PriceResponse;
import tech.xavi.ecommerce.entity.Price;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PriceMapper {
    @Mapping(source = "curr", target = "currency")
    PriceResponse toDto(Price price);
}