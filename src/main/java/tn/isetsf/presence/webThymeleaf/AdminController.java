package tn.isetsf.presence.webThymeleaf;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.isetsf.presence.CalculDate;
import tn.isetsf.presence.Entity.LigneAbsence;
import tn.isetsf.presence.Repository.EnstRepo;
import tn.isetsf.presence.Repository.LigneAbsenceRepo;
import tn.isetsf.presence.Repository.SalleRepo;
import tn.isetsf.presence.sec.entity.AppRole;
import tn.isetsf.presence.sec.entity.AppUser;
import tn.isetsf.presence.sec.entity.Logged;
import tn.isetsf.presence.sec.repository.AppRoleRepo;
import tn.isetsf.presence.sec.repository.AppUserRepo;
import tn.isetsf.presence.sec.repository.LoggedRepo;
import tn.isetsf.presence.sec.service.AppUserInterfaceImpl;
import tn.isetsf.presence.serviceMail.EmailService;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Controller
@Transactional
public class AdminController {
    @Autowired
    LoggedRepo loggedRepo;
    @Autowired
    AppRoleRepo appRoleRepo;
    @Autowired
    AppUserInterfaceImpl appUserInterface;



    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    private final LigneAbsenceRepo ligneAbsenceRepo;
    private final EnstRepo enstRepo;
    private final SalleRepo salleRepo;
    private final ServerStatusService serverStatusService;
    private final CalculDate calculDate = new CalculDate();
    private AppUserRepo appUserRepo;


    public AdminController(LigneAbsenceRepo ligneAbsenceRepo, EnstRepo enstRepo, SalleRepo salleRepo, ServerStatusService serverStatusService, AppUserRepo appUserRepo) {
        this.ligneAbsenceRepo = ligneAbsenceRepo;
        this.enstRepo = enstRepo;
        this.salleRepo = salleRepo;
        this.serverStatusService = serverStatusService;
        this.appUserRepo = appUserRepo;


    }


    @GetMapping(path = "/AbsenceEnseignant")
    public String indexModel(Model model,
                             @RequestParam(value = "page", defaultValue = "0") int page,
                             @RequestParam(value = "size", defaultValue = "5") int size,
                             @RequestParam(value = "keyword", defaultValue = "") String keyword,
                             @RequestParam(value = "dep", defaultValue = "") String dep,
                             @RequestParam(value = "date1", required = false) String date1Str,
                             @RequestParam(value = "date2", required = false) String date2Str,
                             @RequestParam(value = "keyword1",defaultValue = "", required = false)String keyword1,
                             @RequestParam(value = "cren",defaultValue = "")String cren) {

        LocalDate date1 = null;
        LocalDate date2 = null;

        try {
            if (date1Str != null && !date1Str.isEmpty()) {
                date1 = LocalDate.parse(date1Str);
            } else {
                date1 = LocalDate.now().minusMonths(1);
            }

            if (date2Str != null && !date2Str.isEmpty()) {
                date2 = LocalDate.parse(date2Str);
            } else {
                date2 = LocalDate.now();
            }
        } catch (DateTimeParseException e) {
            // Gestion d'une date mal formée (facultatif)
        }
        if(keyword.equals("")&&!keyword1.isEmpty()){
            keyword=keyword1;
        }

        Page<LigneAbsence> absencePage = ligneAbsenceRepo.findByEnseignantNomEnseignantContaining(
                keyword, dep, date1, date2, cren,PageRequest.of(page, size)
        );
            keyword1=keyword;
            keyword="";


        model.addAttribute("keyword1",keyword1);
        model.addAttribute("cren",cren);

        model.addAttribute("listAbsence", absencePage.getContent());
        model.addAttribute("pages", new int[absencePage.getTotalPages()]);
        model.addAttribute("absEns", enstRepo.findAll());
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pathCourant", "/AbsenceEnseignant");
        model.addAttribute("user", findLogged());
        model.addAttribute("isAdmin", isAdmin());
        model.addAttribute("date1", date1);
        model.addAttribute("date2", date2);
        model.addAttribute("dep", dep);

        return "AbsenceEnseignant"; // Vue à afficher
    }


