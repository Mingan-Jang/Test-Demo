package mapstruct_anno.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class OfficeDTO {
	  private String officeName;
	  private Long officeId;
}
