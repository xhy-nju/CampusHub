package com.campushub.user.api;

public record AuthResponse(
        String tokenType,
        String accessToken,
        long expiresInSeconds,
        UserSummaryResponse user
) {
}
