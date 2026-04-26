package ba.unsa.etf.employeemanagement.config;

import ba.unsa.etf.employeemanagement.model.nbp.NbpUser;
import ba.unsa.etf.employeemanagement.repository.nbp.NbpUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AdminSetup implements CommandLineRunner {

    private final NbpUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Optional<NbpUser> adminOpt = userRepository.findByUsername("ems_admin");
        if (adminOpt.isPresent()) {
            NbpUser admin = adminOpt.get();
            admin.setPassword(passwordEncoder.encode("admin123"));
            userRepository.update(admin.getId(), admin);
            System.out.println("=========================================================");
            System.out.println("Password for 'ems_admin' was programmatically reset to 'admin123' using the application's PasswordEncoder.");
            System.out.println("=========================================================");
        }
    }
}

