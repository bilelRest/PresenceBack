package tn.isetsf.presence.sec.webSecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.WebSession;
import org.springframework.web.server.session.InMemoryWebSessionStore;
import tn.isetsf.presence.PresenceApplication;
import tn.isetsf.presence.sec.entity.AppUser;
import tn.isetsf.presence.sec.entity.Logged;
import tn.isetsf.presence.sec.repository.AppUserRepo;
import tn.isetsf.presence.sec.repository.LoggedRepo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;@Controller
@RequestMapping("/default")
public class DefaultController {

    @Autowired
    private LoggedRepo loggedRepo;
    @Autowired
    AppUserRepo appUserRepo;

    @GetMapping
    public String defaultAfterLogin(Authentication authentication, HttpSession httpSession,HttpServletRequest request) {
        String currentUser = getCurrentUser(authentication);
        updateLoggedUsersStatus(currentUser);

        String sessionId = httpSession.getId();
        if (currentUser != null && sessionId != null) {
            AppUser appUser=appUserRepo.findByUsername(currentUser);
            if (appUser.isActif()){

            Logged loggedRecord = new Logged(null, currentUser, authentication.getAuthorities().toString(), true, LocalDateTime.now(), null, sessionId);
            if (loggedRepo.findByLogNameAndSessionId(currentUser, sessionId).isEmpty()) {
                loggedRepo.save(loggedRecord);
            }}
            return redirectToRolePage(authentication,request,sessionId);
        }
        return "redirect:/";
    }

    private String getCurrentUser(Authentication authentication) {
        return (authentication != null) ? ((UserDetails) authentication.getPrincipal()).getUsername() : null;
    }

    private void updateLoggedUsersStatus(String currentUser) {
        List<Logged> connectedUsers = loggedRepo.findByConnectedTrue();
        if (!connectedUsers.isEmpty()) {
            connectedUsers.forEach(loggedUser -> {
                if (loggedUser.getLogName().equals(currentUser)) {
                    loggedUser.setConnected(false);
                    loggedUser.setDateDeconnect(LocalDateTime.now());
                    loggedRepo.save(loggedUser);
                }
            });
        }
    }


    private String redirectToRolePage(Authentication authentication, HttpServletRequest request,String httpSession) {
        String us=((UserDetails)authentication.getPrincipal()).getUsername();
        AppUser appUser=appUserRepo.findByUsername(us);
        if(appUser!=null){
            if (!appUser.isActif()){
                if (authentication != null&&httpSession!=null) {
                    System.out.println("Authentification reçue ....");
                    String CURRENT_USER = ((UserDetails) authentication.getPrincipal()).getUsername();
                    System.out.println("Utilisateur reçu .... " + CURRENT_USER);
                    //Logged logged=loggedRepo.getByLogName(CURRENT_USER);
                    List< Logged> logged=  loggedRepo.findByLogNameAndSessionId(CURRENT_USER,httpSession);
                    System.out.println("Utilisateur trouvé : "+logged);
                    for(Logged log:logged){
                        if (log.getSessionId()!=httpSession)
                            log.setConnected(false);
                        log.setDateDeconnect(LocalDateTime.now());
                        System.out.println("Set false et dateTime effectué");
                        loggedRepo.save(log);
                    }}
                request.getSession().invalidate();
                SecurityContextHolder.clearContext();
                System.out.println("Compte désactivé destruction de la session ...");
                return "redirect:/login";
            }
        }


        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            return "redirect:/Dashboard";
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ENSEIGNANT"))) {
            return "redirect:/EspaceEnseignant";
        }
        return "redirect:/";

    }}
