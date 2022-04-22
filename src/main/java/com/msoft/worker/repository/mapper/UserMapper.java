package com.msoft.worker.repository.mapper;

import com.msoft.worker.repository.domain.user.WechatUser;
import com.msoft.worker.repository.model.UserVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserVo fromEntity(WechatUser wechatUser);
}
