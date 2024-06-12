package mapstruct_demo.vo;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import mapstruct_demo.enumeration.Gender;

@Value
@AllArgsConstructor
@Builder
public class PersonVO {
	  private String name;
	  private Integer age;
	  private Gender gender;
	  private Date bt;
}
