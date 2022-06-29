package it.elisa.cometipare.services;

import it.elisa.cometipare.configurations.JwtUtils;
import it.elisa.cometipare.models.D_Role;
import it.elisa.cometipare.models.D_Users;
import it.elisa.cometipare.models.ERole;
import it.elisa.cometipare.models.UserDetailsModel;
import it.elisa.cometipare.repositories.D_RoleRepository;
import it.elisa.cometipare.repositories.D_UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;


@Service
public class UserService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private D_UserRepository userRepository;

    @Autowired
    private D_RoleRepository roleRepository;

    @Autowired
    private JwtUtils jwtUtils;

    private PasswordEncoder encoder;

    public UserService() {
        this.encoder = new BCryptPasswordEncoder();
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            D_Users user = userRepository.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("User Not Found with username: " + username);
            }
            System.out.println(user);
            return UserDetailsModel.build(user);
        } catch (UsernameNotFoundException e) {
            throw e;
        }
    }

    /**
     * @return
     * @throws UsernameNotFoundException
     */
    public D_Users loadUser() throws UsernameNotFoundException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsModel userDetails = (UserDetailsModel) authentication.getPrincipal();
            String username = ((UserDetailsModel) authentication.getPrincipal()).getUsername();
            return userRepository.findByUsername(username);
        } catch (UsernameNotFoundException e) {
            throw e;
        }
    }

    public D_Users registerUser(D_Users user) throws Exception {
        try {
            if (userRepository.existsByUsername(user.getUsername())) {
                throw new Exception("Error: Username is already taken!");
            }

            if (userRepository.existsByEmail(user.getEmail())) {
                throw new Exception("Error: Email is already in use!");
            }

            // Create new user's account
            D_Users userNew = new D_Users(user.getUsername(),
                    user.getEmail(),
                    encoder.encode(user.getPassword()));

            Set<D_Role> roles = new HashSet<>();
            D_Role userRole = new D_Role(ERole.USER);
            roles.add(userRole);
            user.setRoles(roles);
            return userRepository.save(userNew);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    /**
     * @param roles
     * @return
     */
    private Set<String> extractRoles(Set<D_Role> roles) {

        if (roles != null) {
            Set<String> rolesName = new HashSet<>();
            for (D_Role ruolo : roles) {
                rolesName.add(ruolo.getName().toString());
            }
            return rolesName;
        }
        return null;
    }
}


