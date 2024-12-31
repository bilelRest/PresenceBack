package tn.isetsf.presence.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LigneEmploi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_semaine;

    private Date date;



    @ManyToOne
    @JoinColumn(name = "salle_id")
    private Salle salle1;



    private String nomSalle;
    private String matiere1;
    private String nomMatiere;
    private String abv;
    private String niveau1;
    private String nomNiveau;
    private String nomEnsi;
    private String seance1;
    private String numSeance;
    private int annee1;
    private int semestre1;
    private int par15;
    private String jour1;
    private String nomJour;
    private String cours;
    private String module;
    private int nbHeure1;
    private int coefC;
    private int coefTP;
    private int coefTD;
    private int nomF;
    private String nomDepSalle;
    private String nomDepFiliere;
    private String nomDepEnseignant;

}

