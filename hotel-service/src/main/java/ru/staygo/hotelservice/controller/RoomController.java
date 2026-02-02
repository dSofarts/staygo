package ru.staygo.hotelservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.staygo.hotelservice.api.dto.RoomDto;
import ru.staygo.hotelservice.api.request.ConfirmAvailabilityRequest;
import ru.staygo.hotelservice.api.response.ItemsResponse;
import ru.staygo.hotelservice.service.RoomService;

import java.util.UUID;

import static ru.staygo.hotelservice.constant.Headers.REQUEST_ID;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {
  private final RoomService roomService;

  @GetMapping
  public ItemsResponse<RoomDto> getAvailableRooms() {
    return roomService.getAvailableRooms();
  }

  @GetMapping("/recommend")
  public ItemsResponse<RoomDto> getRecommendedRooms() {
    return roomService.getRecommendedRooms();
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  @ResponseStatus(HttpStatus.CREATED)
  public RoomDto createRoom(
    @RequestBody RoomDto dto
  ) {
    return roomService.createRoom(dto);
  }

  @PostMapping("/{id}/confirm-availability")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void confirmAvailability(
    @PathVariable UUID roomId,
    @RequestHeader(REQUEST_ID) UUID requestId
  ) {
    roomService.confirmAvailability(roomId, requestId);
  }

  @PostMapping("/{id}/release")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void releaseRoom(
    @PathVariable UUID roomId,
    @RequestHeader(REQUEST_ID) UUID requestId
  ) {
    roomService.releaseRoom(roomId, requestId);
  }
}
