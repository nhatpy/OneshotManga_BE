package com.anime_social.services;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.anime_social.dto.request.ChatbotRequest;
import com.anime_social.exception.CusRunTimeException;
import com.anime_social.exception.ErrorCode;
import com.anime_social.models.ChatbotHistory;
import com.anime_social.models.User;
import com.anime_social.repositories.ChatbotRepository;
import com.anime_social.repositories.MangaRepository;
import com.anime_social.repositories.UserRepository;
import com.anime_social.services.interfaces.ChatbotService;
import com.anime_social.services.interfaces.OllamaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatbotServiceImpl implements ChatbotService {
    private final ChatbotRepository chatbotRepository;
    private final OllamaService ollamaService;
    private final MangaRepository mangaRepository;
    private final UserRepository userRepository;

    @Override
    public List<String> getHistory(String userId) {
        Pageable pageable = PageRequest.of(0, 3).withSort(Sort.by(Sort.Direction.DESC, "createAt"));
        return chatbotRepository.findAll(pageable).stream()
                .map(chatbotHistory -> chatbotHistory.getMessage())
                .toList();
    }

    @Override
    public String sendMessage(ChatbotRequest chatbotRequest) {
        ChatbotHistory chatbotHistory = chatbotRepository.findByMessage(chatbotRequest.getMessage());
        if (chatbotHistory != null) {
            return chatbotHistory.getResponse();
        }

        List<String> genres = ollamaService.extractGenres(chatbotRequest.getMessage());
        if (genres.isEmpty()) {
            return "Tôi không hiểu bạn nói gì.";
        }

        Pageable pageable = PageRequest.of(0, 5);
        List<String> mangaNames = mangaRepository.findTop5MangaTitlesByGenres(genres, pageable);
        if (mangaNames.isEmpty()) {
            return "Không tìm thấy manga nào phù hợp với thể loại bạn đã cung cấp.";
        }

        chatbotRequest.getUserId().ifPresent(userId -> {
            if (userRepository.existsById(userId)) {
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new CusRunTimeException(ErrorCode.USER_NOT_FOUND));

                ChatbotHistory userChatbotHistory = ChatbotHistory.builder()
                        .user(user)
                        .message(chatbotRequest.getMessage())
                        .response(String.join(", ", mangaNames))
                        .build();

                chatbotRepository.save(userChatbotHistory);
            }
        });

        return String.join(", ", mangaNames);
    }

}
