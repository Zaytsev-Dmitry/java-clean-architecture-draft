package com.clean.architecture.infrastructure.internal.api.rest;

import com.clean.architecture.application.port.delegate.CompositeUseCaseDelegate;
import com.clean.architecture.application.usecase.dto.UsersWithCountDto;
import com.clean.architecture.infrastructure.internal.api.presenter.UserPresenter;
import com.generated.swaggerCodegen.api.PostApi;
import com.generated.swaggerCodegen.api.UserApi;
import com.generated.swaggerCodegen.model.ListUserBackendResponse;
import com.generated.swaggerCodegen.model.PostCreateRequest;
import com.generated.swaggerCodegen.model.SingleUserBackendResponse;
import com.generated.swaggerCodegen.model.UserRegistrationRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


@AllArgsConstructor
@RestController
@Slf4j
public class WebApi extends AbstractApiController implements UserApi, PostApi {
    private final CompositeUseCaseDelegate delegate;

    // ==================== USER API OPERATIONS ====================
    public ResponseEntity<ListUserBackendResponse> getAllUsers(final Long cursor, final Integer pageSize) {
        UsersWithCountDto data = delegate.getAllUsers(cursor, pageSize);
        return ResponseEntity.ok(
                processing("getAllUsers",
                        new UserPresenter().mapToListResponse(
                                data.getCursor(),
                                data.getTotalCount(),
                                pageSize,
                                data.getUsers(),
                                ListUserBackendResponse.class),
                        cursor, pageSize)
        );
    }

    public ResponseEntity<SingleUserBackendResponse> userRegistration(final UserRegistrationRequest userRegistrationRequest) {
        return ResponseEntity.ok(
                processing("userRegistration",
                        new UserPresenter().mapToSingleResponse(delegate.userRegistration(userRegistrationRequest), SingleUserBackendResponse.class),
                        userRegistrationRequest)
        );
    }

    // ==================== POST API OPERATIONS ====================
    public ResponseEntity<Void> postCreate(final PostCreateRequest postCreateRequest) {
        processingVoid("postCreate", delegate::postCreate, postCreateRequest);
        return ResponseEntity.noContent().build();
    }
} 