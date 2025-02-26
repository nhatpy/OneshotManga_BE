package com.anime_social.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.anime_social.dto.request.PostCategoryRequest;
import com.anime_social.dto.request.UpdateCategoryRequest;
import com.anime_social.dto.response.AppResponse;
import com.anime_social.services.CategoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/category")
@RestControllerAdvice
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/get-all")
    public AppResponse getCategories() {
        return categoryService.getCategories();
    }

    @PostMapping("/create")
    public AppResponse createCategory(@RequestBody @Valid PostCategoryRequest request) {
        return categoryService.createCategory(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/update/{id}")
    public AppResponse updateCategory(@PathVariable String id, @RequestBody UpdateCategoryRequest request) {
        return categoryService.updateCategory(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public AppResponse deleteCategory(@PathVariable String id) {
        return categoryService.deleteCategory(id);
    }

}
