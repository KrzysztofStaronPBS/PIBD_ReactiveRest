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

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("zadanie")
public class Zadanie {

    @Id
    @Column("zadanie_id")
    private Integer zadanieId;

    private String nazwa;
    private String opis;
    private Integer kolejnosc;

    @CreatedDate
    @Column("dataczas_utworzenia")
    private LocalDateTime dataczasUtworzenia;

    @LastModifiedDate
    @Column("dataczas_modyfikacji")
    private LocalDateTime dataczasModyfikacji;

    @Column("projekt_id")
    private Integer projektId;
}
