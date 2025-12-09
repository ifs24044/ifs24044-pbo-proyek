package org.delcom.app.configs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.webmvc.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.ServletWebRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

// ❌ HAPUS @SpringBootTest - tidak diperlukan untuk unit test
class CustomErrorControllerTest {

    private ErrorAttributes errorAttributes;
    private CustomErrorController controller;

    @BeforeEach
    void setUp() {
        errorAttributes = Mockito.mock(ErrorAttributes.class);
        controller = new CustomErrorController(errorAttributes);
    }

    @Test
    @DisplayName("Mengembalikan response error dengan status 500")
    void testHandleErrorReturns500() {
        // Arrange
        Map<String, Object> errorMap = Map.of();

        Mockito.when(
                errorAttributes.getErrorAttributes(
                        any(ServletWebRequest.class),
                        any(ErrorAttributeOptions.class)))
                .thenReturn(errorMap);

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        ServletWebRequest webRequest = new ServletWebRequest(request, response);

        // Act
        ResponseEntity<Map<String, Object>> result = controller.handleError(webRequest);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertEquals(500, result.getStatusCode().value());
        
        Map<String, Object> body = result.getBody();
        assertNotNull(body);
        assertEquals("error", body.get("status"));
        assertEquals("Unknown Error", body.get("error"));
        assertEquals("unknown", body.get("path"));
        assertNotNull(body.get("timestamp"));
    }

    @Test
    @DisplayName("Mengembalikan response error dengan status 404")
    void testHandleErrorReturns404() {
        // Arrange
        Map<String, Object> errorMap = Map.of(
                "status", 404,
                "error", "Not Found",
                "path", "/error404");

        Mockito.when(
                errorAttributes.getErrorAttributes(
                        any(ServletWebRequest.class),
                        any(ErrorAttributeOptions.class)))
                .thenReturn(errorMap);

        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse response = Mockito.mock(HttpServletResponse.class);
        ServletWebRequest webRequest = new ServletWebRequest(request, response);

        // Act
        ResponseEntity<Map<String, Object>> result = controller.handleError(webRequest);

        // Assert
        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals(404, result.getStatusCode().value());
        
        Map<String, Object> body = result.getBody();
        assertNotNull(body);
        assertEquals("fail", body.get("status"));
        assertEquals("Not Found", body.get("error"));
        assertEquals("/error404", body.get("path"));
        assertNotNull(body.get("timestamp"));
    }
}