package ru.staygo.hotelservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.staygo.hotelservice.entity.Room;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {
  @Query("SELECT room FROM Room room WHERE room.available = true ORDER BY room.timesBooked ASC, room.id ASC")
  List<Room> findAvailableRoomsRecommended();
  List<Room> findByAvailableTrue();
}
