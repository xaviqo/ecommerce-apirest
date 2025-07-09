package tech.xavi.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tech.xavi.ecommerce.entity.Price;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {

}