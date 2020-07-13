package com.transport.app.rest.domain;
import lombok.*;
import org.hibernate.type.descriptor.sql.LobTypeMappings;
import org.springframework.web.bind.annotation.PathVariable;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "DATABASE_FILE")
public class DatabaseFile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fileName;
    private String fileType;
    @Lob
    private byte[] data;

    Long orderId;
    String location;

    public DatabaseFile(String fileName, String fileType, byte[] data, Long orderId, String location) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.data = data;
        this.orderId = orderId;
        this.location = location;
    }
}
