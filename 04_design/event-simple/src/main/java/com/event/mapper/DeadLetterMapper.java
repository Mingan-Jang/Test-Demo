package com.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.event.dto.DeadLetterMessageDTO;
import com.event.po.postgres.DeadLetterMessagePO;

@Mapper(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface DeadLetterMapper {

	DeadLetterMapper INSTANCE = Mappers.getMapper(DeadLetterMapper.class);

	DeadLetterMessagePO toPO(DeadLetterMessageDTO dto);

}