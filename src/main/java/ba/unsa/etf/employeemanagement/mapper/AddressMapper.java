package ba.unsa.etf.employeemanagement.mapper;

import ba.unsa.etf.employeemanagement.dto.address.AddressDto;
import ba.unsa.etf.employeemanagement.model.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressDto toDto(Address entity) {
        if (entity == null) {
            return null;
        }
        return AddressDto.builder()
                .id(entity.getId())
                .street(entity.getStreet())
                .city(entity.getCity())
                .postalCode(entity.getPostalCode())
                .country(entity.getCountry())
                .build();
    }

    public Address toEntity(AddressDto dto) {
        if (dto == null) {
            return null;
        }
        return Address.builder()
                .id(dto.getId())
                .street(dto.getStreet())
                .city(dto.getCity())
                .postalCode(dto.getPostalCode())
                .country(dto.getCountry())
                .build();
    }
}
