package ru.staygo.bookingservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.staygo.bookingservice.entity.Booking;
import ru.staygo.bookingservice.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
  List<Booking> findByUserOrderByCreatedAtDesc(User user);
  Optional<Booking> findByRequestId(UUID requestId);
}
