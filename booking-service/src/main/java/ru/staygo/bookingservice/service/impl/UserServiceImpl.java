package ru.staygo.bookingservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.staygo.bookingservice.api.dto.UserDto;
import ru.staygo.bookingservice.api.request.AuthRequest;
import ru.staygo.bookingservice.api.response.AuthResponse;
import ru.staygo.bookingservice.entity.User;
import ru.staygo.bookingservice.entity.enums.Role;
import ru.staygo.bookingservice.mapper.Mapper;
import ru.staygo.bookingservice.repository.UserRepository;
import ru.staygo.bookingservice.security.JwtService;
import ru.staygo.bookingservice.service.UserService;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final JwtService jwtService;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public AuthResponse register(AuthRequest request) {
    if (userRepository.existsByUsername(request.username())) {
      throw new IllegalArgumentException("Username already exists");
    }

    User user = userRepository.save(User.builder()
      .username(request.username())
      .password(passwordEncoder.encode(request.password()))
      .role(Role.USER)
      .build());
    String token = jwtService.generateToken(user);
    log.info("Successfully register user with id {}", user.getId());
    return new AuthResponse(token, user.getUsername(), user.getRole());
  }

  @Override
  @Transactional(readOnly = true)
  public AuthResponse login(AuthRequest request) {
    User user = userRepository.findByUsername(request.username())
      .orElseThrow(() -> new SecurityException("Invalid credentials"));

    if (!passwordEncoder.matches(request.password(), user.getPassword())) {
      throw new SecurityException("Invalid credentials");
    }

    String token = jwtService.generateToken(user);
    log.info("User authenticated: {}", user.getId());
    return new AuthResponse(token, user.getUsername(), user.getRole());
  }

  @Override
  @Transactional
  public UserDto update(UUID userId, UserDto dto) {
    User user = userRepository.findById(userId)
      .orElseThrow(() -> new IllegalArgumentException("User not found"));

    if (dto.username() != null) {
      user.setUsername(dto.username());
    }
    if (dto.password() != null) {
      user.setPassword(passwordEncoder.encode(dto.password()));
    }
    if (dto.role() != null) {
      user.setRole(dto.role());
    }

    User saved = userRepository.save(user);
    log.info("User updated: {}", saved.getUsername());
    return Mapper.toUserDto(user);
  }

  @Override
  @Transactional
  public void delete(UUID userId) {
    userRepository.deleteById(userId);
    log.info("Successfully deleted user with id: {}", userId);
  }
}
