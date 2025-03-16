package com.anime_social.dto.response;

import java.util.Date;

import com.anime_social.models.PaymentBill;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class PaymentBillResponse {
    String id;
    Long amount;
    String status;
    String userName;
    Date createAt;
    Date updateAt;

    public static PaymentBillResponse toPaymentBillResponse(PaymentBill paymentBill) {
        return PaymentBillResponse.builder()
                .id(paymentBill.getId())
                .amount(paymentBill.getAmount())
                .status(paymentBill.getStatus())
                .userName(paymentBill.getUser().getFullName())
                .createAt(paymentBill.getCreateAt())
                .updateAt(paymentBill.getUpdateAt())
                .build();
    }
}
