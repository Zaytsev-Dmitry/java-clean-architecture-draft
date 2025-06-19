package com.clean.architecture.application.usecase;

import com.clean.architecture.application.port.in.UserRegistrationUCase;
import com.clean.architecture.domain.entity.User;
import com.clean.architecture.infrastructure.persistence.repository.UserRepository;
import com.generated.swaggerCodegen.model.UserRegistrationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service(value = "UserRegistrationUCase")
@Slf4j
@RequiredArgsConstructor
public class UserRegistrationUCaseImpl implements UserRegistrationUCase {
    private final UserRepository repository;

    @Override
    public User userRegistration(final UserRegistrationRequest userRegistrationRequest) {
        return this.repository.saveAndFlush(
                User.builder()
                        .email(userRegistrationRequest.getEmail())
                        .firstName(userRegistrationRequest.getFirstName())
                        .lastName(userRegistrationRequest.getLastName())
                        .username(userRegistrationRequest.getUsername())
                        .isActive(true)
                        .build()
        );
    }
}
