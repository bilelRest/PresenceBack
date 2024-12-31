package tn.isetsf.presence.webThymeleaf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import tn.isetsf.presence.serviceMail.EmailService;

import java.util.Objects;

@Service
public class ServerStatusService {

    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    EmailService emailService;

    public boolean checkMobileAppServerStatus(String url) {


        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            // Return true only if the response status is 200 OK
            return response.getStatusCode() == HttpStatus.OK;
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            // Catch any HTTP error status (like 404, 500, etc.) and return false
            return false;
        } catch (Exception e) {
            // Catch any other exceptions (e.g., timeouts, network issues) and return false
            return false;

        }}
    public boolean checkMailServer() {
        try {
            System.out.println("Envoie de email en cours ");
           emailService.sendSimpleEmail("bilelbenabdallah31@gmail.com","Test","Test lors de chargement de la page dashboard sur le status de mail server");
           System.out.println("Success d'envoies");
            return true;

        } catch (Exception e) {
            System.out.println("Echec d'envoie email");
            e.printStackTrace();
            return false;
        }
    }}
