package com.mss.exam.portal.repository.payment;

import com.mss.exam.portal.entity.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
