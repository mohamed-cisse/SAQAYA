package com.saqaya.userapi.controller;
import com.saqaya.userapi.model.User;
import com.saqaya.userapi.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable String id, @RequestParam String accessToken) throws Exception {
        return userService.getUser(id, accessToken);
    }
}
