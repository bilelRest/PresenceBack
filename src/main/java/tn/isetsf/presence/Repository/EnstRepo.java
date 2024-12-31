package tn.isetsf.presence.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.isetsf.presence.Entity.Ens;

public interface EnstRepo extends JpaRepository<Ens,String> {
}
