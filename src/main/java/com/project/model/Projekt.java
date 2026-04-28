package com.project.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("projekt")
public class Projekt {
    @Id
    @Column("projekt_id")
    private Integer projektId;

    private String nazwa;
    private String opis;

    @Column("data_oddania")
    private LocalDate dataOddania;

    @CreatedDate
    @Column("dataczas_utworzenia")
    private LocalDateTime dataczasUtworzenia;

    @LastModifiedDate
    @Column("dataczas_modyfikacji")
    private LocalDateTime dataczasModyfikacji;
}
