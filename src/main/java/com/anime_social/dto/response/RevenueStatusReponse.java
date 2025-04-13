package com.anime_social.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class RevenueStatusReponse {
    Integer numberOfSuccess;
    Integer numberOfFailed;
    Integer numberOfPending;
    Integer totalRevenue;
}
