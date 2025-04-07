package com.anime_social.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anime_social.dto.request.Payment;
import com.anime_social.dto.response.MomoPaymentResponse;
import com.anime_social.services.interfaces.MomoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class MomoController {
    private final MomoService momoService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/momo")
    public MomoPaymentResponse momoPayment(@Valid @RequestBody Payment paymentRequest) {
        return momoService.createQR(paymentRequest);
    }

    // thanh toan hoan tat -> momo redirect ve localhost:5173 kem them param ->
    // check neu co param thi call api nay, trang home luc nay la trang trung gian
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/callback")
    public ResponseEntity<String> handleCallback(@RequestParam Map<String, String> response) {
        return momoService.handleCallback(response);
    }
}
