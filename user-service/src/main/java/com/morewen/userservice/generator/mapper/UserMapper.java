package com.morewen.userservice.generator.mapper;

import com.morewen.userservice.generator.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Wangbw
* @description 针对表【User】的数据库操作Mapper
* @createDate 2023-06-21 17:13:46
* @Entity generator.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




