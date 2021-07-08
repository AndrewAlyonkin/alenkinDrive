package net.alenkin.alenkindrive.rest.v1;

import net.alenkin.alenkindrive.model.User;
import net.alenkin.alenkindrive.repository.UserRepository;
import net.alenkin.alenkindrive.security.AuthDTO;
import net.alenkin.alenkindrive.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alenkin Andrew
 * oxqq@ya.ru
 */
@RestController
@RequestMapping("/v1/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private UserRepository userRepository;

    private JwtTokenProvider tokenProvider;

    public AuthenticationController(AuthenticationManager authenticationManager, UserRepository userRepository, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody AuthDTO credentials) {
        try {
            String name = credentials.getName();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(credentials.getName(), credentials.getPassword()));
            User user = userRepository.findByName(credentials.getName());
            if (user == null) {
                throw new RuntimeException("Invalid credentials");
            }
            String token = tokenProvider.createToken(user.getName(), user.getRole().name());
            Map<Object, Object> response = new HashMap<>();
            response.put("name", credentials.getName());
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (Exception exception) {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler handler = new SecurityContextLogoutHandler();
        handler.logout(request, response, null);
    }


}
