package com.transport.app.rest.domain;

import com.transport.app.rest.mapper.OrderDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class PagedOrders {
    long totalItems;
    List<Order> orders;
}
