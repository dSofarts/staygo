package ru.staygo.hotelservice.api.request;

import java.time.LocalDate;

public record ConfirmAvailabilityRequest(
  LocalDate startDate,
  LocalDate endDate
) {
}
