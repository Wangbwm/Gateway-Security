package com.morewen.gateway.generator.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.morewen.gateway.generator.domain.SysUser;
import com.morewen.gateway.generator.service.SysUserService;
import com.morewen.gateway.generator.mapper.SysUserMapper;
import org.springframework.stereotype.Service;

/**
* @author Wangbw
* @description 针对表【sys_user(用户表)】的数据库操作Service实现
* @createDate 2023-07-10 10:50:52
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService{

}




