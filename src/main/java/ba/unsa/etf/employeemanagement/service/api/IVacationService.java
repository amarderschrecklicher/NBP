package ba.unsa.etf.employeemanagement.service.api;

import ba.unsa.etf.employeemanagement.dto.request.VacationRequest;
import ba.unsa.etf.employeemanagement.dto.response.VacationResponse;

import ba.unsa.etf.employeemanagement.util.enums.VacationStatus;

public interface IVacationService extends IBaseService<VacationRequest, VacationResponse, Long> {
	VacationResponse requestVacation(VacationRequest request);
	VacationResponse approveVacation(Long id, Long approverId);
	VacationResponse rejectVacation(Long id, Long approverId, String reason);
	VacationStatus getVacationStatus(Long id);
}

