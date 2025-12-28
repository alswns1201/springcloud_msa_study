package com.example.userservice.controller;


import com.example.userservice.UserServiceApplication;
import com.example.userservice.dto.RequestUser;
import com.example.userservice.dto.ResponseUser;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/")
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser req) {
        ResponseUser responseUser =   userService.createUser(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
    }
}
