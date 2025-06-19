package com.clean.architecture.application.usecase.dto;

import com.clean.architecture.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class UsersWithCountDto {
    private List<User> users;
    private Long totalCount;
    private Long cursor;
}