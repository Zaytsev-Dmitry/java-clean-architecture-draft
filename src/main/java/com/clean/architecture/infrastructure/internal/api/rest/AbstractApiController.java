package com.clean.architecture.infrastructure.internal.api.rest;

import com.clean.architecture.shared.decorator.AbstractLoggableApiDecorator;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Optional;

/**
 * Абстрактный базовый класс для всех API контроллеров.
 * Предоставляет общую функциональность и решает проблему с методом getRequest().
 */
public abstract class AbstractApiController extends AbstractLoggableApiDecorator {
    
    /**
     * Предоставляет доступ к NativeWebRequest.
     * По умолчанию возвращает пустой Optional, так как в большинстве случаев
     * этот метод не используется в реальных контроллерах.
     * 
     * @return Optional<NativeWebRequest> - пустой Optional по умолчанию
     */
    public Optional<NativeWebRequest> getRequest() {
        return Optional.empty();
    }
} 