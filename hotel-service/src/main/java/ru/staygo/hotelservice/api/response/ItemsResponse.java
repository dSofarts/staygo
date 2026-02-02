package ru.staygo.hotelservice.api.response;

import java.util.List;

public record ItemsResponse<T> (
  List<T> items
) {}
