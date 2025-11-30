package com.comet.opik.examples.springai.config;

import io.micrometer.common.KeyValue;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationFilter;
import org.springframework.ai.chat.observation.ChatModelObservationContext;
import org.springframework.ai.content.Content;
import org.springframework.ai.observation.ObservabilityHelper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * A filter implementation for enriching {@link ChatModelObservationContext}
 * with high-cardinality key-value pairs to enhance observability.
 * <p>
 * This class processes specific context information, including prompts and completions
 * from the application, and maps them into a form suitable for observability systems.
 * It concatenates text from the request instructions (prompts) and response outputs (completions)
 *  to populate high cardinality key-values.
 * <p>
 * Implements the {@link ObservationFilter} interface, which requires mapping
 * an {@link Observation.Context} object.
 * <p>
 * Key Functionality:
 * - Extracts prompts from the context request instructions.
 * - Gathers completions from the context response, including results and their outputs.
 * - Adds processed information as high-cardinality key-value pairs for observability,
 *   using predefined keys for prompts (gen_ai.prompt) and completions (gen_ai.completion).
 * <p>
 * Thread Safety:
 * This class is assumed to be thread-safe as it does not maintain state
 * and works only on input context objects.
 */
@Component
public class ChatModelObservationContextObservationFilter implements ObservationFilter {

    private static final String GEN_AI_PROMPT = "gen_ai.prompt";
    private static final String GEN_AI_COMPLETION = "gen_ai.completion";

    @Override
    @NonNull
    public Observation.Context map(@NonNull Observation.Context context) {
        if (!(context instanceof ChatModelObservationContext chatModelObservationContext)) {
            return context;
        }

        var prompts = processPrompts(chatModelObservationContext);
        var completions = processCompletion(chatModelObservationContext);

        chatModelObservationContext.addHighCardinalityKeyValue(
                KeyValue.of(GEN_AI_PROMPT, ObservabilityHelper.concatenateStrings(prompts)));

        chatModelObservationContext.addHighCardinalityKeyValue(
                KeyValue.of(GEN_AI_COMPLETION, ObservabilityHelper.concatenateStrings(completions)));


        return chatModelObservationContext;
    }

    private List<String> processPrompts(ChatModelObservationContext chatModelObservationContext) {
        if (CollectionUtils.isEmpty(chatModelObservationContext.getRequest().getInstructions())) {
            return List.of();
        }

        return chatModelObservationContext.getRequest().getInstructions()
                .stream().map(Content::getText)
                .toList();
    }

    private List<String> processCompletion(ChatModelObservationContext context) {
        if (Objects.isNull(context.getResponse()) || CollectionUtils.isEmpty((context.getResponse()).getResults())) {
            return List.of();
        }

        String output = context.getResponse().getResult().getOutput().getText();
        if (StringUtils.hasText(output)) {
            return List.of(output);
        }

        return context.getResponse().getResults().stream()
                .filter(generation -> Objects.nonNull(generation.getOutput()) &&
                        StringUtils.hasText(generation.getOutput().getText()))
                .map(generation -> generation.getOutput().getText())
                .toList();
    }
}