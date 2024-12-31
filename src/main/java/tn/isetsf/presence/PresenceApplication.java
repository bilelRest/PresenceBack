package tn.isetsf.presence;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionIdChangedEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.session.InMemoryWebSessionStore;
import tn.isetsf.presence.Repository.LigneAbsenceRepo;
import tn.isetsf.presence.sec.entity.AppRole;
import tn.isetsf.presence.sec.entity.AppUser;
import tn.isetsf.presence.sec.repository.AppRoleRepo;
import tn.isetsf.presence.sec.repository.AppUserRepo;
import tn.isetsf.presence.sec.service.AppUserInterfaceImpl;
import tn.isetsf.presence.serviceMail.EmailService;
import tn.isetsf.presence.webThymeleaf.AdminController;


import javax.servlet.http.HttpSession;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@SpringBootApplication


public class PresenceApplication {
	@Autowired
	private AppRoleRepo appRoleRepo;
	@Autowired
	private AppUserRepo appUserRepo;
	@Bean
	PasswordEncoder passwordEncoder () {
		return new BCryptPasswordEncoder();
	}


	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}


	public static void main(String[] args) {

		SpringApplication.run(PresenceApplication.class, args);
	}

@Autowired
	EmailService emailService;




//		int index = 0, indexdouble = 0;
//
//		LocalTime time = LocalTime.now();
//		if (time.isAfter(LocalTime.of(8, 15)) && time.isBefore(LocalTime.of(9, 45))) {
//			index = 1;
//		} else if (time.isAfter(LocalTime.of(9, 45)) && time.isBefore(LocalTime.of(11, 30))) {
//			index = 2;
//		} else if (time.isAfter(LocalTime.of(11, 30)) && time.isBefore(LocalTime.of(13, 15))) {
//			index = 3;
//		} else if (time.isAfter(LocalTime.of(13, 15)) && time.isBefore(LocalTime.of(14, 50))) {
//			index = 4;
//		} else if (time.isAfter(LocalTime.of(14, 50)) && time.isBefore(LocalTime.of(16, 25))) {
//			index = 5;
//		} else if (time.isAfter(LocalTime.of(16, 25)) && time.isBefore(LocalTime.of(18, 0))) {
//			index = 6;
//
//
//		}
//
//
//		if (time.isAfter(LocalTime.of(8, 15)) && time.isBefore(LocalTime.of(11, 30))) {
//			indexdouble = 7;
//		} else if (time.isAfter(LocalTime.of(9, 45)) && time.isBefore(LocalTime.of(13, 15))) {
//			indexdouble = 8;
//		} else if (time.isAfter(LocalTime.of(11, 30)) && time.isBefore(LocalTime.of(14, 50))) {
//			indexdouble = 11;
//		} else if (time.isAfter(LocalTime.of(13, 15)) && time.isBefore(LocalTime.of(16, 25))) {
//			indexdouble = 9;
//		} else if (time.isAfter(LocalTime.of(14, 50)) && time.isBefore(LocalTime.of(18, 0))) {
//			indexdouble = 10;
//		} else {
//			indexdouble = 0;
//		}
//		System.out.println("simple = " + index + " double = " + indexdouble);
//
//	}
//

@Autowired
	LigneAbsenceRepo ligneAbsenceRepo;
	@Bean
	CommandLineRunner start(AppUserInterfaceImpl appUserInterface) {
		return args -> {
//			ligneAbsenceRepo.deleteAll();
//			System.out.println("Success delete ...");
//			String tok2=UUID.randomUUID().toString().replace("-","#_è");
//			String tok=tok2+UUID.randomUUID().toString().replace("-","&éàè");
//			URLEncoder.encode(tok, StandardCharsets.UTF_8);
//			System.out.println("random généré : "+tok);
//			try{emailService.sendSimpleEmail(
//					"bilelbenabdallah31@gmail.com",
//					"Mise à jour et correctifs sur votre site",
//					"Bonjour Mr Nissen,\n\nNous avons récemment  ajouté de nouvelles fonctionnalités au site https://www.apirest.tech précisément sur la vue Absence Enseignants. Nous vous invitons à visiter le site avec vos  paramètres crédentiels pour découvrir ces améliorations et nous faire part de vos retours.\n\nCordialement,\nL'équipe technique."
//			);
//			System.out.println("Success d'envoie de l'email");
//			}catch (Exception e){
//				System.out.println("echec d'envoie de l'email");
//			}

//			AppUser appUser=appUserRepo.findByUsername("user2");
//			if(appUser!=null){
//				appUser.setActif(true);
//				appUserRepo.save(appUser);
//				System.out.println("Déactivé avec success ...");
//			}
//			AppUser appUser=appUserRepo.findByUsername("admin");
//			if(appUser!=null){
//				appUser.setActif(true);
//			}
//			AppUser appUser2=appUserRepo.findByUsername("user2");
//			if(appUser2!=null){
//				appUser2.setActif(true);
//			}

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

			LocalDate dateNaissance = LocalDate.parse("30-12-1985", formatter);
//appUserInterface.AddRoleToUser("admin","ADMIN");
//appUserInterface.AddRoleToUser("user2","ENSEIGNANT");
//			AppUser appUser=new AppUser("admin","Bilel","BENABDALLAH","Sfax","Sfax","bilel@apirest.tech",null,98186144,0,0,true,"",null,"","1234",null);
//
//			appUserInterface.AddUser(appUser);
//
//System.out.println(appUserRepo.findAll());
//
//			appRoleRepo.save(new AppRole(null, "ADMIN"));
//			appRoleRepo.save(new AppRole(null, "ENSEIGNANT"));
//			appRoleRepo.save(new AppRole(null, "ETUDIANT"));
//			appUserInterface.AddRoleToUser("admin","ADMIN");
//			appUserInterface.AddRoleToUser("admin","ETUDIANT");
//			appUserInterface.AddRoleToUser("admin","ENSEIGNANT");

//appUserRepo.findByUsername("amdin");
			//appUserRepo.deleteById(15);
//			System.out.println(appUserRepo.findAll());





		};}}






//	}






