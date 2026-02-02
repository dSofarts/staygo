package ru.staygo.bookingservice.api.request;

import java.time.LocalDate;
import java.util.UUID;

public record CreateBookingRequest(
  UUID roomId,
  LocalDate startDate,
  LocalDate endDate,
  Boolean autoChoosing
) {
}
