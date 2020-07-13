package com.transport.app.rest.repository;

import com.transport.app.rest.domain.DatabaseFile;
import com.transport.app.rest.domain.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;
import java.util.List;

@Repository
public interface DatabaseFileRepository extends JpaRepository<DatabaseFile, Long> {
//    List<DatabaseFile> findAll(Specification<DatabaseFile> spec);
    @Query("SELECT files.id from DatabaseFile files WHERE fileType LIKE %:fileType%")
    List<Long> getAllFileUriWithFileType(@Param("fileType") String fileType);
    @Query("SELECT files.id from DatabaseFile files WHERE fileType LIKE %:fileType% AND orderId = :orderId AND location = :location")
    List<Long> getByOrderIdAndLocation(String fileType, Long orderId, String location);
}