    @GetMapping(path = "/Utilisateurs")
    public String userControl(Model model) {
        model.addAttribute("user", findLogged());
        model.addAttribute("isAdmin", isAdmin());


        List<AppUser> users = appUserRepo.findAll();
        System.out.println(users);
        model.addAttribute("users", users);
        return "Utilisateurs";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(LocalDate.class, new CustomDateEditor(dateFormat, true));
    }

    @PostMapping(path = "/SaveEssentielUser")
    public String SaveEssentielUser(Model model, @ModelAttribute("newUser") AppUser appUser) {
        String s = "Nom d'utilisateur existant";

        System.out.println(appUser);
        if (appUser.getUsername() != null) {
            AppUser appUser1 = appUserRepo.findByUsername(appUser.getUsername());
            if (appUser1 != null) {
                model.addAttribute("erreur", s);
                return "redirect:/AddUser?us=" + s;

            }
        }
        appUserInterface.AddUser(appUser);
        //appUserRepo.save(appUser);
        System.out.println("Utilisateur enregistré : " + appUser.getUsername());
        return "redirect:/AddUserDetail?us=" + appUser.getUsername();


    }

    @Autowired
    EmailService emailService;

    @GetMapping(path = "/AddUserRole")
    public String AddUserRole(Model model,
                              @RequestParam(value = "adminRole", defaultValue = "false") boolean adminRole,
                              @RequestParam(value = "key", defaultValue = "") String key,
                              @RequestParam(value = "err", defaultValue = "") String err,
                              @RequestParam(value = "us", defaultValue = "") String us,
                              @RequestParam(value = "del", defaultValue = "false") boolean delete) {
        model.addAttribute("delete", delete);
        model.addAttribute("user", findLogged());
        model.addAttribute("isAdmin", isAdmin());

        model.addAttribute("adminRole", adminRole);
        model.addAttribute("erreur", err.isEmpty() ? "" : "Rôle déjà accordé !");

        AppUser appUser = appUserRepo.findByUsername(us);
        model.addAttribute("us", us);
        model.addAttribute("newUser", appUser);
        model.addAttribute("roleCollection1", appUser.getRoleCollection());
        model.addAttribute("roleCollection", appRoleRepo.findAll());

        return "AddUserRole";
    }


    @PostMapping(path = "/AddRoleToUser")
    public String addRoleToUser(@RequestParam("us") String us,
                                @RequestParam(value = "key", defaultValue = "") String key,
                                @RequestParam("role") String role, HttpSession httpSession,
                                @RequestParam("delete") boolean delete) {
        if (delete) {
            // Redirection vers la suppression du rôle
            return deleteRoleToUser(us, key, role, httpSession);
        }

        if ("ADMIN".equals(role) && (key == null || key.isEmpty())) {
            String rand = UUID.randomUUID().toString();
            httpSession.setAttribute("keyValue", rand);
            System.out.println(rand);
            //   AppUser appUser=appUserRepo.findByUsername(findLogged().getUsername());

            AppUser loggedUser = appUserRepo.findByUsername(findLogged().getUsername());
            if (loggedUser.getRoleCollection().stream().anyMatch(r -> "ADMIN".equals(r.getRoleName()))) {
                emailService.sendSimpleEmail(loggedUser.getEmail(), "Code de vérification", rand);
                return "redirect:/AddUserRole?us=" + us + "&adminRole=true&del=false";
            } else {
                return "redirect:/deconnect";
            }

        }

        if ("ADMIN".equals(role) && !key.isEmpty()) {
            String storedKey = (String) httpSession.getAttribute("keyValue");
            if (storedKey != null && storedKey.equals(key)) {
                if (appUserInterface.AddRoleToUser(us, role)) {
                    httpSession.removeAttribute("keyValue");
                    return "redirect:/AddUserRole?us=" + us + "&del=false";
                } else {
                    return "redirect:/AddUserRole?us=" + us + "&err=Exist&del=false";
                }
            } else {
                return "redirect:/AddUserRole?us=" + us + "&adminRole=true&err=VerifyKey&del=false";
            }
        }

        if (appUserInterface.AddRoleToUser(us, role)) {
            return "redirect:/AddUserRole?us=" + us + "&del=false";
        } else {
            return "redirect:/AddUserRole?us=" + us + "&err=Exist&del=false";
        }
    }

