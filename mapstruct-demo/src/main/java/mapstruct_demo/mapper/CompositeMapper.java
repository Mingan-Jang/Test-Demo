package mapstruct_demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import mapstruct_demo.dto.CompositeDTO;
import mapstruct_demo.vo.CountryVO;
import mapstruct_demo.vo.PersonVO;

@Mapper
public interface CompositeMapper {
    CompositeMapper INSTANCE = Mappers.getMapper(CompositeMapper.class);

    @Mappings({
        @Mapping(source = "countryVO.name", target = "belongingCountryName"),
        @Mapping(source = "countryVO.code", target = "belongingCountryCode"),
        @Mapping(source = "countryVO.position", target = "countryPosition"),
        // Several possible source properties for target property "name".
        @Mapping(source = "personVO.name", target = "personName")
    })
    CompositeDTO toDTO(PersonVO personVO, CountryVO countryVO);
}