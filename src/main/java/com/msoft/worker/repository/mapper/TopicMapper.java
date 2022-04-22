package com.msoft.worker.repository.mapper;

import com.msoft.worker.repository.domain.worker.Topic;
import com.msoft.worker.repository.model.TopicVm;
import com.msoft.worker.repository.model.TopicVo;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TopicMapper {
    TopicMapper INSTANCE = Mappers.getMapper(TopicMapper.class);

    Topic fromVm(TopicVm vm);

    Topic fromVm(TopicVm vm, @MappingTarget Topic diary);

    TopicVo fromEntity(Topic diary);

}
