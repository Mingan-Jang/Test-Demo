package mapstruct_demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder(setterPrefix = "set", toBuilder = true)
public class CountryArea {
    private Double area;
    private String unit;
}
