package com.anime_social.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.management.RuntimeErrorException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import com.anime_social.dto.request.PostManga;
import com.anime_social.dto.request.UpdateManga;
import com.anime_social.dto.response.AppResponse;
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
        User author = userRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new RuntimeException("User not found"));
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

        return AppResponse.builder()
                .data(mangaSaved)
                .status(HttpStatus.CREATED)
                .message("create manga successfully")
                .build();
    }

    public AppResponse updateManga(String id, UpdateManga request) {
        Manga manga = mangaRepository.findById(id).orElseThrow(() -> new RuntimeException("Manga not found"));

        request.getName().ifPresent(manga::setName);
        request.getSlug().ifPresent(manga::setSlug);
        request.getIsDone().ifPresent(manga::setIsDone);
        request.getCoverImg().ifPresent(manga::setCoverImage);
        request.getDescription().ifPresent(manga::setDescription);

        return AppResponse.builder()
                .data(mangaRepository.save(manga))
                .status(HttpStatus.OK)
                .message("update manga successfully")
                .build();
    }

    public AppResponse deleteManga(String id) {
        Optional<Manga> manga = mangaRepository.findById(id);

        manga.ifPresent(m -> mangaRepository.delete(m));

        return AppResponse.builder()
                .message("delete manga successfully")
                .status(HttpStatus.OK)
                .build();
    }

    public AppResponse getMangaBySlug(String slug) {
        Manga manga = mangaRepository.findBySlug(slug);

        if (manga == null) {
            return AppResponse.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Manga not found")
                    .build();
        } else {
            return AppResponse.builder()
                    .status(HttpStatus.OK)
                    .data(manga)
                    .build();
        }
    }

    public AppResponse bulkActiveManga(List<String> mangaIds) {
        List<Manga> mangas = mangaRepository.findAllById(mangaIds);
        mangas.forEach(manga -> manga.setIsActive(true));

        return AppResponse.builder()
                .status(HttpStatus.OK)
                .message("active all Manga selected successfully")
                .build();
    }
}
