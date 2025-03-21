package com.anime_social.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.anime_social.dto.request.MomoPaymentRequest;
import com.anime_social.dto.request.Payment;
import com.anime_social.dto.response.MomoPaymentResponse;
import com.anime_social.enums.Status;
import com.anime_social.exception.CusRunTimeException;
import com.anime_social.exception.ErrorCode;
import com.anime_social.models.PaymentBill;
import com.anime_social.models.User;
import com.anime_social.repositorys.PaymentBillRepository;
import com.anime_social.repositorys.UserRepository;

import java.util.Map;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MomoService {
    private final PaymentBillRepository paymentBillRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final NotificationService notificationService;

    @Value("${MOMO_ACCESS_KEY}")
    private String accessKey;
    @Value("${MOMO_SECRET_KEY}")
    private String secretKey;
    @Value("${MOMO_REQUEST_TYPE}")
    private String requestType;
    @Value("${MOMO_END_POINT}")
    private String endPoint;
    @Value("${MOMO_RETURN_URL}")
    private String returnUrl;
    @Value("${MOMO_CALLBACK_URL}")
    private String callbackUrl;

    public MomoPaymentResponse createQR(Payment paymentRequest) {
        String requestId = UUID.randomUUID().toString();
        String orderId = UUID.randomUUID().toString();
        String orderInfo = paymentRequest.getUserId();
        long amount = paymentRequest.getAmount();
        String extraData = "Have no discount for this order";

        String rawSignature = String.format(
                "accessKey=%s&amount=%s&extraData=%s&ipnUrl=%s&orderId=%s&orderInfo=%s&partnerCode=%s&redirectUrl=%s&requestId=%s&requestType=%s",
                accessKey, amount, extraData, callbackUrl, orderId, orderInfo,
                "MOMO", returnUrl, requestId, requestType);

        String signature = "";

        try {
            signature = sign(rawSignature, secretKey);
        } catch (Exception e) {
            log.error("Error when sign signature: {}", e.getMessage());
            return null;
        }

        if (signature.isBlank()) {
            log.error("Signature is blank");
            return null;
        }

        MomoPaymentRequest momoPaymentRequest = MomoPaymentRequest.builder()
                .partnerCode("MOMO")
                .requestType(requestType)
                .ipnUrl(callbackUrl)
                .redirectUrl(returnUrl)
                .orderId(orderId)
                .amount(amount)
                .orderInfo(orderInfo)
                .requestId(requestId)
                .extraData(extraData)
                .signature(signature)
                .lang("vi")
                .build();

        User user = userRepository.findById(orderInfo)
                .orElseThrow(() -> new CusRunTimeException(ErrorCode.USER_NOT_FOUND));

        PaymentBill paymentBill = PaymentBill.builder()
                .id(requestId)
                .user(user)
                .amount(amount)
                .build();

        paymentBillRepository.save(paymentBill);

        return restTemplate.postForObject(endPoint, momoPaymentRequest, MomoPaymentResponse.class);
    }

    private String sign(String rawSignature, String secretKey) throws Exception {
        Mac sha256Hmac = Mac.getInstance("HmacSHA256");

        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        sha256Hmac.init(secretKeySpec);

        byte[] hash = sha256Hmac.doFinal(rawSignature.getBytes());
        StringBuilder hashString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hashString.append('0');
            }
            hashString.append(hex);
        }
        return hashString.toString();
    }

    public ResponseEntity<String> handleCallback(Map<String, String> response) {
        String requestId = response.get("requestId");
        String orderInfo = response.get("orderInfo");
        Integer resultCode = Integer.parseInt(response.get("resultCode"));
        long amount = Long.parseLong(response.get("amount"));

        if (resultCode != 0) {
            log.error("Error when payment: {}", resultCode);

            PaymentBill paymentBill = paymentBillRepository.findById(requestId)
                    .orElseThrow(() -> new CusRunTimeException(ErrorCode.PAYMENT_BILL_NOT_FOUND));
            paymentBill.setStatus(Status.FAILED.name());
            paymentBillRepository.save(paymentBill);

            return ResponseEntity.badRequest().build();
        }

        PaymentBill paymentBill = paymentBillRepository.findById(requestId)
                .orElseThrow(() -> new CusRunTimeException(ErrorCode.PAYMENT_BILL_NOT_FOUND));
        paymentBill.setStatus(Status.SUCCESS.name());
        paymentBillRepository.save(paymentBill);

        notificationService.paymentSuccessNotification(orderInfo, requestId, amount);

        return ResponseEntity.ok().build();
    }
}
