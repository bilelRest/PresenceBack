package tn.isetsf.presence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;


import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@Immutable
@AllArgsConstructor
@NoArgsConstructor
public class Emploi {

        @Id
        private String num;
        private String salle1;
        private String nom_salle;
        private String matiere1;
        private String nom_matiere;
        private String abv;
        private String niveau1;
        private String Nom_niveau;
        private String ensi1;
        private String nom_ensi;
        private String seance1;
        private String nom_seance;
        private String annee1;
        private String semestre1;
        private String par15;
        private String jour1;
        private String nom_jour;
        private String cours;
        private String module;
        private String nb_heure1;
        private String coefC;
        private String coefTP;
        private String coefTD;
        private String nom_f;
        private String nomdepsalle;
        private String nomdepfiliere;
        private String nomdepEnseignant;

    }


