package com.morewen.gateway.controller;

import com.morewen.gateway.common.utils.AjaxResult;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController {
    @GetMapping("/hello")
    protected AjaxResult index() {
        return AjaxResult.success("hello");
    }
}
