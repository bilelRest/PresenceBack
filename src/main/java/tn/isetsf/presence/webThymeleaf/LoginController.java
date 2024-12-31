package tn.isetsf.presence.webThymeleaf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.session.InMemoryWebSessionStore;
import tn.isetsf.presence.sec.config.SecurityConfig;
import tn.isetsf.presence.sec.entity.Logged;
import tn.isetsf.presence.sec.repository.LoggedRepo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Controller
@Transactional
public class LoginController {
    @Autowired
    LoggedRepo loggedRepo;

    @GetMapping(path = "/login")
    public String logUser(Model model, @RequestParam(value = "check" ,defaultValue = "") String check){

        return "login"; // Retirer le slash ici
    }

    @GetMapping(path = "/deconnect")
    public String logOutUser(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
        String sessionId=httpSession.getId();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null&&sessionId!=null) {
            System.out.println("Authentification reçue ....");
            String CURRENT_USER = ((UserDetails) authentication.getPrincipal()).getUsername();
            System.out.println("Utilisateur reçu .... " + CURRENT_USER);
            //Logged logged=loggedRepo.getByLogName(CURRENT_USER);
           List< Logged> logged=  loggedRepo.findByLogNameAndSessionId(CURRENT_USER,sessionId);
           System.out.println("Utilisateur trouvé : "+logged);
           for(Logged log:logged){
               if (log.getSessionId()!=sessionId)
               log.setConnected(false);
               log.setDateDeconnect(LocalDateTime.now());
               System.out.println("Set false et dateTime effectué");
               loggedRepo.save(log);
           }

            // Invalider la session
            request.getSession().invalidate();
            SecurityContextHolder.clearContext();

            // Optionnel : ajouter un message à afficher après la déconnexion
            request.getSession().setAttribute("message", "Déconnexion réussie");
        }

        // Rediriger vers la page de connexion ou une autre page
        return "redirect:/login"; // Redirection vers la page de connexion
    }

}

