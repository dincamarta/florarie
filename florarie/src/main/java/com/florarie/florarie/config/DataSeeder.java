/** Clasa pentru popularea initiala a bazei de date cu date de test
 * @author Dinca (Mateas) Marta
 * @version 05 Ianuarie 2026
 */
package com.florarie.florarie.config;

import com.florarie.florarie.model.AppUser;
import com.florarie.florarie.model.Role;
import com.florarie.florarie.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedAdmin(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            String adminEmail = "admin@florarie.ro";
            String adminPass = "admin";

            if (!userRepository.existsByEmail(adminEmail)) {
                AppUser admin = new AppUser(adminEmail, encoder.encode(adminPass), Role.ADMIN);
                userRepository.save(admin);
            }
        };
    }
}