    @PostMapping(path = "/deleteRoleToUser")
    public String deleteRoleToUser(@RequestParam("us") String us,
                                   @RequestParam(value = "key", defaultValue = "") String key,
                                   @RequestParam("role") String role, HttpSession httpSession) {

        // Vérification si le rôle est `ADMIN` et si `key` est vide
        if ("ADMIN".equals(role) && (key == null || key.isEmpty())) {
            String rand = UUID.randomUUID().toString();
            System.out.println("Code de vérification généré : " + rand);

            // Enregistrer le code dans la session pour vérification ultérieure
            httpSession.setAttribute("keyValue", rand);

            // Récupérer l'utilisateur actuellement connecté et envoyer l'email si le rôle `ADMIN` est présent
            AppUser loggedUser = appUserRepo.findByUsername(findLogged().getUsername());
            if (loggedUser.getRoleCollection().stream().anyMatch(r -> "ADMIN".equals(r.getRoleName()))) {
                emailService.sendSimpleEmail(loggedUser.getEmail(), "Code de vérification", rand);
                return "redirect:/AddUserRole?us=" + us + "&adminRole=true&del=true";
            } else {
                return "redirect:/deconnect";
            }
        }

        // Vérification si le rôle est `ADMIN` et si `key` est renseigné
        if ("ADMIN".equals(role) && !key.isEmpty()) {
            String storedKey = (String) httpSession.getAttribute("keyValue");

            if (storedKey != null && storedKey.equals(key)) {
                // Suppression du rôle
                AppUser appUser = appUserRepo.findByUsername(us);
                appUser.getRoleCollection().removeIf(r -> r.getRoleName().equals(role));
                appUserRepo.save(appUser);

                // Supprimer le code de vérification de la session après utilisation
                httpSession.removeAttribute("keyValue");

                return "redirect:/AddUserRole?us=" + us + "&del=true";
            } else {
                return "redirect:/AddUserRole?us=" + us + "&adminRole=true&err=VerifyKey&del=true";
            }
        }

        // Suppression du rôle pour les rôles autres que `ADMIN`
        AppUser appUser = appUserRepo.findByUsername(us);
        appUser.getRoleCollection().removeIf(r -> r.getRoleName().equals(role));
        appUserRepo.save(appUser);

        return "redirect:/AddUserRole?us=" + us;
    }


    @PostMapping(path = "/deleteUser")
    public String deleteUser(@RequestParam(value = "id", defaultValue = "") int id) {
        AppUser appUser = appUserRepo.getById(id);

        String imageName = appUser.getPhoto();
        // Create a file object for the image to be deleted
        File file = new File(imageName);

        // Check if the file exists and delete it
        if (file.exists()) {
            file.delete();  // Returns true if the deletion was successful
        } else {
            System.out.println("File not found: " + imageName);

        }
        appUserRepo.deleteById(id);
        System.out.println("Successs delete : " + id);
        return "redirect:/Utilisateurs";
    }


    @PostMapping(path = "/lockUnlockUser")
    public String lockUnlockUser(@RequestParam(value = "id", defaultValue = "") int id) {
        AppUser appUser = appUserRepo.getById(id);
        if (appUser != null) {
            if (appUser.isActif()) {
                appUser.setActif(false);
            } else appUser.setActif(true);
        }
        System.out.println("Successs Locking : " + id);
        return "redirect:/Utilisateurs";
    }

    @GetMapping(path = "/EditUser")
    public String EditUser(Model model, @RequestParam(value = "id", defaultValue = "") int id) {
        model.addAttribute("user", findLogged());
        model.addAttribute("isAdmin", isAdmin());

        AppUser appUser = appUserRepo.getById(id);
        if (appUser != null) {
            model.addAttribute("userEdit", appUser);
            model.addAttribute("roleCollection", appUser.getRoleCollection());
        }

        return "EditUser";
    }

