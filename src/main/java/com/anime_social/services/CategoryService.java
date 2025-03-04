package com.anime_social.services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.anime_social.dto.request.CreateCategory;
import com.anime_social.dto.request.UpdateCategory;
import com.anime_social.dto.response.AppResponse;
import com.anime_social.models.Category;
import com.anime_social.repositorys.CategoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public AppResponse getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return AppResponse.builder()
                .status(HttpStatus.OK)
                .message("Get all categories successfully")
                .data(categories)
                .build();
    }

    public AppResponse createCategory(CreateCategory request) {
        Optional<Category> category = categoryRepository.findByName(request.getName());
        if (category.isPresent()) {
            return AppResponse.builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Category already exists")
                    .build();
        }

        log.info("Create category with request: {}", request);
        Category newCategory = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        categoryRepository.save(newCategory);
        return AppResponse.builder()
                .status(HttpStatus.CREATED)
                .message("Create category successfully")
                .data(newCategory)
                .build();
    }

    public AppResponse updateCategory(String id, UpdateCategory request) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return AppResponse.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Category not found")
                    .build();
        }

        request.getName().ifPresent(value -> category.setName(value));
        request.getName().ifPresent(value -> category.setDescription(value));

        categoryRepository.save(category);

        return AppResponse.builder()
                .status(HttpStatus.OK)
                .message("Update category successfully")
                .data(category)
                .build();
    }

    public AppResponse deleteCategory(String id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            return AppResponse.builder()
                    .status(HttpStatus.NOT_FOUND)
                    .message("Category not found")
                    .build();
        }

        categoryRepository.delete(category);

        return AppResponse.builder()
                .status(HttpStatus.OK)
                .message("Delete category successfully")
                .data(category)
                .build();
    }
}
