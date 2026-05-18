package com.campushub.security;

public record AuthenticatedUser(
        Long id,
        String studentNo,
        String role
) {
}
