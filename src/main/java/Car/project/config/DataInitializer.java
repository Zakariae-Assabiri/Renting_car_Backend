package Car.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import Car.project.Entities.Role;
import Car.project.Entities.RoleNom;
import Car.project.Repositories.RoleRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        for (RoleNom roleNom : RoleNom.values()) {
            if (!roleRepository.existsByNom(roleNom)) {
                Role role = new Role();
                role.setNom(roleNom);
                roleRepository.save(role);
                System.out.println("Rôle ajouté : " + roleNom);
            }
        }
    }
}
