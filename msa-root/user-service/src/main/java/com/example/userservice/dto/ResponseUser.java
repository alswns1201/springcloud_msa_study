package com.example.userservice.dto;

import com.example.userservice.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ResponseUser(
        String email,
        String name,
        String userId


) {
    public ResponseUser(UserEntity user) {
        this(user.getEmail(), user.getName(), user.getUserId());
    }
}