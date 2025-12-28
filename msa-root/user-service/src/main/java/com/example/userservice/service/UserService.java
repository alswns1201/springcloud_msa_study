package com.example.userservice.service;

import com.example.userservice.dto.RequestUser;
import com.example.userservice.dto.ResponseUser;

public interface UserService {
    ResponseUser createUser(RequestUser req);
}
