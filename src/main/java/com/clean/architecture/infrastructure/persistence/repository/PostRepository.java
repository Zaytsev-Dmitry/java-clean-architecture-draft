package com.clean.architecture.infrastructure.persistence.repository;

import com.clean.architecture.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
