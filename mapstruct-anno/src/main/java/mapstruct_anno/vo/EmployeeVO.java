package mapstruct_anno.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class EmployeeVO {
	  private String name;
	  private Long id;
	  private Long empId;
	  
}
