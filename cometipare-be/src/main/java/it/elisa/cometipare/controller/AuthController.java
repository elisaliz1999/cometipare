package it.elisa.cometipare.controller;


import it.elisa.cometipare.configurations.JwtUtils;
import it.elisa.cometipare.models.D_Users;
import it.elisa.cometipare.models.JwtResponse;
import it.elisa.cometipare.models.UserDetailsModel;
import it.elisa.cometipare.repositories.D_RoleRepository;
import it.elisa.cometipare.repositories.D_UserRepository;
import it.elisa.cometipare.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cometipare/")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private D_UserRepository userRepository;

    @Autowired
    private D_RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    public AuthController() {
    }

    /**
     * Controller method for login
     *
     * @param loginRequest
     * @return
     */
    @PostMapping("login")
    public ResponseEntity<?> authenticateUser(@RequestBody D_Users loginRequest) {
        try {
            System.out.println(loginRequest.toString());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            UserDetailsModel userDetails = (UserDetailsModel) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new JwtResponse(jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    /**
     * Controller method for registration
     *
     * @param appUser
     * @return
     */
    @PostMapping("registrazione")
    public ResponseEntity<?> RegistreUser(@RequestBody D_Users appUser) {
        try {
            /*System.out.println("Registrazione......");
            D_AppUser user = userRepository.findByUsername(appUser.getUsername());
            if (user == null) {
                appUser.setPassword(encoder.encode(appUser.getPassword()));
                appUser.getRoles().add(new D_Role(ERole.USER));*/
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(userService.registerUser(appUser));
            /*} else {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new Exception("Utente esiste già..."));
            }*/
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }

    @GetMapping("info")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok(new D_Users());
    }
}

