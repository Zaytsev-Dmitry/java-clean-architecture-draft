package com.clean.architecture.infrastructure.internal.api.presenter;

import com.generated.swaggerCodegen.model.BasicBackendResponse;
import com.generated.swaggerCodegen.model.MetaData;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.List;

public abstract class BasePresenter<E, I> {
    public <R extends BasicBackendResponse> R mapToListResponse(final List<E> entities, Class<R> responseType) {
        R response = createListResponse(entities.stream().map(this::map).toList(), responseType);
        decorate(response);
        return response;
    }

    public <R extends BasicBackendResponse> R mapToListResponse(
            final Long currentCursor,
            final Long totalCount,
            final Integer pageSize,
            final List<E> entities, Class<R> responseType
    ) {
        R response = createListResponse(entities.stream().map(this::map).toList(), responseType);
        decorateWithPagination(currentCursor, totalCount, pageSize, response);
        return response;
    }

    public <R extends BasicBackendResponse> R mapToSingleResponse(final E entity, Class<R> responseType) {
        R response = createSingleResponse(map(entity), responseType);
        decorate(response);
        return response;
    }

    private void decorate(final BasicBackendResponse response) {
        MetaData metaData = new MetaData();
        metaData.setTimestamp(Instant.now().toString());
        response.setMeta(metaData);
        response.setDescription("Запрос успешно выполнен");
    }

    private void decorateWithPagination(
            final Long currentCursor,
            final Long totalCount,
            final Integer pageSize,
            final BasicBackendResponse response
    ) {
        MetaData metaData = new MetaData();
        metaData.setCurrentCursor(currentCursor);
        metaData.setTotalCount(totalCount);
        metaData.setPageSize(pageSize);
        metaData.setTimestamp(Instant.now().toString());

        response.setMeta(metaData);
        response.setDescription("Запрос успешно выполнен");
    }

    protected abstract I map(E entity);

    // Универсальные методы
    protected <R extends BasicBackendResponse> R createListResponse(List<I> collection, Class<R> responseType) {
        try {
            R response = responseType.getDeclaredConstructor().newInstance();

            // Используем рефлексию для установки payload
            Method setPayloadMethod = responseType.getMethod("setPayload", List.class);
            setPayloadMethod.invoke(response, collection);

            return response;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create response of type: " + responseType.getSimpleName(), e);
        }
    }

    protected <R extends BasicBackendResponse> R createSingleResponse(I single, Class<R> responseType) {
        try {
            R response = responseType.getDeclaredConstructor().newInstance();

            // Используем рефлексию для установки payload
            Method setPayloadMethod = responseType.getMethod("setPayload", single.getClass());
            setPayloadMethod.invoke(response, single);

            return response;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create response of type: " + responseType.getSimpleName(), e);
        }
    }
}

