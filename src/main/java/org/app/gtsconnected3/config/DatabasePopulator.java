package org.app.gtsconnected3.config;

import org.app.gtsconnected3.entity.Role;
import org.app.gtsconnected3.repository.RoleRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabasePopulator implements CommandLineRunner {
    private final RoleRepo roleRepository;

    public DatabasePopulator(RoleRepo roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Role role1 = new Role("ROLE_USER");
        Role role2 = new Role("ROLE_ADMIN");

        if (roleRepository.findAll().isEmpty()) {
            roleRepository.save(role1);
            roleRepository.save(role2);
        }
    }
}

