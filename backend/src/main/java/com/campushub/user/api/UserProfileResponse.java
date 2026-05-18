package com.campushub.user.api;

public record UserProfileResponse(
        String nickname,
        String avatarUrl,
        String college,
        String contact,
        String bio
) {
}
