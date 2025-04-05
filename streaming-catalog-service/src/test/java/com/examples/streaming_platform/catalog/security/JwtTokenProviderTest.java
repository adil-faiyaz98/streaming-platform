package com.examples.streaming_platform.catalog.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private static final String SECRET = "testSecretKeyThatIsLongEnoughForHS256Algorithm";
    private static final long VALIDITY = 3600000; // 1 hour

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", SECRET);
        ReflectionTestUtils.setField(jwtTokenProvider, "validityInMilliseconds", VALIDITY);
        jwtTokenProvider.init();
    }

    @Test
    void createToken_ShouldGenerateValidToken() {
        // Given
        String username = "testuser";
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_USER"));

        // When
        String token = jwtTokenProvider.createToken(username, authorities);

        // Then
        assertNotNull(token);
        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void getAuthentication_ShouldReturnValidAuthentication() {
        // Given
        String username = "testuser";
        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_USER"));
        String token = jwtTokenProvider.createToken(username, authorities);

        // When
        Authentication authentication = jwtTokenProvider.getAuthentication(token);

        // Then
        assertNotNull(authentication);
        assertEquals(username, authentication.getName());
        assertTrue(authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void validateToken_ShouldReturnFalseForExpiredToken() throws Exception {
        // Given
        String username = "testuser";
        
        // Create a token that is already expired
        String secretKey = Base64.getEncoder().encodeToString(SECRET.getBytes());
        Date now = new Date();
        Date validity = new Date(now.getTime() - 1000); // Expired 1 second ago
        
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(jwtTokenProvider.getKey())
                .compact();

        // When & Then
        assertFalse(jwtTokenProvider.validateToken(token));
    }

    @Test
    void validateToken_ShouldReturnFalseForInvalidSignature() {
        // Given
        String invalidToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImF1dGgiOiJST0xFX1VTRVIiLCJpYXQiOjE2MTY3NjY0MDB9.INVALID_SIGNATURE";

        // When & Then
        assertFalse(jwtTokenProvider.validateToken(invalidToken));
    }
}
