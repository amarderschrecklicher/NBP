package ba.unsa.etf.employeemanagement.service.impl;

import ba.unsa.etf.employeemanagement.dto.request.FamilyMemberRequest;
import ba.unsa.etf.employeemanagement.dto.response.FamilyMemberResponse;
import ba.unsa.etf.employeemanagement.exceptions.ResourceNotFoundException;
import ba.unsa.etf.employeemanagement.mapper.FamilyMemberMapper;
import ba.unsa.etf.employeemanagement.model.FamilyMember;
import ba.unsa.etf.employeemanagement.repository.FamilyMemberRepository;
import ba.unsa.etf.employeemanagement.service.api.IFamilyMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FamilyMemberService implements IFamilyMemberService {

    private final FamilyMemberRepository familyMemberRepository;
    private final FamilyMemberMapper familyMemberMapper;

    @Override
    public List<FamilyMemberResponse> findAll() {
        return familyMemberRepository.findAll()
                .stream()
                .map(familyMemberMapper::mapToResponse)
                .toList();
    }

    @Override
    public FamilyMemberResponse findById(Long id) {
        FamilyMember familyMember = familyMemberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Family member not found with id: " + id));
        return familyMemberMapper.mapToResponse(familyMember);
    }

    @Override
    public FamilyMemberResponse save(FamilyMemberRequest request) {
        FamilyMember familyMember = familyMemberMapper.mapToEntity(request);
        Long generatedId = familyMemberRepository.save(familyMember);

        FamilyMember saved = familyMemberRepository.findById(generatedId)
                .orElseThrow(() -> new ResourceNotFoundException("Family member not found after save"));

        return familyMemberMapper.mapToResponse(saved);
    }

    @Override
    public FamilyMemberResponse update(Long id, FamilyMemberRequest request) {
        FamilyMember existing = familyMemberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Family member not found with id: " + id));

        FamilyMember familyMember = familyMemberMapper.mapToEntity(request);
        familyMember.setId(existing.getId());

        familyMemberRepository.update(id, familyMember);

        FamilyMember updated = familyMemberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Family member not found after update"));

        return familyMemberMapper.mapToResponse(updated);
    }

    @Override
    public void delete(Long id) {
        familyMemberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Family member not found with id: " + id));

        familyMemberRepository.deleteById(id);
    }
}