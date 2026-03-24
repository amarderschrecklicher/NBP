package ba.unsa.etf.employeemanagement.repository;

import ba.unsa.etf.employeemanagement.model.Address;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class AddressRepository {

    private final JdbcTemplate jdbcTemplate;

    private static final String SCHEMA = "NBPT9";
    private static final String TABLE = SCHEMA + ".ADDRESS";
    private static final String SEQUENCE = SCHEMA + ".ADDRESS_SEQ";

    private final RowMapper<Address> rowMapper = (rs, rowNum) -> Address.builder()
            .id(rs.getLong("ID"))
            .street(rs.getString("STREET"))
            .city(rs.getString("CITY"))
            .postalCode(rs.getString("POSTAL_CODE"))
            .country(rs.getString("COUNTRY"))
            .build();

    public List<Address> findAll() {
        String sql = "SELECT * FROM " + TABLE + " ORDER BY ID";
        log.debug("Executing query: {}", sql);
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<Address> findById(Long id) {
        String sql = "SELECT * FROM " + TABLE + " WHERE ID = ?";
        log.debug("Executing query: {} with id: {}", sql, id);
        return jdbcTemplate.query(sql, rowMapper, id)
                .stream()
                .findFirst();
    }

    public Address save(Address address) {
        if (address.getId() == null) {
            return insert(address);
        } else {
            return update(address);
        }
    }

    private Address insert(Address address) {
        String sql = "INSERT INTO " + TABLE + " (ID, STREET, CITY, POSTAL_CODE, COUNTRY) " +
                "VALUES (" + SEQUENCE + ".NEXTVAL, ?, ?, ?, ?)";

        log.debug("Inserting address: {}", address);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID"});
            ps.setString(1, address.getStreet());
            ps.setString(2, address.getCity());
            ps.setString(3, address.getPostalCode());
            ps.setString(4, address.getCountry());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            address.setId(key.longValue());
        }

        log.debug("Address inserted with id: {}", address.getId());
        return address;
    }

    private Address update(Address address) {
        String sql = "UPDATE " + TABLE +
                " SET STREET = ?, CITY = ?, POSTAL_CODE = ?, COUNTRY = ? " +
                "WHERE ID = ?";

        log.debug("Updating address: {}", address);

        int rowsAffected = jdbcTemplate.update(sql,
                address.getStreet(),
                address.getCity(),
                address.getPostalCode(),
                address.getCountry(),
                address.getId());

        if (rowsAffected == 0) {
            throw new RuntimeException("Address not found with id: " + address.getId());
        }

        log.debug("Address updated successfully");
        return address;
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM " + TABLE + " WHERE ID = ?";
        log.debug("Deleting address with id: {}", id);

        int rowsAffected = jdbcTemplate.update(sql, id);

        if (rowsAffected == 0) {
            throw new RuntimeException("Address not found with id: " + id);
        }

        log.debug("Address deleted successfully");
    }

    public long count() {
        String sql = "SELECT COUNT(*) FROM " + TABLE;
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }
}
