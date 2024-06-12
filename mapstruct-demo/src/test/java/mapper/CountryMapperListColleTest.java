package mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import mapstruct_demo.dto.CountryDTO;
import mapstruct_demo.mapper.CountryMapperListColleMapper;
import mapstruct_demo.model.CountryArea;
import mapstruct_demo.model.GeoPosition;
import mapstruct_demo.vo.CountryVO;


class CountryMapperListColleTest {
    @ParameterizedTest
    @MethodSource("provideVOArguments")
    void toDTOList(CountryVO countryVO) {
        List<CountryDTO> countryDTOs = CountryMapperListColleMapper.INSTANCE.toDTOList(Collections.singletonList(countryVO));

        assertThat(countryDTOs)
            .hasSize(1)
            .extracting(CountryDTO::getCountryCode, CountryDTO::getCountryName, CountryDTO::getCountryArea,
                CountryDTO::getGeoPosition)
            .contains(tuple(countryVO.getCode(), countryVO.getName(), countryVO.getArea(), countryVO.getPosition()));
    }

    @ParameterizedTest
    @MethodSource("provideVOArguments")
    void toDTOSet(CountryVO countryVO) {
        Set<CountryDTO> countryDTOs = CountryMapperListColleMapper.INSTANCE.toDTOSet(Collections.singleton(countryVO));

        assertThat(countryDTOs)
            .hasSize(1)
            .extracting(CountryDTO::getCountryCode, CountryDTO::getCountryName, CountryDTO::getCountryArea,
                CountryDTO::getGeoPosition)
            .contains(tuple(countryVO.getCode(), countryVO.getName(), countryVO.getArea(), countryVO.getPosition()));
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