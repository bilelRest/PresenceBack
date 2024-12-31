package tn.isetsf.presence.webThymeleaf;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tn.isetsf.presence.sec.entity.AppUser;
import tn.isetsf.presence.sec.service.AppUserInterfaceImpl;
import tn.isetsf.presence.serviceMail.EmailService;

import javax.management.modelmbean.ModelMBeanOperationInfo;
import javax.transaction.Transactional;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

@Controller
@Transactional
public class Forgot {
    @Autowired
    AppUserInterfaceImpl appUserInterface;

    @GetMapping(path = "/ForgotPassword")
    public String ForgotPassword(Model model, @RequestParam(value = "stat" ,defaultValue = "")String stat){
        String stat1="";
        boolean suc=false;

        if (stat!=null){
      if(stat.equals("0")) { stat1="Un email est envoyé à l'adresse  indiqué";
          suc=true;
      }
      else if(stat.equals("1"))stat1="Aucun utilisateur trouvé";}
        model.addAttribute("suc",suc);
        model.addAttribute("stat",stat1);
        model.addAttribute("email",new EmailUs());
        return "ForgotPassword";
    }
@Autowired
    EmailService emailService;
    @PostMapping(path = "/forgot")
    public String forgot(Model model,@ModelAttribute(value = "email")EmailUs email){
        if(email!=null&&!email.emailUs.equals("")){
           AppUser appUser= appUserInterface.findByEmail(email.emailUs);
            if (appUser!=null){
                String tok2=UUID.randomUUID().toString().replace("-","èàé");
                String tok1=tok2+UUID.randomUUID().toString().replace("-","èàé");
                String tok=tok1+"#"+UUID.randomUUID().toString().replace("-","jdhdhdhdjkklls");
                URLEncoder.encode(tok, StandardCharsets.UTF_8);
                System.out.println("random généré : "+tok);
                appUser.setResetToken(tok1);
                appUser.setExpiration(LocalDateTime.now().plusMinutes(15));
                appUserInterface.SaveUser(appUser);
                String subject = "Réinitialisation de votre mot de passe";
                String resetUrl = "https://www.apirest.tech/reset-password?token=" + tok;
                String message = "Veuillez cliquer sur le lien suivant pour réinitialiser votre mot de passe : " + resetUrl;

                // Utilisez votre service d'envoi d'e-mails ici
                emailService.sendSimpleEmail(appUser.getEmail(), subject, message);
                return "redirect:/ForgotPassword?stat=0";
            }
        }
        return "redirect:/ForgotPassword?stat=1";
    }
    @PostMapping(path = "/CheckPass")
    public String CheckPass(Model model, @ModelAttribute(value = "pass") Pass pass) {
        System.out.println("Token recu = "+pass.getToken());
        AppUser appUser = appUserInterface.FindByToken(pass.getToken());
        if (appUser != null) {
            System.out.println("Appuer non null ");
            if (appUser.getExpiration()!=null&&appUser.getExpiration().isAfter(LocalDateTime.now())) {
                System.out.println("Condition date expiration valide ");

                if (appUser.getResetToken().equals( pass.getToken())&&pass.getToken()!=null){
                System.out.println("Condition token expiration valide ");
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                System.out.println("Mot de passe recu =  "+pass.getPass1());
                String pas=encoder.encode(pass.getPass1());
                System.out.println("Mot de passe haché =  "+pas);
                appUser.setPassword(pas);
                appUser.setResetToken("");
                appUser.setExpiration(null);
                appUserInterface.SaveUser(appUser);
                System.out.println("Appuer enregistré avec success  ");
                return "redirect:/reset-password?cond=true";}
            }
        }
        return "redirect:/reset-password?cond=false";
    }

    @GetMapping(path = "/reset-password")
    public String ResetPassword(Model model, @RequestParam(value = "token" ,defaultValue = "") String token, @RequestParam(value = "cond", defaultValue = "") String cond) {
        String msg = "";
        if (!cond.isEmpty()) {
            if (cond.equals("false")) msg = "Lien expiré ou inexistant !";
            if (cond.equals("true")) msg = "Mot de passe mis à jour avec succès";
        }
        model.addAttribute("msg", msg);
        Pass pass = new Pass();
        pass.setToken(token);
        model.addAttribute("pass", pass);
        return "reset-password";
    }

    @Data
    class Pass{
        private String token;
        private String pass1;
    }
    @Data
    class EmailUs{
        private String emailUs;
    }
}
