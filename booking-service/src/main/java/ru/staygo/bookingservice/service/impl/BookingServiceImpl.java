package ru.staygo.bookingservice.service.impl;

import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.staygo.bookingservice.api.dto.BookingDto;
import ru.staygo.bookingservice.api.dto.RoomDto;
import ru.staygo.bookingservice.api.request.CreateBookingRequest;
import ru.staygo.bookingservice.api.response.ItemsResponse;
import ru.staygo.bookingservice.client.HotelClient;
import ru.staygo.bookingservice.entity.Booking;
import ru.staygo.bookingservice.entity.User;
import ru.staygo.bookingservice.entity.enums.BookingStatus;
import ru.staygo.bookingservice.mapper.Mapper;
import ru.staygo.bookingservice.repository.BookingRepository;
import ru.staygo.bookingservice.repository.UserRepository;
import ru.staygo.bookingservice.service.BookingService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
  private final BookingRepository bookingRepository;
  private final UserRepository userRepository;
  private final HotelClient hotelClient;

  @Override
  @Transactional
  public BookingDto createBooking(String username, CreateBookingRequest request) {
    User user = userRepository.findByUsername(username)
      .orElseThrow(() -> new IllegalArgumentException("User not found"));

    if (request.startDate().isAfter(request.endDate())) {
      throw new IllegalArgumentException("Start date must be before end date");
    }

    UUID requestId = UUID.randomUUID();

    UUID roomId = request.roomId();
    if (Boolean.TRUE.equals(request.autoChoosing())) {
      roomId = chooseRoomAuto();
    }

    if (roomId == null) {
      throw new IllegalArgumentException("No available rooms");
    }

    Booking booking = new Booking();
    booking.setUser(user);
    booking.setRoomId(roomId);
    booking.setStartDate(request.startDate());
    booking.setEndDate(request.endDate());
    booking.setStatus(BookingStatus.PENDING);
    booking.setCreatedAt(LocalDateTime.now());
    booking.setRequestId(requestId);

    Booking saved = bookingRepository.save(booking);
    log.info("Booking created with status PENDING: bookingId={}, requestId={}", saved.getId(), requestId);

    try {
      confirmWithHotelService(saved);
      saved.setStatus(BookingStatus.CONFIRMED);
      bookingRepository.save(saved);
      log.info("Booking confirmed: bookingId={}", saved.getId());
    } catch (Exception e) {
      log.error("Failed to confirm booking: bookingId={}, error={}", saved.getId(), e.getMessage());
      saved.setStatus(BookingStatus.CANCELLED);
      bookingRepository.save(saved);
      compensate(saved);
      throw new IllegalArgumentException("Booking failed: " + e.getMessage());
    }

    return Mapper.toBookingDto(saved);
  }

  @Override
  @Retryable(
    retryFor = {FeignException.class, Exception.class},
    maxAttempts = 3,
    backoff = @Backoff(delay = 500, multiplier = 2)
  )
  public void confirmWithHotelService(Booking booking) {
    log.info("Attempting to confirm availability with Hotel Service: bookingId={}, attempt", booking.getId());
    hotelClient.confirmAvailability(booking.getRoomId(), booking.getRequestId());
  }

  @Override
  @Transactional(readOnly = true)
  public ItemsResponse<BookingDto> getUserBookings(String username) {
    User user = userRepository.findByUsername(username)
      .orElseThrow(() -> new IllegalArgumentException("User not found"));

    return new ItemsResponse<>(bookingRepository.findByUserOrderByCreatedAtDesc(user).stream()
      .map(Mapper::toBookingDto)
      .collect(Collectors.toList()));
  }

  @Override
  @Transactional(readOnly = true)
  public BookingDto getBooking(UUID id, String username) {
    Booking booking = bookingRepository.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

    if (!booking.getUser().getUsername().equals(username)) {
      throw new IllegalArgumentException("Access denied");
    }

    return Mapper.toBookingDto(booking);
  }

  @Override
  @Transactional
  public void cancelBooking(UUID id, String username) {
    Booking booking = bookingRepository.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

    if (!booking.getUser().getUsername().equals(username)) {
      throw new IllegalArgumentException("Access denied");
    }

    if (booking.getStatus() != BookingStatus.CONFIRMED) {
      throw new IllegalArgumentException("Only confirmed bookings can be cancelled");
    }

    booking.setStatus(BookingStatus.CANCELLED);
    bookingRepository.save(booking);

    compensate(booking);
    log.info("Booking cancelled: bookingId={}", id);
  }

  private void compensate(Booking booking) {
    try {
      log.info("Executing compensation: bookingId={}, requestId={}", booking.getId(), booking.getRequestId());
      hotelClient.releaseRoom(booking.getRoomId(), booking.getRequestId());
      log.info("Compensation successful: bookingId={}", booking.getId());
    } catch (Exception e) {
      log.error("Compensation failed: bookingId={}, error={}", booking.getId(), e.getMessage());
    }
  }

  private UUID chooseRoomAuto() {
    List<RoomDto> rooms = hotelClient.getRecommendedRooms().items();
    if (rooms.isEmpty()) {
      return null;
    }
    return rooms.getFirst().id();
  }
}
