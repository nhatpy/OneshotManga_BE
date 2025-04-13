package com.anime_social.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.anime_social.dto.response.AppResponse;
import com.anime_social.services.interfaces.StatisticService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/statistic")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class StatisticController {
    private final StatisticService statisticService;

    @GetMapping("/revenue")
    public AppResponse getRevenue() {
        return statisticService.getRevenue();
    }

    @GetMapping("/most-popular-category")
    public AppResponse getMostPopularCategory() {
        return statisticService.getMostPopularCategory();
    }

    @GetMapping("/count")
    public AppResponse getCount() {
        return statisticService.getCount();
    }

    @GetMapping("/user")
    public AppResponse getUser(
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size) {
        return statisticService.getUser(page, size);
    }

    @GetMapping("/manga")
    public AppResponse getManga(
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size) {
        return statisticService.getManga(page, size);
    }
}
