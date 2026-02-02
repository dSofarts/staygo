package ru.staygo.bookingservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;

import ru.staygo.bookingservice.api.dto.UserDto;
import ru.staygo.bookingservice.api.request.AuthRequest;
import ru.staygo.bookingservice.api.response.AuthResponse;
import ru.staygo.bookingservice.entity.User;
import ru.staygo.bookingservice.entity.enums.Role;
import ru.staygo.bookingservice.mapper.Mapper;
import ru.staygo.bookingservice.repository.UserRepository;
import ru.staygo.bookingservice.security.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import ru.staygo.bookingservice.service.impl.UserServiceImpl;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock
  private JwtService jwtService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  private UserServiceImpl userService;

  @BeforeEach
  void setUp() {
    userService = new UserServiceImpl(jwtService, userRepository, passwordEncoder);
  }

  @Test
  void register_success() {
    AuthRequest req = new AuthRequest("alice", "rawPass");
    when(userRepository.existsByUsername("alice")).thenReturn(false);
    when(passwordEncoder.encode("rawPass")).thenReturn("encoded");
    User savedUser = User.builder()
      .id(UUID.randomUUID())
      .username("alice")
      .password("encoded")
      .role(Role.USER)
      .build();
    when(userRepository.save(any(User.class))).thenReturn(savedUser);
    when(jwtService.generateToken(savedUser)).thenReturn("token123");

    AuthResponse resp = userService.register(req);

    assertNotNull(resp);
    assertEquals("token123", resp.token());
    assertEquals("alice", resp.username());
    assertEquals(Role.USER, resp.role());
    verify(userRepository).existsByUsername("alice");
    verify(passwordEncoder).encode("rawPass");
    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).save(captor.capture());
    User toSave = captor.getValue();
    assertEquals("alice", toSave.getUsername());
    assertEquals("encoded", toSave.getPassword());
    verify(jwtService).generateToken(savedUser);
  }

  @Test
  void register_existingUsername_throws() {
    when(userRepository.existsByUsername("bob")).thenReturn(true);
    AuthRequest req = new AuthRequest("bob", "p");
    assertThrows(IllegalArgumentException.class, () -> userService.register(req));
    verify(userRepository).existsByUsername("bob");
    verifyNoMoreInteractions(passwordEncoder, userRepository, jwtService);
  }

  @Test
  void login_success() {
    AuthRequest req = new AuthRequest("carol", "raw");
    User user = User.builder()
      .id(UUID.randomUUID())
      .username("carol")
      .password("encoded")
      .role(Role.USER)
      .build();
    when(userRepository.findByUsername("carol")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("raw", "encoded")).thenReturn(true);
    when(jwtService.generateToken(user)).thenReturn("tk");

    AuthResponse resp = userService.login(req);

    assertNotNull(resp);
    assertEquals("tk", resp.token());
    assertEquals("carol", resp.username());
    assertEquals(Role.USER, resp.role());
    verify(userRepository).findByUsername("carol");
    verify(passwordEncoder).matches("raw", "encoded");
    verify(jwtService).generateToken(user);
  }

  @Test
  void login_userNotFound_throws() {
    when(userRepository.findByUsername("nope")).thenReturn(Optional.empty());
    AuthRequest req = new AuthRequest("nope", "x");
    assertThrows(SecurityException.class, () -> userService.login(req));
    verify(userRepository).findByUsername("nope");
  }

  @Test
  void login_wrongPassword_throws() {
    User user = User.builder()
      .id(UUID.randomUUID())
      .username("dave")
      .password("enc")
      .role(Role.USER)
      .build();
    when(userRepository.findByUsername("dave")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches("wrong", "enc")).thenReturn(false);
    AuthRequest req = new AuthRequest("dave", "wrong");
    assertThrows(SecurityException.class, () -> userService.login(req));
    verify(passwordEncoder).matches("wrong", "enc");
  }

  @Test
  void update_success() {
    UUID id = UUID.randomUUID();
    User existing = User.builder()
      .id(id)
      .username("old")
      .password("oldEnc")
      .role(Role.USER)
      .build();
    when(userRepository.findById(id)).thenReturn(Optional.of(existing));

    UserDto dto = new UserDto(id, "newName", "newRaw", Role.ADMIN);
    when(passwordEncoder.encode("newRaw")).thenReturn("newEnc");

    User saved = User.builder()
      .id(id)
      .username("newName")
      .password("newEnc")
      .role(Role.ADMIN)
      .build();
    when(userRepository.save(any(User.class))).thenReturn(saved);

    UserDto expectedDto = new UserDto(id, "newName", null, Role.ADMIN); // Mapper may drop password
    try (MockedStatic<Mapper> mapperMock = Mockito.mockStatic(Mapper.class)) {
      mapperMock.when(() -> Mapper.toUserDto(Mockito.any(User.class))).thenReturn(expectedDto);

      UserDto result = userService.update(id, dto);

      assertNotNull(result);
      assertEquals(expectedDto.username(), result.username());
      assertEquals(expectedDto.role(), result.role());
      verify(userRepository).findById(id);
      verify(passwordEncoder).encode("newRaw");
      verify(userRepository).save(any(User.class));
      mapperMock.verify(() -> Mapper.toUserDto(Mockito.any(User.class)));
    }
  }

  @Test
  void update_userNotFound_throws() {
    UUID id = UUID.randomUUID();
    when(userRepository.findById(id)).thenReturn(Optional.empty());
    UserDto dto = new UserDto(id, "x", "p", Role.USER);
    assertThrows(IllegalArgumentException.class, () -> userService.update(id, dto));
    verify(userRepository).findById(id);
  }

  @Test
  void delete_callsRepository() {
    UUID id = UUID.randomUUID();
    doNothing().when(userRepository).deleteById(id);
    userService.delete(id);
    verify(userRepository).deleteById(id);
  }
}
