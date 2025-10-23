package mapstruct_demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import mapstruct_demo.dto.PersonDTO;
import mapstruct_demo.vo.PersonVO;

@Mapper
public interface PersonMapper {
	PersonMapper INSTANCE = Mappers.getMapper(PersonMapper.class);
	
	PersonDTO toDTO(PersonVO personVO);
	
	PersonVO toVO(PersonDTO personDTO);
}
