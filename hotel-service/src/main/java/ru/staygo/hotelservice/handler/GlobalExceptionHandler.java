package ru.staygo.hotelservice.handler;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.staygo.hotelservice.api.response.ErrorResponse;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
    return new ErrorResponse(
      HttpStatus.BAD_REQUEST.value(),
      ex.getMessage(),
      LocalDateTime.now()
    );
  }

  @ExceptionHandler(AccessDeniedException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ErrorResponse handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
    return new ErrorResponse(
      HttpStatus.FORBIDDEN.value(),
      "Access denied",
      LocalDateTime.now()
    );
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ErrorResponse handleException(Exception ex, HttpServletRequest request) {
    return new ErrorResponse(
      HttpStatus.INTERNAL_SERVER_ERROR.value(),
      ex.getMessage(),
      LocalDateTime.now()
    );
  }
}
