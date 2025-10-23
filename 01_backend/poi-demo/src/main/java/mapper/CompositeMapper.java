package mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import dto.CompositeDTO;
import dto.ExtendDTO;
import dto.PrimaryDTO;


@Mapper
public interface CompositeMapper {
	CompositeMapper INSTANCE = Mappers.getMapper(CompositeMapper.class);

    @Mapping(source = "extendDTO.email", target = "email")
    @Mapping(source = "extendDTO.address", target = "address")
    @Mapping(source = "primaryDTO.name", target = "name")
    @Mapping(source = "primaryDTO.age", target = "age")
    CompositeDTO toCompositeDTO(ExtendDTO extendDTO, PrimaryDTO primaryDTO);
}