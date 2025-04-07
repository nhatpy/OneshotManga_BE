package com.anime_social.services.interfaces;

import org.springframework.stereotype.Service;

import com.anime_social.dto.request.CreateCategory;
import com.anime_social.dto.request.UpdateCategory;
import com.anime_social.dto.response.AppResponse;

@Service
public interface CategoryService {
    public AppResponse getCategories();

    public AppResponse createCategory(CreateCategory request);

    public AppResponse updateCategory(String id, UpdateCategory request);

    public AppResponse deleteCategory(String id);

    public AppResponse getPaging(int page, int size);
}
