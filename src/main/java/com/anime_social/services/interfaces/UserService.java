package com.anime_social.services.interfaces;

import org.springframework.stereotype.Service;

import com.anime_social.dto.request.UpdateUser;
import com.anime_social.dto.response.AppResponse;

@Service
public interface UserService {
    public AppResponse getUserById(String id);

    public AppResponse updateUser(String id, UpdateUser userRequest);

    public AppResponse deleteUser(String id);

    public AppResponse currentUser();

    public AppResponse warningUser(String id);

    public AppResponse getUsersPaging(int page, int size);

    public AppResponse getTopUsers();
}
