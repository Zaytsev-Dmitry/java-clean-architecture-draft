package com.clean.architecture.application.usecase;

import com.clean.architecture.application.port.in.GetAllUsersUCase;
import com.clean.architecture.application.usecase.dto.UsersWithCountDto;
import com.clean.architecture.domain.entity.User;
import com.clean.architecture.infrastructure.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "GetAllUsersUCase")
@Slf4j
@RequiredArgsConstructor
public class GetAllUsersUCaseImpl implements GetAllUsersUCase {
    private final UserRepository repository;

    @Override
    public UsersWithCountDto getAllUsers(final Long cursor, final Integer pageSize) {
        Long totalCount = repository.count();
        List<User> users = repository.getAllByCursor(cursor, pageSize);
        return new UsersWithCountDto(users, totalCount, cursor);
    }
}
