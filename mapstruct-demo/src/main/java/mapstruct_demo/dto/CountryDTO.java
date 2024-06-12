package mapstruct_demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import mapstruct_demo.model.CountryArea;
import mapstruct_demo.model.GeoPosition;

@Value
@AllArgsConstructor
@Builder
public class CountryDTO {
	private String countryName;
    private String countryCode;
    private CountryArea countryArea;
    private GeoPosition geoPosition;
}
