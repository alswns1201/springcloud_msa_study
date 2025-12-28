package com.example.userservice.service;

import com.example.userservice.dto.RequestUser;
import com.example.userservice.dto.ResponseUser;
import com.example.userservice.entity.UserEntity;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    @Override
    public ResponseUser createUser(RequestUser req) {

        UserEntity user = new UserEntity(req);
        user.setEncryptedPwd(passwordEncoder.encode(req.pwd()));
        userRepository.save(user);

        return new ResponseUser(user);

    }
}
