package tn.isetsf.presence.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor@NoArgsConstructor
public class Salle {
    @Id
    private int salle1;
    private String nom_salle;
    private  String nomdepsalle;
    private String seanceDouble;
    private String nomSeance;
}
