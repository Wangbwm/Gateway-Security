package com.morewen.feignapi.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(value = "userservice")
public interface UserClient {
    @GetMapping("/user/{id}")
    Map<String, Object> findMapById(@PathVariable("id") Long id);
}