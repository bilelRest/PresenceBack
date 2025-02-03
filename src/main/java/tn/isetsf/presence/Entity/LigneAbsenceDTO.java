package tn.isetsf.presence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
@Data@AllArgsConstructor@NoArgsConstructor
public class LigneAbsenceDTO {
    private String nomEnseignant;
    private String nomdepfiliere;
    private String nom_seance;
    private String date;
    private String nom_matiere;
    private String semestre1;
    private String nom_salle;

    public LigneAbsenceDTO(LigneAbsence ligne) {
        this.nomEnseignant = (ligne.getEnseignant() != null) ? ligne.getEnseignant().getNomEnseignant() : null;
        this.nomdepfiliere = ligne.getNomdepfiliere();
        this.nom_seance = ligne.getNom_seance();
        this.date = String.valueOf(ligne.getDate());
        this.nom_matiere = ligne.getNom_matiere();
        this.semestre1 = ligne.getSemestre1();
        this.nom_salle=ligne.getNom_salle();
    }
}

