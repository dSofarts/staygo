package ru.staygo.hotelservice.service;

import ru.staygo.hotelservice.api.dto.RoomDto;
import ru.staygo.hotelservice.api.request.ConfirmAvailabilityRequest;
import ru.staygo.hotelservice.api.response.ItemsResponse;

import java.util.UUID;

public interface RoomService {
  RoomDto createRoom(RoomDto dto);
  ItemsResponse<RoomDto> getAvailableRooms();
  ItemsResponse<RoomDto> getRecommendedRooms();
  void confirmAvailability(UUID roomId, UUID requestId);
  void releaseRoom(UUID roomId, UUID requestId);
}
