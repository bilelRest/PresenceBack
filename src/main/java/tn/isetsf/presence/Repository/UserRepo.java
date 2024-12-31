package tn.isetsf.presence.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.isetsf.presence.Entity.Users;

import java.util.Optional;

public interface UserRepo extends JpaRepository<Users, Integer> {
    Optional<Users> findByLoginAndPassword(String login, String password);

    Users findByLogin(String username);
}
