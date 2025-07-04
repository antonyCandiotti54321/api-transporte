package com.antonycandiotti.api_transporte.auth;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor  //obligatorio todos los contrusctores con argumentos
public class AuthController {

    private final AuthService authService;

//   @PostMapping(value="auth/login")
   @PostMapping(value="auth/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request){
        AuthResponse response = authService.login(request);
        System.out.println("Login realizado: usuario=" + request.getUsername());
        System.out.println(response);
        return ResponseEntity.ok(response);
    }

//   @PostMapping(value="admins/register")
   @PostMapping(value="auth/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request){
        AuthResponse response = authService.register(request);
        System.out.println("Registro realizado: usuario=" + request.getUsername());
        return ResponseEntity.ok(response);
    }

}
