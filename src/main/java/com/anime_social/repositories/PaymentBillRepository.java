package com.anime_social.repositories;

import com.anime_social.models.PaymentBill;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentBillRepository extends JpaRepository<PaymentBill, String> {
    @Query("""
                SELECT
                    MONTH(p.createAt),
                    SUM(p.amount),
                    COUNT(CASE WHEN p.status = 'SUCCESS' THEN 1 END),
                    COUNT(CASE WHEN p.status = 'FAILED' THEN 1 END),
                    COUNT(CASE WHEN p.status = 'PENDING' THEN 1 END),
                    MIN(p.createAt)
                FROM PaymentBill p
                WHERE YEAR(p.createAt) = :year
                GROUP BY MONTH(p.createAt)
                ORDER BY MONTH(p.createAt)
            """)
    List<Object[]> getMonthlyRevenueStats(@Param("year") int year);
}
