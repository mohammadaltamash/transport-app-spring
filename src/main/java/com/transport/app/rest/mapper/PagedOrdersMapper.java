package com.transport.app.rest.mapper;

import com.transport.app.rest.domain.PagedOrders;

public class PagedOrdersMapper {

    public static PagedOrdersDto toPagedOrdersDto(PagedOrders pagedOrders) {
        return PagedOrdersDto.builder()
                .totalItems(pagedOrders.getTotalItems())
                .orders(OrderMapper.toOrderDtos(pagedOrders.getOrders()))
                .build();
    }
}
