package com.transport.app.rest.domain;

import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Audited
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PREFERENCES")
public class Preferences {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "TERMS_AND_CONDITIONS", columnDefinition = "LONGTEXT")
//    @Column(name = "TERMS_AND_CONDITIONS")
//    @Lob
    private String termsAndConditions;
}
