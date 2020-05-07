package com.transport.app.rest.mapper;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.transport.app.rest.domain.Order;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class PagedOrdersDto {
    long totalItems;
    List<OrderDto> orders;
}
