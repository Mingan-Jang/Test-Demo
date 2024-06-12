package mapstruct_nested.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class OfficeTypeVO {
	private String roomSize;
}
