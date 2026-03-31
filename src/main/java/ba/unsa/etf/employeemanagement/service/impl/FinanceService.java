package ba.unsa.etf.employeemanagement.service.impl;

import ba.unsa.etf.employeemanagement.dto.request.FinanceRequest;
import ba.unsa.etf.employeemanagement.dto.response.FinanceResponse;
import ba.unsa.etf.employeemanagement.exceptions.ResourceNotFoundException;
import ba.unsa.etf.employeemanagement.mapper.FinanceMapper;
import ba.unsa.etf.employeemanagement.model.Finance;
import ba.unsa.etf.employeemanagement.repository.FinanceRepository;
import ba.unsa.etf.employeemanagement.service.api.IFinanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FinanceService implements IFinanceService {

    private final FinanceRepository financeRepository;
    private final FinanceMapper financeMapper;

    @Override
    public List<FinanceResponse> findAll() {
        return financeRepository.findAll()
                .stream()
                .map(financeMapper::mapToResponse)
                .toList();
    }

    @Override
    public FinanceResponse findById(Long id) {
        Finance finance = financeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Finance not found with id: " + id));
        return financeMapper.mapToResponse(finance);
    }

    @Override
    public FinanceResponse save(FinanceRequest request) {
        Finance finance = financeMapper.mapToEntity(request);
        Long generatedId = financeRepository.save(finance);

        Finance saved = financeRepository.findById(generatedId)
                .orElseThrow(() -> new ResourceNotFoundException("Finance not found after save"));

        return financeMapper.mapToResponse(saved);
    }

    @Override
    public FinanceResponse update(Long id, FinanceRequest request) {
        Finance existing = financeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Finance not found with id: " + id));

        Finance finance = financeMapper.mapToEntity(request);
        finance.setId(existing.getId());

        financeRepository.update(id, finance);

        Finance updated = financeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Finance not found after update"));

        return financeMapper.mapToResponse(updated);
    }

    @Override
    public void delete(Long id) {
        financeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Finance not found with id: " + id));

        financeRepository.deleteById(id);
    }
}
