package com.campushub.user.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank
        @Size(max = 64)
        String studentNo,

        @NotBlank
        @Size(max = 72)
        String password
) {
}
