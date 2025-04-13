package com.anime_social.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.anime_social.dto.response.AppResponse;
import com.anime_social.dto.response.MangaResponse;
import com.anime_social.dto.response.RevenueResponse;
import com.anime_social.dto.response.RevenueStatusReponse;
import com.anime_social.dto.response.StatisticCountResponse;
import com.anime_social.dto.response.TopCateriesStatisticResponse;
import com.anime_social.dto.response.UserResponse;
import com.anime_social.models.MangaInteraction;
import com.anime_social.models.User;
import com.anime_social.repositories.CategoryMangaRepository;
import com.anime_social.repositories.MangaInteractionRepository;
import com.anime_social.repositories.PaymentBillRepository;
import com.anime_social.repositories.UserRepository;
import com.anime_social.services.interfaces.StatisticService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticServiceImpl implements StatisticService {
        private final UserRepository userRepository;
        private final MangaInteractionRepository mangaInteractionRepository;
        private final PaymentBillRepository paymentBillRepository;
        private final CategoryMangaRepository categoryMangaRepository;

        @Override
        public AppResponse getUser(int page, int size) {
                int starterPage = page - 1;
                Pageable pageable = PageRequest.of(starterPage, size).withSort(Sort.by("wallet").descending());

                List<User> users = userRepository.findAllTopUser(pageable);
                Integer totalUser = userRepository.getNumberOfTopUserVerified().orElse(0);
                List<UserResponse> userResponses = users.stream()
                                .map((user) -> UserResponse.toUserResponse(user))
                                .collect(Collectors.toList());

                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .data(userResponses)
                                .totalItem(totalUser)
                                .message("Lấy danh sách người dùng thành công")
                                .build();
        }

        @Override
        public AppResponse getManga(int page, int size) {
                int starterPage = page - 1;
                Pageable pageable = PageRequest.of(starterPage, size);

                List<MangaInteraction> mangaInteractions = mangaInteractionRepository.findTopDayManga(pageable);
                Integer totalManga = mangaInteractionRepository.countTopDayManga().orElse(0);
                List<MangaResponse> mangaResponses = mangaInteractions.stream()
                                .map((manga) -> MangaResponse.toMangaResponse(manga.getManga()))
                                .collect(Collectors.toList());

                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .data(mangaResponses)
                                .totalItem(totalManga)
                                .message("Lấy danh sách manga thành công")
                                .build();
        }

        @Override
        public AppResponse getRevenue() {
                int currentYear = LocalDate.now().getYear();

                List<Object[]> resultList = paymentBillRepository.getMonthlyRevenueStats(currentYear);
                List<Integer> revenue = new ArrayList<>();
                List<Date> date = new ArrayList<>();
                int success = 0, failed = 0, pending = 0, totalRevenue = 0;

                for (Object[] row : resultList) {
                        Integer total = ((Number) row[1]).intValue();
                        revenue.add(total);
                        date.add((Date) row[5]);
                        success += ((Number) row[2]).intValue();
                        failed += ((Number) row[3]).intValue();
                        pending += ((Number) row[4]).intValue();
                        totalRevenue += total;
                }

                RevenueResponse revenueResponse = RevenueResponse.builder()
                                .revenue(revenue)
                                .date(date)
                                .status(RevenueStatusReponse.builder()
                                                .numberOfSuccess(success)
                                                .numberOfFailed(failed)
                                                .numberOfPending(pending)
                                                .totalRevenue(totalRevenue)
                                                .build())
                                .build();

                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .data(revenueResponse)
                                .message("Lấy danh sách doanh thu thành công")
                                .build();
        }

        @Override
        public AppResponse getMostPopularCategory() {
                List<Object[]> resultList = categoryMangaRepository.getTopCategoriesByMangaCount();
                List<String> categories = new ArrayList<>();
                List<Integer> mangaCount = new ArrayList<>();
                Integer totalManga = categoryMangaRepository.getNumberOfManga().orElse(0);

                log.warn("Total manga: {}", totalManga);

                for (Object[] row : resultList) {
                        String categoryName = (String) row[0];
                        Integer count = ((Number) row[1]).intValue();
                        categories.add(categoryName);
                        mangaCount.add(count);
                }
                log.warn(mangaCount.toString());

                List<Integer> mangaPercentage = mangaCount.stream()
                                .map(count -> (int) ((double) count / totalManga * 100))
                                .collect(Collectors.toList());

                TopCateriesStatisticResponse topCateriesStatisticResponse = TopCateriesStatisticResponse.builder()
                                .name(categories)
                                .mangaPercentage(mangaPercentage)
                                .build();

                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .data(topCateriesStatisticResponse)
                                .message("Lấy danh sách thể loại manga thành công")
                                .build();
        }

        @Override
        public AppResponse getCount() {
                Integer numberOfManga = categoryMangaRepository.getNumberOfManga().orElse(0);
                Integer numberOfCategory = categoryMangaRepository.getNumberOfCategory().orElse(0);
                Integer numberOfUser = userRepository.getNumberOfUserVerified().orElse(0);

                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .data(StatisticCountResponse.builder()
                                                .numberOfCategory(numberOfCategory)
                                                .numberOfManga(numberOfManga)
                                                .numberOfUser(numberOfUser)
                                                .build())
                                .message("Lấy thống kê thành công")
                                .build();
        }

}
