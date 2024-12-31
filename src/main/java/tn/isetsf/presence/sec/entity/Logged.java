package tn.isetsf.presence.sec.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor@NoArgsConstructor
@Data
public class Logged {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String logName;
    private String roles;
    private boolean connected;
    private LocalDateTime dateConnect;
    private LocalDateTime dateDeconnect;
    private String sessionId;

}
