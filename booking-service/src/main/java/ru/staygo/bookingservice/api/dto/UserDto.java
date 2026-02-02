package ru.staygo.bookingservice.api.dto;

import ru.staygo.bookingservice.entity.enums.Role;

import java.util.UUID;

public record UserDto(
  UUID id,
  String username,
  String password,
  Role role
) {
}
