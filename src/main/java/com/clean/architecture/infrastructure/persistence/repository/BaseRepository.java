package com.clean.architecture.infrastructure.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

/**
 * Базовый интерфейс репозитория с универсальными методами для работы с сущностями
 * и получения метаданных (количество записей, пагинация)
 */
@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {

    /**
     * Получить все записи с общим количеством
     * @return DTO с записями и количеством
     */
    default List<T> findAllWithCount() {
        return findAll();
    }

    /**
     * Получить количество всех записей
     * @return количество записей
     */
    @Query("select count(e) from #{#entityName} e")
    Long countAll();

    /**
     * Получить записи с пагинацией и метаданными
     * @param pageable параметры пагинации
     * @return страница с записями и метаданными
     */
    default Page<T> findAllWithMetadata(Pageable pageable) {
        return findAll(pageable);
    }
} 