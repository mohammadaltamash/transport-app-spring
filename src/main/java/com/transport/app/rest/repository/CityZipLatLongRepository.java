package com.transport.app.rest.repository;

import com.transport.app.rest.domain.CityZipLatLong;
import com.transport.app.rest.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityZipLatLongRepository extends JpaRepository<CityZipLatLong, Long> {

//    @Query("select * from CityZipLatLong where zip like ?1 or city like ?1")
//    List<CityZipLatLong> findAllByOrderStatusIn(String text);

    List<CityZipLatLong> findByZipContainsOrCityContainsIgnoreCase(String zip, String city);
}
