package mapper;

import dto.CompositeDTO;
import dto.ExtendDTO;
import dto.PrimaryDTO;
import javax.annotation.processing.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-28T20:19:25+0800",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.36.0.v20231114-0937, environment: Java 17.0.9 (Eclipse Adoptium)"
)
public class CompositeMapperImpl implements CompositeMapper {

    @Override
    public CompositeDTO toCompositeDTO(ExtendDTO extendDTO, PrimaryDTO primaryDTO) {
        if ( extendDTO == null && primaryDTO == null ) {
            return null;
        }

        CompositeDTO compositeDTO = new CompositeDTO();

        if ( extendDTO != null ) {
            compositeDTO.setEmail( extendDTO.getEmail() );
            compositeDTO.setAddress( extendDTO.getAddress() );
        }
        if ( primaryDTO != null ) {
            compositeDTO.setName( primaryDTO.getName() );
            compositeDTO.setAge( primaryDTO.getAge() );
        }

        return compositeDTO;
    }
}
