package com.campushub.security;

import com.campushub.user.domain.UserAccount;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtServiceTest {

    private static final Clock CLOCK = Clock.fixed(Instant.parse("2026-05-18T00:00:00Z"), ZoneOffset.UTC);

    @Test
    void generateAndParseToken() {
        JwtService jwtService = new JwtService(properties(3600), new ObjectMapper(), CLOCK);
        UserAccount user = new UserAccount();
        user.setId(1L);
        user.setStudentNo("student001");
        user.setRole("user");

        String token = jwtService.generateToken(user);

        JwtClaims claims = jwtService.parse(token);
        assertThat(claims.userId()).isEqualTo(1L);
        assertThat(claims.studentNo()).isEqualTo("student001");
        assertThat(claims.role()).isEqualTo("user");
        assertThat(claims.issuedAt()).isEqualTo(Instant.parse("2026-05-18T00:00:00Z"));
        assertThat(claims.expiresAt()).isEqualTo(Instant.parse("2026-05-18T01:00:00Z"));
    }

    @Test
    void rejectExpiredToken() {
        JwtService issuingService = new JwtService(properties(1), new ObjectMapper(), CLOCK);
        UserAccount user = new UserAccount();
        user.setId(1L);
        user.setStudentNo("student001");
        user.setRole("user");
        String token = issuingService.generateToken(user);

        JwtService parsingService = new JwtService(
                properties(1),
                new ObjectMapper(),
                Clock.fixed(Instant.parse("2026-05-18T00:00:02Z"), ZoneOffset.UTC)
        );

        assertThatThrownBy(() -> parsingService.parse(token))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining("expired");
    }

    private JwtProperties properties(long expiresInSeconds) {
        JwtProperties properties = new JwtProperties();
        properties.setSecret("test-secret-value-with-at-least-32-bytes");
        properties.setExpiresInSeconds(expiresInSeconds);
        return properties;
    }
}
