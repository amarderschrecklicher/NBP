package ba.unsa.etf.employeemanagement.service.impl;

import ba.unsa.etf.employeemanagement.exceptions.ResourceNotFoundException;
import ba.unsa.etf.employeemanagement.model.Employee;
import ba.unsa.etf.employeemanagement.model.EmployeePhoto;
import ba.unsa.etf.employeemanagement.repository.EmployeePhotoRepository;
import ba.unsa.etf.employeemanagement.repository.EmployeeRepository;
import ba.unsa.etf.employeemanagement.service.api.IEmployeePhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class EmployeePhotoService implements IEmployeePhotoService {

    private final EmployeePhotoRepository employeePhotoRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeePhoto getPhotoByEmployeeId(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));

        return employeePhotoRepository.findByUserId(employee.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Photo not found for employee with id: " + employeeId));
    }

    @Override
    public void uploadPhoto(Long employeeId, MultipartFile file) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));

        byte[] photoBytes;
        try {
            photoBytes = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read photo file", e);
        }

        String contentType = file.getContentType() != null ? file.getContentType() : "application/octet-stream";

        var existing = employeePhotoRepository.findByUserId(employee.getUserId());
        if (existing.isPresent()) {
            employeePhotoRepository.update(employee.getUserId(), photoBytes, contentType);
        } else {
            employeePhotoRepository.save(employee.getUserId(), photoBytes, contentType);
        }
    }

    @Override
    public void deletePhoto(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + employeeId));

        employeePhotoRepository.deleteByUserId(employee.getUserId());
    }
}


