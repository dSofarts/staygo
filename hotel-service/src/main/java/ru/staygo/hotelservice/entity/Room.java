package ru.staygo.hotelservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "room")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  @ManyToOne
  @JoinColumn(name = "hotel_id", nullable = false)
  private Hotel hotel;
  @Column(nullable = false)
  private String number;
  @Column(nullable = false)
  private Boolean available = true;
  @Column(nullable = false)
  private Integer timesBooked = 0;
  @Version
  private Long version;
}
