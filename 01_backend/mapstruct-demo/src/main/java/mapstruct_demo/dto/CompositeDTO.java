package mapstruct_demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mapstruct_demo.model.GeoPosition;

//@Value
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompositeDTO {
    private String personName;
    private Integer age=1;
    private String belongingCountryName;
    private String belongingCountryCode;
    private GeoPosition countryPosition;
}
