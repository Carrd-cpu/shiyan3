package com.shiyan3.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public final class JsonUtil {
    public static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private JsonUtil() {
    }

    public static Map<String, Object> readJsonBody(String body) {
        if (body == null || body.isBlank()) {
            return Collections.emptyMap();
        }
        try {
            return MAPPER.readValue(body, new TypeReference<>() {
            });
        } catch (IOException e) {
            return Collections.emptyMap();
        }
    }

    public static void writeJson(HttpServletResponse response, ApiResponse<?> result) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        MAPPER.writeValue(response.getWriter(), result);
    }
}
