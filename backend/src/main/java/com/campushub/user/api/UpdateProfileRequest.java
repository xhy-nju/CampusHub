package com.campushub.user.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
        @NotBlank
        @Size(max = 64)
        String nickname,

        @Size(max = 1024)
        String avatarUrl,

        @Size(max = 128)
        String college,

        @Size(max = 128)
        String contact,

        @Size(max = 500)
        String bio
) {
}
