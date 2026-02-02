package ru.staygo.bookingservice.api.dto;

import java.util.UUID;

public record RoomDto(
  UUID id,
  UUID hotelId,
  String number,
  Boolean available,
  Integer timesBooked
) {
}
