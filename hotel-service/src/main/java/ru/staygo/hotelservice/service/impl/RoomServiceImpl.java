package ru.staygo.hotelservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.staygo.hotelservice.api.dto.RoomDto;
import ru.staygo.hotelservice.api.request.ConfirmAvailabilityRequest;
import ru.staygo.hotelservice.api.response.ItemsResponse;
import ru.staygo.hotelservice.entity.Hotel;
import ru.staygo.hotelservice.entity.Room;
import ru.staygo.hotelservice.mapper.Mapper;
import ru.staygo.hotelservice.repository.HotelRepository;
import ru.staygo.hotelservice.repository.RoomRepository;
import ru.staygo.hotelservice.service.RoomService;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
  private final RoomRepository roomRepository;
  private final HotelRepository hotelRepository;
  private final Map<UUID, Boolean> processedRequests = new ConcurrentHashMap<>();

  @Override
  @Transactional
  public RoomDto createRoom(RoomDto dto) {
    Hotel hotel = hotelRepository.findById(dto.hotelId())
      .orElseThrow(() -> new IllegalArgumentException("Hotel with id: " + dto.hotelId() + " is not found"));

    Room room = new Room();
    room.setHotel(hotel);
    room.setNumber(dto.number());
    room.setAvailable(true);
    room.setTimesBooked(0);

    Room saved = roomRepository.save(room);
    log.info("Created room: {} in hotel: {}", saved.getId(), hotel.getId());
    return Mapper.toRoomDto(saved);
  }

  @Override
  @Transactional(readOnly = true)
  public ItemsResponse<RoomDto> getAvailableRooms() {
    return new ItemsResponse<>(
      roomRepository.findByAvailableTrue().stream()
        .map(Mapper::toRoomDto)
        .collect(Collectors.toList()));
  }

  @Override
  @Transactional(readOnly = true)
  public ItemsResponse<RoomDto> getRecommendedRooms() {
    return new ItemsResponse<>(
      roomRepository.findAvailableRoomsRecommended().stream()
        .map(Mapper::toRoomDto)
        .collect(Collectors.toList()));
  }

  @Override
  @Transactional
  public void confirmAvailability(UUID roomId, UUID requestId) {
    if (processedRequests.containsKey(requestId)) {
      log.info("Request {} already processed (idempotency)", requestId);
      return;
    }

    Room room = roomRepository.findById(roomId)
      .orElseThrow(() -> new IllegalArgumentException("Room not found"));

    if (!room.getAvailable()) {
      throw new IllegalArgumentException("Room is not available");
    }

    room.setTimesBooked(room.getTimesBooked() + 1);
    roomRepository.save(room);

    processedRequests.put(requestId, true);
    log.info("Confirmed availability for room {} (requestId: {})", roomId, requestId);
  }

  @Override
  @Transactional
  public void releaseRoom(UUID roomId, UUID requestId) {
    if (!processedRequests.containsKey(requestId)) {
      log.info("Request {} not found, nothing to release", requestId);
      return;
    }

    Room room = roomRepository.findById(roomId)
      .orElseThrow(() -> new IllegalArgumentException("Room not found"));

    if (room.getTimesBooked() > 0) {
      room.setTimesBooked(room.getTimesBooked() - 1);
      roomRepository.save(room);
    }

    processedRequests.remove(requestId);
    log.info("Released room {} (requestId: {})", roomId, requestId);
  }
}
