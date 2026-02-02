package ru.staygo.bookingservice.api.request;

public record AuthRequest(
  String username,
  String password
) {
}
