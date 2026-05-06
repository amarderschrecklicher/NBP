package ba.unsa.etf.employeemanagement.service.api;

import ba.unsa.etf.employeemanagement.model.EmployeePhoto;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface IEmployeePhotoService {
    EmployeePhoto getPhotoByEmployeeId(Long employeeId);
    void uploadPhoto(Long employeeId, MultipartFile file);
    void deletePhoto(Long employeeId);
}

