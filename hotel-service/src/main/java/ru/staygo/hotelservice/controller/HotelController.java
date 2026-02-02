package ru.staygo.hotelservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.staygo.hotelservice.api.dto.HotelDto;
import ru.staygo.hotelservice.api.response.ItemsResponse;
import ru.staygo.hotelservice.service.HotelService;

@RestController
@RequestMapping("/api/hotels")
@RequiredArgsConstructor
public class HotelController {
  private final HotelService hotelService;

  @GetMapping
  public ItemsResponse<HotelDto> getAllHotels() {
    return hotelService.getAllHotels();
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  @ResponseStatus(HttpStatus.CREATED)
  public HotelDto createHotel(@RequestBody HotelDto hotelDTO) {
    return hotelService.createHotel(hotelDTO);
  }
}
