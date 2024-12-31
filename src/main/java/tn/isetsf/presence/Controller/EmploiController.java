package tn.isetsf.presence.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import tn.isetsf.presence.CalculDate;
import tn.isetsf.presence.Entity.Emploi;
import tn.isetsf.presence.Entity.Ens;
import tn.isetsf.presence.Entity.LigneAbsence;
import tn.isetsf.presence.Repository.EmploiRepo;
import tn.isetsf.presence.Repository.EnstRepo;
import tn.isetsf.presence.Repository.LigneAbsenceRepo;
import tn.isetsf.presence.serviceMail.EmailController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
public class EmploiController {
    @Autowired
    EnstRepo enstRepo;
    @Autowired
    LigneAbsenceRepo ligneAbsenceRepo;
    @Autowired
    EmploiRepo emploiRepo;

    @Autowired
    private  EmailController emailController;
    CalculDate calculDate=new CalculDate();

    @GetMapping(value = "/emploi/all",produces = MediaType.APPLICATION_JSON_VALUE)
    public ArrayList<Emploi> getAllEmploi(){
        return (ArrayList<Emploi>) emploiRepo.findAll();
    }

    @PostMapping(value = "/ens",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Ens> getEnseignant(){
         return enstRepo.findAll();

           }




    @GetMapping(value = "/emploi/creneau")
    public Boolean getEnsi(@RequestParam String salle) {
        List<Emploi> emploi = emploiRepo.trouverCreneau("1", "1", salle, "1", "6");





        LigneAbsence ligneAbsence = new LigneAbsence();
        if (!emploi.isEmpty()) {
System.out.println("emploi trouvé"+emploi.get(0).toString());
System.out.println("salle = "+emploi.get(0).getNom_salle()+" seance =  "+emploi.get(0).getSeance1()+" enseigant =  "+ emploi.get(0).getEnsi1());
            if (!ligneAbsenceRepo.trouverAbsence( LocalDate.now(),emploi.get(0).getNom_salle(),emploi.get(0).getSeance1(), emploi.get(0).getEnsi1()).isEmpty()) {
                System.out.println("Ligne absence  déja existante");
                return false;
            } else {
                System.out.println("Nouvelle Ligne absence");



        Boolean notified = false;

           Optional<Ens> ens = enstRepo.findById(emploi.get(0).getEnsi1());

           String msg = "Mr " + ens.get().getNomEnseignant() + "On vous informe que vous etes  absent le : " + LocalDate.now() + " à la salle : " + salle + " seances de :" + emploi.get(0).getNom_seance();


                for (Emploi emploi1 : emploi) {
                    ligneAbsence.setNomdepfiliere(emploi1.getNomdepfiliere());
                    ligneAbsence.setEnseignant(ens.get());
                    ligneAbsence.setNom_jour(emploi1.getNom_jour());
                    ligneAbsence.setAnnee1(emploi1.getAnnee1());
                    ligneAbsence.setSemestre1(emploi1.getSemestre1());
                    ligneAbsence.setNom_salle(emploi1.getNom_salle());
                    ligneAbsence.setNom_matiere(emploi1.getNom_matiere());
                    ligneAbsence.setNom_seance(emploi1.getNom_seance());
                    ligneAbsence.setSeanceDouble(emploi1.getSeance1());
                    ligneAbsence.setDate(LocalDate.now());

                }
                try {

                    ligneAbsence.setNotified(emailController.sendEmail(ens.get().getEmail_enseignant(), "Service Scolarité ISET SFAX : Notification d'absence", msg));

                } catch (Exception ignored) {


                }
                ligneAbsence.setNotified(notified);
                ligneAbsenceRepo.save(ligneAbsence);
                System.out.println("ligne absence ajouté" + ligneAbsence.toString());

                return true;
            }} else {
            System.out.println("Aucun creneau trouver !!");
                return false;
            }

    }
    @GetMapping(value = "/lbs",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<LigneAbsence> getLigneAbsnce(@RequestParam String salle, @RequestParam String seance, @RequestParam String ens){
        return ligneAbsenceRepo.trouverAbsence(LocalDate.now(),seance,salle,ens);
    }
    public void session(){

    }
    @PutMapping(value = ("/ens/update"),consumes = MediaType.APPLICATION_JSON_VALUE)
    public Boolean updateUser(@RequestParam Long mat, @RequestBody Ens ens){
        Optional<Ens> ens1 =enstRepo.findById(String.valueOf(mat));
        if(ens1.isPresent()){
            ens1.get().setEmail_enseignant(ens.getEmail_enseignant());
            ens1.get().setTel_enseignant(ens.getTel_enseignant());
            enstRepo.save(ens1.get());
            return true;
        }else {
            return false;
        }
    }

}
