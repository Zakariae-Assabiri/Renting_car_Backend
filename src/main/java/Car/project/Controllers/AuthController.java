package Car.project.Controllers;

import Car.project.Security.JwtTokenProvider;
import Car.project.Services.UserService;
import Car.project.Entities.User;
import Car.project.dto.LoginRequest;
import Car.project.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // Route pour s'enregistrer
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        User newUser = userService.register(registerRequest);
        return ResponseEntity.ok(newUser);
    }

    // Route pour se connecter
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        User user = userService.authenticate(loginRequest);
        String jwtToken = jwtTokenProvider.generateToken(user);
        return ResponseEntity.ok(jwtToken);
    }
}
