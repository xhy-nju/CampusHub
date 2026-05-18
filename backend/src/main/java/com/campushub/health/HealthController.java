package com.campushub.health;

import com.campushub.common.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthController {

    private final HealthStatusService healthStatusService;

    public HealthController(HealthStatusService healthStatusService) {
        this.healthStatusService = healthStatusService;
    }

    @GetMapping
    public ApiResponse<HealthStatusResponse> health() {
        return ApiResponse.ok(healthStatusService.check());
    }
}
