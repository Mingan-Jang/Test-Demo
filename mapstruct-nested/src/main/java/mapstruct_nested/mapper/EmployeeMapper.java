package mapstruct_nested.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import mapstruct_nested.dto.EmployeeDTO;
import mapstruct_nested.vo.EmployeeVO;

@Mapper
public interface EmployeeMapper {
	EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);
	
	EmployeeDTO toDTO(EmployeeVO employeeVO);
	
	EmployeeVO toVO(EmployeeDTO employeeDTO);
}
