package com.morewen.userservice.controller;

import cn.hutool.core.map.MapUtil;
import com.morewen.userservice.generator.domain.User;
import com.morewen.userservice.generator.service.UserService;
import com.morewen.userservice.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @GetMapping("/{id}")
    protected Result queryById(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        return Result.succ(MapUtil.builder()
                .put("user", user)
                .map());
    }
}
