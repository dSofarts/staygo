package ru.staygo.hotelservice.api.dto;

import java.util.UUID;

public record HotelDto(
  UUID id,
  String name,
  String address
) {
}