    public AppUser findLogged() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            boolean isAdmin = false;

            String utilisateur = ((UserDetails) authentication.getPrincipal()).getUsername();


            AppUser appUser1 = appUserRepo.findByUsername(utilisateur);

            return appUser1;

        } else return null;
    }

    public boolean isAdmin() {
        System.out.println("Liste des role dans isAdmin = " + findLogged().getRoleCollection());
        for (AppRole appRole : findLogged().getRoleCollection()) {

            if (appRole.getRoleName().equals("ADMIN")) {
                System.out.println("Role trouvé dans la nouvelle methode isAdmin");
                return true;
            }
        }
        return false;
    }

    @GetMapping(path = "/AddUserDetail")
    public String AddUserDetail(Model model, @RequestParam(value = "us", defaultValue = "") String us) {

        model.addAttribute("user", findLogged());
        model.addAttribute("isAdmin", isAdmin());
        model.addAttribute("us", us);
        System.out.println("Path recur user details : " + us);
        model.addAttribute("newUser", new AppUser());
        return "AddUserDetail";


    }

    @Autowired
    private FtpService ftpService;

    @PostMapping(path = "/saveUserDetail")
    public String SaveUserDetail(@RequestParam(value = "us", defaultValue = "") String us, @ModelAttribute(value = "newUser") AppUser appUser) {
        System.out.println("path recu sur details poste   : " + us);
        if (us != null) {
            AppUser newUser = appUserRepo.findByUsername(us);
            System.out.println("Utilistaeur trouvé : " + newUser);
            if (newUser != null) {
                //newUser.setUsername(newUser.getUsername());

                newUser.setAdresse(appUser.getAdresse()); // Peut être null
                newUser.setAdresse2(appUser.getAdresse2()); // Peut être null
                newUser.setTelephone1(appUser.getTelephone1()); // Peut être null
                newUser.setTelephone2(appUser.getTelephone2()); // Peut être null
                newUser.setTelephone3(appUser.getTelephone3()); // Peut être null
                newUser.setEmail(appUser.getEmail());
                newUser.setActif(appUser.isActif());


                // Vous pouvez également conserver les valeurs non modifiées pour les autres champs requis
                System.out.println("Utilisateur qui va etre enregistré");
                appUserRepo.save(newUser);
                return "redirect:/AddUserRole?us=" + newUser.getUsername();
            } else {
                if (appUser != null) appUserRepo.save(appUser);
                return "redirect:/AddUserDetail?us=Erreur parvenu !";
            }
        } else {
            return "redirect:/AddUserDetail?us=Erreur parvenu !";
        }
    }


    @GetMapping(path = "/AddUser")
    public String addUser(Model model, @RequestParam(value = "us", defaultValue = "") String us) {
        model.addAttribute("user", findLogged());
        model.addAttribute("isAdmin", isAdmin());


        if (us != "") {
            model.addAttribute("erreur", us);

            model.addAttribute("newUser", new AppUser());
            List<AppRole> list = appRoleRepo.findAll();
            List<AppRole> appRole = new ArrayList<>();
            model.addAttribute("rappRole", appRole);
            model.addAttribute("roleCollection", list);

        }
        return "AddUser";

    }

    @PostMapping("/saveUser")
    public String uploadImage(Model model, @ModelAttribute(value = "newUser") AppUser appUser) {
        System.out.println("User received " + appUser);
        if (appUser != null) {
            AppUser test = appUserRepo.findByUsername(appUser.getUsername());
            System.out.println("user find in base :" + test);
            if (test != null) {
                test.setNom(appUser.getNom());
                test.setPrenom(appUser.getPrenom());
                //  test.setActif(appUser.isActif());
                test.setEmail(appUser.getEmail());
                test.setTelephone1(appUser.getTelephone1());
                test.setTelephone2(appUser.getTelephone2());
                test.setTelephone3(appUser.getTelephone3());
                test.setAdresse(appUser.getAdresse());
                test.setAdresse2(appUser.getAdresse2());
                // test.setRoleCollection(appUser.getRoleCollection());
                System.out.println("Saved User" + test);
                appUserRepo.save(test);
            } else {
                appUserRepo.save(appUser);
            }
        }
        Collection<AppRole> list=findLogged().getRoleCollection();
        for (AppRole appRole:list){
            if (appRole.getRoleName().equals("ENSEIGNANT")){
                return "redirect:/EspaceEnseignant";

            }
        }
            return "redirect:/Utilisateurs";


    }

    @GetMapping(path = "/AddUserPhoto")
    public String AddUserPhoto(Model model, @RequestParam(value = "errFormat",defaultValue = "")String errorFormat,@RequestParam(value = "us", defaultValue = "") String us) {
        model.addAttribute("user", findLogged());
        model.addAttribute("isAdmin", isAdmin());
        model.addAttribute("errFormat", errorFormat);
        if (us != null) {
            AppUser appUser = appUserRepo.findByUsername(us);
            if (appUser != null) {
                model.addAttribute("newUser", appUser);
                System.out.println(appUser.getPhoto());
            }
        }

        return "AddUserPhoto";
    }

    private final String uploadsDirectory = "https://www.apirest.tech/downloads/uploads/"; // Répertoire sur le serveur FTP

    @PostMapping(path = "/AddUserImg")
    public String addUserImg(Model model, @RequestParam String us, @RequestParam("photo") MultipartFile photo) throws IOException {
        System.out.println("Received " + us);


        if (us != null) {
            AppUser appUser = appUserRepo.findByUsername(us);
            System.out.println("Taille de l'image recu :"+photo.getSize());

            // Vérifiez si l'utilisateur existe
            if (appUser != null) {
                // Si l'utilisateur a déjà une photo, supprimez-la du serveur FTP
                if(photo.getSize()>200000){
                    return "redirect:/AddUserPhoto?us="+us+"&errFormat="+"Taille invalide :taille max de l'image = 200 KB!";

                }

                // Vérifiez le format du fichier
                String extension = photo.getOriginalFilename().substring(photo.getOriginalFilename().lastIndexOf("."));

                if (!extension.equalsIgnoreCase(".jpg")) {

                    return "redirect:/AddUserPhoto?us="+us+"&errFormat="+"Format d'image invalide :format JPG uniquement !";
                }

                // Créez un nom de fichier unique pour le téléversement
                String newFileName = appUser.getUsername() + LocalDateTime.now().toString().replace(":", "_").replace(".", "_") + extension;
                if (appUser.getPhoto() != null && !appUser.getPhoto().isEmpty()) {
                    System.out.println("Ancien path de l'image :" + appUser.getPhoto());

                    // Extraire le nom de fichier pour la suppression
                    String oldFileName = appUser.getPhoto().substring(appUser.getPhoto().lastIndexOf("/") + 1);
                    System.out.println("Path de suppression" + oldFileName);
                    ftpService.deleteFile(oldFileName); // Méthode pour supprimer le fichier du serveur FTP
                }
                // Téléversez le fichier sur le serveur FTP
                ftpService.uploadFile(photo, newFileName); // Méthode pour téléverser le fichier

                // Mettez à jour l'utilisateur avec le nouveau chemin de photo
                appUser.setPhoto(uploadsDirectory + newFileName); // Mettez à jour avec le chemin complet
                appUserRepo.save(appUser);
                return "redirect:/AddUserPhoto?us=" + appUser.getUsername();
            }
        }
        String success = "";
        model.addAttribute("success", success);
        return "redirect:/AddUserPhoto?us=" + us;
    }

    @GetMapping("/CheckPoint")
    public String CheckPoint(Model model, @RequestParam(value = "id") int id,
                             @RequestParam(value = "error", defaultValue = "") String error,
                             @RequestParam(value = "conf", defaultValue = "") String conf,
                             HttpSession httpSession) {
        // Vérification de la présence d'un code de confirmation
        if (!conf.equals("") ) {
            String sessionConfCode = (String) httpSession.getAttribute("confcode");

          //  if (sessionConfCode != null && sessionConfCode.equals(conf)) {
                model.addAttribute("error", error);
             //   AppUser appUser = appUserRepo.getById(id);
                VerfPas verfPas = new VerfPas();
                verfPas.setId(id);
                verfPas.setAncienPass("");
                verfPas.setNouveauPass("");

                model.addAttribute("userEdit", verfPas);
                model.addAttribute("user", findLogged());
                model.addAttribute("isAdmin", isAdmin());
                model.addAttribute("test", "Hello check");
                // Rediriger vers CheckPoint si le code est correct
                return "redirect:/CheckPoint?id="+verfPas.getId();
//            } else {
//                // Rediriger vers déconnexion si le code est incorrect
//                return "redirect:/deconnect";
//            }
        }

        // Ajouter les attributs au modèle
        model.addAttribute("error", error);
        AppUser appUser = appUserRepo.getById(id);
        VerfPas verfPas = new VerfPas();
        verfPas.setId(id);
        verfPas.setAncienPass("");
        verfPas.setNouveauPass("");

        model.addAttribute("userEdit", verfPas);
        model.addAttribute("user", findLogged());
        model.addAttribute("isAdmin", isAdmin());
        model.addAttribute("test", "Hello check");

        return "CheckPoint"; // Retourner la vue CheckPoint
    }

    int i = 1;

    @PostMapping("/CheckCred")
    public String CheckCred(@ModelAttribute("userEdit") VerfPas verfPas,
                            Model model, HttpSession httpSession) {
        System.out.println("NewUser RECU :" + verfPas);
        System.out.println("MDP RECU :" + verfPas.getAncienPass());

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        AppUser appUser = appUserRepo.getById(verfPas.getId());
        System.out.println("Utilisateur trouvé : " + appUser);

        // Vérification du mot de passe
        if (!encoder.matches(verfPas.getAncienPass(), appUser.getPassword())) {
            System.out.println("Mot de Passe Non Conforme");
            httpSession.setAttribute("tentative", i++);
            System.out.println("Nombre d'essai = " + httpSession.getAttribute("tentative"));

            if (Integer.parseInt(httpSession.getAttribute("tentative").toString()) > 0) {
                String ranf = UUID.randomUUID().toString().replace("-","");
                emailService.sendSimpleEmail("bilelbenabdallah31@gmail.com",
                        "Tentative de changement de mot de passe",
                        "Une tentative de changement de votre mot de passe actuelle est détectée. " +
                                "Si vous êtes à l'origine de cette action, veuillez suivre le lien suivant : " +
                                "http://localhost:8085/CheckPoint?id=" + findLogged().getId() + "&conf=" + ranf +
                                " et changer votre mot de passe actuel. Si vous n'êtes pas à l'origine de cette action, " +
                                "votre compte sera désactivé temporairement pour des raisons de sécurité. " +
                                "Veuillez contacter l'administrateur du site.");
                httpSession.setAttribute("confcode", ranf);
                httpSession.setAttribute("tentative", 1);
            }

            return "redirect:/CheckPoint?id=" + verfPas.getId() + "&error=Mot de passe actuel incorrect."; // redirige avec un message d'erreur
        } else {
            System.out.println("Mot de passe identique");
            appUser.setPassword(encoder.encode(verfPas.getNouveauPass()));
            appUserRepo.save(appUser);
            return "redirect:/EditUser?id=" + verfPas.getId(); // redirige vers la page d'édition de l'utilisateur
        }
    }

    @Data
