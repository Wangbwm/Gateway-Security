package com.morewen.orderservice.generator.mapper;

import com.morewen.orderservice.generator.domain.Myorder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Wangbw
* @description 针对表【MyOrder】的数据库操作Mapper
* @createDate 2023-06-21 17:26:25
* @Entity generator.domain.Myorder
*/
@Mapper
public interface MyorderMapper extends BaseMapper<Myorder> {

}




