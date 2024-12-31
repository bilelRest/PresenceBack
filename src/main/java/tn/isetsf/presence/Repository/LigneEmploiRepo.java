package tn.isetsf.presence.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.isetsf.presence.Entity.Emploi;
import tn.isetsf.presence.Entity.LigneEmploi;

import java.util.List;

public interface LigneEmploiRepo extends JpaRepository<LigneEmploi,Long> {
    @Query("SELECT e FROM Emploi e WHERE e.jour1 = :jrs AND e.seance1= :seance AND e.nomdepsalle= :dept")
    List<Emploi> chargerDept(String jrs,  String seance,  String dept);
}