package ru.staygo.bookingservice.service;

import ru.staygo.bookingservice.api.dto.UserDto;
import ru.staygo.bookingservice.api.request.AuthRequest;
import ru.staygo.bookingservice.api.response.AuthResponse;

import java.util.UUID;

public interface UserService {
  AuthResponse register(AuthRequest request);
  AuthResponse login(AuthRequest request);
  UserDto update(UUID userId, UserDto dto);
  void delete(UUID userId);
}
