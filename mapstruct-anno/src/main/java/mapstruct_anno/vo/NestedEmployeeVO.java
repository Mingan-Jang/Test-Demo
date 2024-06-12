package mapstruct_anno.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class NestedEmployeeVO {
	  private String name;
	  private Long id;
	  private Long empId;
	  private OfficeVO officeVO;
}
