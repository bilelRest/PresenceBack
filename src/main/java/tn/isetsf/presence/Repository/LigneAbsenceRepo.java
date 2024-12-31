package tn.isetsf.presence.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import tn.isetsf.presence.Entity.LigneAbsence;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface LigneAbsenceRepo extends JpaRepository<LigneAbsence, Long> {

    @Query("SELECT l FROM LigneAbsence l WHERE l.date = :date AND l.nom_salle = :salle AND l.seanceDouble = :seance AND l.enseignant.matEnseignant = :enseignant") // Correction ici
    List<LigneAbsence> trouverAbsence(LocalDate date, String salle, String seance, String enseignant);


    @Query("SELECT la FROM LigneAbsence la WHERE la.enseignant.nomEnseignant LIKE %:keyword% AND la.nomdepfiliere LIKE %:dep% AND la.date>= :date1 AND la.date<= :date2 AND la.seanceDouble LIKE %:cren%")
    Page<LigneAbsence> findByEnseignantNomEnseignantContaining(@Param("keyword") String keyword,@Param("dep")String dep, @Param("date1") LocalDate date1,@Param("date2") LocalDate date2,@Param("cren")String cren,Pageable pageable);


    @Query("SELECT la FROM LigneAbsence la WHERE la.nomdepfiliere LIKE %:keyword%")
    Page<LigneAbsence> findByDepartementContaining(@Param("keyword") String keyword, Pageable pageable);


    @Query(value = "SELECT e.mat_enseignant, COUNT(*) AS nombre_absences " +
            "FROM ligne_absence la " +
            "JOIN ens e ON la.mat_enseignant = e.mat_enseignant " +
            "GROUP BY e.mat_enseignant " +
            "ORDER BY nombre_absences DESC;", nativeQuery = true)
    List<Object[]> countAbsencesByEnseignantNative();

    List<LigneAbsence> findByNotifiedFalse();


    @Query("SELECT l FROM LigneAbsence l WHERE l.date>= :date1 AND l.date<= :date2")
    Page<LigneAbsence> findByDate(@Param("date1") LocalDate date1,@Param("date2") LocalDate date2,Pageable pageable);


    @Query("SELECT l FROM LigneAbsence l")
    Page<LigneAbsence> findTous(Pageable pageable);
}
