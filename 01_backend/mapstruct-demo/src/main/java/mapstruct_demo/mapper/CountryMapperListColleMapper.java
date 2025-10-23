package mapstruct_demo.mapper;

import java.util.List;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import mapstruct_demo.dto.CountryDTO;
import mapstruct_demo.vo.CountryVO;

@Mapper
public interface CountryMapperListColleMapper {
	CountryMapperListColleMapper INSTANCE = Mappers.getMapper(CountryMapperListColleMapper.class);

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

	    List<CountryDTO> toDTOList(List<CountryVO> voList);

	    Set<CountryDTO> toDTOSet(Set<CountryVO> voSet);
}
