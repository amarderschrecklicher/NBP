package ba.unsa.etf.employeemanagement.service.impl;

import ba.unsa.etf.employeemanagement.dto.request.HolidayRequest;
import ba.unsa.etf.employeemanagement.dto.response.HolidayResponse;
import ba.unsa.etf.employeemanagement.exceptions.ResourceNotFoundException;
import ba.unsa.etf.employeemanagement.mapper.HolidayMapper;
import ba.unsa.etf.employeemanagement.model.Holiday;
import ba.unsa.etf.employeemanagement.repository.HolidayRepository;
import ba.unsa.etf.employeemanagement.service.api.IHolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HolidayService implements IHolidayService {
    private final HolidayRepository repository;
    private final HolidayMapper mapper;

    @Override
    public List<HolidayResponse> findAll() {
        return repository.findAll().stream().map(mapper::mapToResponse).toList();
    }

    @Override
    public HolidayResponse findById(Long id) {
        Holiday entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Holiday not found"));
        return mapper.mapToResponse(entity);
    }

    @Override
    public HolidayResponse save(HolidayRequest request) {
        Holiday entity = mapper.mapToEntity(request);
        Long id = repository.save(entity);
        return mapper.mapToResponse(repository.findById(id).orElseThrow());
    }

    @Override
    public HolidayResponse update(Long id, HolidayRequest request) {
        if(repository.findById(id).isEmpty()) throw new ResourceNotFoundException("Holiday not found");
        Holiday entity = mapper.mapToEntity(request);
        entity.setId(id);
        repository.update(id, entity);
        return mapper.mapToResponse(repository.findById(id).orElseThrow());
    }

    @Override
    public void delete(Long id) {
        if(repository.findById(id).isEmpty()) throw new ResourceNotFoundException("Holiday not found");
        repository.deleteById(id);
    }
}

