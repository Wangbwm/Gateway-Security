package com.morewen.orderservice.controller;

import cn.hutool.core.map.MapUtil;
import com.morewen.orderservice.generator.domain.Myorder;
import com.morewen.orderservice.generator.service.MyorderService;
import com.morewen.orderservice.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    MyorderService orderService;
    @GetMapping("/{id}")
    protected Result queryById(@PathVariable("id") Long id) {
        Myorder order = orderService.getOrderAndUserById(id);
        return Result.succ(MapUtil.builder()
                .put("order", order)
                .map());
    }
    @GetMapping("/hello")
    protected String hello() {
        return "hello";
    }
}
