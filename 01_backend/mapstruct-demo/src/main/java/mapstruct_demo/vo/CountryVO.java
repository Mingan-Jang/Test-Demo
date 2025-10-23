package mapstruct_demo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import mapstruct_demo.model.CountryArea;
import mapstruct_demo.model.GeoPosition;

@Value
@AllArgsConstructor
@Builder(setterPrefix = "set", toBuilder = true)
public class CountryVO {
    private String name;
    private String code;
    private CountryArea area;
    private GeoPosition position;
}