package mapstruct_anno.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class NestedEmployeeDTO {
	  private String name;
	  private Long id;
	  private Long empId;
	  
	  private String officeName;
	  private String officeId;
	  
	  private String roomSize;
}