class VerfPas{
        private int id;
      private String ancienPass;
      private String nouveauPass;

}

    @GetMapping("/Profile")
    public String profile(Model model, HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String utilisateur = "";

        model.addAttribute("user", findLogged());
        model.addAttribute("isAdmin", isAdmin());


        return "Profile"; // Your Thymeleaf template name
    }



    @GetMapping(path = "/AbsenceEtudiant")
    public String etudiantPage(Model model) {
        model.addAttribute("pathCourant", "/AbsenceEtudiant");
        //String utilisateurCourant = "";
        model.addAttribute("user", findLogged());
        model.addAttribute("isAdmin", isAdmin());

        String utilisateur = "";


        return "AbsenceEtudiant"; // Ensure this returns the correct view name
    }


    @GetMapping(path = "/Dashboard")
    public String dashboardModel(Model model,
                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "size", defaultValue = "5") int size,
                                 @RequestParam(value = "keyword", defaultValue = "") String keyword) throws IOException {
        String utilisateur = "";

        model.addAttribute("user", findLogged());
        model.addAttribute("isAdmin", isAdmin());


        List<Logged> loggedList = loggedRepo.findByConnectedTrue();
        System.out.println("Utilisateur trouvé a la connection" + loggedList);
        System.out.println("Utilisateur trouvé apres l'erreur" + loggedList);
        System.out.println(loggedList);
        model.addAttribute("loggedList", loggedList);
        int notifiedCount = 0;
        Page<LigneAbsence> absencePage = ligneAbsenceRepo.findTous(PageRequest.of(page, size));
        for (LigneAbsence ligneAbsence : absencePage) {
            if (ligneAbsence.getNotified()) {
                notifiedCount++;
            }
        }

        boolean mobileServer = serverStatusService.checkMobileAppServerStatus("https://www.apirest.tech/emploi/all");
        System.out.println("Mobile " + mobileServer);
        boolean mailServer = true;// = serverStatusService.checkMailServer();
        System.out.println("Mail " + mailServer);
        boolean webServer = serverStatusService.checkMobileAppServerStatus("https://www.apirest.tech/");
        System.out.println("Web " + webServer);
        boolean statusGeneral = mailServer && mobileServer && webServer;
        System.out.println("Générale  " + statusGeneral);

        model.addAttribute("statusGeneral", statusGeneral);
        model.addAttribute("webServer", webServer);
        model.addAttribute("mailServer", mailServer);
        model.addAttribute("mobileServer", mobileServer);

        logger.info("Mobile server status: {}", mobileServer);

        Long nbEns = enstRepo.count();
        Long nbSalles = salleRepo.count();
        LocalDate date = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateNow = formatter.format(date);
        String fullDate = calculDate.JourEnTouteLettre() + "  " + dateNow;

        List<LigneAbsence> listNonNotified = ligneAbsenceRepo.findByNotifiedFalse();
int index=0;
if(listNonNotified.size()>4)index=5;
if(listNonNotified.size()<4)index=listNonNotified.size();
        if(!listNonNotified.isEmpty()){
            List<LigneAbsence> listNonNotifier = new ArrayList<>();

            for (int i = 0; i <index; i++) {
            listNonNotifier.add(listNonNotified.get(i));
        }
            model.addAttribute("listNonNotifier", listNonNotifier);
        }
        model.addAttribute("dateNow", fullDate);
        model.addAttribute("nbEns", nbEns);
        model.addAttribute("nbSalles", nbSalles);
        model.addAttribute("listAbsence", absencePage.getContent());
        model.addAttribute("pages", new int[absencePage.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("pathCourant", "/Dashboard");
        model.addAttribute("nbNotifier", notifiedCount);


        List<Object[]> objectList = ligneAbsenceRepo.countAbsencesByEnseignantNative();
        System.out.println("Nombre d'absences = "+objectList.toString());
        model.addAttribute("absenceByEnseignant", objectList);

        return "Dashboard"; // Ensure this returns the correct view name
    }

    @GetMapping("/api/absences")
    @ResponseBody
    public List<Object[]> getAbsencesByEnseignant() {
        List<Object[]> objectList = ligneAbsenceRepo.countAbsencesByEnseignantNative();
        List<Object[]> objects = new ArrayList<>();
        int index=5;
        if (objectList.size()<5)index=objectList.size();
        for (int i = 0;i < index; i++) {
            objects.add(objectList.get(i));
        }


        return objects;

    }

}
