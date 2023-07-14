package com.morewen.orderservice.generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.morewen.feignapi.client.UserClient;
import com.morewen.feignapi.entity.User;
import com.morewen.orderservice.generator.domain.Myorder;
import com.morewen.orderservice.generator.mapper.MyorderMapper;
import com.morewen.orderservice.generator.service.MyorderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
* @author Wangbw
* @description 针对表【MyOrder】的数据库操作Service实现
* @createDate 2023-06-21 17:26:25
*/
@Service
public class MyorderServiceImpl extends ServiceImpl<MyorderMapper, Myorder>
    implements MyorderService {
    @Autowired
    MyorderMapper mapper;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private UserClient userClient;

    @Override
    public Myorder getOrderAndUserById(Long id) {
        Myorder myorder = mapper.selectById(id);
        /*
        String url = "http://userservice/user/" + myorder.getUserId();
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        Map<String, Object> data = (Map<String, Object>) response.get("data"); // 获取data
        Map<String, Object> userMap = (Map<String, Object>) data.get("user"); // 获取user
        ObjectMapper mapper = new ObjectMapper();
        User user = mapper.convertValue(userMap, User.class); // 将Map转换为User对象
        myorder.setUser(user);
        return myorder;
        */
        try {
            Map<String, Object> response = userClient.findMapById(Long.valueOf(myorder.getUserId()));
            myorder.setUser(UserFromMap(response));
            return myorder;
        }catch (Exception e){
            return null;
        }

    }

    private User UserFromMap(Map<String, Object> response) {
        try {
            Map<String, Object> data = (Map<String, Object>) response.get("data"); // 获取data
            Map<String, Object> userMap = (Map<String, Object>) data.get("user"); // 获取user
            ObjectMapper mapper = new ObjectMapper();
            return mapper.convertValue(userMap, User.class);
        }catch (Exception e) {
            return null;
        }
    }
}




