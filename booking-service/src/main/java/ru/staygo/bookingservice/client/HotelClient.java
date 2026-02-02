package ru.staygo.bookingservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import ru.staygo.bookingservice.api.dto.RoomDto;
import ru.staygo.bookingservice.api.response.ItemsResponse;
import ru.staygo.bookingservice.config.FeignConfig;

import java.util.UUID;

import static ru.staygo.bookingservice.constant.Headers.REQUEST_ID;

@FeignClient(name = "hotel-service", configuration = FeignConfig.class)
public interface HotelClient {
  @GetMapping("/api/rooms/recommend")
  ItemsResponse<RoomDto> getRecommendedRooms();

  @PostMapping("/api/rooms/{id}/confirm-availability")
  void confirmAvailability(@PathVariable("id") UUID roomId, @RequestHeader(REQUEST_ID) UUID requestId);

  @PostMapping("/api/rooms/{id}/release")
  void releaseRoom(@PathVariable("id") UUID roomId, @RequestHeader(REQUEST_ID) UUID requestId);
}
