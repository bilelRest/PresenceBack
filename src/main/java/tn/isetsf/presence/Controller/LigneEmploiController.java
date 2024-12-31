package tn.isetsf.presence.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import tn.isetsf.presence.CalculDate;
import tn.isetsf.presence.Entity.Emploi;
import tn.isetsf.presence.Entity.LigneAbsence;
import tn.isetsf.presence.Entity.LigneEmploi;
import tn.isetsf.presence.Repository.EmploiRepo;
import tn.isetsf.presence.Repository.LigneAbsenceRepo;
import tn.isetsf.presence.Repository.LigneEmploiRepo;

import java.util.*;

@RestController
@CrossOrigin("*")
public class LigneEmploiController {
    @Autowired
    LigneAbsenceRepo absenceRepoRepo;
    @PostMapping(value = "/absence",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<LigneAbsence> getAbscece(){
        return absenceRepoRepo.findAll();
    }
    @DeleteMapping(value = "/absence/delete")
    public Boolean deleteAbsence(){
        try {
            absenceRepoRepo.deleteAll();
            return true;

        }catch (Exception e){
            return false;
        }
    }

}
