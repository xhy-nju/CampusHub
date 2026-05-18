package com.campushub.health;

import java.sql.Connection;
import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;

@Service
public class HealthStatusService {

    private final DataSource dataSource;
    private final RedisConnectionFactory redisConnectionFactory;

    public HealthStatusService(DataSource dataSource, RedisConnectionFactory redisConnectionFactory) {
        this.dataSource = dataSource;
        this.redisConnectionFactory = redisConnectionFactory;
    }

    public HealthStatusResponse check() {
        Map<String, HealthStatusResponse.ComponentStatus> components = new LinkedHashMap<>();
        components.put("database", checkDatabase());
        components.put("redis", checkRedis());

        boolean allUp = components.values().stream()
                .allMatch(component -> "UP".equals(component.status()));

        return new HealthStatusResponse(allUp ? "UP" : "DEGRADED", OffsetDateTime.now(), components);
    }

    private HealthStatusResponse.ComponentStatus checkDatabase() {
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(2)
                    ? up("PostgreSQL connection is available")
                    : down("PostgreSQL connection is invalid");
        } catch (Exception ex) {
            return down(ex.getMessage());
        }
    }

    private HealthStatusResponse.ComponentStatus checkRedis() {
        try (RedisConnection connection = redisConnectionFactory.getConnection()) {
            String response = connection.ping();
            return "PONG".equalsIgnoreCase(response)
                    ? up("Redis connection is available")
                    : down("Redis ping returned: " + response);
        } catch (Exception ex) {
            return down(ex.getMessage());
        }
    }

    private HealthStatusResponse.ComponentStatus up(String message) {
        return new HealthStatusResponse.ComponentStatus("UP", message);
    }

    private HealthStatusResponse.ComponentStatus down(String message) {
        return new HealthStatusResponse.ComponentStatus("DOWN", message);
    }
}

