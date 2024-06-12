package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompositeDTO {
	public String email;
	public String address;
	public String name;
	public int age;
	
}
