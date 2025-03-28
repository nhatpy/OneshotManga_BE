package com.anime_social.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import com.anime_social.models.User;
import com.anime_social.repositorys.CategoryMangaRepository;
import com.anime_social.repositorys.CategoryRepository;
import com.anime_social.repositorys.MangaRepository;
import com.anime_social.repositorys.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MangaService {
        private final MangaRepository mangaRepository;
        private final CategoryMangaRepository categoryMangaRepository;
        private final UserRepository userRepository;
        private final CategoryRepository categoryRepository;

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

        public AppResponse updateManga(String slug, UpdateManga request) {
                Manga manga = mangaRepository.findBySlug(slug)
                                .orElseThrow(() -> new CusRunTimeException(ErrorCode.MANGA_NOT_FOUND));

                request.getName().ifPresent(manga::setName);
                request.getSlug().ifPresent(manga::setSlug);
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

        public AppResponse deleteManga(String slug) {
                Manga manga = mangaRepository.findBySlug(slug)
                                .orElseThrow(() -> new CusRunTimeException(ErrorCode.MANGA_NOT_FOUND));

                mangaRepository.delete(manga);

                return AppResponse.builder()
                                .message("Xóa manga thành công")
                                .status(HttpStatus.OK)
                                .build();
        }

        public AppResponse getMangaBySlug(String slug) {
                Manga manga = mangaRepository.findBySlug(slug)
                                .orElseThrow(() -> new CusRunTimeException(ErrorCode.MANGA_NOT_FOUND));

                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .data(MangaResponse.toMangaResponse(manga))
                                .build();
        }

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

        public AppResponse getMangaPaging(int page, int size, int type) {
                int staterPage = page - 1;
                Pageable request = PageRequest.of(staterPage, size);

                List<Manga> mangas = (type == 1)
                                ? mangaRepository.findAllPagingWithActive(request)
                                : mangaRepository.findAll(request).toList();
                List<MangaResponse> mangaResponses = mangas.stream()
                                .map(MangaResponse::toMangaResponse)
                                .toList();

                Integer total = (type == 1)
                                ? mangaRepository.getNumberOfAllActive().orElse(0)
                                : mangaRepository.getNumberOfAll().orElse(0);

                return AppResponse.builder()
                                .status(HttpStatus.OK)
                                .totalItem(total)
                                .data(mangaResponses)
                                .message(type == 1
                                                ? "Lấy danh sách manga đã kích hoạt thành công"
                                                : "Lấy danh sách tất cả manga thành công")
                                .build();
        }
}
