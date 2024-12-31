package tn.isetsf.presence.sec.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.isetsf.presence.sec.entity.AppRole;

public interface AppRoleRepo extends JpaRepository<AppRole,Integer> {
    AppRole findByRoleName(String roleName);
}
