package mapstruct_demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import mapstruct_demo.dto.CountryDTO;
import mapstruct_demo.vo.CountryVO;

@Mapper
public interface CountryMapper {
    CountryMapper INSTANCE = Mappers.getMapper(CountryMapper.class);
    
    @Mappings({
        @Mapping(target = "name", source = "dto.countryName"),
        @Mapping(target = "code", source = "dto.countryCode"),
        @Mapping(target = "area", source = "dto.countryArea"),
        @Mapping(target = "position", source = "dto.geoPosition")
    })
    CountryVO toVO(CountryDTO dto);


    @Mappings({
        @Mapping(target = "countryName", source = "vo.name"),
        @Mapping(target = "countryCode", source = "vo.code"),
        @Mapping(target = "countryArea", source = "vo.area"),
        @Mapping(target = "geoPosition", source = "vo.position")
    })
    CountryDTO toDTO(CountryVO vo);
}
