package ba.unsa.etf.employeemanagement.service.api;

import ba.unsa.etf.employeemanagement.controller.auth.LoginRequest;
import ba.unsa.etf.employeemanagement.controller.auth.JwtResponse;

public interface IAuthService {
    JwtResponse authenticate(LoginRequest loginRequest);
    JwtResponse refreshToken(String refreshToken);
    void logout(String refreshToken);
}
