package ru.staygo.hotelservice.mapper;

import ru.staygo.hotelservice.api.dto.HotelDto;
import ru.staygo.hotelservice.api.dto.RoomDto;
import ru.staygo.hotelservice.entity.Hotel;
import ru.staygo.hotelservice.entity.Room;

public class Mapper {
  public static HotelDto toHotelDto(Hotel hotel) {
    return new HotelDto(hotel.getId(), hotel.getName(), hotel.getAddress());
  }

  public static RoomDto toRoomDto(Room room) {
    return new RoomDto(room.getId(), room.getHotel().getId(), room.getNumber(), room.getAvailable(), room.getTimesBooked());
  }
}
