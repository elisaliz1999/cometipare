package it.elisa.cometipare.repositories;

import it.elisa.cometipare.models.D_Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public interface D_UserRepository extends JpaRepository<D_Users, Long> {

    D_Users findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
