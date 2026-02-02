package ru.staygo.bookingservice.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.staygo.bookingservice.entity.enums.Role;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  @Column(nullable = false, unique = true)
  private String username;
  @Column(nullable = false)
  private String password;
  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Role role;
}
