package tn.isetsf.presence.Entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true) // Indique d'inclure uniquement les champs explicitement marqués
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Ens {

    @Id
    private String matEnseignant;

    private String nomEnseignant;
    private String depEnseignant;
    private String email_enseignant;
    private String tel_enseignant;

    // Correction de la relation
    @OneToMany(mappedBy = "enseignant", fetch = FetchType.LAZY) // mappedBy doit correspondre au nom de la propriété dans LigneAbsence
    private List<LigneAbsence> lignesAbsences; // Changement en List pour représenter plusieurs lignes d'absence
}
