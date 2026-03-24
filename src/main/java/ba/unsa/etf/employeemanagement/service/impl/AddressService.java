package ba.unsa.etf.employeemanagement.service.impl;
import ba.unsa.etf.employeemanagement.dto.address.AddressDto;
import ba.unsa.etf.employeemanagement.mapper.AddressMapper;
import ba.unsa.etf.employeemanagement.model.Address;
import ba.unsa.etf.employeemanagement.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public List<AddressDto> findAll() {
        return addressRepository.findAll()
                .stream()
                .map(addressMapper::toDto)
                .collect(Collectors.toList());
    }

    public AddressDto findById(Long id) {
        return addressRepository.findById(id)
                .map(addressMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + id));
    }

    public AddressDto create(AddressDto addressDto) {
        Address address = addressMapper.toEntity(addressDto);
        Address savedAddress = addressRepository.save(address);
        return addressMapper.toDto(savedAddress);
    }

    public AddressDto update(Long id, AddressDto addressDto) {
        addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + id));

        addressDto.setId(id);
        Address address = addressMapper.toEntity(addressDto);
        Address updatedAddress = addressRepository.save(address);
        return addressMapper.toDto(updatedAddress);
    }

    public void delete(Long id) {
        addressRepository.deleteById(id);
    }
}

