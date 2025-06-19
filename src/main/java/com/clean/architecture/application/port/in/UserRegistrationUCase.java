package com.clean.architecture.application.port.in;

import com.clean.architecture.domain.entity.User;
import com.generated.swaggerCodegen.model.UserRegistrationRequest;

public interface UserRegistrationUCase {
    User userRegistration(final UserRegistrationRequest userRegistrationRequest);
}
