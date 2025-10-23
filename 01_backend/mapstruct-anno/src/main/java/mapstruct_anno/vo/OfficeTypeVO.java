package mapstruct_anno.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class OfficeTypeVO {
	private String roomSize;
}
