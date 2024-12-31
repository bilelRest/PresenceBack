package tn.isetsf.presence.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import javax.persistence.GeneratedValue;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor@NoArgsConstructor
public class Pointage {
    @Id
    @GeneratedValue
    private int idPointage;
    private int salle;
    private int seance;
    private boolean present;
    private String date;

}
