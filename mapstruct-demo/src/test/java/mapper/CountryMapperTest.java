package mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;

import mapstruct_demo.dto.PersonDTO;
import mapstruct_demo.enumeration.Gender;
import mapstruct_demo.mapper.PersonMapper;
import mapstruct_demo.model.CountryArea;
import mapstruct_demo.model.GeoPosition;
import mapstruct_demo.vo.CountryVO;
import mapstruct_demo.vo.PersonVO;

class CountryMapperTest {

    @Test
    void testToDTO() {
        PersonVO personVO = PersonVO.builder()
            .name("Brian")
            .age(30)
            .gender(Gender.Male)
            .build();
        PersonDTO personDTO = PersonMapper.INSTANCE.toDTO(personVO);

        assertThat(personDTO)
            .extracting(PersonDTO::getName, PersonDTO::getAge, PersonDTO::getGender)
            .containsExactly(personVO.getName(),  personVO.getAge(), personVO.getGender());
    }

    @Test
    void testToVO() {
        PersonDTO personDTO = PersonDTO.builder()
            .name("Brian")
            .age(30)
            .gender(Gender.Male)
            .build();
        PersonVO personVO = PersonMapper.INSTANCE.toVO(personDTO);

        assertThat(personVO)
            .extracting(PersonVO::getName,PersonVO::getAge, PersonVO::getGender)
            .containsExactly(personDTO.getName(),  personDTO.getAge(), personDTO.getGender());
    }
    
    
    
    private static Stream<Arguments> provideVOArguments2(){
    	return Stream.of(
    			Arguments.of(
    					CountryVO.builder()
    					.setCode(null)
    					.setArea(CountryArea.builder()
    							.setArea(null).setUnit(null).build())
    					.setName("Taiwan")
    					.setPosition(GeoPosition.builder()
    							.setLatitude(23.0)
    							.setLongitude(121.0)
    	                        .build()).build()
    					)
    			);
    }
    
    
    private static Stream<Arguments> provideVOArguments() {
        return Stream.of(
            Arguments.of(
                CountryVO.builder()
                    .setCode("TW")
                    .setName("Taiwan")
                    .setArea(CountryArea.builder()
                        .setArea(123.0)
                        .setUnit("km2")
                        .build())
                    .setPosition(GeoPosition.builder()
                        .setLongitude(121.597366d)
                        .setLatitude(25.105497d)
                        .build())
                    .build()
            )
        );
    }
}