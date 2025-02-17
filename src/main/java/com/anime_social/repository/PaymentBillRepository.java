package com.anime_social.repository;

import com.anime_social.entity.PaymentBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentBillRepository extends JpaRepository<PaymentBill, String> {
}
