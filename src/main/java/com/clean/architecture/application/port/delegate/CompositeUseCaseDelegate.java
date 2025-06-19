package com.clean.architecture.application.port.delegate;

import com.clean.architecture.application.port.in.GetAllUsersUCase;
import com.clean.architecture.application.port.in.PostCreateUCase;
import com.clean.architecture.application.port.in.UserRegistrationUCase;
import com.clean.architecture.application.prepared.base.PreparedUseCaseDelegate;
import com.generated.swaggerCodegen.model.PostCreateRequest;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CompositeUseCaseDelegate extends PreparedUseCaseDelegate {

    @Delegate
    private final GetAllUsersUCase getAllUsersUCase;

    @Delegate
    private final UserRegistrationUCase userRegistrationUCase;

    @Delegate
    private final PostCreateUCase postCreateUCase;

    @Autowired
    public CompositeUseCaseDelegate(
            @Qualifier("GetAllUsersUCase") final GetAllUsersUCase getAllUsersUCase,
            @Qualifier("UserRegistrationUCase") final UserRegistrationUCase userRegistrationUCase,
            @Qualifier("PostCreateUCase") final PostCreateUCase postCreateUCase
    ) {
        this.getAllUsersUCase = getAllUsersUCase;
        this.userRegistrationUCase = userRegistrationUCase;
        this.postCreateUCase = postCreateUCase;
    }
}