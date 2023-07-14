package com.morewen.userservice.generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.morewen.userservice.generator.domain.User;
import com.morewen.userservice.generator.mapper.UserMapper;
import com.morewen.userservice.generator.service.UserService;
import org.springframework.stereotype.Service;

/**
* @author Wangbw
* @description 针对表【User】的数据库操作Service实现
* @createDate 2023-06-21 17:13:46
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

}




