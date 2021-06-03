package ru.job4j.chat.controllers;

import com.auth0.jwt.JWT;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.job4j.chat.domain.Person;
import ru.job4j.chat.domain.Role;
import ru.job4j.chat.service.PersonService;
import ru.job4j.chat.service.RoleService;

import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

@RestController
public class SecurityController {
    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 864_000_000; /* 10 days */
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/sign-up";
    public static final String SIGN_IN_URL = "/sign-in";

    private final BCryptPasswordEncoder encoder;
    private final PersonService persons;
    private final RoleService roles;
    private final AuthenticationManager auth;

    public SecurityController(BCryptPasswordEncoder encoder,
                              PersonService persons,
                              RoleService roles,
                              AuthenticationManager auth) {
        this.encoder = encoder;
        this.persons = persons;
        this.roles = roles;
        this.auth = auth;
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody Person person) {
        person.setPassword(encoder.encode(person.getPassword()));
        person.setRole(roles.findByName("ROLE_USERS").orElse(new Role()));
        persons.save(person);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<Void> login(@RequestBody Person person) {
        try {
            Authentication authenticate = auth
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    person.getName(), person.getPassword()
                            )
                    );
            String token = JWT.create()
                    .withSubject(((User) authenticate.getPrincipal()).getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .sign(HMAC512(SECRET.getBytes()));
            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            TOKEN_PREFIX + token
                    ).build();
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
