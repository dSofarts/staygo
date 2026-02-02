package ru.staygo.bookingservice.mapper;

import ru.staygo.bookingservice.api.dto.BookingDto;
import ru.staygo.bookingservice.api.dto.UserDto;
import ru.staygo.bookingservice.entity.Booking;
import ru.staygo.bookingservice.entity.User;

public class Mapper {
  public static UserDto toUserDto(User user) {
    return new UserDto(user.getId(), user.getUsername(), user.getPassword(), user.getRole());
  }

  public static BookingDto toBookingDto(Booking booking) {
    return new BookingDto(booking.getId(), booking.getUser().getId(), booking.getUser().getUsername(),
      booking.getRoomId(), booking.getStartDate(), booking.getEndDate(), booking.getStatus(),
      booking.getCreatedAt());
  }
}
