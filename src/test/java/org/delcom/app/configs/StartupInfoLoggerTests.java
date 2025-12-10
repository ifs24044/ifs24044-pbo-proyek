package org.delcom.app.configs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class StartupInfoLoggerTests {

    private StartupInfoLogger logger;

    private ConfigurableEnvironment environment;
    private ConfigurableApplicationContext context;
    private ApplicationReadyEvent event;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setup() {
        // Redirect System.out
        System.setOut(new PrintStream(outContent));

        logger = new StartupInfoLogger();

        environment = mock(ConfigurableEnvironment.class);
        context = mock(ConfigurableApplicationContext.class);
        event = mock(ApplicationReadyEvent.class);

        when(event.getApplicationContext()).thenReturn(context);
        when(context.getEnvironment()).thenReturn(environment);
    }

    @AfterEach
    void restoreSystemOut() {
        System.setOut(originalOut);
    }

    // ============================================
    // TEST: Default Configuration
    // ============================================

    @Test
    @DisplayName("Test dengan konfigurasi default (port 8080, contextPath /, localhost)")
    void test_dengan_konfigurasi_default() {
        // Arrange - semua property return default value
        when(environment.getProperty("server.port", "8080")).thenReturn("8080");
        when(environment.getProperty("server.servlet.context-path", "/")).thenReturn("/");
        when(environment.getProperty("spring.devtools.livereload.enabled", Boolean.class, false)).thenReturn(false);
        when(environment.getProperty("spring.devtools.livereload.port", "35729")).thenReturn("35729");
        when(environment.getProperty("server.address", "localhost")).thenReturn("localhost");

        // Act
        logger.onApplicationEvent(event);

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("Application started successfully!"));
        assertTrue(output.contains("> URL: http://localhost:8080"));
        assertTrue(output.contains("> LiveReload: DISABLED"));
    }

    // ============================================
    // TEST: Custom Port
    // ============================================

    @Test
    @DisplayName("Test dengan custom port (9090)")
    void test_dengan_custom_port() {
        // Arrange
        when(environment.getProperty("server.port", "8080")).thenReturn("9090");
        when(environment.getProperty("server.servlet.context-path", "/")).thenReturn("/");
        when(environment.getProperty("spring.devtools.livereload.enabled", Boolean.class, false)).thenReturn(false);
        when(environment.getProperty("spring.devtools.livereload.port", "35729")).thenReturn("35729");
        when(environment.getProperty("server.address", "localhost")).thenReturn("localhost");

        // Act
        logger.onApplicationEvent(event);

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("> URL: http://localhost:9090"));
    }

    // ============================================
    // TEST: Context Path Cases
    // ============================================

    @Test
    @DisplayName("Test dengan contextPath null (harus jadi empty string)")
    void test_dengan_contextPath_null() {
        // Arrange
        when(environment.getProperty("server.port", "8080")).thenReturn("8080");
        when(environment.getProperty("server.servlet.context-path", "/")).thenReturn(null); // NULL
        when(environment.getProperty("spring.devtools.livereload.enabled", Boolean.class, false)).thenReturn(false);
        when(environment.getProperty("spring.devtools.livereload.port", "35729")).thenReturn("35729");
        when(environment.getProperty("server.address", "localhost")).thenReturn("localhost");

        // Act
        logger.onApplicationEvent(event);

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("> URL: http://localhost:8080")); // Tanpa context path
    }

    @Test
    @DisplayName("Test dengan contextPath '/' (harus jadi empty string)")
    void test_dengan_contextPath_slash() {
        // Arrange
        when(environment.getProperty("server.port", "8080")).thenReturn("8080");
        when(environment.getProperty("server.servlet.context-path", "/")).thenReturn("/"); // Slash
        when(environment.getProperty("spring.devtools.livereload.enabled", Boolean.class, false)).thenReturn(false);
        when(environment.getProperty("spring.devtools.livereload.port", "35729")).thenReturn("35729");
        when(environment.getProperty("server.address", "localhost")).thenReturn("localhost");

        // Act
        logger.onApplicationEvent(event);

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("> URL: http://localhost:8080")); // Tanpa context path
    }

    @Test
    @DisplayName("Test dengan contextPath custom '/api'")
    void test_dengan_contextPath_custom() {
        // Arrange
        when(environment.getProperty("server.port", "8080")).thenReturn("8080");
        when(environment.getProperty("server.servlet.context-path", "/")).thenReturn("/api");
        when(environment.getProperty("spring.devtools.livereload.enabled", Boolean.class, false)).thenReturn(false);
        when(environment.getProperty("spring.devtools.livereload.port", "35729")).thenReturn("35729");
        when(environment.getProperty("server.address", "localhost")).thenReturn("localhost");

        // Act
        logger.onApplicationEvent(event);

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("> URL: http://localhost:8080/api"));
    }

    @Test
    @DisplayName("Test dengan contextPath '/app/home'")
    void test_dengan_contextPath_app_home() {
        // Arrange
        when(environment.getProperty("server.port", "8080")).thenReturn("8080");
        when(environment.getProperty("server.servlet.context-path", "/")).thenReturn("/app/home");
        when(environment.getProperty("spring.devtools.livereload.enabled", Boolean.class, false)).thenReturn(false);
        when(environment.getProperty("spring.devtools.livereload.port", "35729")).thenReturn("35729");
        when(environment.getProperty("server.address", "localhost")).thenReturn("localhost");

        // Act
        logger.onApplicationEvent(event);

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("> URL: http://localhost:8080/app/home"));
    }

    // ============================================
    // TEST: LiveReload Enabled
    // ============================================

    @Test
    @DisplayName("Test dengan LiveReload ENABLED (default port 35729)")
    void test_dengan_liveReload_enabled_default_port() {
        // Arrange
        when(environment.getProperty("server.port", "8080")).thenReturn("8080");
        when(environment.getProperty("server.servlet.context-path", "/")).thenReturn("/");
        when(environment.getProperty("spring.devtools.livereload.enabled", Boolean.class, false)).thenReturn(true);
        when(environment.getProperty("spring.devtools.livereload.port", "35729")).thenReturn("35729");
        when(environment.getProperty("server.address", "localhost")).thenReturn("localhost");

        // Act
        logger.onApplicationEvent(event);

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("> LiveReload: ENABLED (port 35729)"));
    }

    @Test
    @DisplayName("Test dengan LiveReload ENABLED (custom port 40000)")
    void test_dengan_liveReload_enabled_custom_port() {
        // Arrange
        when(environment.getProperty("server.port", "8080")).thenReturn("8080");
        when(environment.getProperty("server.servlet.context-path", "/")).thenReturn("/");
        when(environment.getProperty("spring.devtools.livereload.enabled", Boolean.class, false)).thenReturn(true);
        when(environment.getProperty("spring.devtools.livereload.port", "35729")).thenReturn("40000");
        when(environment.getProperty("server.address", "localhost")).thenReturn("localhost");

        // Act
        logger.onApplicationEvent(event);

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("> LiveReload: ENABLED (port 40000)"));
    }

    @Test
    @DisplayName("Test dengan LiveReload DISABLED")
    void test_dengan_liveReload_disabled() {
        // Arrange
        when(environment.getProperty("server.port", "8080")).thenReturn("8080");
        when(environment.getProperty("server.servlet.context-path", "/")).thenReturn("/");
        when(environment.getProperty("spring.devtools.livereload.enabled", Boolean.class, false)).thenReturn(false);
        when(environment.getProperty("spring.devtools.livereload.port", "35729")).thenReturn("35729");
        when(environment.getProperty("server.address", "localhost")).thenReturn("localhost");

        // Act
        logger.onApplicationEvent(event);

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("> LiveReload: DISABLED"));
    }

    // ============================================
    // TEST: Custom Host
    // ============================================

    @Test
    @DisplayName("Test dengan custom host (0.0.0.0)")
    void test_dengan_custom_host() {
        // Arrange
        when(environment.getProperty("server.port", "8080")).thenReturn("8080");
        when(environment.getProperty("server.servlet.context-path", "/")).thenReturn("/");
        when(environment.getProperty("spring.devtools.livereload.enabled", Boolean.class, false)).thenReturn(false);
        when(environment.getProperty("spring.devtools.livereload.port", "35729")).thenReturn("35729");
        when(environment.getProperty("server.address", "localhost")).thenReturn("0.0.0.0");

        // Act
        logger.onApplicationEvent(event);

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("> URL: http://0.0.0.0:8080"));
    }

    // ============================================
    // TEST: Integration - Complex Scenarios
    // ============================================

    @Test
    @DisplayName("Integration test - All custom values")
    void integration_test_all_custom_values() {
        // Arrange
        when(environment.getProperty("server.port", "8080")).thenReturn("9090");
        when(environment.getProperty("server.servlet.context-path", "/")).thenReturn("/myapp");
        when(environment.getProperty("spring.devtools.livereload.enabled", Boolean.class, false)).thenReturn(true);
        when(environment.getProperty("spring.devtools.livereload.port", "35729")).thenReturn("40000");
        when(environment.getProperty("server.address", "localhost")).thenReturn("192.168.1.100");

        // Act
        logger.onApplicationEvent(event);

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("Application started successfully!"));
        assertTrue(output.contains("> URL: http://192.168.1.100:9090/myapp"));
        assertTrue(output.contains("> LiveReload: ENABLED (port 40000)"));
    }

    @Test
    @DisplayName("Integration test - Production-like config (no contextPath, no LiveReload)")
    void integration_test_production_config() {
        // Arrange
        when(environment.getProperty("server.port", "8080")).thenReturn("80");
        when(environment.getProperty("server.servlet.context-path", "/")).thenReturn("/");
        when(environment.getProperty("spring.devtools.livereload.enabled", Boolean.class, false)).thenReturn(false);
        when(environment.getProperty("spring.devtools.livereload.port", "35729")).thenReturn("35729");
        when(environment.getProperty("server.address", "localhost")).thenReturn("myapp.com");

        // Act
        logger.onApplicationEvent(event);

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("Application started successfully!"));
        assertTrue(output.contains("> URL: http://myapp.com:80"));
        assertTrue(output.contains("> LiveReload: DISABLED"));
    }

    // ============================================
    // TEST: Verify Mock Interactions
    // ============================================

    @Test
    @DisplayName("Verify semua environment properties dipanggil dengan benar")
    void verify_semua_environment_properties_dipanggil() {
        // Arrange
        when(environment.getProperty("server.port", "8080")).thenReturn("8080");
        when(environment.getProperty("server.servlet.context-path", "/")).thenReturn("/");
        when(environment.getProperty("spring.devtools.livereload.enabled", Boolean.class, false)).thenReturn(false);
        when(environment.getProperty("spring.devtools.livereload.port", "35729")).thenReturn("35729");
        when(environment.getProperty("server.address", "localhost")).thenReturn("localhost");

        // Act
        logger.onApplicationEvent(event);

        // Assert - Verify all properties were called
        verify(environment).getProperty("server.port", "8080");
        verify(environment).getProperty("server.servlet.context-path", "/");
        verify(environment).getProperty("spring.devtools.livereload.enabled", Boolean.class, false);
        verify(environment).getProperty("server.address", "localhost");
        verify(event).getApplicationContext();
        verify(context).getEnvironment();
    }

    // ============================================
    // TEST: ANSI Color Output
    // ============================================

    @Test
    @DisplayName("Verify output mengandung ANSI color codes")
    void verify_output_mengandung_ansi_colors() {
        // Arrange
        when(environment.getProperty("server.port", "8080")).thenReturn("8080");
        when(environment.getProperty("server.servlet.context-path", "/")).thenReturn("/");
        when(environment.getProperty("spring.devtools.livereload.enabled", Boolean.class, false)).thenReturn(true);
        when(environment.getProperty("spring.devtools.livereload.port", "35729")).thenReturn("35729");
        when(environment.getProperty("server.address", "localhost")).thenReturn("localhost");

        // Act
        logger.onApplicationEvent(event);

        // Assert
        String output = outContent.toString();
        
        // Check ANSI codes exist (colors)
        assertTrue(output.contains("\u001B[32m")); // GREEN
        assertTrue(output.contains("\u001B[36m")); // CYAN
        assertTrue(output.contains("\u001B[33m")); // YELLOW
        assertTrue(output.contains("\u001B[0m"));  // RESET
    }
}