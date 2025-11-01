package com.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.event.dto.EventDTO;
import com.event.po.postgres.EventPO;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

	EventMapper INSTANCE = Mappers.getMapper(EventMapper.class);

	EventPO toEntity(EventDTO dto);

	EventDTO toDTO(EventPO entity);
}