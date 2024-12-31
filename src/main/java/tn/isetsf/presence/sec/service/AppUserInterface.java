package tn.isetsf.presence.sec.service;

import tn.isetsf.presence.sec.entity.AppRole;
import tn.isetsf.presence.sec.entity.AppUser;

import java.util.List;

public interface AppUserInterface {
    AppRole AddNewRole(AppRole appRole);
    AppUser AddUser(AppUser appUser);
    boolean AddRoleToUser(String userName, String roleName);
    AppUser LoadUserByUserName(String userName);
    List<AppUser> ListUser();
    AppUser findByEmail(String email);

}
