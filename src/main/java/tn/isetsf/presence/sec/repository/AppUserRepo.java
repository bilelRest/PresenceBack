package tn.isetsf.presence.sec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.isetsf.presence.sec.entity.AppUser;

public interface AppUserRepo extends JpaRepository<AppUser,Integer> {
    AppUser findByUsername(String userName);
    AppUser findByEmail(String email);
    AppUser findByResetToken(String token);
}
