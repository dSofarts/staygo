package ru.staygo.bookingservice.service;

import ru.staygo.bookingservice.api.dto.BookingDto;
import ru.staygo.bookingservice.api.request.CreateBookingRequest;
import ru.staygo.bookingservice.api.response.ItemsResponse;
import ru.staygo.bookingservice.entity.Booking;

import java.util.UUID;

public interface BookingService {
  BookingDto createBooking(String username, CreateBookingRequest request);
  void confirmWithHotelService(Booking booking);
  ItemsResponse<BookingDto> getUserBookings(String username);
  BookingDto getBooking(UUID id, String username);
  void cancelBooking(UUID id, String username);
}
