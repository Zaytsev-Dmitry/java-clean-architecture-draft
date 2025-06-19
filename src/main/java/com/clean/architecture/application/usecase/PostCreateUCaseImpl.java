package com.clean.architecture.application.usecase;

import com.clean.architecture.application.port.in.PostCreateUCase;
import com.clean.architecture.domain.entity.Post;
import com.clean.architecture.infrastructure.persistence.repository.PostRepository;
import com.generated.swaggerCodegen.model.PostCreateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service(value = "PostCreateUCase")
@Slf4j
@RequiredArgsConstructor
public class PostCreateUCaseImpl implements PostCreateUCase {
    private final PostRepository repository;

    @Override
    public void postCreate(final PostCreateRequest postCreateRequest) {
        repository.save(
                Post.builder()
                        .content(postCreateRequest.getContent())
                        .userId(Long.parseLong(postCreateRequest.getUserId()))
                        .build()
        );
    }
}
