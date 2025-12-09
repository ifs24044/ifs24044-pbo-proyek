package org.delcom.app.configs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit Test untuk SecurityConfig
 * Hanya menguji bean dan logic sederhana tanpa memuat Spring Context
 */
class SecurityConfigTest {

    private SecurityConfig securityConfig;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        securityConfig = new SecurityConfig();
        passwordEncoder = securityConfig.passwordEncoder();
    }

    @Test
    void passwordEncoder_shouldBeBCrypt() {
        // Verify bean creation
        assertThat(passwordEncoder).isNotNull();
        assertThat(passwordEncoder).isInstanceOf(BCryptPasswordEncoder.class);
    }

    @Test
    void passwordEncoder_shouldEncodePassword() {
        String rawPassword = "test123";
        String encoded = passwordEncoder.encode(rawPassword);

        // Verify encoding works
        assertThat(encoded).isNotBlank();
        assertThat(encoded).isNotEqualTo(rawPassword);
        assertThat(encoded).startsWith("$2a$"); // BCrypt prefix
    }

    @Test
    void passwordEncoder_shouldMatchPassword() {
        String rawPassword = "mySecretPassword";
        String encoded = passwordEncoder.encode(rawPassword);

        // Verify password matching
        assertThat(passwordEncoder.matches(rawPassword, encoded)).isTrue();
        assertThat(passwordEncoder.matches("wrongPassword", encoded)).isFalse();
    }

    @Test
    void passwordEncoder_shouldGenerateUniqueHashes() {
        String password = "samePassword";
        String hash1 = passwordEncoder.encode(password);
        String hash2 = passwordEncoder.encode(password);

        // BCrypt generates unique salt each time
        assertThat(hash1).isNotEqualTo(hash2);
        
        // But both should match original password
        assertThat(passwordEncoder.matches(password, hash1)).isTrue();
        assertThat(passwordEncoder.matches(password, hash2)).isTrue();
    }
}