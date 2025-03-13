package com.anime_social.models;

import com.anime_social.enums.Status;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "payment_bill")
public class PaymentBill extends BaseEntity {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    String id;

    @Column(name = "amount")
    Long amount;

    @Column(name = "status")
    @Builder.Default
    String status = Status.PENDING.name();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;
}
