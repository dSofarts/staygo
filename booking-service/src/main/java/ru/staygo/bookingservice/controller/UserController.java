package ru.staygo.bookingservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.staygo.bookingservice.api.dto.UserDto;
import ru.staygo.bookingservice.api.request.AuthRequest;
import ru.staygo.bookingservice.api.response.AuthResponse;
import ru.staygo.bookingservice.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public AuthResponse register(@RequestBody AuthRequest request) {
    return userService.register(request);
  }

  @PostMapping("/login")
  public AuthResponse login(@RequestBody AuthRequest request) {
    return userService.login(request);
  }

  @PatchMapping
  @PreAuthorize("hasRole('ADMIN')")
  public UserDto updateUser(@RequestBody UserDto dto) {
    return userService.update(dto.id(), dto);
  }

  @DeleteMapping
  @PreAuthorize("hasRole('ADMIN')")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUser(@RequestParam UUID userId) {
    userService.delete(userId);
  }
}
