package com.saqaya.userapi.controller;
import com.saqaya.userapi.model.User;
import com.saqaya.userapi.model.UserCreateCredentialsDTO;
import com.saqaya.userapi.model.UserResponseDTO;
import com.saqaya.userapi.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public UserCreateCredentialsDTO createUser(@RequestBody User user) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return userService.createUser(user);
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUser(@PathVariable String id, @RequestParam String accessToken) throws Exception {

        return userService.getUser(id, accessToken);
    }
}
