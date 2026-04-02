package ba.unsa.etf.employeemanagement.service.impl;

import ba.unsa.etf.employeemanagement.dto.request.VehicleRequest;
import ba.unsa.etf.employeemanagement.dto.response.VehicleResponse;
import ba.unsa.etf.employeemanagement.exceptions.ResourceNotFoundException;
import ba.unsa.etf.employeemanagement.mapper.VehicleMapper;
import ba.unsa.etf.employeemanagement.model.Vehicle;
import ba.unsa.etf.employeemanagement.repository.VehicleRepository;
import ba.unsa.etf.employeemanagement.service.api.IVehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService implements IVehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;

    @Override
    public List<VehicleResponse> findAll() {
        return vehicleRepository.findAll()
                .stream()
                .map(vehicleMapper::mapToResponse)
                .toList();
    }

    @Override
    public VehicleResponse findById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
        return vehicleMapper.mapToResponse(vehicle);
    }

    @Override
    public VehicleResponse save(VehicleRequest request) {
        Vehicle vehicle = vehicleMapper.mapToEntity(request);
        Long id = vehicleRepository.save(vehicle);

        Vehicle saved = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found after save"));

        return vehicleMapper.mapToResponse(saved);
    }

    @Override
    public VehicleResponse update(Long id, VehicleRequest request) {
        Vehicle existing = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));

        Vehicle vehicle = vehicleMapper.mapToEntity(request);
        vehicle.setId(existing.getId());

        vehicleRepository.update(id, vehicle);

        Vehicle updated = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found after update"));

        return vehicleMapper.mapToResponse(updated);
    }

    @Override
    public void delete(Long id) {
        vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));

        vehicleRepository.deleteById(id);
    }
}
