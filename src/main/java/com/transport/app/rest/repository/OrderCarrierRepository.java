package com.transport.app.rest.repository;

import com.transport.app.rest.domain.OrderCarrier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderCarrierRepository extends JpaRepository<OrderCarrier, Long> {
}
