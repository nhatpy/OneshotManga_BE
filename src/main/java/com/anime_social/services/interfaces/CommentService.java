package com.anime_social.services.interfaces;

import org.springframework.stereotype.Service;

import com.anime_social.dto.request.PostComment;
import com.anime_social.dto.request.UpdateComment;
import com.anime_social.dto.response.AppResponse;

@Service
public interface CommentService {
    public AppResponse createComment(PostComment request);

    public AppResponse deleteComment(String id);

    public AppResponse updateComment(String id, UpdateComment request);
}
