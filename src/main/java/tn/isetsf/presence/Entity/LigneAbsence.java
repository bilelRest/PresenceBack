package tn.isetsf.presence.Entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true) // Indique d'inclure uniquement les champs explicitement marqués
@AllArgsConstructor
@NoArgsConstructor
public class LigneAbsence {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ligne_absence_seq")
    @SequenceGenerator(name = "ligne_absence_seq", sequenceName = "ligne_absence_seq", allocationSize = 1)
    private Long num;

    private String nom_salle;
    private String nom_matiere;
    private String nom_seance;
    private String seanceDouble;
    private String annee1;
    private String semestre1;
    private String nom_jour;
    private LocalDate date;
    private Boolean notified;
    private String nomdepfiliere;

    @ManyToOne
    @JoinColumn(name = "mat_enseignant", referencedColumnName = "matEnseignant")
    private Ens enseignant; // Renommé pour plus de clarté
}
