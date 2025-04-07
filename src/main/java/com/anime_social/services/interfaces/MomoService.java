package com.anime_social.services.interfaces;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.anime_social.dto.request.Payment;
import com.anime_social.dto.response.MomoPaymentResponse;

@Service
public interface MomoService {
    public MomoPaymentResponse createQR(Payment paymentRequest);

    public ResponseEntity<String> handleCallback(Map<String, String> response);
}
