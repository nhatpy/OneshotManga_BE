package com.anime_social.dto.request;

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
public class MomoPaymentRequest {
    String partnerCode;
    String requestType;
    String ipnUrl;
    String redirectUrl;
    String orderId;
    long amount;
    String orderInfo;
    String requestId;
    String extraData;
    String signature;
    String lang;
}
