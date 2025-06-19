package com.clean.architecture.infrastructure.internal.api.presenter;

import com.clean.architecture.domain.entity.User;
import com.generated.swaggerCodegen.model.UserResponse;

public class UserPresenter extends BasePresenter<User, UserResponse> {
    @Override
    protected UserResponse map(final User entity) {
        UserResponse resp = new UserResponse();
        resp.setIsActive(entity.isActive());
        resp.setEmail(entity.getEmail());
        resp.setFirstName(entity.getFirstName());
        resp.setLastName(entity.getLastName());
        resp.setId(String.valueOf(entity.getId()));
        return resp;
    }
}
