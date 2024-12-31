package tn.isetsf.presence.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.isetsf.presence.EmploiProjection;
import tn.isetsf.presence.Entity.Emploi;

import java.util.ArrayList;
import java.util.List;

public interface EmploiRepo extends JpaRepository<Emploi,String> {
    @Query("SELECT e FROM Emploi e WHERE e.semestre1= :sem AND e.jour1 = :jrs AND e.nom_salle = :salle1 AND (e.seance1= :seance OR e.seance1= :seance2)")
    List<Emploi> trouverCreneau(String sem,String jrs, String salle1, String seance, String seance2);



    @Query("SELECT e FROM Emploi e WHERE  e.annee1= :anne AND e.semestre1 = :sem AND e.jour1 = :jrs AND (e.seance1 = :seance OR e.seance1 = :seance2)")
    List<Emploi> trouverCren(String anne,String sem,String jrs, String seance, String seance2);


    @Query("SELECT e.ensi1,e.nom_ensi,e.nomdepEnseignant FROM Emploi e")
    ArrayList<Object> trouverEns();


}

