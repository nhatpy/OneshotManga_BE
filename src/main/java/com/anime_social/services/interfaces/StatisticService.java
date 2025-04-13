package com.anime_social.services.interfaces;

import org.springframework.stereotype.Service;

import com.anime_social.dto.response.AppResponse;

@Service
public interface StatisticService {

    AppResponse getRevenue();

    AppResponse getMostPopularCategory();

    AppResponse getCount();

    AppResponse getUser(int page, int size);

    AppResponse getManga(int page, int size);

}
