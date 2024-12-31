package tn.isetsf.presence.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import tn.isetsf.presence.Entity.Emploi;
import tn.isetsf.presence.Entity.Salle;

import java.util.List;

public interface SalleRepo extends JpaRepository<Salle,Integer> {
    @Query("SELECT s FROM Salle s WHERE  s.nomdepsalle=:nomdep")
    List<Salle> getdept(String nomdep);
    @Query("SELECT COUNT(s) FROM Salle s WHERE  s.nomdepsalle=:nomdep")
    Integer getNbreSalleParDep(String nomdep);


}
