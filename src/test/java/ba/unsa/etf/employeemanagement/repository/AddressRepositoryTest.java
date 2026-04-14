package ba.unsa.etf.employeemanagement.repository;

import ba.unsa.etf.employeemanagement.model.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private AddressRepository addressRepository;

    @BeforeEach
    void setUp() {
        addressRepository = new AddressRepository(jdbcTemplate);
    }

    @Test
    void shouldFindAllAddresses() {
        Address address1 = Address.builder()
                .id(1L)
                .street("Street 1")
                .city("City 1")
                .postalCode("71000")
                .country("Country 1")
                .build();

        Address address2 = Address.builder()
                .id(2L)
                .street("Street 2")
                .city("City 2")
                .postalCode("88000")
                .country("Country 2")
                .build();

        when(jdbcTemplate.query(anyString(), any(RowMapper.class)))
                .thenReturn(Arrays.asList(address1, address2));
        List<Address> addresses = addressRepository.findAll();

        assertThat(addresses).hasSize(2);
        assertThat(addresses.get(0).getStreet()).isEqualTo("Street 1");
        verify(jdbcTemplate, times(1)).query(anyString(), any(RowMapper.class));
    }

    @Test
    void shouldFindAddressById() {
        Address address = Address.builder()
                .id(1L)
                .street("Zmaja od Bosne 8")
                .city("Sarajevo")
                .postalCode("71000")
                .country("Bosnia and Herzegovina")
                .build();

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(1L)))
                .thenReturn(Arrays.asList(address));

        Optional<Address> found = addressRepository.findById(1L);

        assertThat(found).isPresent();
        assertThat(found.get().getStreet()).isEqualTo("Zmaja od Bosne 8");
        verify(jdbcTemplate, times(1)).query(anyString(), any(RowMapper.class), eq(1L));
    }

    @Test
    void shouldReturnEmptyWhenAddressNotFound() {

        when(jdbcTemplate.query(anyString(), any(RowMapper.class), eq(999L)))
                .thenReturn(Arrays.asList());


        Optional<Address> found = addressRepository.findById(999L);


        assertThat(found).isEmpty();
    }

    @Test
    void shouldDeleteAddress() {

        when(jdbcTemplate.update(anyString(), eq(1L))).thenReturn(1);


        addressRepository.deleteById(1L);


        verify(jdbcTemplate, times(1)).update(anyString(), eq(1L));
    }
}
