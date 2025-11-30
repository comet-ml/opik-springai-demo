package com.comet.opik.examples.springai.helpers;

import io.micrometer.tracing.Span;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * Utility class for adding tracing-related data to a Span.
 * Provides methods to set tags and metadata on a given Span object using
 * naming conventions defined in the OPIK backend.
 */
public class OpikTracingUtils {

    private static final String TAGS_KEY = "opik.tags";
    private static final String METADATA_PREFIX = "opik.metadata.";

    /**
     * Associates a list of tags with the given {@code span}.
     * If the provided list of tags is not empty, they will be added to the span
     * under a predefined key, allowing for standardized tracing and analysis.
     *
     * @param span the span to which the tags will be added
     * @param tags the list of tags to associate with the span
     */
    public static void setTags(@NonNull Span span, List<String> tags) {
        if (!CollectionUtils.isEmpty(tags)) {
            span.tagOfStrings(TAGS_KEY, tags);
        }
    }

    /**
     * Sets metadata on the provided {@code span} object.
     * Each entry in the metadata map is added to the span as a tag with
     * a predefined prefix, allowing for enhanced tracing capabilities.
     *
     * @param span the span to which the metadata will be added
     * @param metadata a map of metadata key-value pairs to associate with the span
     */
    public static void setMetadata(@NonNull Span span, Map<String, String> metadata) {
        if ( !CollectionUtils.isEmpty(metadata) ) {
            // populate metadata
            metadata.forEach((String k, String v) ->
                    span.tag(METADATA_PREFIX + k, v));
        }
    }
}
