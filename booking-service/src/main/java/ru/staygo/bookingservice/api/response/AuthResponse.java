package ru.staygo.bookingservice.api.response;

import ru.staygo.bookingservice.entity.enums.Role;

public record AuthResponse(
  String token,
  String username,
  Role role
) {
}
