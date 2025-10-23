package mapstruct_demo.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import mapstruct_demo.enumeration.Gender;

@Value
@AllArgsConstructor
@Builder
public class PersonDTO {
  private String name;
  private Integer age;
  private Gender gender;
  private Date bt;
}
