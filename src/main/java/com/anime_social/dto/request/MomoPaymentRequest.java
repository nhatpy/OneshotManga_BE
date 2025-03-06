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
    // {
    // "partnerCode": "MOMOT5BZ20231213_TEST",
    // "requestType": "captureWallet",
    // "ipnUrl": "https://example.com/momo_ip",
    // "redirectUrl": "https://momo.vn/",
    // "orderId": "Partner_Transaction_ID_1721725424433",
    // "amount": "1000",
    // "orderInfo": "Thank you for your purchase at MoMo_test",
    // "requestId": "Request_ID_1721725424433",
    // "extraData": "eyJza3VzIjoiIn0=",
    // "signature":
    // "5d9eae90a89b45731c7667e9056c95739eb5162a272dfc288aac6090e762b0b9",
    // "lang": "en"
    // }
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
