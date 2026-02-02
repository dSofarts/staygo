package ru.staygo.bookingservice.api.dto;

import ru.staygo.bookingservice.entity.enums.BookingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record BookingDto(
  UUID id,
  UUID userId,
  String username,
  UUID roomId,
  LocalDate startDate,
  LocalDate endDate,
  BookingStatus status,
  LocalDateTime createdAt
) {
}
