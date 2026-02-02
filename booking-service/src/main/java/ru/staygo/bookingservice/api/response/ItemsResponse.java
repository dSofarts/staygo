package ru.staygo.bookingservice.api.response;

import java.util.List;

public record ItemsResponse<T> (
  List<T> items
) {}
