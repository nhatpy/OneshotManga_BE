package com.anime_social.services;

import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.anime_social.dto.request.CreateCategory;
import com.anime_social.dto.request.UpdateCategory;
import com.anime_social.dto.response.AppResponse;
import com.anime_social.exception.CusRunTimeException;
import com.anime_social.exception.ErrorCode;
import com.anime_social.models.Category;
import com.anime_social.repositories.CategoryRepository;
import com.anime_social.services.interfaces.CategoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    @Cacheable(value = "CATEGORY_CACHE", key = "#root.methodName")
    public AppResponse getCategories() {
        List<Category> categories = categoryRepository.findAll();
        return AppResponse.builder()
                .status(HttpStatus.OK)
                .message("Lấy danh sách thể loại thành công")
                .data(categories)
                .build();
    }

    @Override
    @CacheEvict(value = "CATEGORY_CACHE", key = "'getCategories'")
    public AppResponse createCategory(CreateCategory request) {
        Optional<Category> category = categoryRepository.findByName(request.getName());
        if (category.isPresent()) {
            throw new CusRunTimeException(ErrorCode.CATEGORY_ALREADY_EXISTS);
        }

        log.info("Create category with request: {}", request);
        Category newCategory = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        categoryRepository.save(newCategory);
        return AppResponse.builder()
                .status(HttpStatus.CREATED)
                .message("Tạo thể loại thành công")
                .data(newCategory)
                .build();
    }

    @Override
    @CacheEvict(value = "CATEGORY_CACHE", key = "'getCategories'")
    public AppResponse updateCategory(String id, UpdateCategory request) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            throw new CusRunTimeException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        request.getName().ifPresent(value -> category.setName(value));
        request.getName().ifPresent(value -> category.setDescription(value));

        categoryRepository.save(category);

        return AppResponse.builder()
                .status(HttpStatus.OK)
                .message("Cập nhật thể loại thành công")
                .data(category)
                .build();
    }

    @Override
    @CacheEvict(value = "CATEGORY_CACHE", key = "'getCategories'")
    public AppResponse deleteCategory(String id) {
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            throw new CusRunTimeException(ErrorCode.CATEGORY_NOT_FOUND);
        }

        categoryRepository.delete(category);

        return AppResponse.builder()
                .status(HttpStatus.OK)
                .message("Xóa thể loại thành công")
                .data(category)
                .build();
    }

    @Override
    public AppResponse getPaging(int page, int size) {
        int starterPage = page - 1;
        PageRequest pageRequest = PageRequest.of(starterPage, size);
        List<Category> categories = categoryRepository.findAll(pageRequest).toList();

        Integer total = categoryRepository.getNumberOfAll();

        return AppResponse.builder()
                .data(categories)
                .totalItem(total)
                .message("Lấy danh sách thể loại phân trang thành công")
                .status(HttpStatus.OK)
                .build();
    }
}
