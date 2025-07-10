package tech.xavi.ecommerce.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tech.xavi.ecommerce.entity.Price;

import java.time.Instant;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {

    @Query("""
            SELECT p
            FROM Price p
            WHERE p.brandId = :brandId
             AND p.productId = :productId
             AND p.startDate <= :targetDate
             AND p.endDate >= :targetDate
            ORDER BY p.priority DESC
        """)
    Page<Price> findPriceForDate(
            @Param("brandId") long brandId,
            @Param("productId") long productId,
            @Param("targetDate") Instant targetDate,
            Pageable pageable
    );

}