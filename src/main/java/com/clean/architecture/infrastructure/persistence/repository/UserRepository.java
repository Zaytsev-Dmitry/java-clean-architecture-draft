package com.clean.architecture.infrastructure.persistence.repository;

import com.clean.architecture.domain.entity.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends BaseRepository<User, Long> {
    @Query("""
            select u from User u where u.id >= ?1 order by u.id asc limit ?2
            """)
    List<User> getAllByCursor(final Long cursor, final Integer pageSize);

    @Query("""
            select count (u) from User u
            """)
    long count();
}
