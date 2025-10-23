package mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import mapstruct_demo.dto.PersonDTO;
import mapstruct_demo.enumeration.Gender;
import mapstruct_demo.mapper.PersonMapper;
import mapstruct_demo.vo.PersonVO;

class PersonMapperTest {

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
}