package it.elisa.cometipare.repositories;

import it.elisa.cometipare.models.D_Role;
import it.elisa.cometipare.models.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface D_RoleRepository extends JpaRepository<D_Role, Long> {

    D_Role findByName(ERole name);
}

