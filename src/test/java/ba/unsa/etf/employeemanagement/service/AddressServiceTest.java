package ba.unsa.etf.employeemanagement.service;

import ba.unsa.etf.employeemanagement.dto.address.AddressDto;
import ba.unsa.etf.employeemanagement.mapper.AddressMapper;
import ba.unsa.etf.employeemanagement.model.Address;
import ba.unsa.etf.employeemanagement.repository.AddressRepository;
import ba.unsa.etf.employeemanagement.service.impl.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private AddressMapper addressMapper;

    @InjectMocks
    private AddressService addressService;

    private Address address;
    private AddressDto addressDto;

    @BeforeEach
    void setUp() {
        address = Address.builder()
                .id(1L)
                .street("Zmaja od Bosne 8")
                .city("Sarajevo")
                .postalCode("71000")
                .country("Bosnia and Herzegovina")
                .build();

        addressDto = AddressDto.builder()
                .id(1L)
                .street("Zmaja od Bosne 8")
                .city("Sarajevo")
                .postalCode("71000")
                .country("Bosnia and Herzegovina")
                .build();
    }

    @Test
    void shouldFindAllAddresses() {

        when(addressRepository.findAll()).thenReturn(Arrays.asList(address));
        when(addressMapper.toDto(any(Address.class))).thenReturn(addressDto);


        List<AddressDto> result = addressService.findAll();


        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStreet()).isEqualTo("Zmaja od Bosne 8");
        verify(addressRepository, times(1)).findAll();
        verify(addressMapper, times(1)).toDto(any(Address.class));
    }

    @Test
    void shouldFindAddressById() {

        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(addressMapper.toDto(address)).thenReturn(addressDto);


        AddressDto result = addressService.findById(1L);


        assertThat(result).isNotNull();
        assertThat(result.getStreet()).isEqualTo("Zmaja od Bosne 8");
        verify(addressRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenAddressNotFound() {

        when(addressRepository.findById(999L)).thenReturn(Optional.empty());


        assertThatThrownBy(() -> addressService.findById(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void shouldCreateAddress() {

        AddressDto inputDto = AddressDto.builder()
                .street("New Street")
                .city("New City")
                .postalCode("71000")
                .country("New Country")
                .build();

        when(addressMapper.toEntity(inputDto)).thenReturn(address);
        when(addressRepository.save(any(Address.class))).thenReturn(address);
        when(addressMapper.toDto(address)).thenReturn(addressDto);


        AddressDto result = addressService.create(inputDto);


        assertThat(result).isNotNull();
        verify(addressMapper, times(1)).toEntity(inputDto);
        verify(addressRepository, times(1)).save(any(Address.class));
        verify(addressMapper, times(1)).toDto(address);
    }

    @Test
    void shouldUpdateAddress() {

        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(addressMapper.toEntity(addressDto)).thenReturn(address);
        when(addressRepository.save(any(Address.class))).thenReturn(address);
        when(addressMapper.toDto(address)).thenReturn(addressDto);


        AddressDto result = addressService.update(1L, addressDto);


        assertThat(result).isNotNull();
        verify(addressRepository, times(1)).findById(1L);
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    void shouldDeleteAddress() {

        doNothing().when(addressRepository).deleteById(1L);


        addressService.delete(1L);


        verify(addressRepository, times(1)).deleteById(1L);
    }
}
