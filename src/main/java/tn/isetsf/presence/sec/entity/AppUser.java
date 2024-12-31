package tn.isetsf.presence.sec.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.format.annotation.DateTimeFormat;
import tn.isetsf.presence.sec.entity.AppRole;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private String username;

    public AppUser(String username, String nom, String prenom, String adresse, String adresse2, String email, LocalDate dateNaissance, Integer telephone1, Integer telephone2, Integer telephone3, boolean actif, String resetToken, LocalDateTime expiration, String photo, String password, Collection<AppRole> roleCollection) {
        this.username = username;
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.adresse2 = adresse2;
        this.email = email;
        this.dateNaissance = dateNaissance;
        this.telephone1 = telephone1;
        this.telephone2 = telephone2;
        this.telephone3 = telephone3;
        this.actif = actif;
        this.resetToken = resetToken;
        this.expiration = expiration;
        this.photo = photo;
        this.password = password;
        this.roleCollection = roleCollection;
    }

    private String nom;

    private String prenom;

    private String adresse="";
    private String adresse2="";
    private String email="";

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateNaissance;

    private Integer telephone1=0;
    private Integer telephone2=0;
    private Integer telephone3=0;
    private boolean actif=true;
    private String resetToken="";
    private LocalDateTime expiration;

    private String photo="";

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<AppRole> roleCollection = new ArrayList<>();
}
