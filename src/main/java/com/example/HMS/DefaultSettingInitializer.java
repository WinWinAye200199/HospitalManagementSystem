package com.example.HMS;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.HMS.model.entities.Role;
import com.example.HMS.model.entities.User;
import com.example.HMS.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DefaultSettingInitializer implements CommandLineRunner {

	private final PasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

		@Override
		public void run(String... args) throws Exception {
			
				User user = userRepository.findByName("admin").orElse(null);
				if (user == null) {
					user = new User();
					user.setName("admin");
					user.setEmail("Admin123@gmail.com");
					user.setRole(Role.ADMIN);

					String encodedPassword = passwordEncoder.encode("Admin@123");
					user.setPassword(encodedPassword);

					userRepository.save(user);
				}
			
		}
}

