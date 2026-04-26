package ba.unsa.etf.employeemanagement.service.impl.auth;

import ba.unsa.etf.employeemanagement.controller.auth.LoginRequest;
import ba.unsa.etf.employeemanagement.util.enums.EmsRole;
import ba.unsa.etf.employeemanagement.model.nbp.NbpRole;
import ba.unsa.etf.employeemanagement.model.nbp.NbpUser;
import ba.unsa.etf.employeemanagement.model.auth.RefreshToken;
import ba.unsa.etf.employeemanagement.repository.nbp.NbpRoleRepository;
import ba.unsa.etf.employeemanagement.repository.nbp.NbpUserRepository;
import ba.unsa.etf.employeemanagement.repository.auth.RefreshTokenRepository;
import ba.unsa.etf.employeemanagement.service.JwtProvider;
import ba.unsa.etf.employeemanagement.service.api.IAuthService;
import ba.unsa.etf.employeemanagement.controller.auth.JwtResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final NbpUserRepository userRepository;
    private final NbpRoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    public JwtResponse authenticate(LoginRequest loginRequest) {
        Optional<NbpUser> userOpt = userRepository.findByUsername(loginRequest.getUsername());

        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid credentials");
        }

        NbpUser user = userOpt.get();
        Optional<String> dbPasswordOpt = userRepository.findPasswordById(user.getId());

        if (dbPasswordOpt.isEmpty() || !passwordEncoder.matches(loginRequest.getPassword(), dbPasswordOpt.get())) {
            throw new RuntimeException("Invalid credentials");
        }

        String roleName = EmsRole.EMS_EMPLOYEE.name();
        if (user.getRoleId() != null) {
            Optional<NbpRole> roleOpt = roleRepository.findById(user.getRoleId());
            if (roleOpt.isPresent()) {
                roleName = roleOpt.get().getName();
            }
        }

        String accessToken = jwtProvider.generateToken(user.getUsername(), roleName);

        // Remove old tokens
        refreshTokenRepository.deleteByUserId(user.getId());

        // Create new refresh token
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUserId(user.getId());
        refreshToken.setExpiryDate(new Date(System.currentTimeMillis() + 604800000)); // 7 days

        refreshTokenRepository.save(refreshToken);

        return new JwtResponse(accessToken, refreshToken.getToken());
    }

    @Override
    public JwtResponse refreshToken(String reqRefreshToken) {
        Optional<RefreshToken> tokenOpt = refreshTokenRepository.findByToken(reqRefreshToken);
        if (tokenOpt.isEmpty()) {
            throw new RuntimeException("Invalid refresh token");
        }

        RefreshToken rToken = tokenOpt.get();
        if (rToken.getExpiryDate().before(new Date())) {
            refreshTokenRepository.deleteByUserId(rToken.getUserId());
            throw new RuntimeException("Refresh token was expired. Please make a new signin request");
        }

        Optional<NbpUser> userOpt = userRepository.findById(rToken.getUserId());

        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        NbpUser user = userOpt.get();

        String roleName = EmsRole.EMS_EMPLOYEE.name();
        if (user.getRoleId() != null) {
            Optional<NbpRole> roleOpt = roleRepository.findById(user.getRoleId());
            if (roleOpt.isPresent()) {
                roleName = roleOpt.get().getName();
            }
        }

        String newAccessToken = jwtProvider.generateToken(user.getUsername(), roleName);

        return new JwtResponse(newAccessToken, reqRefreshToken);
    }

    @Override
    public void logout(String refreshToken) {
        Optional<RefreshToken> tokenOpt = refreshTokenRepository.findByToken(refreshToken);
        tokenOpt.ifPresent(token -> refreshTokenRepository.deleteByUserId(token.getUserId()));
    }
}
