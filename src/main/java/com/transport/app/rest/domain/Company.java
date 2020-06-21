package com.transport.app.rest.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "COMPANY")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "CONTACT_NAME")
    private String contactName;
    @Column(name = "COMPANY_NAME")
    private String companyName;
    @Column(name = "ADDRESS")
    private String address;
    @Column(name = "ADDRESS_STATE")
    private String addressState;
    @Column(name = "ZIP")
    private String zip;
    @Column(name = "LATITUDE")
    private Double latitude;
    @Column(name = "LONGITUDE")
    private Double longitude;
    @Column(name = "PHONES")
    @ElementCollection
    private Map<String, String> phones;
    @Column(name = "COMPANY_EMAIL")
    private String companyEmail;

    @ManyToOne
    @JoinColumn(name = "CREATED_BY_ID")
    @NotAudited
    private User createdBy;
    @Column(name = "CREATED_BY_NAME")
    private String createdByName; // For search
    @Column(name = "CREATED_AT")
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(name = "UPDATED_AT")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
