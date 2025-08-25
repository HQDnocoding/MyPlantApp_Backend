package com.dat.plantbackend.enities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "treatment")
public class Treatment {
    @Id
    @ColumnDefault("nextval('treatment_id_seq')")
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "doseperacre", nullable = false, length = 100)
    private String doseperacre;

    @NotNull
    @Column(name = "instruction", nullable = false, length = Integer.MAX_VALUE)
    private String instruction;

    @Size(max = 500)
    @NotNull
    @Column(name = "disease_description", nullable = false, length = Integer.MAX_VALUE)
    private String diseaseDescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "disease_id")
    private Disease disease;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "pesticide_id")
    private Pesticide pesticide;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "plant_id")
    private Plant plant;

}