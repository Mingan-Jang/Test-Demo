package mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import mapstruct_demo.dto.CompositeDTO;
import mapstruct_demo.enumeration.Gender;
import mapstruct_demo.mapper.CompositeMapper;
import mapstruct_demo.model.CountryArea;
import mapstruct_demo.model.GeoPosition;
import mapstruct_demo.vo.CountryVO;
import mapstruct_demo.vo.PersonVO;

class CompositeMapperTest {

	  @Test
	    void testToDTO() {
	        PersonVO personVO = PersonVO.builder()
	            .name("Su")
	            .age(30)
	            .gender(Gender.Male)
	            .build();

	        CountryVO countryVO = CountryVO.builder()
	            .setName("Taiwan")
	            .setCode("TW")
	            .setArea(CountryArea.builder()
	                .setArea(123.0)
	                .setUnit("km2")
	                .build())
	            .setPosition(GeoPosition.builder()
	                .setLongitude(121.597366d)
	                .setLatitude(25.105497d)
	                .build())
	            .build();
	        
	        CompositeDTO compositeDTO = CompositeMapper.INSTANCE.toDTO(personVO, countryVO);
	        assertThat(compositeDTO)
	            .extracting(CompositeDTO::getPersonName, CompositeDTO::getAge,
	                CompositeDTO::getBelongingCountryName, CompositeDTO::getBelongingCountryCode,
	                CompositeDTO::getCountryPosition)
	            .containsExactly(personVO.getName(),  personVO.getAge(), countryVO.getName(),
	                countryVO.getCode(), countryVO.getPosition());
	    }
}