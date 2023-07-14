package com.morewen.orderservice.generator.service;

import com.morewen.orderservice.generator.domain.Myorder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Wangbw
* @description 针对表【MyOrder】的数据库操作Service
* @createDate 2023-06-21 17:26:25
*/
public interface MyorderService extends IService<Myorder> {

    Myorder getOrderAndUserById(Long id);
}
