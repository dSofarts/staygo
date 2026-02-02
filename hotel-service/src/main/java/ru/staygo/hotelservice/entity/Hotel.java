package ru.staygo.hotelservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "hotel")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private String address;
  @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
  private List<Room> rooms = new ArrayList<>();
}
