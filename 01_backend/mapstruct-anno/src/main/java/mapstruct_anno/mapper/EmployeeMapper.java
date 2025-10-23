package mapstruct_anno.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import mapstruct_anno.dto.EmployeeDTO;
import mapstruct_anno.vo.EmployeeVO;

@Mapper
public interface EmployeeMapper {
	EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);
	
	EmployeeDTO toDTO(EmployeeVO employeeVO);
	
	EmployeeVO toVO(EmployeeDTO employeeDTO);
}
