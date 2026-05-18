package com.campushub.user.api;

public record UserSummaryResponse(
        Long id,
        String studentNo,
        String role,
        String status,
        Integer creditScore,
        UserProfileResponse profile
) {
}
