package com.comet.opik.examples.springai.dto;

import java.util.List;
import java.util.Map;

/**
 * Data Transfer Object for chat requests containing question, tags, and metadata.
 */
public record ChatRequest(
        String question,
        List<String> tags,
        Map<String, String> metadata
) {
}