package ba.unsa.etf.employeemanagement.service.impl;

import ba.unsa.etf.employeemanagement.dto.request.VacationRequest;
import ba.unsa.etf.employeemanagement.dto.response.VacationResponse;
import ba.unsa.etf.employeemanagement.exceptions.ResourceNotFoundException;
import ba.unsa.etf.employeemanagement.mapper.VacationMapper;
import ba.unsa.etf.employeemanagement.model.Vacation;
import ba.unsa.etf.employeemanagement.repository.VacationRepository;
import ba.unsa.etf.employeemanagement.service.api.IVacationService;
import ba.unsa.etf.employeemanagement.util.enums.VacationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static ba.unsa.etf.employeemanagement.util.VacationUtil.daysBetween;
import static ba.unsa.etf.employeemanagement.util.VacationUtil.getYear;

@Service
@RequiredArgsConstructor
public class VacationService implements IVacationService {
    private final VacationRepository repository;
    private final VacationMapper mapper;

    @Override
    public List<VacationResponse> findAll() {
        return repository.findAll().stream().map(mapper::mapToResponse).toList();
    }

    @Override
    public VacationResponse findById(Long id) {
        Vacation entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Vacation not found"));
        return mapper.mapToResponse(entity);
    }

    @Override
    public VacationResponse save(VacationRequest request) {
        Vacation entity = mapper.mapToEntity(request);
        Long id = repository.save(entity);
        return mapper.mapToResponse(repository.findById(id).orElseThrow());
    }

    @Override
    public VacationResponse update(Long id, VacationRequest request) {
        if(repository.findById(id).isEmpty()) throw new ResourceNotFoundException("Vacation not found");
        Vacation entity = mapper.mapToEntity(request);
        entity.setId(id);
        repository.update(id, entity);
        return mapper.mapToResponse(repository.findById(id).orElseThrow());
    }

    @Override
    public void delete(Long id) {
        if(repository.findById(id).isEmpty()) throw new ResourceNotFoundException("Vacation not found");
        repository.deleteById(id);
    }

    @Override
    public VacationResponse requestVacation(VacationRequest request) {
        int year = getYear(request.getStartDate());
        List<Vacation> vacations = repository.findByEmployeeIdAndYear(request.getEmployeeId(), year);
        int usedDays = vacations.stream()
            .filter(v -> v.getStatus().equals(VacationStatus.APPROVED.name()) || v.getStatus().equals(VacationStatus.PENDING.name()))
            .mapToInt(v -> daysBetween(v.getStartDate(), v.getEndDate()) + 1)
            .sum();
        int requestedDays = daysBetween(request.getStartDate(), request.getEndDate()) + 1;
        if (usedDays + requestedDays > 21) {
            throw new IllegalArgumentException("Vacation days limit (21) exceeded for the year.");
        }
        Vacation entity = mapper.mapToEntity(request);
        entity.setStatus(VacationStatus.PENDING.name());
        Long id = repository.save(entity);
        return mapper.mapToResponse(repository.findById(id).orElseThrow());
    }

    @Override
    public VacationResponse approveVacation(Long id, Long approverId) {
        Vacation vacation = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Vacation not found"));
        repository.updateStatus(id, VacationStatus.APPROVED, approverId, vacation.getReason());
        return mapper.mapToResponse(repository.findById(id).orElseThrow());
    }

    @Override
    public VacationResponse rejectVacation(Long id, Long approverId, String reason) {
        repository.updateStatus(id, VacationStatus.REJECTED, approverId, reason);
        return mapper.mapToResponse(repository.findById(id).orElseThrow());
    }

    @Override
    public VacationStatus getVacationStatus(Long id) {
        Vacation vacation = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Vacation not found"));
        return VacationStatus.valueOf(vacation.getStatus());
    }

}
