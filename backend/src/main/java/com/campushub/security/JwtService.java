package com.campushub.security;

import com.campushub.user.domain.UserAccount;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Clock;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class JwtService {

    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {
    };

    private final JwtProperties properties;
    private final ObjectMapper objectMapper;
    private final Clock clock;
    private final byte[] secretBytes;

    @Autowired
    public JwtService(JwtProperties properties, ObjectMapper objectMapper) {
        this(properties, objectMapper, Clock.systemUTC());
    }

    JwtService(JwtProperties properties, ObjectMapper objectMapper, Clock clock) {
        this.properties = properties;
        this.objectMapper = objectMapper;
        this.clock = clock;
        this.secretBytes = properties.getSecret().getBytes(StandardCharsets.UTF_8);
        if (secretBytes.length < 32) {
            throw new IllegalStateException("JWT secret must be at least 32 bytes");
        }
        if (properties.getExpiresInSeconds() <= 0) {
            throw new IllegalStateException("JWT expiration must be greater than 0");
        }
    }

    public String generateToken(UserAccount user) {
        Instant issuedAt = Instant.now(clock);
        Instant expiresAt = issuedAt.plusSeconds(properties.getExpiresInSeconds());

        Map<String, Object> header = new LinkedHashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("sub", user.getId().toString());
        payload.put("studentNo", user.getStudentNo());
        payload.put("role", user.getRole());
        payload.put("iat", issuedAt.getEpochSecond());
        payload.put("exp", expiresAt.getEpochSecond());

        String signingInput = encodeJson(header) + "." + encodeJson(payload);
        return signingInput + "." + base64UrlEncode(sign(signingInput));
    }

    public JwtClaims parse(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new InvalidTokenException("Invalid token format");
        }

        String signingInput = parts[0] + "." + parts[1];
        byte[] expectedSignature = sign(signingInput);
        byte[] actualSignature = base64UrlDecode(parts[2]);
        if (!MessageDigest.isEqual(expectedSignature, actualSignature)) {
            throw new InvalidTokenException("Invalid token signature");
        }

        Map<String, Object> header = decodeJson(parts[0]);
        if (!"HS256".equals(header.get("alg"))) {
            throw new InvalidTokenException("Unsupported token algorithm");
        }

        Map<String, Object> payload = decodeJson(parts[1]);
        long expiresAtEpoch = numberValue(payload.get("exp"), "exp");
        Instant expiresAt = Instant.ofEpochSecond(expiresAtEpoch);
        if (!expiresAt.isAfter(Instant.now(clock))) {
            throw new InvalidTokenException("Token expired");
        }

        return new JwtClaims(
                Long.valueOf(stringValue(payload.get("sub"), "sub")),
                stringValue(payload.get("studentNo"), "studentNo"),
                stringValue(payload.get("role"), "role"),
                Instant.ofEpochSecond(numberValue(payload.get("iat"), "iat")),
                expiresAt
        );
    }

    public long getExpiresInSeconds() {
        return properties.getExpiresInSeconds();
    }

    private String encodeJson(Map<String, Object> value) {
        try {
            return base64UrlEncode(objectMapper.writeValueAsBytes(value));
        } catch (JsonProcessingException exception) {
            throw new InvalidTokenException("Could not encode token", exception);
        }
    }

    private Map<String, Object> decodeJson(String value) {
        try {
            return objectMapper.readValue(base64UrlDecode(value), MAP_TYPE);
        } catch (Exception exception) {
            throw new InvalidTokenException("Could not decode token", exception);
        }
    }

    private byte[] sign(String value) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(secretBytes, HMAC_ALGORITHM));
            return mac.doFinal(value.getBytes(StandardCharsets.UTF_8));
        } catch (Exception exception) {
            throw new InvalidTokenException("Could not sign token", exception);
        }
    }

    private String base64UrlEncode(byte[] value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value);
    }

    private byte[] base64UrlDecode(String value) {
        try {
            return Base64.getUrlDecoder().decode(value);
        } catch (IllegalArgumentException exception) {
            throw new InvalidTokenException("Invalid token encoding", exception);
        }
    }

    private String stringValue(Object value, String claimName) {
        if (value instanceof String text && !text.isBlank()) {
            return text;
        }
        throw new InvalidTokenException("Missing token claim: " + claimName);
    }

    private long numberValue(Object value, String claimName) {
        if (value instanceof Number number) {
            return number.longValue();
        }
        throw new InvalidTokenException("Missing token claim: " + claimName);
    }
}
