package com.clean.architecture.application.port.in;

import com.generated.swaggerCodegen.model.PostCreateRequest;

public interface PostCreateUCase {
    void postCreate(final PostCreateRequest postCreateRequest);
}
