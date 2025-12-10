package org.delcom.app.configs;

import org.delcom.app.interceptors.AuthInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.inOrder;

/**
 * Unit Test untuk WebMvcConfig
 * Test untuk konfigurasi AuthInterceptor tanpa memuat Spring Context
 */
class WebMvcConfigTests {

    private WebMvcConfig webMvcConfig;
    private AuthInterceptor mockAuthInterceptor;

    @BeforeEach
    void setup() {
        webMvcConfig = new WebMvcConfig();
        mockAuthInterceptor = mock(AuthInterceptor.class);
        
        // Inject mock AuthInterceptor menggunakan reflection
        try {
            java.lang.reflect.Field field = WebMvcConfig.class.getDeclaredField("authInterceptor");
            field.setAccessible(true);
            field.set(webMvcConfig, mockAuthInterceptor);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mock AuthInterceptor", e);
        }
    }

    // ============================================
    // BASIC CONFIGURATION TESTS
    // ============================================

    @Test
    @DisplayName("WebMvcConfig instance harus dapat dibuat")
    void webMvcConfig_instance_harus_dapat_dibuat() {
        // Assert
        assertThat(webMvcConfig).isNotNull();
    }

    @Test
    @DisplayName("WebMvcConfig implements WebMvcConfigurer")
    void webMvcConfig_implements_webMvcConfigurer() {
        // Assert
        assertThat(webMvcConfig).isInstanceOf(org.springframework.web.servlet.config.annotation.WebMvcConfigurer.class);
    }

    // ============================================
    // ADD INTERCEPTORS METHOD TESTS
    // ============================================

    @Test
    @DisplayName("addInterceptors harus menambahkan AuthInterceptor ke registry")
    void addInterceptors_harus_menambahkan_authInterceptor_ke_registry() {
        // Arrange
        InterceptorRegistry mockRegistry = mock(InterceptorRegistry.class);
        InterceptorRegistration mockRegistration = mock(InterceptorRegistration.class);

        when(mockRegistry.addInterceptor(any(HandlerInterceptor.class))).thenReturn(mockRegistration);
        when(mockRegistration.addPathPatterns(anyString())).thenReturn(mockRegistration);
        when(mockRegistration.excludePathPatterns(anyString())).thenReturn(mockRegistration);

        // Act
        webMvcConfig.addInterceptors(mockRegistry);

        // Assert
        verify(mockRegistry, times(1)).addInterceptor(mockAuthInterceptor);
    }

    @Test
    @DisplayName("addInterceptors harus menambahkan path pattern /api/**")
    void addInterceptors_harus_menambahkan_path_pattern_api() {
        // Arrange
        InterceptorRegistry mockRegistry = mock(InterceptorRegistry.class);
        InterceptorRegistration mockRegistration = mock(InterceptorRegistration.class);

        when(mockRegistry.addInterceptor(any())).thenReturn(mockRegistration);
        when(mockRegistration.addPathPatterns(anyString())).thenReturn(mockRegistration);
        when(mockRegistration.excludePathPatterns(anyString())).thenReturn(mockRegistration);

        // Act
        webMvcConfig.addInterceptors(mockRegistry);

        // Assert
        verify(mockRegistration, times(1)).addPathPatterns("/api/**");
    }

    @Test
    @DisplayName("addInterceptors harus exclude path pattern /api/auth/**")
    void addInterceptors_harus_exclude_path_pattern_api_auth() {
        // Arrange
        InterceptorRegistry mockRegistry = mock(InterceptorRegistry.class);
        InterceptorRegistration mockRegistration = mock(InterceptorRegistration.class);

        when(mockRegistry.addInterceptor(any())).thenReturn(mockRegistration);
        when(mockRegistration.addPathPatterns(anyString())).thenReturn(mockRegistration);
        when(mockRegistration.excludePathPatterns(anyString())).thenReturn(mockRegistration);

        // Act
        webMvcConfig.addInterceptors(mockRegistry);

        // Assert
        verify(mockRegistration, times(1)).excludePathPatterns("/api/auth/**");
    }

    @Test
    @DisplayName("addInterceptors harus exclude path pattern /api/public/**")
    void addInterceptors_harus_exclude_path_pattern_api_public() {
        // Arrange
        InterceptorRegistry mockRegistry = mock(InterceptorRegistry.class);
        InterceptorRegistration mockRegistration = mock(InterceptorRegistration.class);

        when(mockRegistry.addInterceptor(any())).thenReturn(mockRegistration);
        when(mockRegistration.addPathPatterns(anyString())).thenReturn(mockRegistration);
        when(mockRegistration.excludePathPatterns(anyString())).thenReturn(mockRegistration);

        // Act
        webMvcConfig.addInterceptors(mockRegistry);

        // Assert
        verify(mockRegistration, times(1)).excludePathPatterns("/api/public/**");
    }

