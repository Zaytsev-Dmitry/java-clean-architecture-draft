package com.clean.architecture.application.port.in;

import com.clean.architecture.application.usecase.dto.UsersWithCountDto;

public interface GetAllUsersUCase {
    UsersWithCountDto getAllUsers(final Long cursor, final Integer pageSize);
}
