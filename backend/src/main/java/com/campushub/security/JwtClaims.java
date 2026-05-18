package com.campushub.security;

import java.time.Instant;

public record JwtClaims(
        Long userId,
        String studentNo,
        String role,
        Instant issuedAt,
        Instant expiresAt
) {
}
