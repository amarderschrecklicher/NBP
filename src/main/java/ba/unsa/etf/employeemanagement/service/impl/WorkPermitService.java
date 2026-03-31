package ba.unsa.etf.employeemanagement.service.impl;

import ba.unsa.etf.employeemanagement.dto.request.WorkPermitRequest;
import ba.unsa.etf.employeemanagement.dto.response.WorkPermitResponse;
import ba.unsa.etf.employeemanagement.exceptions.ResourceNotFoundException;
import ba.unsa.etf.employeemanagement.mapper.WorkPermitMapper;
import ba.unsa.etf.employeemanagement.model.WorkPermit;
import ba.unsa.etf.employeemanagement.repository.WorkPermitRepository;
import ba.unsa.etf.employeemanagement.service.api.IWorkPermitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkPermitService implements IWorkPermitService {

    private final WorkPermitRepository workPermitRepository;
    private final WorkPermitMapper workPermitMapper;

    @Override
    public List<WorkPermitResponse> findAll() {
        return workPermitRepository.findAll()
                .stream()
                .map(workPermitMapper::mapToResponse)
                .toList();
    }

    @Override
    public WorkPermitResponse findById(Long id) {
        WorkPermit workPermit = workPermitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work permit not found with id: " + id));
        return workPermitMapper.mapToResponse(workPermit);
    }

    @Override
    public WorkPermitResponse save(WorkPermitRequest request) {
        WorkPermit workPermit = workPermitMapper.mapToEntity(request);
        Long id = workPermitRepository.save(workPermit);

        WorkPermit saved = workPermitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work permit not found after save"));

        return workPermitMapper.mapToResponse(saved);
    }

    @Override
    public WorkPermitResponse update(Long id, WorkPermitRequest request) {
        WorkPermit existing = workPermitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work permit not found with id: " + id));

        WorkPermit workPermit = workPermitMapper.mapToEntity(request);
        workPermit.setId(existing.getId());

        workPermitRepository.update(id, workPermit);

        WorkPermit updated = workPermitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work permit not found after update"));

        return workPermitMapper.mapToResponse(updated);
    }

    @Override
    public void delete(Long id) {
        workPermitRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Work permit not found with id: " + id));

        workPermitRepository.deleteById(id);
    }
}
