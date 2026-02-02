package ru.staygo.hotelservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.staygo.hotelservice.api.dto.HotelDto;
import ru.staygo.hotelservice.api.response.ItemsResponse;
import ru.staygo.hotelservice.entity.Hotel;
import ru.staygo.hotelservice.mapper.Mapper;
import ru.staygo.hotelservice.repository.HotelRepository;
import ru.staygo.hotelservice.service.HotelService;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
  private final HotelRepository hotelRepository;

  @Override
  @Transactional(readOnly = true)
  public ItemsResponse<HotelDto> getAllHotels() {
    return new ItemsResponse<>(hotelRepository.findAll().stream()
      .map(Mapper::toHotelDto)
      .collect(Collectors.toList()));
  }

  @Override
  @Transactional
  public HotelDto createHotel(HotelDto dto) {
    Hotel hotel = new Hotel();
    hotel.setName(dto.name());
    hotel.setAddress(dto.address());

    Hotel saved = hotelRepository.save(hotel);
    log.info("Created hotel: {}", saved.getId());
    return Mapper.toHotelDto(saved);
  }
}
