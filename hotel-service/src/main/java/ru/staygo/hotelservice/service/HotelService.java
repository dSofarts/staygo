package ru.staygo.hotelservice.service;

import ru.staygo.hotelservice.api.dto.HotelDto;
import ru.staygo.hotelservice.api.response.ItemsResponse;

public interface HotelService {
  ItemsResponse<HotelDto> getAllHotels();
  HotelDto createHotel(HotelDto dto);
}
