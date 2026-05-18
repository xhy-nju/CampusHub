package com.campushub.health;

import java.time.OffsetDateTime;
import java.util.Map;

public record HealthStatusResponse(
        String status,
        OffsetDateTime checkedAt,
        Map<String, ComponentStatus> components
) {

    public record ComponentStatus(String status, String message) {
    }
}

