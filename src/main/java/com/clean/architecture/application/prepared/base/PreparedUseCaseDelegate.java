package com.clean.architecture.application.prepared.base;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class PreparedUseCaseDelegate {

    private final Map<String, BusinessBasePrepareData<?>> prepareDataMap;

    @Autowired
    private Map<String, BusinessBasePrepareData<?>> prepareDataBeans;

    protected PreparedUseCaseDelegate() {
        this.prepareDataMap = new HashMap<>();
    }

    @PostConstruct
    private void autoRegisterPrepareData() {
        for (BusinessBasePrepareData<?> prepareData : prepareDataBeans.values()) {
            prepareDataMap.put(prepareData.getName(), prepareData);
        }
    }

    protected <T> T prepare(T dto) {
        @SuppressWarnings("unchecked")
        BusinessBasePrepareData<T> prepareData = (BusinessBasePrepareData<T>) prepareDataMap.get(dto.getClass().getSimpleName());

        if (prepareData == null) {
            throw new IllegalStateException("PrepareData is not registered for " + dto.getClass().getSimpleName());
        }

        return prepareData.prepare(dto);
    }

    protected <T, R> R executeWithPreparation(T dto, Function<T, R> action) {
        return action.apply(prepare(dto));
    }

    protected <T> void executeWithPreparation(T dto, Consumer<T> action) {
        action.accept(prepare(dto));
    }
}
