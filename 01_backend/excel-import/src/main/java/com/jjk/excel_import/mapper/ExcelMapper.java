package com.jjk.excel_import.mapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.jjk.excel_import.dto.EventDTO;
import com.jjk.excel_import.dto.ExtendDTO;
import com.jjk.excel_import.po.EventPO;
import com.jjk.excel_import.po.ExtendPO;

@Mapper
public interface ExcelMapper {
    ExcelMapper INSTANCE = Mappers.getMapper(ExcelMapper.class);

    @Mapping(source = "occurTime", target = "occurTime", qualifiedByName = "stringToInstant")
    EventPO eventDTOToEventPO(EventDTO eventDTO);
    EventDTO eventPOToEventDTO(EventPO eventPO);

    ExtendPO extendDTOToExtendPO(ExtendDTO extendDTO);
    ExtendDTO extendPOToExtendDTO(ExtendPO extendPO);
    
    
    @Named("stringToInstant")
    default Instant stringToInstant(String value) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return LocalDateTime.parse(value, formatter).atZone(ZoneId.systemDefault()).toInstant();
    }
}
