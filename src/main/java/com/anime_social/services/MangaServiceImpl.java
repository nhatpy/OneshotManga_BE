package com.anime_social.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.anime_social.dto.request.BulkActiveRequest;
import com.anime_social.dto.request.PostManga;
import com.anime_social.dto.request.UpdateManga;
import com.anime_social.dto.response.AppResponse;
import com.anime_social.dto.response.MangaResponse;
import com.anime_social.exception.CusRunTimeException;
import com.anime_social.exception.ErrorCode;
import com.anime_social.models.Category;
import com.anime_social.models.CategoryManga;
import com.anime_social.models.Manga;
import com.anime_social.models.MangaInteraction;
import com.anime_social.models.User;
import com.anime_social.repositories.CategoryMangaRepository;
import com.anime_social.repositories.CategoryRepository;
import com.anime_social.repositories.MangaInteractionRepository;
import com.anime_social.repositories.MangaRepository;
import com.anime_social.repositories.UserRepository;
import com.anime_social.services.interfaces.MangaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MangaServiceImpl implements MangaService {
        private final MangaRepository mangaRepository;
        private final CategoryMangaRepository categoryMangaRepository;
        private final UserRepository userRepository;
        private final CategoryRepository categoryRepository;
        private final MangaInteractionRepository mangaInteractionRepository;

        @Override
        public AppResponse createManga(PostManga request) {
                Manga existedManga = mangaRepository.findBySlug(request.getSlug())
                                .orElse(null);
                if (existedManga != null) {
                        throw new CusRunTimeException(ErrorCode.MANGA_ALREADY_EXISTS);
                }
                User author = userRepository.findById(request.getAuthorId())
                                .orElseThrow(() -> new CusRunTimeException(ErrorCode.USER_NOT_FOUND));
                List<Category> categories = categoryRepository.findAllById(request.getCategoryIds());

                Manga manga = new Manga();
                manga.setAuthor(author);
                manga.setName(request.getName());
                manga.setSlug(request.getSlug());
                manga.setCoverImage(request.getCoverImg());
                manga.setDescription(request.getDescription());
                manga.setIsDone(request.getIsDone());

                Manga mangaSaved = mangaRepository.save(manga);

                List<CategoryManga> categoryMangas = categories.stream().map(category -> {
                        CategoryManga mg = new CategoryManga();
                        mg.setManga(mangaSaved);
                        mg.setCategory(category);
                        return mg;
                }).collect(Collectors.toList());

                categoryMangaRepository.saveAll(categoryMangas);
                mangaSaved.setCategoryMangas(categoryMangas);

                return AppResponse.builder()
                                .status(HttpStatus.CREATED)
                                .message("Tạo manga thành công")
                                .data(MangaResponse.toMangaResponse(mangaSaved))
                                .build();
        }

        @Override
        public AppResponse updateManga(String slug, UpdateManga request) {
                Manga manga = mangaRepository.findBySlug(slug)
                                .orElseThrow(() -> new CusRunTimeException(ErrorCode.MANGA_NOT_FOUND));

                request.getIsDone().ifPresent(manga::setIsDone);
                request.getCoverImg().ifPresent(manga::setCoverImage);
                request.getDescription().ifPresent(manga::setDescription);

                Manga savedManga = mangaRepository.save(manga);

                return AppResponse.builder()
                                .data(MangaResponse.toMangaResponse(savedManga))
                                .status(HttpStatus.OK)
                                .message("Cập nhật manga thành công")
                                .build();
        }

        @Override
        public AppResponse deleteManga(String slug) {
                Manga manga = mangaRepository.findBySlug(slug)
                                .orElseThrow(() -> new CusRunTimeException(ErrorCode.MANGA_NOT_FOUND));

                mangaRepository.delete(manga);

                return AppResponse.builder()
                                .message("Xóa manga thành công")
                                .status(HttpStatus.OK)
                                .build();
        }

        @Override
        public AppResponse getMangaBySlug(String slug) {
                Manga manga = mangaRepository.findBySlug(slug)
                                .orElseThrow(() -> new CusRunTimeException(ErrorCode.MANGA_NOT_FOUND));

                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .data(MangaResponse.toMangaResponse(manga))
                                .build();
        }

        @Override
        public AppResponse bulkActiveManga(BulkActiveRequest bulkActiveRequest) {
                List<String> mangaIds = bulkActiveRequest.getMangaIds();

                List<Manga> mangas = mangaRepository.findAllById(mangaIds);

                mangas.forEach(manga -> {
                        manga.setIsActive(true);
                        mangaRepository.save(manga);
                });

                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .message("Đã kích hoạt các manga được chọn")
                                .build();
        }

        @Override
        public AppResponse getMangaPaging(
                        int page,
                        int size,
                        int type,
                        String searchQuery,
                        String sortBy,
                        String categorySlug,
                        Boolean status) {
                Boolean isActive = null;
                int staterPage = page - 1;
                Pageable request = PageRequest.of(staterPage, size);

                if (sortBy != null) {
                        request = PageRequest.of(staterPage, size, Sort.by(sortBy));
                }

                if (type == 1) {
                        isActive = true;
                } else {
                        isActive = false;
                }

                List<Manga> mangas = mangaRepository.findFilterManga(isActive, searchQuery, categorySlug, status,
                                request);
                Integer total = mangaRepository.countFilterManga(isActive, searchQuery, categorySlug, status, request)
                                .orElse(0);

                List<MangaResponse> mangaResponses = mangas.stream()
                                .map(MangaResponse::toMangaResponse)
                                .toList();

                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .totalItem(total)
                                .data(mangaResponses)
                                .message("Lấy danh sách manga thành công")
                                .build();
        }

        @Override
        public AppResponse getByAuthorId(int page, int size, String authorId) {
                int staterPage = page - 1;
                Pageable request = PageRequest.of(staterPage, size);

                List<Manga> mangas = mangaRepository.findAllByAuthorId(authorId, request);
                List<MangaResponse> mangaResponses = mangas.stream()
                                .map(MangaResponse::toMangaResponse)
                                .toList();

                Integer total = mangaRepository.countByAuthorId(authorId).orElse(0);
                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .totalItem(total)
                                .data(mangaResponses)
                                .message("Lấy danh sách manga của tác giả thành công")
                                .build();
        }

        @Override
        public AppResponse getTopDayManga() {
                Pageable request = PageRequest.of(0, 5);

                Date startOfDay = Date.from(LocalDate.now()
                                .atStartOfDay(ZoneId.systemDefault())
                                .toInstant());

                List<MangaInteraction> mangaInteractions = mangaInteractionRepository.findTopDayManga(startOfDay,
                                request);

                List<MangaResponse> mangaResponses = mangaInteractions.stream()
                                .map((mangaInteraction) -> MangaResponse.toMangaResponse(mangaInteraction.getManga()))
                                .toList();

                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .data(mangaResponses)
                                .message("Lấy danh sách Top Manga thành công")
                                .build();
        }

}