    @Test
    @DisplayName("addInterceptors harus exclude path patterns sebanyak 2 kali")
    void addInterceptors_harus_exclude_path_patterns_sebanyak_2_kali() {
        // Arrange
        InterceptorRegistry mockRegistry = mock(InterceptorRegistry.class);
        InterceptorRegistration mockRegistration = mock(InterceptorRegistration.class);

        when(mockRegistry.addInterceptor(any())).thenReturn(mockRegistration);
        when(mockRegistration.addPathPatterns(anyString())).thenReturn(mockRegistration);
        when(mockRegistration.excludePathPatterns(anyString())).thenReturn(mockRegistration);

        // Act
        webMvcConfig.addInterceptors(mockRegistry);

        // Assert
        verify(mockRegistration, times(2)).excludePathPatterns(anyString());
    }

    // ============================================
    // METHOD CALL ORDER TESTS
    // ============================================

    @Test
    @DisplayName("addInterceptors harus dipanggil dengan urutan yang benar")
    void addInterceptors_harus_dipanggil_dengan_urutan_yang_benar() {
        // Arrange
        InterceptorRegistry mockRegistry = mock(InterceptorRegistry.class);
        InterceptorRegistration mockRegistration = mock(InterceptorRegistration.class);

        when(mockRegistry.addInterceptor(any())).thenReturn(mockRegistration);
        when(mockRegistration.addPathPatterns(anyString())).thenReturn(mockRegistration);
        when(mockRegistration.excludePathPatterns(anyString())).thenReturn(mockRegistration);

        // Act
        webMvcConfig.addInterceptors(mockRegistry);

        // Assert - Verify call order
        var inOrder = inOrder(mockRegistry, mockRegistration);
        inOrder.verify(mockRegistry).addInterceptor(mockAuthInterceptor);
        inOrder.verify(mockRegistration).addPathPatterns("/api/**");
        inOrder.verify(mockRegistration).excludePathPatterns("/api/auth/**");
        inOrder.verify(mockRegistration).excludePathPatterns("/api/public/**");
    }

    @Test
    @DisplayName("addInterceptors harus memanggil addPathPatterns sebelum excludePathPatterns")
    void addInterceptors_harus_memanggil_addPathPatterns_sebelum_excludePathPatterns() {
        // Arrange
        InterceptorRegistry mockRegistry = mock(InterceptorRegistry.class);
        InterceptorRegistration mockRegistration = mock(InterceptorRegistration.class);

        when(mockRegistry.addInterceptor(any())).thenReturn(mockRegistration);
        when(mockRegistration.addPathPatterns(anyString())).thenReturn(mockRegistration);
        when(mockRegistration.excludePathPatterns(anyString())).thenReturn(mockRegistration);

        // Act
        webMvcConfig.addInterceptors(mockRegistry);

        // Assert
        var inOrder = inOrder(mockRegistration);
        inOrder.verify(mockRegistration).addPathPatterns("/api/**");
        inOrder.verify(mockRegistration).excludePathPatterns("/api/auth/**");
    }

    // ============================================
    // INTEGRATION LOGIC TESTS
    // ============================================

    @Test
    @DisplayName("addInterceptors dengan path patterns yang lengkap")
    void addInterceptors_dengan_path_patterns_yang_lengkap() {
        // Arrange
        InterceptorRegistry mockRegistry = mock(InterceptorRegistry.class);
        InterceptorRegistration mockRegistration = mock(InterceptorRegistration.class);

        when(mockRegistry.addInterceptor(any(HandlerInterceptor.class))).thenReturn(mockRegistration);
        when(mockRegistration.addPathPatterns(anyString())).thenReturn(mockRegistration);
        when(mockRegistration.excludePathPatterns(anyString())).thenReturn(mockRegistration);

        // Act
        webMvcConfig.addInterceptors(mockRegistry);

        // Assert - Verify all calls
        verify(mockRegistry).addInterceptor(mockAuthInterceptor);
        verify(mockRegistration).addPathPatterns("/api/**");
        verify(mockRegistration).excludePathPatterns("/api/auth/**");
        verify(mockRegistration).excludePathPatterns("/api/public/**");
        verify(mockRegistration, times(1)).addPathPatterns(anyString());
        verify(mockRegistration, times(2)).excludePathPatterns(anyString());
    }

