package com.gabriel.sales.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabriel.sales.dtos.LoginRecordDto;
import com.gabriel.sales.dtos.LoginResponseDto;
import com.gabriel.sales.dtos.RegisterRecordDto;
import com.gabriel.sales.models.UserModel;
import com.gabriel.sales.repositories.UserRepository;
import com.gabriel.sales.services.TokenService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/auth", produces = { "application/json" })
@Tag(name =  "Auth Routes")
public class AuthenticationController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TokenService tokenService;

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRecordDto body) {
    var usernamePassword = new UsernamePasswordAuthenticationToken(body.login(), body.password());
    var auth = this.authenticationManager.authenticate(usernamePassword);

    var token = tokenService.generateToken((UserModel) auth.getPrincipal());

    return ResponseEntity.ok().body(new LoginResponseDto(token));
  }

  @PostMapping("/register")
  public ResponseEntity register(@RequestBody @Valid RegisterRecordDto body) {
      
    if (this.userRepository.findByLogin(body.login()) != null) {
      return ResponseEntity.badRequest().build();
    } 

    String encryptedPassword = new BCryptPasswordEncoder().encode(body.password());
    UserModel user = new UserModel(body.login(), encryptedPassword, body.role());

    this.userRepository.save(user);

    return ResponseEntity.ok().build();
  }
}
