package com.anime_social.dto.response;

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
public class MomoPaymentResponse {
    // {
    // "partnerCode": "MOMOT5BZ20231213_TEST",
    // "orderId": "Partner_Transaction_ID_1721720620078",
    // "requestId": "Request_ID_1721720620078",
    // "amount": 1000,
    // "responseTime": 1721720619912,
    // "message": "Successful.",
    // "resultCode": 0,
    // "payUrl":
    // "https://test-payment.momo.vn/v2/gateway/pay?t=TU9NT1Q1QloyMDIzMTIxM19URVNUfFBhcnRuZXJfVHJhbnNhY3Rpb25fSURfMTcyMTcyMDYyMDA3OA&s=6c14385cd4355e0abe0e0563a2da20705bceca9fac79746b2bf6a4c380374b44",
    // "deeplink":
    // "momo://app?action=payWithApp&isScanQR=false&serviceType=app&sid=TU9NT1Q1QloyMDIzMTIxM19URVNUfFBhcnRuZXJfVHJhbnNhY3Rpb25fSURfMTcyMTcyMDYyMDA3OA&v=3.0",
    // "qrCodeUrl":
    // "00020101021226110007vn.momo38260010A0000007270208QRIBFTTA5303704540410005802VN62480515MMTCfKWQmuH5nQR0825Thank
    // you for your purcha6304B293"
    // }
    String partnerCode;
    String orderId;
    String requestId;
    Long amount;
    Long responseTime;
    String message;
    Integer resultCode;
    String payUrl;
}
