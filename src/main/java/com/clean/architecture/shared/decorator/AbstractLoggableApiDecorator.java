package com.clean.architecture.shared.decorator;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.*;
import java.util.stream.Collectors;

@Slf4j
public class AbstractLoggableApiDecorator {
    private final ObjectMapper objectMapper;
    private static final String TRACE_ID_KEY = "X-Trace-Id";

    public AbstractLoggableApiDecorator() {
        this.objectMapper = new ObjectMapper();
    }

    private String generateTraceId() {
        return UUID.randomUUID().toString();
    }

    private void setTraceId(String traceId) {
        MDC.put(TRACE_ID_KEY, traceId);
    }

    private void clearTraceId() {
        MDC.remove(TRACE_ID_KEY);
    }

    @SneakyThrows
    protected <T> void processingVoid(
            final String nameMethod,
            final Consumer<T> operation,
            final T request
    ) {
        final String traceId = generateTraceId();
        setTraceId(traceId);
        final long startTime = System.nanoTime();

        try {
            operation.accept(request);
            log.info("processing {}({}) UID {} TIME {} ms - VOID operation",
                    nameMethod,
                    this.objectMapper.writeValueAsString(request),
                    MDC.get("X-Trace-Id"),
                    (System.nanoTime() - startTime) / 1_000_000D
            );
        } catch (Exception exception) {
            log.error("processing {}({}) UID {} TIME {} ms - VOID operation failed with error {}",
                    nameMethod,
                    this.objectMapper.writeValueAsString(request),
                    MDC.get("X-Trace-Id"),
                    (System.nanoTime() - startTime) / 1_000_000D,
                    exception.getMessage()
            );
            throw exception;
        } finally {
            clearTraceId();
        }
    }

    @SafeVarargs
    protected final <R> R processing(
            final String nameMethod,
            final R result,
            final Object... params
    ) {
        return processing(nameMethod, result, Arrays.asList(params));
    }

    @SneakyThrows
    private <R> R processing(
            final String nameMethod,
            final R result,
            final List<Object> params
    ) {
        final String traceId = generateTraceId();
        setTraceId(traceId);
        final long startTime = System.nanoTime();

        try {
            // Формируем строку параметров для логирования
            String paramsString = params.stream()
                    .map(param -> {
                        try {
                            return this.objectMapper.writeValueAsString(param);
                        } catch (Exception e) {
                            return String.valueOf(param);
                        }
                    })
                    .collect(Collectors.joining(", "));

            log.info("processing UseCase {}({}) UID {} TIME {} ms with result {}",
                    nameMethod,
                    paramsString,
                    MDC.get("X-Trace-Id"),
                    (System.nanoTime() - startTime) / 1_000_000D,
                    this.objectMapper.writeValueAsString(result)
            );
            return result;
        } catch (Exception exception) {
            // Формируем строку параметров для ошибки
            String paramsString = params.stream()
                    .map(param -> {
                        try {
                            return this.objectMapper.writeValueAsString(param);
                        } catch (Exception e) {
                            return String.valueOf(param);
                        }
                    })
                    .collect(Collectors.joining(", "));

            log.error("processing {}({}) UID {} with error {}",
                    nameMethod,
                    paramsString,
                    MDC.get("X-Trace-Id"),
                    exception.getMessage()
            );

            throw exception;
        } finally {
            clearTraceId();
        }
    }
}
