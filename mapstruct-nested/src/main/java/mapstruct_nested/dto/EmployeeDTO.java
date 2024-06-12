package mapstruct_nested.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class EmployeeDTO {
	  private String name;
	  private Long id;
	  private Long empId;
}
