package tn.isetsf.presence.webThymeleaf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import tn.isetsf.presence.sec.entity.AppUser;
import tn.isetsf.presence.sec.service.AppUserInterfaceImpl;

import javax.annotation.security.RolesAllowed;
import javax.transaction.Transactional;

@Controller
@Transactional
public class EnseignantController {
    @Autowired
    AppUserInterfaceImpl userInterface;
    @RolesAllowed({"ROLE_ADMIN","ROLE_ENSEIGNANT"})
    @GetMapping(path = "/EspaceEnseignant")
    public String EnseignantZone(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            AppUser utilisateur = userInterface.LoadUserByUserName(userDetails.getUsername()); // Récupérez l'utilisateur complet
            model.addAttribute("user", utilisateur);
            System.out.println("Utilisateur connecté : " + utilisateur.getUsername());
        }

        model.addAttribute("message", "Hello Teacher");
        return "EspaceEnseignant";
    }

}
