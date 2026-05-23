package ba.unsa.etf.employeemanagement.service.impl.plsql;

import ba.unsa.etf.employeemanagement.dto.plsql.RemainingVacationDays;
import ba.unsa.etf.employeemanagement.dto.request.VacationRequest;
import ba.unsa.etf.employeemanagement.dto.response.VacationResponse;
import ba.unsa.etf.employeemanagement.exceptions.ResourceNotFoundException;
import ba.unsa.etf.employeemanagement.mapper.VacationMapper;
import ba.unsa.etf.employeemanagement.repository.VacationRepository;
import ba.unsa.etf.employeemanagement.repository.plsql.PkgVacationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;

@Service
@RequiredArgsConstructor
public class PlsqlVacationService {

    private final PkgVacationRepository pkgVacationRepository;
    private final VacationRepository vacationRepository;
    private final VacationMapper vacationMapper;

    public VacationResponse submitRequest(VacationRequest request) {
        Long vacationId = pkgVacationRepository.submitVacationRequest(
                request.getEmployeeId(),
                request.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                request.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                request.getVacationType(),
                request.getReason()
        );
        return vacationRepository.findById(vacationId)
                .map(vacationMapper::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Vacation not found after PL/SQL insert"));
    }

    public VacationResponse decide(Long vacationId, Long approverId, boolean approve, String reason) {
        pkgVacationRepository.decideVacation(vacationId, approverId, approve, reason);
        return vacationRepository.findById(vacationId)
                .map(vacationMapper::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Vacation not found"));
    }

    public RemainingVacationDays remainingDays(Long employeeId, Integer year) {
        return pkgVacationRepository.calculateRemainingDays(employeeId, year);
    }
}
