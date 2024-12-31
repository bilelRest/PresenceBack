package tn.isetsf.presence.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import tn.isetsf.presence.CalculDate;
import tn.isetsf.presence.Entity.Emploi;
import tn.isetsf.presence.Entity.Salle;
import tn.isetsf.presence.Repository.EmploiRepo;
import tn.isetsf.presence.Repository.SalleRepo;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
public class SalleController {
    @Autowired
    SalleRepo salleRepo;
    @Autowired
    EmploiRepo emploiRepo;
    CalculDate calculDate=new CalculDate();

    @PostMapping(value = "/salle/pointage", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Salle> getSalle() {
        List<Salle> salleList = new ArrayList<>();
        List<Emploi> emplois = emploiRepo.trouverCren(String.valueOf(calculDate.getYear()), String.valueOf(calculDate.getSemestre()),String.valueOf(calculDate.indexJour()),"1","7");
System.out.println("ANNEE : "+calculDate.getYear()+" Semestre : " +   calculDate.getSemestre()+" jour : "+calculDate.indexJour()+" Seance simple : "+calculDate.getSeance()+" seance double : "+calculDate.getSeanceDouble());
        System.out.println("requete recue pour salle");
        System.out.println(emplois.toString());
        for (Emploi emploi : emplois) {
            Salle salle = new Salle();
            salle.setNom_salle(emploi.getNom_salle());
            salle.setSalle1(Integer.parseInt(emploi.getSalle1()));
            salle.setNomdepsalle(emploi.getNomdepsalle());
            salle.setSeanceDouble(emploi.getSeance1());
            salle.setNomSeance(emploi.getNom_seance());
            salleList.add(salle);
        }
        return salleList;
    }
    @PostMapping(value = "/salle", produces = MediaType.APPLICATION_JSON_VALUE)
public List<Salle> getAll(){
        return salleRepo.findAll();
    }
}