    @Test
    @DisplayName("addInterceptors tidak memanggil method lain selain yang dibutuhkan")
    void addInterceptors_tidak_memanggil_method_lain_selain_yang_dibutuhkan() {
        // Arrange
        InterceptorRegistry mockRegistry = mock(InterceptorRegistry.class);
        InterceptorRegistration mockRegistration = mock(InterceptorRegistration.class);

        when(mockRegistry.addInterceptor(any())).thenReturn(mockRegistration);
        when(mockRegistration.addPathPatterns(anyString())).thenReturn(mockRegistration);
        when(mockRegistration.excludePathPatterns(anyString())).thenReturn(mockRegistration);

        // Act
        webMvcConfig.addInterceptors(mockRegistry);

        // Assert - Verify hanya method yang dibutuhkan yang dipanggil
        verify(mockRegistry, times(1)).addInterceptor(any());
        verify(mockRegistration, times(1)).addPathPatterns(anyString());
        verify(mockRegistration, times(2)).excludePathPatterns(anyString());
    }

    // ============================================
    // EDGE CASES & VALIDATION
    // ============================================

    @Test
    @DisplayName("addInterceptors dengan InterceptorRegistry yang valid")
    void addInterceptors_dengan_interceptorRegistry_yang_valid() {
        // Arrange
        InterceptorRegistry mockRegistry = mock(InterceptorRegistry.class);
        InterceptorRegistration mockRegistration = mock(InterceptorRegistration.class);

        when(mockRegistry.addInterceptor(any())).thenReturn(mockRegistration);
        when(mockRegistration.addPathPatterns(anyString())).thenReturn(mockRegistration);
        when(mockRegistration.excludePathPatterns(anyString())).thenReturn(mockRegistration);

        // Act & Assert - Should not throw exception
        try {
            webMvcConfig.addInterceptors(mockRegistry);
            assertThat(true).isTrue(); // Test passed
        } catch (Exception e) {
            assertThat(false).isTrue(); // Test failed
        }
    }

    @Test
    @DisplayName("Verify exact path patterns digunakan")
    void verify_exact_path_patterns_digunakan() {
        // Arrange
        InterceptorRegistry mockRegistry = mock(InterceptorRegistry.class);
        InterceptorRegistration mockRegistration = mock(InterceptorRegistration.class);

        when(mockRegistry.addInterceptor(any())).thenReturn(mockRegistration);
        when(mockRegistration.addPathPatterns(anyString())).thenReturn(mockRegistration);
        when(mockRegistration.excludePathPatterns(anyString())).thenReturn(mockRegistration);

        // Act
        webMvcConfig.addInterceptors(mockRegistry);

        // Assert - Verify exact strings
        verify(mockRegistration).addPathPatterns("/api/**");
        verify(mockRegistration).excludePathPatterns("/api/auth/**");
        verify(mockRegistration).excludePathPatterns("/api/public/**");
    }

    // ============================================
    // COMPREHENSIVE TESTS
    // ============================================

    @Test
    @DisplayName("Comprehensive test - All interceptor configuration")
    void comprehensive_test_all_interceptor_configuration() {
        // Arrange
        InterceptorRegistry mockRegistry = mock(InterceptorRegistry.class);
        InterceptorRegistration mockRegistration = mock(InterceptorRegistration.class);

        when(mockRegistry.addInterceptor(mockAuthInterceptor)).thenReturn(mockRegistration);
        when(mockRegistration.addPathPatterns("/api/**")).thenReturn(mockRegistration);
        when(mockRegistration.excludePathPatterns("/api/auth/**")).thenReturn(mockRegistration);
        when(mockRegistration.excludePathPatterns("/api/public/**")).thenReturn(mockRegistration);

        // Act
        webMvcConfig.addInterceptors(mockRegistry);

        // Assert - Complete verification
        verify(mockRegistry).addInterceptor(mockAuthInterceptor);
        verify(mockRegistration).addPathPatterns("/api/**");
        verify(mockRegistration).excludePathPatterns("/api/auth/**");
        verify(mockRegistration).excludePathPatterns("/api/public/**");
        
        // Verify call counts
        verify(mockRegistry, times(1)).addInterceptor(any());
        verify(mockRegistration, times(1)).addPathPatterns(anyString());
        verify(mockRegistration, times(2)).excludePathPatterns(anyString());
        
        // Verify order
        var inOrder = inOrder(mockRegistry, mockRegistration);
        inOrder.verify(mockRegistry).addInterceptor(mockAuthInterceptor);
        inOrder.verify(mockRegistration).addPathPatterns("/api/**");
        inOrder.verify(mockRegistration).excludePathPatterns("/api/auth/**");
        inOrder.verify(mockRegistration).excludePathPatterns("/api/public/**");
    }
}