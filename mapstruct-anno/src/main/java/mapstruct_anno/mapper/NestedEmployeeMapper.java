package mapstruct_anno.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import mapstruct_anno.dto.NestedEmployeeDTO;
import mapstruct_anno.vo.NestedEmployeeVO;

@Mapper
public interface NestedEmployeeMapper {
	NestedEmployeeMapper INSTANCE = Mappers.getMapper(NestedEmployeeMapper.class);
	
	// 可以把nestedEmployeeVO省略
	@Mapping(source="nestedEmployeeVO.officeVO.name", target="officeName")
	@Mapping(source="nestedEmployeeVO.officeVO.id", target="officeId")
    @Mapping(source ="nestedEmployeeVO.officeVO.officeTypeVO.roomSize", target = "roomSize")
	NestedEmployeeDTO toDTO(NestedEmployeeVO nestedEmployeeVO);

}
