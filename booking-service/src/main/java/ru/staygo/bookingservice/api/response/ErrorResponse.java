package ru.staygo.bookingservice.api.response;

import java.time.LocalDateTime;

public record ErrorResponse (
  int status,
  String message,
  LocalDateTime timestamp
) {}
